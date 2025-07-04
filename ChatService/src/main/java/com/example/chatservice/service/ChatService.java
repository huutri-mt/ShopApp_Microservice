package com.example.chatservice.service;

import com.example.chatservice.dto.ChatMessage;
import com.example.chatservice.entity.Message;
import com.example.chatservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;

    public void userSendMessage(ChatMessage message) {
        message.setTimestamp(Instant.now());
        message.setSenderRole("USER");
        messageRepository.save(toEntity(message));
        messagingTemplate.convertAndSend("/topic/admin", message);
    }

    public void adminReply(ChatMessage message) {
        message.setTimestamp(Instant.now());
        message.setSenderRole("ADMIN");
        messageRepository.save(toEntity(message));
        messagingTemplate.convertAndSendToUser(message.getReceiverId().toString(), "/queue/messages", message);
    }

    private Message toEntity(ChatMessage m) {
        return new Message(null, m.getSenderId(), m.getSenderRole(), m.getReceiverId(), m.getContent(), m.getTimestamp());
    }
}

