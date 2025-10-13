package com.money.manager.money_manager_api.service;

import com.money.manager.money_manager_api.dto.ChangePasswordDTO;
import com.money.manager.money_manager_api.dto.ProfileDTO;
import com.money.manager.money_manager_api.entity.ProfileEntity;
import com.money.manager.money_manager_api.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${app.activation.url}")
    private String activationUrl;

    public ProfileDTO registerProfile(ProfileDTO profileDTO) {
        ProfileEntity profileEntity = ProfileEntity.builder()
                .fullName(profileDTO.getFullName())
                .email(profileDTO.getEmail())
                .password(passwordEncoder.encode(profileDTO.getPassword()))
                .activationToken(UUID.randomUUID().toString())
                .build();

        ProfileEntity savedEntity = profileRepository.save(profileEntity);

        String activationLink = activationUrl + "?token=" + savedEntity.getActivationToken();
        String subject = "Activate Your Money Manager Account";
        String body = "Hi " + savedEntity.getFullName() + ",<br><br>Thank you for registering. " +
                "Please click the link below to activate your account:<br>" +
                "<a href=\"" + activationLink + "\">Activate Account</a>";

        emailService.sendEmail(savedEntity.getEmail(), subject, body);

        return toDTO(savedEntity);
    }

    public boolean activateProfile(String token) {
        Optional<ProfileEntity> profileOptional = profileRepository.findByActivationToken(token);

        if (profileOptional.isPresent()) {
            ProfileEntity profile = profileOptional.get();
            profile.setIsActive(true);
            profile.setActivationToken(null);
            profileRepository.save(profile);
            return true;
        }
        return false;
    }

    public ProfileDTO getPublicProfile(String email) {
        ProfileEntity profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return toDTO(profile);
    }

    public ProfileEntity getCurrentProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return profileRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    // --- ADDED FOR PROFILE MANAGEMENT ---

    /**
     * Updates the full name and profile image URL for the currently authenticated user.
     * @param profileDTO Data containing the new full name and/or image URL.
     * @return The updated profile as a DTO.
     */
    public ProfileDTO updateProfileDetails(ProfileDTO profileDTO) {
        ProfileEntity currentUser = getCurrentProfile();

        // Update fields only if they are provided in the request
        if (profileDTO.getFullName() != null && !profileDTO.getFullName().isEmpty()) {
            currentUser.setFullName(profileDTO.getFullName());
        }
        if (profileDTO.getProfileImageUrl() != null) {
            currentUser.setProfileImageUrl(profileDTO.getProfileImageUrl());
        }

        ProfileEntity updatedProfile = profileRepository.save(currentUser);
        return toDTO(updatedProfile);
    }

    /**
     * Changes the password for the currently authenticated user after verifying their old password.
     * @param changePasswordDTO DTO containing the old and new passwords.
     */
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        ProfileEntity currentUser = getCurrentProfile();

        // 1. Verify the old password matches the one in the database
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), currentUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect old password");
        }

        // 2. Encode and set the new password
        currentUser.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));

        // 3. Save the updated user profile
        profileRepository.save(currentUser);
    }

    // --- HELPER METHOD (NO CHANGES) ---

    private ProfileDTO toDTO(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setEmail(entity.getEmail());
        dto.setProfileImageUrl(entity.getProfileImageUrl());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }
}

