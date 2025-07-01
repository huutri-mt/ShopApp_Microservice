package com.example.orderservice.enums;

public enum PaymentStatus {
    PAID("PAID"),
    UNPAID("UNPAID"),
    PENDING("PENDING"),
    FAILED("FAILED");

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
