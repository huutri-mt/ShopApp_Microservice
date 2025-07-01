package com.example.profileservice.dto.request;

import com.example.profileservice.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileUpdateRequest {
    @Size(min = 2, max = 100, message = "Họ tên phải từ 2 đến 100 ký tự")
    String fullName;

    @Email(message = "Email không đúng định dạng")
    @Size(max = 100, message = "Email không vượt quá 100 ký tự")
    String email;

    @Pattern(regexp = "^[+]?[0-9]{10,15}$",
            message = "Số điện thoại phải có 10-15 số và có thể bao gồm mã quốc gia")
    String phoneNumber;

    @Past(message = "Ngày sinh phải trong quá khứ")
    LocalDate dateOfBirth;

    @Pattern(regexp = "^(NAM|NỮ|KHÁC)$",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Giới tính phải là NAM, NỮ hoặc KHÁC")
    Gender gender;
}
