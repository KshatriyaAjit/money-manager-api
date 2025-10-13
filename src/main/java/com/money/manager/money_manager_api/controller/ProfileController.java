package com.money.manager.money_manager_api.controller;

import com.money.manager.money_manager_api.dto.AuthDTO;
import com.money.manager.money_manager_api.dto.ChangePasswordDTO;
import com.money.manager.money_manager_api.dto.ProfileDTO;
import com.money.manager.money_manager_api.service.AppUserDetailsService;
import com.money.manager.money_manager_api.service.ProfileService;
import com.money.manager.money_manager_api.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> registerProfile(@RequestBody ProfileDTO profileDTO) {
        ProfileDTO registeredProfile = profileService.registerProfile(profileDTO);
        return new ResponseEntity<>(registeredProfile, HttpStatus.CREATED);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token) {
        boolean isActivated = profileService.activateProfile(token);

        if (isActivated) {
            return ResponseEntity.ok("<h1>Your account has been successfully activated!</h1>");
        } else {
            return ResponseEntity.badRequest().body("<h1>Error: Invalid activation link or account already activated.</h1>");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthDTO authDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword())
            );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authDTO.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("user", profileService.getPublicProfile(authDTO.getEmail()));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileDTO> getCurrentUserProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ProfileDTO profileDTO = profileService.getPublicProfile(email);
        return ResponseEntity.ok(profileDTO);
    }

    // --- ADDED FOR PROFILE MANAGEMENT ---

    @PutMapping("/details")
    public ResponseEntity<ProfileDTO> updateDetails(@RequestBody ProfileDTO profileDTO) {
        ProfileDTO updatedProfile = profileService.updateProfileDetails(profileDTO);
        return ResponseEntity.ok(updatedProfile);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        profileService.changePassword(changePasswordDTO);
        return ResponseEntity.ok().build(); // Returns a 200 OK with no body
    }
}

