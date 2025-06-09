package com.example.profileservice.service.Impl;

import com.example.profileservice.dto.request.ProfileCreationRequest;
import com.example.profileservice.entity.UserProfile;
import com.example.profileservice.exception.AppException;
import com.example.profileservice.exception.ErrorCode;
import com.example.profileservice.repository.UserProfileRepository;
import com.example.profileservice.service.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@Primary
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {
    @Autowired
    private UserProfileRepository userProfileRepository;

    public String createUserProfile(ProfileCreationRequest request) {
        if(request == null) {
            throw new AppException(ErrorCode.INVALID_PROFILE_DATA);
        }
        UserProfile userProfile = new UserProfile();

        if (userProfileRepository.existsUserProfileByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        userProfileRepository.save(
            UserProfile.builder()
                    .id(request.getUserId())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                    .createAt(LocalDate.now())
                .build()
        );
        return "User profile created successfully";
    }

    public boolean checkEmailExists(String email) {
        return userProfileRepository.existsUserProfileByEmail(email);
    }

}
