package com.example.profileservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressUpdateRequest {
    @Size(max = 100, message = "Tên liên hệ không vượt quá 100 ký tự")
    String contactName;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Số điện thoại không hợp lệ")
    String contactPhone;

    @Size(max = 255, message = "Địa chỉ dòng 1 không vượt quá 255 ký tự")
    String addressLine1;

    @Size(max = 255, message = "Địa chỉ dòng 2 không vượt quá 255 ký tự")
    String addressLine2;

    @Size(max = 100, message = "Tên thành phố không vượt quá 100 ký tự")
    String city;

    @Size(max = 100, message = "Tên tỉnh/thành phố không vượt quá 100 ký tự")
    String province;

    @Pattern(regexp = "^[0-9]{5,10}$", message = "Mã bưu điện không hợp lệ")
    String postalCode;

    @Size(max = 100, message = "Tên quốc gia không vượt quá 100 ký tự")
    String country;

    Boolean isDefault;
}
