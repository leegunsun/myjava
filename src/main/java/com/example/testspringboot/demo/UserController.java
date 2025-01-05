package com.example.testspringboot.demo;

import com.example.testspringboot.demo.AppConfig;
import com.example.testspringboot.demo.UserRepository;
import com.example.testspringboot.demo.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AppConfig appConfig;
    private JwtTokenProvider jwtTokenProvider;
    private final JdbcOperations jdbcTemplate;

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
                .username(name)
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
                .username(userDTO.getName())
                .email(userDTO.getEmail())
                .build();

        return userRepository.save(user);
    }

    @PutMapping("update")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
        // Optional을 활용하여 사용자 조회
        Optional<UserEntity> optionalUser = userRepository.findById(userDTO.getId());

        // 사용자 존재 여부를 안전하게 확인
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("아이디가 존재하지 않습니다.");
        }

        // 존재하는 사용자 정보 업데이트
        UserEntity existingUser = optionalUser.get();
        existingUser.setUsername(userDTO.getName());
        existingUser.setEmail(userDTO.getEmail());

        // 변경된 사용자 정보 저장
        UserEntity updatedUser = userRepository.save(existingUser);

        // 성공적으로 업데이트된 사용자 정보 반환
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("create")
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> requestBody) {

        try {
            String username = requestBody.get("username");
            String email = requestBody.get("email");
            String passwordHash = requestBody.get("passwordHash");

            UserEntity user = UserEntity.builder()
                    .username(username)
                    .email(email)
                    .password_hash(passwordHash)
                    .build();

            // UserEntity 생성 및 저장
            UserEntity result = userRepository.save(user);

            return ResponseEntity.ok(result);
        } catch (DataIntegrityViolationException e) {

            return ResponseEntity.badRequest().body("Data integrity violation occurred.");
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.badRequest().body("An unexpected error occurred: " + e.getMessage());
        }
    }


    @PostMapping("create2")
    public ResponseEntity<?> createUser2(@RequestBody Map<String, String> requestBody) {

        try {

            String sql = "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)";

            String username = requestBody.get("username");
            String email = requestBody.get("email");
            String passwordHash = requestBody.get("passwordHash");

            // UserEntity 생성 및 저장
            int rowsAffected = jdbcTemplate.update(sql, username, email, passwordHash);

            // 응답 반환
            if (rowsAffected > 0) {
                return ResponseEntity.ok("User created successfully.");
            } else {
                return ResponseEntity.badRequest().body("Failed to create user.");
            }
        } catch (DataIntegrityViolationException e) {

            return ResponseEntity.badRequest().body("Data integrity violation occurred.");
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.badRequest().body("An unexpected error occurred: " + e.getMessage());
        }
    }


    @PostMapping("update")
    public int updateUser(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");
        String email = requestBody.get("email");
        String passwordHash = requestBody.get("passwordHash");

        String sql = "UPDATE users SET email = ?, password_hash = ? WHERE username = ?";
        return jdbcTemplate.update(sql, email, passwordHash, username);
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

    @GetMapping("getName")
    public ResponseEntity<?> getUsersByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(userRepository.findByName(name));
    }

    @GetMapping("getEmail")
    public ResponseEntity<?> getEmail(@RequestParam("email") String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        // 여러 열을 반환하는 쿼리 실행
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, email);

        return ResponseEntity.ok(result);
    }

}

