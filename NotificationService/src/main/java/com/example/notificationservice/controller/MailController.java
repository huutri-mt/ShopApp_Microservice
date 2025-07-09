package com.example.notificationservice.controller;

import com.example.notificationservice.dto.request.*;
import com.example.notificationservice.dto.response.ProductResponse;
import com.example.notificationservice.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/user-created")
    public ResponseEntity<String> userCreated(@RequestBody UserCreatedEmailRequest request) {
        Map<String, Object> data = Map.of(
                "username", request.getUsername(),
                "registerTime", request.getRegisterTime()
        );
        mailService.send(request.getTo(), "user_created", data);
        return ResponseEntity.ok("Email đã gửi!");
    }

    @PostMapping("/order-success")
    public ResponseEntity<String> orderSuccess(@RequestBody OrderSuccessEmailRequest request) {
        request.getProducts().forEach(ProductResponse::getFormattedPrice);

        Map<String, Object> data = new HashMap<>();
        data.put("username", request.getUsername());
        data.put("orderId", request.getOrderId());
        data.put("totalPrice", request.getTotalPrice());
        data.put("formattedTotalPrice", request.getFormattedTotalPrice());
        data.put("products", request.getProducts());

        mailService.send(request.getTo(), "order_success", data);
        return ResponseEntity.ok("Email sent successfully!");
    }

    @PostMapping("/payment-success")
    public ResponseEntity<String> paymentSuccess(@RequestBody PaymentSuccessEmailRequest request) {
        Map<String, Object> data = Map.of(
                "username", request.getUsername(),
                "orderId", request.getOrderId(),
                "paymentTime", request.getPaymentTime()
        );
        mailService.send(request.getTo(), "payment_success", data);
        return ResponseEntity.ok("Email đã gửi!");
    }

    @PostMapping("/password-changed")
    public ResponseEntity<String> passwordChanged(@RequestBody PasswordChangedEmailRequest request) {
        Map<String, Object> data = Map.of(
                "username", request.getUsername(),
                "changeTime", request.getChangeTime()
        );
        mailService.send(request.getTo(), "password_changed", data);
        return ResponseEntity.ok("Email đã gửi!");
    }

    @PostMapping("/user-updated")
    public ResponseEntity<String> userUpdated(@RequestBody UserUpdateEmailRequest request) {
        Map<String, Object> data = Map.of(
                "username", request.getUsername(),
                "updateTime", request.getUpdateTime()
        );
        mailService.send(request.getTo(), "user_updated", data);
        return ResponseEntity.ok("Email đã gửi!");
    }

    @PostMapping("/new-message")
    public ResponseEntity<String> newMessage(@RequestBody NewMessageEmailRequest request) {
        Map<String, Object> data = Map.of(
                "fromUser", request.getFromUser(),
                "messagePreview", request.getMessagePreview()
        );
        mailService.send(request.getTo(), "new_message", data);
        return ResponseEntity.ok("Email đã gửi!");
    }
}

