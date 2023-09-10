package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
}
