package com.example.profileservice.service;

import com.example.profileservice.dto.request.ProfileCreationRequest;
import com.example.profileservice.entity.UserProfile;
import org.springframework.stereotype.Service;

@Service
public interface UserProfileService {
    String createUserProfile(ProfileCreationRequest request);
    boolean checkEmailExists(String email);
}
