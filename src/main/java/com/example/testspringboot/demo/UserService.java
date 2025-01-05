package com.example.testspringboot.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createUserWithTransaction() {

        // 현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now();

        // 원하는 형식으로 시간 포맷하기
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ss");
        String formattedNow = now.format(formatter);

        UserEntity user1 = new UserEntity();

        user1.setEmail("Trans_email [" + formattedNow + "]");
        user1.setUsername("Trans_name [" + formattedNow + "]");
        userRepository.save(user1);

        UserEntity user2 = new UserEntity();
        user2.setUsername("Trans_name2 [" + formattedNow + "]");
        user2.setEmail("Trans_email2 [" + formattedNow + "]");
        userRepository.save(user2);
    }

    @Transactional
    public Optional<UserEntity> getUserWithPessimisticLock(Long userId) {
        UserEntity user = entityManager.find(UserEntity.class, userId,
                LockModeType.PESSIMISTIC_WRITE);

        // Optional로 결과 반환
        return Optional.ofNullable(user);
    }

    @Transactional
    public void updateUser(UserEntity user2) {

        userRepository.save(user2);

    }

}

