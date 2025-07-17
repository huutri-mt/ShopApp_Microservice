package com.example.notificationservice.controller;

import com.example.event.dto.NotificationEvent;
import com.example.notificationservice.service.MailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationController {
    @Autowired
    private MailService mailService;

    @KafkaListener(topics = "notification-delivery")
    public void handleNotification(NotificationEvent request) {
        log.info("Received notification message: {}", request.getRecipient());
        mailService.send(request.getRecipient(), request.getTemplate(), request.getData());

    }
}
