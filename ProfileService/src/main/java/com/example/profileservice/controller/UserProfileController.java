package com.example.profileservice.controller;

import com.example.profileservice.constan.UrlConstan;
import com.example.profileservice.dto.request.ProfileCreationRequest;
import com.example.profileservice.entity.UserProfile;
import com.example.profileservice.service.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(UrlConstan.API_V1_PROFILE)
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @PostMapping("/create")
    public ResponseEntity<String> createProfile(@RequestBody ProfileCreationRequest request ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userProfileService.createUserProfile(request));
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        boolean exists = userProfileService.checkEmailExists(email);
        return ResponseEntity.ok(exists);
    }

}
