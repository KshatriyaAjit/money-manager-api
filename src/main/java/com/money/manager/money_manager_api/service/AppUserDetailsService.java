package com.money.manager.money_manager_api.service;

import com.money.manager.money_manager_api.entity.ProfileEntity;
import com.money.manager.money_manager_api.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Find the user profile from the database by email
        Optional<ProfileEntity> profileOptional = profileRepository.findByEmail(email);

        if (profileOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        ProfileEntity profile = profileOptional.get();

        // Create and return a Spring Security User object
        return new User(
                profile.getEmail(),
                profile.getPassword(),
                Collections.emptyList() // We are not using roles/authorities in this project
        );
    }
}
