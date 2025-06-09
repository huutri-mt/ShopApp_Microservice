package com.example.profileservice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressCreationRequest {
    String contactName;
    String contactPhone;
    String addressLine1;
    String addressLine2;
    String city;
    String province;
    String postalCode;
    String country;
    Boolean isDefault;
}
