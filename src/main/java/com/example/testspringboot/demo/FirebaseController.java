package com.example.testspringboot.demo;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Service
@RestController
@RequiredArgsConstructor
public class FirebaseController {

    private final FirebaseMessaging firebaseMessaging;

    public static Message makeMessage(String targetToken, String title, String body) {
        Notification notification = Notification
                .builder()
                .setTitle(title)
                .setBody(body)
                .build();
        return Message
                .builder()
                .setNotification(notification)
                .setToken(targetToken)
                .build();
    }

    @PostMapping("sendMessage")
    public ResponseEntity<String> sendMessage (@RequestBody Map<String, String> requestBody) throws FirebaseMessagingException {
        String token = requestBody.get("token");
        Message message = makeMessage(token, "테스트 타이틀", "테스트 바디");
        firebaseMessaging.send(message);

        return ResponseEntity.ok("성공");
    }

    @GetMapping("testMessage")
    public ResponseEntity<String> testMessage () throws FirebaseMessagingException {


        return ResponseEntity.ok("성공");
    }
}
