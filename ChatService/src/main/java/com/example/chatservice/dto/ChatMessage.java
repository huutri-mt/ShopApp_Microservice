package com.example.chatservice.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class ChatMessage {
    private String content;
    private Long senderId;
    private String senderRole; // "USER" or "ADMIN"
    private Long receiverId;   // chỉ dùng khi admin reply
    private Instant timestamp;
}

