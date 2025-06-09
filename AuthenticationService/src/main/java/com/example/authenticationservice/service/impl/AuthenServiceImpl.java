package com.example.authenticationservice.service.impl;

import com.example.authenticationservice.dto.request.LoginRequest;
import com.example.authenticationservice.dto.request.ProfileCreationRequest;
import com.example.authenticationservice.dto.request.UserCreationRequest;
import com.example.authenticationservice.dto.response.LoginResponse;
import com.example.authenticationservice.entity.AuthenUser;
import com.example.authenticationservice.exception.AppException;
import com.example.authenticationservice.exception.ErrorCode;
import com.example.authenticationservice.repository.AuthRepository;
import com.example.authenticationservice.repository.httpClient.ProfileClient;
import com.example.authenticationservice.service.AuthService;
import com.example.authenticationservice.utill.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Service
@Primary
public class AuthenServiceImpl implements AuthService {

    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private ProfileClient profileClient;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public String createUser(UserCreationRequest request) {
        if(request == null) {
            throw new AppException(ErrorCode.PROFILE_NOT_FOUND);
        }
        if(authRepository.existsUserByUserName(request.getUserName())) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
        if(Boolean.TRUE.equals(profileClient.checkEmailExists(request.getEmail()))) {
            throw  new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        log.info("request : {}", request);
        AuthenUser authenUser = AuthenUser.builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .enabled(true)
                .build();


        authRepository.save(authenUser);
        ProfileCreationRequest profileCreationRequest = ProfileCreationRequest.builder()
                .userId(authenUser.getId())
                .fullName(request.getFullname())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .build();

        try {
            profileClient.createProfile(profileCreationRequest);
        } catch (Exception e) {
            authRepository.delete(authenUser);
            log.error("Failed to create profile", e);
            throw new AppException(ErrorCode.PROFILE_CREATION_FAILED);
        }
        return "User created successfully";
    }

    public LoginResponse login(LoginRequest request) {
        if(request == null) {
            throw new AppException(ErrorCode.PROFILE_NOT_FOUND);
        }
        AuthenUser authenUser = authRepository.findByUserName(request.getUserName());
        if (authenUser == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        if(!passwordEncoder.matches(request.getPassword(), authenUser.getPassword())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        if(!authenUser.getEnabled()) {
            throw new AppException(ErrorCode.ACCOUNT_DISABLED);
        }
        String token = jwtUtil.generateToken(authenUser);
        LoginResponse loginResponse = LoginResponse.builder()
                .token(token)
                .build();
        return loginResponse;
    }


}
