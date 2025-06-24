package com.example.authenticationservice.controller;

import com.example.authenticationservice.constan.UrlConstan;
import com.example.authenticationservice.dto.request.*;
import com.example.authenticationservice.dto.response.LoginResponse;
import com.example.authenticationservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(UrlConstan.API_V1_AUTH)
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserCreationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.createUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequest request) {
        return ResponseEntity.ok(authService.logout(request));
    }

    @PostMapping("/outbound/authentication")
    public ResponseEntity<LoginResponse> outboundAuthenticate(@RequestParam("code") String code,
                                                              @RequestParam("provider") String provider) {
        return ResponseEntity.ok(authService.outboundAuthenticate(code, provider));
    }
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.claims['userId']")
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(authService.changePassword(request));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/block-user/{id}")
    public ResponseEntity<String> blockUser(@PathVariable("id") Integer userId) {
        return ResponseEntity.ok(authService.blockUser(userId));
    }

    @PostMapping("/create-password")
    public ResponseEntity<String> createPassword(@RequestBody CreatePasswordRequest request) {
        return ResponseEntity.ok(authService.createPassword(request));
    }


}


