package com.example.testspringboot.demo;

import com.example.testspringboot.demo.AppConfig;
import com.example.testspringboot.demo.UserRepository;
import com.example.testspringboot.demo.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        List<UserEntity> users;

        try {
            if (page <= 0) {
                throw new IllegalArgumentException("Page number should be greater than 0.");
            }
        users = userRepository.findAll(PageRequest.of(page - 1, size)).getContent();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR");
        }

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
    public UserEntity signupWithPost (@RequestBody UserDTO userDTO) {
        UserEntity user = UserEntity.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .build();

        return userRepository.save(user);
    }

    @PutMapping("update")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
        UserEntity existingUser = userRepository.findById(userDTO.getId()).orElse(null);

        if(existingUser == null) {
            return ResponseEntity.badRequest().body("아이디가 존재하지 않습니다.");
        }

        existingUser.setName(userDTO.getName());
        existingUser.setEmail(userDTO.getEmail());

        UserEntity updateUser = userRepository.save(existingUser);

        return ResponseEntity.ok(updateUser);
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

