package com.example.profileservice.controller;

import com.example.profileservice.constan.UrlConstant;
import com.example.profileservice.dto.request.ProfileUpdateRequest;
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


    @GetMapping("/myInfo")
    public ResponseEntity<UserProfileResponse> getMyInfo() {
        return ResponseEntity.ok(userProfileService.getMyInfo());
    }


    @PutMapping("/update")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @RequestBody ProfileUpdateRequest request) {
        return ResponseEntity.ok(userProfileService.updateUserProfile(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<UserProfileResponse>> getAllUserProfiles() {
        return ResponseEntity.ok(userProfileService.getAllUserProfiles());
    }
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.claims['userId']")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUserProfile(@PathVariable Integer userId) {
        return ResponseEntity.ok (userProfileService.deleteUserProfile(userId));
    }
}