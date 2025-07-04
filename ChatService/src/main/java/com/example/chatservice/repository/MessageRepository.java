package com.example.chatservice.repository;

import com.example.chatservice.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
}

