package com.example.orderservice.exception;

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
    INVALID_SHIPPING_ADDRESS (1006, "Địa chỉ giao hàng không hợp lệ", HttpStatus.BAD_REQUEST),

    // --- Lỗi chung ---
    INTERNAL_SERVER_ERROR(1006, "Lỗi máy chủ nội bộ", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_ADDRESS_DATA(1007, "Dữ liệu địa chỉ không hợp lệ", HttpStatus.BAD_REQUEST),

    // --- Lỗi liên quan đến người dùng/tài khoản ---
    EMAIL_ALREADY_EXISTS(2001, "Email đã tồn tại", HttpStatus.CONFLICT),
    USERNAME_ALREADY_EXISTS(2002, "Tên người dùng đã tồn tại", HttpStatus.CONFLICT),
    USER_NOT_FOUND(2003, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    INVALID_USER_DATA(2004, "Dữ liệu người dùng không hợp lệ", HttpStatus.BAD_REQUEST),
    USER_CREATION_FAILED(2005, "Tạo người dùng thất bại", HttpStatus.INTERNAL_SERVER_ERROR),
    ACCOUNT_DISABLED(2006, "Tài khoản bị vô hiệu hóa", HttpStatus.FORBIDDEN),
    PASSWORD_EXISTED(2007, "Mật khẩu đã tồn tại", HttpStatus.BAD_REQUEST),

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
    PRODUCT_NOT_FOUND(4001, "Không tìm thấy sản phẩm", HttpStatus.NOT_FOUND),
    PRODUCT_CREATION_FAILED(4002, "Tạo sản phẩm thất bại", HttpStatus.INTERNAL_SERVER_ERROR),
    PRODUCT_UPDATE_FAILED(4003, "Cập nhật sản phẩm thất bại", HttpStatus.INTERNAL_SERVER_ERROR),
    PRODUCT_DELETION_FAILED(4004, "Xóa sản phẩm thất bại", HttpStatus.INTERNAL_SERVER_ERROR),
    PRODUCT_ALREADY_EXISTS(4005, "Sản phẩm đã tồn tại", HttpStatus.CONFLICT),
    INVALID_QUANTITY(4006, "Số lượng sản phẩm không hợp lệ", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_STOCK(4007, "Không đủ hàng trong kho", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_AVAILABLE (4008, "Sản phẩm không có sẵn", HttpStatus.NOT_FOUND),
    // --- Lỗi liên quan đến danh mục ---
    CATEGORY_NOT_FOUND(5001, "Không tìm thấy danh mục", HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXISTS (5002, "Danh mục đã tồn tại", HttpStatus.CONFLICT),
    INVALID_CATEGORY_NAME(5003, "Tên danh mục không hợp lệ", HttpStatus.BAD_REQUEST),
    PRODUCT_INFO_UNAVAILABLE (5004, "Thông tin sản phẩm không có sẵn", HttpStatus.NOT_FOUND),

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
    // --- Lỗi khác ---
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}