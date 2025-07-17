package com.example.profileservice.service.Impl;

import com.example.event.dto.NotificationEvent;
import com.example.profileservice.dto.request.ProfileCreationRequest;
import com.example.profileservice.dto.request.ProfileUpdateRequest;
import com.example.profileservice.dto.response.UserProfileResponse;
import com.example.profileservice.dto.response.UserProfileResponseInternal;
import com.example.profileservice.entity.UserProfile;
import com.example.profileservice.exception.AppException;
import com.example.profileservice.exception.ErrorCode;
import com.example.profileservice.repository.UserProfileRepository;
import com.example.profileservice.service.UserProfileService;
import com.example.profileservice.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Primary
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public UserProfileResponseInternal getProfile (int email) {
        UserProfile userProfile = userProfileRepository.findById(email)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        return UserProfileResponseInternal.builder()
                .userId(userProfile.getId())
                .fullName(userProfile.getFullName())
                .email(userProfile.getEmail())
                .phoneNumber(userProfile.getPhoneNumber())
                .dateOfBirth(userProfile.getDateOfBirth())
                .gender(userProfile.getGender())
                .build();
    }

public UserProfileResponseInternal getProfile(String email) {
        UserProfile userProfile = userProfileRepository.findByEmail(email);
        if (userProfile == null) {
            throw new AppException(ErrorCode.PROFILE_NOT_FOUND);
        }
    return UserProfileResponseInternal.builder()
            .userId(userProfile.getId())
            .fullName(userProfile.getFullName())
            .email(userProfile.getEmail())
            .phoneNumber(userProfile.getPhoneNumber())
            .dateOfBirth(userProfile.getDateOfBirth())
            .gender(userProfile.getGender())
            .build();
}
    public String createUserProfile(ProfileCreationRequest request) {
        if(request == null) {
            throw new AppException(ErrorCode.INVALID_PROFILE_DATA);
        }

        if (userProfileRepository.existsUserProfileByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        log.info( "Creating user profile with request: {}", request);

        UserProfile profile = UserProfile.builder()
                .id(request.getUserId())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .build();

        try {
            userProfileRepository.save(profile);
            log.info("Created profile for user ID: {}", request.getUserId());
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.PROFILE_CREATION_FAILED);
        }

        //kafka
        Map<String, Object> data = new HashMap<>();
        data.put("fullName", request.getFullName());
        data.put("registerTime", LocalDate.now());

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel("email")
                .recipient(request.getEmail())
                .template("user_created")
                .data(data)
                .build();
        try {
            kafkaTemplate.send("notification-delivery", notificationEvent);
        } catch (Exception e) {
            log.error("Failed to send Kafka notification event", e);
        }
        return "User profile created successfully";
    }

    public boolean checkEmailExists(String email) {
        return userProfileRepository.existsUserProfileByEmail(email);
    }

    public UserProfileResponse getMyInfo() {
        Integer userId = SecurityUtil.getCurrentUserId();

        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        return UserProfileResponse.builder()
                .fullName(userProfile.getFullName())
                .email(userProfile.getEmail())
                .phoneNumber(userProfile.getPhoneNumber())
                .dateOfBirth(userProfile.getDateOfBirth())
                .addresses(userProfile.getAddresses())
                .build();
    }

    public UserProfileResponse updateUserProfile(ProfileUpdateRequest request ) {
        log.info("Updating user profile with request: {}", request);
        if (request == null) {
            throw new AppException(ErrorCode.INVALID_PROFILE_DATA);
        }

        Integer userId = SecurityUtil.getCurrentUserId();

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

        Map<String, Object> data = new HashMap<>();
        data.put("fullName", userProfile.getFullName());
        data.put("updateTime", LocalDate.now());

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel("email")
                .recipient(userProfile.getEmail())
                .template("user_updated")
                .data(data)
                .build();
        try {
            kafkaTemplate.send("notification-delivery", notificationEvent);
            log.info("email: ",notificationEvent.getRecipient());
        } catch (Exception e) {
            log.error("Failed to send Kafka notification event", e);
        }

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

    public String deleteUserProfile(Integer userId) {
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_FOUND));

        userProfileRepository.delete(userProfile);
        return "User profile deleted successfully";
    }

}
