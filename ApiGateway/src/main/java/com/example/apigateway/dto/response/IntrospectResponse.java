package com.example.apigateway.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntrospectResponse {
    private boolean valid;
    // Add getter/setter or use @Data

    // Add this method if not using boolean naming convention
    public boolean isValid() {
        return valid;
    }
}



