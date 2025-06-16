package com.example.authenticationservice.service.impl;

import com.example.authenticationservice.dto.request.*;
import com.example.authenticationservice.dto.response.ExchangeTokenResponse;
import com.example.authenticationservice.dto.response.OutboundUserResponse;
import com.example.authenticationservice.dto.response.IntrospectResponse;
import com.example.authenticationservice.dto.response.LoginResponse;
import com.example.authenticationservice.entity.AuthenUser;
import com.example.authenticationservice.entity.InvalidatedToken;
import com.example.authenticationservice.exception.AppException;
import com.example.authenticationservice.exception.ErrorCode;
import com.example.authenticationservice.repository.AuthRepository;
import com.example.authenticationservice.repository.InvalidatedTokenRepository;
import com.example.authenticationservice.repository.httpClient.*;
import com.example.authenticationservice.service.AuthService;
import com.example.authenticationservice.utill.JwtUtil;
import com.nimbusds.jose.JOSEException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    private InvalidatedTokenRepository invalidatedTokenRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private FacebookIdentityClient facebookIdentityClient;

    @Autowired
    private GoogleUserClient googleUserClient;

    @Autowired
    private FacebookUserClient facebookUserClient;

    @Autowired
    private GoogleIdentityClient googleIdentityClient;

    @Value("${google.client-id}")
    private String googleClientId;
    @Value("${google.client-secret}")
    private String googleClientSecret;
    @Value("${google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${facebook.client-id}")
    private String facebookClientId;
    @Value("${facebook.client-secret}")
    private String facebookClientSecret;
    @Value("${facebook.redirect-uri}")
    private String facebookRedirectUri;
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

    public String logout(LogoutRequest request) {
        try {
            var signToken = jwtUtil.verifyToken(request.getToken(), true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = new InvalidatedToken();
            invalidatedToken.setId(jit);
            invalidatedToken.setExpiryTime(expiryTime);

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (JOSEException | ParseException e) {
            throw new AppException(ErrorCode.TOKEN_PARSING_ERROR);
        }
        return "Logout successful";
    }

    public LoginResponse refreshToken(RefreshRequest request) {
        try {
            var signToken = jwtUtil.verifyToken(request.getToken(), true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken = new InvalidatedToken();
            invalidatedToken.setId(jit);
            invalidatedToken.setExpiryTime(expiryTime);

            invalidatedTokenRepository.save(invalidatedToken);

            String userName = signToken.getJWTClaimsSet().getSubject();
            AuthenUser user = authRepository.findByUserName(userName);
            if (user == null) {
                throw new AppException(ErrorCode.PROFILE_NOT_FOUND);
            }
            String token = jwtUtil.generateToken(user);

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(token);
            return loginResponse;

        } catch (JOSEException | ParseException e) {
            throw new AppException(ErrorCode.TOKEN_PARSING_ERROR);
        }
    }

    public IntrospectResponse introspect(IntrospectRequest request) {
        String token = request.getToken();
        if (token == null || token.isEmpty()) {
            throw new AppException(ErrorCode.TOKEN_PARSING_ERROR);
        }

        IntrospectResponse introspectResponse = new IntrospectResponse();

        try {
            introspectResponse.setValid(jwtUtil.validateToken(token));
        } catch (ParseException e) {
            throw new AppException(ErrorCode.AUTH_TOKEN_INVALID);
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.AUTH_TOKEN_INVALID);
        }
        log.info("Introspect token: {}, valid: {}", token, introspectResponse.isValid());
        return introspectResponse;
    }
    public LoginResponse outboundAuthenticate(String code, String provider) {
        Map<String, String> formData = new HashMap<>();
        String clientId;
        String clientSecret;
        String redirectUri;
        if ("google".equalsIgnoreCase(provider)) {
            clientId = googleClientId;
            clientSecret = googleClientSecret;
            redirectUri = googleRedirectUri;
        } else if ("facebook".equalsIgnoreCase(provider)) {
            clientId = facebookClientId;
            clientSecret = facebookClientSecret;
            redirectUri = facebookRedirectUri;
        } else {
            throw new AppException(ErrorCode.UNKNOWN_PROVIDER);
        }

        formData.put("code", code);
        formData.put("client_id", clientId);
        formData.put("client_secret", clientSecret);
        formData.put("redirect_uri", redirectUri);
        formData.put("grant_type", "authorization_code");
        log.info("Sending formData: {}", formData);

        ExchangeTokenResponse response;
        OutboundUserResponse userInfo;

        try {
            if ("google".equalsIgnoreCase(provider)) {
                response = googleIdentityClient.exchangeToken(formData);
                userInfo = googleUserClient.getUserInfo("json", response.getAccessToken());
            } else {
                response = facebookIdentityClient.exchangeToken(formData);
                userInfo = facebookUserClient.getUserInfo("id,name,email,picture", response.getAccessToken());

            }
        }  catch (FeignException e) {
            String errorMessage = e.contentUTF8();
            log.error("FeignException status: {}, content: {}", e.status(), errorMessage, e);

            if (errorMessage.contains("redirect_uri_mismatch")) {
                throw new AppException(ErrorCode.REDIRECT_URI_MISMATCH);
            }

            throw new AppException(ErrorCode.OAUTH_PROVIDER_ERROR);
        }

        // Kiểm tra hoặc tạo user
        AuthenUser authenUser;
        if(Boolean.FALSE.equals(profileClient.checkEmailExists(userInfo.getEmail()))) {
            authenUser = AuthenUser.builder()
                    .userName(userInfo.getEmail().split("@")[0])
                    .role("USER")
                    .enabled(true)
                    .build();
            authRepository.save(authenUser);
            ProfileCreationRequest profileCreationRequest = ProfileCreationRequest.builder()
                    .fullName(userInfo.getName())
                    .email(userInfo.getEmail())
                    .build();
            try {
                profileClient.createProfile(profileCreationRequest);
            } catch (Exception e) {
                authRepository.delete(authenUser);
                log.error("Failed to create profile", e);
                throw new AppException(ErrorCode.PROFILE_CREATION_FAILED);
            }

        }
        else {
            String email = userInfo.getEmail();
            int userId = profileClient.getProfileByEmail(email).getBody();
            authenUser = authRepository.getUserById(userId);
        }

        String jwt = jwtUtil.generateToken(authenUser);
        log.info("token: {}",jwt);
        return LoginResponse.builder().token(jwt).build();
    }

    public String changePassword(ChangePasswordRequest request) {
        if (request == null || request.getNewPassword() == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        AuthenUser authenUser = authRepository.findByUserName(userName);
        if (authenUser == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(request.getOldPassword(), authenUser.getPassword())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }
        if (request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        authenUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        authRepository.save(authenUser);

        return "Password changed successfully";
    }
}

