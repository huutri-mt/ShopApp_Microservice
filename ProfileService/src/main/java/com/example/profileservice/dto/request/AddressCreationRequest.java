package com.example.profileservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressCreationRequest {

    @NotBlank(message = "Tên liên hệ không được để trống")
    @Size(max = 100, message = "Tên liên hệ không vượt quá 100 ký tự")
    String contactName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Số điện thoại không hợp lệ")
    String contactPhone;

    @NotBlank(message = "Địa chỉ dòng 1 không được để trống")
    @Size(max = 255, message = "Địa chỉ dòng 1 không vượt quá 255 ký tự")
    String addressLine1;

    @Size(max = 255, message = "Địa chỉ dòng 2 không vượt quá 255 ký tự")
    String addressLine2;

    @NotBlank(message = "Thành phố không được để trống")
    @Size(max = 100, message = "Tên thành phố không vượt quá 100 ký tự")
    String city;

    @NotBlank(message = "Tỉnh/Thành phố không được để trống")
    @Size(max = 100, message = "Tên tỉnh/thành phố không vượt quá 100 ký tự")
    String province;

    @NotBlank(message = "Mã bưu điện không được để trống")
    @Pattern(regexp = "^[0-9]{5,10}$", message = "Mã bưu điện không hợp lệ")
    String postalCode;

    @NotBlank(message = "Quốc gia không được để trống")
    @Size(max = 100, message = "Tên quốc gia không vượt quá 100 ký tự")
    String country;

    @NotNull(message = "Trường isDefault không được null")
    Boolean isDefault;
}