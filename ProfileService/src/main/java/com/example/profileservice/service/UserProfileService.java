package com.example.profileservice.service;

import com.example.profileservice.dto.request.ProfileCreationRequest;
import com.example.profileservice.dto.request.UpdateProfileRequest;
import com.example.profileservice.dto.response.UserProfileResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserProfileService {
    String createUserProfile(ProfileCreationRequest request);
    boolean checkEmailExists(String email);
    UserProfileResponse getUserProfileById(int id);
    UserProfileResponse updateUserProfile(UpdateProfileRequest request, int userId);
    List<UserProfileResponse> getAllUserProfiles();
}
