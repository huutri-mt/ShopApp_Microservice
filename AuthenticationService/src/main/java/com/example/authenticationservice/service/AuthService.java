package com.example.authenticationservice.service;

import com.example.authenticationservice.dto.request.*;
import com.example.authenticationservice.dto.response.IntrospectResponse;
import com.example.authenticationservice.dto.response.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    String createUser(UserCreationRequest request);
    LoginResponse login(LoginRequest request);
    String logout(LogoutRequest token);
    LoginResponse refreshToken(RefreshRequest request);
    IntrospectResponse introspect(IntrospectRequest request);
    LoginResponse outboundAuthenticate(String code, String provider);
    String changePassword(ChangePasswordRequest request);
}
