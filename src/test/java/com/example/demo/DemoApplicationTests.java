package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.OptimisticLockingFailureException;

@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Test
	public void testOptimisticLocking() {
		UserEntity user = new UserEntity();
		user.setName("Initial Name");
		user.setEmail("initial@example.com");

		UserEntity savedUser = userRepository.save(user);

		UserEntity user1 = userRepository.findById(savedUser.getId()).get();
		UserEntity user2 = userRepository.findById(savedUser.getId()).get();

		user1.setName("Name 1");
		userService.updateUser(user1);

		assertThrows(OptimisticLockingFailureException.class, () -> {
			user2.setName("Name 2");
			userService.updateUser(user2);
		});
	}

	@Test
	public void testPessimisticLocking() {
		UserEntity user = new UserEntity();
		user.setName("Test User");
		user.setEmail("test@example.com");

		UserEntity savedUser = userRepository.save(user);

		Thread thread1 = new Thread(() -> {
			userService.getUserWithPessimisticLock(savedUser.getId());
			try {
				// 시간 지연을 위한 임시 코드
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		Thread thread2 = new Thread(() -> {
			assertThrows(CannotAcquireLockException.class, () -> {
				userService.getUserWithPessimisticLock(savedUser.getId());
			});
		});

		thread1.start();

		try {
			// thread1이 락을 획득하는데 약간의 시간을 줍니다.
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		thread2.start();

		try {
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertNotNull(userService.getUserWithPessimisticLock(savedUser.getId()).orElse(null));
	}
}
