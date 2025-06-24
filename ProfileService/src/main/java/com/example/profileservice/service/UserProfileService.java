package com.example.profileservice.service;

import com.example.profileservice.dto.request.ProfileCreationRequest;
import com.example.profileservice.dto.request.ProfileUpdateRequest;
import com.example.profileservice.dto.response.UserProfileResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserProfileService {
    String createUserProfile(ProfileCreationRequest request);
    boolean checkEmailExists(String email);
    UserProfileResponse getMyInfo();
    UserProfileResponse updateUserProfile(ProfileUpdateRequest request);
    List<UserProfileResponse> getAllUserProfiles();
    String deleteUserProfile(Integer userId);
}
