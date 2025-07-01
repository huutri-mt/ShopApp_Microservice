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
public class ProfileCreationRequest {

    @NotNull(message = "ID người dùng không được để trống")
    @Positive(message = "ID người dùng phải là số dương")
    int userId;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(min = 2, max = 100, message = "Họ tên phải từ 2 đến 100 ký tự")
    String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Size(max = 100, message = "Email không vượt quá 100 ký tự")
    String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$",
            message = "Số điện thoại phải có 10-15 số và có thể bao gồm mã quốc gia")
    String phoneNumber;

    @NotNull(message = "Ngày sinh không được để trống")
    @Past(message = "Ngày sinh phải trong quá khứ")
    LocalDate dateOfBirth;

    @NotBlank(message = "Giới tính không được để trống")
    @Pattern(regexp = "^(NAM|NỮ|KHÁC)$",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Giới tính phải là NAM, NỮ hoặc KHÁC")
    Gender gender;
}