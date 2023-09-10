package com.example.demo;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

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
            // TODO: handle exception
            return new ResponseEntity<>("Car test", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("user")
    public UserResponse findAllUser() {
        System.out.println("이것은 콘솔에 출력되는 메시지입니다.");
        List<UserEntity> users = userRepository.findAll();
        return new UserResponse(users);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestParam int page, @RequestParam int size) {

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
    public UserEntity signupWithGet(@RequestParam String name, @RequestParam String email) {
        UserEntity user = UserEntity.builder()
                .name(name)
                .email(email)
                .build();

        return userRepository.save(user);
    };

    @GetMapping("/set-session")
    public String setSessionAttribute(HttpSession session) {
        session.setAttribute("user", "ChatGPT");
        return "Session attribute 'user' set!";
    }

    @GetMapping("/get-session")
    public String getSessionAttribute(HttpSession session) {
        return (String) session.getAttribute("user");
    }

    @PostMapping("signup")
    public UserEntity signupWithPost(@RequestBody UserDTO userDTO) {
        UserEntity user = UserEntity.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .build();

        return userRepository.save(user);
    }

    @PutMapping("update")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
        UserEntity existingUser = userRepository.findById(userDTO.getId()).orElse(null);
        if (existingUser == null) {
            return ResponseEntity.badRequest().body("null");
        }

        existingUser.setName(userDTO.getName());
        existingUser.setEmail(userDTO.getEmail());

        UserEntity updateUser = userRepository.save(existingUser);

        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> deleteUser(@RequestBody UserDTO userDTO) {
        UserEntity existingUser = userRepository.findById(userDTO.getId()).orElse(null);

        userRepository.delete(existingUser);

        return ResponseEntity.ok().build();
    }

    @PostMapping("user")
    public UserEntity signup() {
        final UserEntity user = UserEntity.builder()
                .name("test")
                .email("test@gmail.com")
                .build();

        return userRepository.save(user);
    }

    @GetMapping("Tx")
    public ResponseEntity<?> tryTx() {
        try {
            userService.createUserWithTransaction();
            return new ResponseEntity<>("Trans Success", null, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Trnas Failed", null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
