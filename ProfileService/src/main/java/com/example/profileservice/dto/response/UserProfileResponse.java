package com.example.profileservice.dto.response;

import com.example.profileservice.entity.Addresses;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserProfileResponse {
    String fullName;
    String email;
    String phoneNumber;
    String gender;
    LocalDate dateOfBirth;

    List<Addresses> addresses;
}
