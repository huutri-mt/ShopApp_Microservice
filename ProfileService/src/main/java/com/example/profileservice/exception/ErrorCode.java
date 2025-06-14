package com.example.profileservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // --- Profile-related ---
    PROFILE_NOT_FOUND(1001, "Profile not found", HttpStatus.NOT_FOUND),
    INVALID_PROFILE_DATA(1002, "Invalid profile data provided", HttpStatus.BAD_REQUEST),
    PROFILE_CREATION_FAILED(1003, "Profile creation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    PROFILE_UPDATE_FAILED(1004, "Profile update failed", HttpStatus.INTERNAL_SERVER_ERROR),
    PROFILE_DELETION_FAILED(1005, "Profile deletion failed", HttpStatus.INTERNAL_SERVER_ERROR),

    // --- General ---
    INTERNAL_SERVER_ERROR(1006, "An internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_ADDRESS_DATA(1007, "Invalid address data provided", HttpStatus.BAD_REQUEST),
    // --- User-related / Account-related ---
    EMAIL_ALREADY_EXISTS(2001, "Email already exists", HttpStatus.CONFLICT),
    USERNAME_ALREADY_EXISTS(2002, "Username already exists", HttpStatus.CONFLICT),
    USER_NOT_FOUND(2003, "User not found", HttpStatus.NOT_FOUND),
    INVALID_USER_DATA(2004, "Invalid user data", HttpStatus.BAD_REQUEST),
    USER_CREATION_FAILED(2005, "Failed to create user", HttpStatus.INTERNAL_SERVER_ERROR),

    // --- Authentication & Authorization ---
    UNAUTHORIZED(3001, "Unauthorized access", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(3002, "Access denied", HttpStatus.FORBIDDEN),
    INVALID_CREDENTIALS(3003, "Invalid username or password", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(3004, "Authentication token expired", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(3005, "Invalid authentication token", HttpStatus.UNAUTHORIZED),

    UNCATEGORIZED_EXCEPTION(9999, "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
