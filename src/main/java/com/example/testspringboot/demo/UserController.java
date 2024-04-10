package com.example.testspringboot.demo;

import com.example.testspringboot.demo.AppConfig;
import com.example.testspringboot.demo.UserRepository;
import com.example.testspringboot.demo.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AppConfig appConfig;

    @GetMapping("car")
    public ResponseEntity<?> carTest() {
        try {
            Car car = appConfig.car();

            car.drive();

            return new ResponseEntity<>("Car test succeeded!", null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Car test ERROR", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("user")
    public UserResponse findAllUser() {
        System.out.println("콘솔 테스트");
        List<UserEntity> users = userRepository.findAll();
        return new UserResponse(users);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestParam("page") int page, @RequestParam("size") int size) {

        try {
            if (page <= 0) {
                throw new IllegalArgumentException("Page number should be greater than 0.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR");
        }
        List<UserEntity> users = userRepository.findAll(PageRequest.of(page - 1, size)).getContent();

        return ResponseEntity.ok(users);
    }

    @GetMapping("signup")
    public UserEntity signupWIthGet(@RequestParam("name") String name, @RequestParam("email") String email) {
        UserEntity user = UserEntity.builder()
                .name(name)
                .email(email)
                .build();

        return userRepository.save(user);
    }


}