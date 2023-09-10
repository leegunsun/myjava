package com.example.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void createUserWithTransaction() {

        // 현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now();

        // 원하는 형식으로 시간 포맷하기
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ss");
        String formattedNow = now.format(formatter);

        UserEntity user1 = new UserEntity();

        user1.setEmail("Trans_email [" + formattedNow + "]");
        user1.setName("Trans_name [" + formattedNow + "]");
        userRepository.save(user1);

        // if (true) {
        // throw new RuntimeException("intenionally cause an error");
        // }

        UserEntity user2 = new UserEntity();
        user2.setName("Trans_name2 [" + formattedNow + "]");
        user2.setEmail("Trans_email2 [" + formattedNow + "]");
        userRepository.save(user2);
    }

    public void updateUser(UserEntity user2) {

        userRepository.save(user2);

    }

}
