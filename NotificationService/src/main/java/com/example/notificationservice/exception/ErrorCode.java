package com.example.notificationservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // --- Lỗi liên quan đến hồ sơ ---
    PROFILE_NOT_FOUND(1001, "Không tìm thấy hồ sơ", HttpStatus.NOT_FOUND),
    INVALID_PROFILE_DATA(1002, "Dữ liệu hồ sơ không hợp lệ", HttpStatus.BAD_REQUEST),
    PROFILE_CREATION_FAILED(1003, "Tạo hồ sơ thất bại", HttpStatus.INTERNAL_SERVER_ERROR),
    PROFILE_UPDATE_FAILED(1004, "Cập nhật hồ sơ thất bại", HttpStatus.INTERNAL_SERVER_ERROR),
    PROFILE_DELETION_FAILED(1005, "Xóa hồ sơ thất bại", HttpStatus.INTERNAL_SERVER_ERROR),

    // --- Lỗi chung ---
    INTERNAL_SERVER_ERROR(1006, "Lỗi máy chủ nội bộ", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_ADDRESS_DATA(1007, "Dữ liệu địa chỉ không hợp lệ", HttpStatus.BAD_REQUEST),

    // --- Lỗi liên quan đến người dùng/tài khoản ---
    EMAIL_ALREADY_EXISTS(2001, "Email đã tồn tại", HttpStatus.CONFLICT),
    USERNAME_ALREADY_EXISTS(2002, "Tên đăng nhập đã tồn tại", HttpStatus.CONFLICT),
    USER_NOT_FOUND(2003, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    INVALID_USER_DATA(2004, "Dữ liệu người dùng không hợp lệ", HttpStatus.BAD_REQUEST),
    USER_CREATION_FAILED(2005, "Tạo người dùng thất bại", HttpStatus.INTERNAL_SERVER_ERROR),
    ADDRESS_NOT_FOUND(2006, "Không tìm thấy địa chỉ", HttpStatus.NOT_FOUND),
    ADDRESS_ALREADY_EXISTS(2007, "Địa chỉ đã tồn tại", HttpStatus.CONFLICT),

    // --- Lỗi xác thực & phân quyền ---
    UNAUTHORIZED(3001, "Không có quyền truy cập", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(3002, "Truy cập bị từ chối", HttpStatus.FORBIDDEN),
    INVALID_CREDENTIALS(3003, "Tên đăng nhập hoặc mật khẩu không đúng", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(3004, "Token xác thực đã hết hạn", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(3005, "Token xác thực không hợp lệ", HttpStatus.UNAUTHORIZED),

    // --- Lỗi khác ---
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}