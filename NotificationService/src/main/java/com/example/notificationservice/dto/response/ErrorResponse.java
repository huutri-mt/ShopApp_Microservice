package com.example.notificationservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {
    int code;
    String message;
}
