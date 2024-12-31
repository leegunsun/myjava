package com.example.testspringboot.demo;

import com.example.testspringboot.demo.AppConfig;
import com.example.testspringboot.demo.UserRepository;
import com.example.testspringboot.demo.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AppConfig appConfig;
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Request received: " + loginRequest.getUsername());
        // 인증 로직 추가 (예: 사용자 인증)
        String username = loginRequest.getUsername();

        // 토큰 발급
        String accessToken = JwtTokenProvider.generateAccessToken(username);
        String refreshToken = JwtTokenProvider.generateRefreshToken(username);

        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader(value = "Authorization", required = true) String authorizationHeader) {
//        if (jwtTokenProvider.validateToken(authorizationHeader)) {
//            String userId = jwtTokenProvider.getClaims(refreshRequest.getRefreshToken()).getSubject();
//            String newAccessToken = jwtTokenProvider.createAccessToken(userId, "USER");
//            return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshRequest.getRefreshToken()));
//        }

        try {

            if (!authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("Invalid refresh token");
            }

            // "Bearer " 제거 후 JWT 추출
            String token = authorizationHeader.substring(7);

            Claims claims = JwtTokenProvider.validateToken(token);

            return ResponseEntity.status(200).body("토큰이 유효합니다. 사용자: " + claims.getSubject());
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", 401);
            response.put("error", "Unauthorized");
            response.put("message", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(401).body(response);
        }
    }

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

