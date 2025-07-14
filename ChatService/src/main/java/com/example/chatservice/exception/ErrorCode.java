package com.example.chatservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // --- Lỗi liên quan đến hồ sơ ---
    INVALID_SHIPPING_ADDRESS (1006, "Địa chỉ giao hàng không hợp lệ", HttpStatus.BAD_REQUEST),

    // --- Lỗi chung ---
    INTERNAL_SERVER_ERROR(1006, "Lỗi máy chủ nội bộ", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_ADDRESS_DATA(1007, "Dữ liệu địa chỉ không hợp lệ", HttpStatus.BAD_REQUEST),


    // --- Lỗi xác thực & ủy quyền ---
    UNAUTHORIZED(3001, "Truy cập trái phép", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(3002, "Truy cập bị từ chối", HttpStatus.FORBIDDEN),
    INVALID_CREDENTIALS(3003, "Tên người dùng hoặc mật khẩu không đúng", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(3004, "Token xác thực hết hạn", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(3005, "Token xác thực không hợp lệ", HttpStatus.UNAUTHORIZED),
    AUTH_TOKEN_INVALID(3006, "Token xác thực không hợp lệ", HttpStatus.UNAUTHORIZED),
    TOKEN_PARSING_ERROR(3007, "Lỗi phân tích token xác thực", HttpStatus.BAD_REQUEST),
    UNKNOWN_PROVIDER(3008, "Nhà cung cấp xác thực không xác định", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST(3000, "Yêu cầu không hợp lệ", HttpStatus.BAD_REQUEST),
    REDIRECT_URI_MISMATCH(3009, "Redirect URI không khớp", HttpStatus.BAD_REQUEST),
    OAUTH_PROVIDER_ERROR(3010, "Lỗi nhà cung cấp OAuth", HttpStatus.BAD_REQUEST),
    // --- Lỗi liên quan đến sản phẩm ---
    PRODUCT_NOT_AVAILABLE (4008, "Sản phẩm không có sẵn", HttpStatus.NOT_FOUND),

    // --- Lỗi liên quan đến giỏ hàng ---
    CART_NOT_FOUND (6001, "Không tìm thấy giỏ hàng", HttpStatus.NOT_FOUND),
    CART_EMPTY (6002, "Giỏ hàng trống", HttpStatus.BAD_REQUEST),
    CART_ITEM_NOT_FOUND (6003, "Không tìm thấy sản phẩm trong giỏ hàng", HttpStatus.NOT_FOUND),
    OPERATION_FAILED (6004, "Thao tác thất bại", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_CART_ID (6005, "ID giỏ hàng không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_CART_SELECTION (6006, "Lựa chọn giỏ hàng không hợp lệ", HttpStatus.BAD_REQUEST),

    // --- Lỗi liên quan đến đơn hàng ---
    ORDER_NOT_FOUND (7001, "Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND),
    ORDER_CANCELLATION_NOT_ALLOWED (7002, "Không thể hủy đơn hàng", HttpStatus.BAD_REQUEST),
    ORDER_STATUS_UPDATE_NOT_ALLOWED (7003, "Không thể cập nhật trạng thái đơn hàng", HttpStatus.BAD_REQUEST),
    ORDER_PAYMENT_FAILED (7004, "Thanh toán đơn hàng thất bại", HttpStatus.BAD_REQUEST),
    // --- Lỗi khác ---
    VNPAY_SIGNING_FAILED (8001, "Lỗi ký VNPAY", HttpStatus.INTERNAL_SERVER_ERROR),
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}