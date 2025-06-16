package com.example.profileservice.controller;

import com.example.profileservice.constan.UrlConstant;
import com.example.profileservice.dto.request.UpdateProfileRequest;
import com.example.profileservice.dto.response.UserProfileResponse;
import com.example.profileservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(UrlConstant.API_V1_PROFILE_USER)
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.claims['userId']")
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> getUserProfile(
            @PathVariable Integer userId) {
        log.info("Fetching profile for user ID: {}", userId);
        return ResponseEntity.ok(userProfileService.getUserProfileById(userId));
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.claims['userId']")
    @PutMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @RequestBody UpdateProfileRequest request,
            @PathVariable Integer userId) {
        log.info("Updating profile for user ID: {}", userId);
        return ResponseEntity.ok(userProfileService.updateUserProfile(request, userId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserProfileResponse>> getAllUserProfiles() {
        log.info("Fetching all user profiles");
        return ResponseEntity.ok(userProfileService.getAllUserProfiles());
    }
}