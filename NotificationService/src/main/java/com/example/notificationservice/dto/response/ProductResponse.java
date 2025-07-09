package com.example.notificationservice.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.text.DecimalFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String name;
    int quantity;
    double price;

    public String getFormattedPrice() {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(this.price) + "đ";
    }

    public double getTotalPrice() {
        return this.price * this.quantity;
    }

    public String getFormattedTotalPrice() {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(getTotalPrice()) + "đ";
    }
}