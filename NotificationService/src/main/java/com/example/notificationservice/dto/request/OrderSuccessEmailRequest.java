package com.example.notificationservice.dto.request;

import com.example.notificationservice.dto.response.ProductResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.text.DecimalFormat;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderSuccessEmailRequest extends BaseEmailRequest {
    String username;
    int orderId;
    List<ProductResponse> products;
    double totalPrice;
    String formatTotalPrice;

    // Thêm phương thức format tổng tiền
    public String getFormattedTotalPrice() {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(this.totalPrice) + "đ";
    }
}
