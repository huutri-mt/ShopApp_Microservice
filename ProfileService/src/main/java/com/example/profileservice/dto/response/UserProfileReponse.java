package com.example.profileservice.dto.response;

import com.example.profileservice.entity.Addresses;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileReponse {
    String fullName;
    String email;
    String phoneNumber;
    String gender;
    String dateOfBirth;

    List<Addresses> addresses;
}
