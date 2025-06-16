package com.example.profileservice.service.Impl;

import com.example.profileservice.dto.request.ProfileCreationRequest;
import com.example.profileservice.dto.request.UpdateProfileRequest;
import com.example.profileservice.dto.response.UserProfileResponse;
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
import java.util.List;
import java.util.stream.Collectors;


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

    public UserProfileResponse getUserProfileById(int id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        return UserProfileResponse.builder()

                .fullName(userProfile.getFullName())
                .email(userProfile.getEmail())
                .phoneNumber(userProfile.getPhoneNumber())
                .dateOfBirth(userProfile.getDateOfBirth())
                .addresses(userProfile.getAddresses())
                .build();
    }
    public UserProfileResponse updateUserProfile(UpdateProfileRequest request, int userId) {
        if (request == null) {
            throw new AppException(ErrorCode.INVALID_PROFILE_DATA);
        }

        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        if (request.getEmail() != null &&
                !request.getEmail().equals(userProfile.getEmail()) &&
                userProfileRepository.existsUserProfileByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (request.getFullName() != null) {
            userProfile.setFullName(request.getFullName());
        }

        if (request.getEmail() != null) {
            userProfile.setEmail(request.getEmail());
        }

        if (request.getPhoneNumber() != null) {
            userProfile.setPhoneNumber(request.getPhoneNumber());
        }

        if (request.getDateOfBirth() != null) {
            userProfile.setDateOfBirth(request.getDateOfBirth());
        }

        if (request.getGender() != null) {
            userProfile.setGender(request.getGender());
        }

        userProfileRepository.save(userProfile);

        return UserProfileResponse .builder()
                .fullName(userProfile.getFullName())
                .email(userProfile.getEmail())
                .phoneNumber(userProfile.getPhoneNumber())
                .dateOfBirth(userProfile.getDateOfBirth())
                .gender(userProfile.getGender())
                .addresses(userProfile.getAddresses())
                .build();
    }
    public List<UserProfileResponse> getAllUserProfiles() {
        return userProfileRepository.findAll().stream()
                .map(userProfile -> UserProfileResponse.builder()
                        .fullName(userProfile.getFullName())
                        .email(userProfile.getEmail())
                        .phoneNumber(userProfile.getPhoneNumber())
                        .dateOfBirth(userProfile.getDateOfBirth())
                        .build())
                .collect(Collectors.toList());
    }

}
