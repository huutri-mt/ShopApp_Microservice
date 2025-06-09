package com.example.authenticationservice.service;

import com.example.authenticationservice.dto.request.LoginRequest;
import com.example.authenticationservice.dto.request.UserCreationRequest;
import com.example.authenticationservice.dto.response.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    String createUser(UserCreationRequest request);
    LoginResponse login(LoginRequest request);
}
