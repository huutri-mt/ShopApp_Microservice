package com.example.chatservice.controller;

import com.example.chatservice.dto.ChatMessage;
import com.example.chatservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat/user-send")
    public void userSend(ChatMessage msg, Principal principal) {
        // principal.getName() -> lấy userId nếu có JWT
        chatService.userSendMessage(msg);
    }

    @MessageMapping("/chat/admin-reply")
    public void adminReply(ChatMessage msg, Principal principal) {
        chatService.adminReply(msg);
    }
}

