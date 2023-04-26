package cos.peerna.controller;

import cos.peerna.controller.dto.UserPatchRequestDto;
import cos.peerna.security.LoginUser;
import cos.peerna.security.dto.SessionUser;
import cos.peerna.controller.dto.UserRegisterRequestDto;
import cos.peerna.domain.Career;
import cos.peerna.domain.Interest;
import cos.peerna.domain.User;
import cos.peerna.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/api/users/signout")
    public ResponseEntity<String> signOut(@LoginUser SessionUser user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("No User Data");
        }
        userService.delete(user);
        return ResponseEntity.ok()
                .body("success");
    }

    @GetMapping("/api/users/info")
    public ResponseEntity<SessionUser> userStatus(@LoginUser SessionUser user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
        return ResponseEntity.ok()
                .body(user);
    }

    @PatchMapping("/api/users/info")
    public ResponseEntity<String> updateInfo(@LoginUser SessionUser user, UserPatchRequestDto dto) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("No User Data");
        }
        userService.patchUpdate(user, dto);

        return ResponseEntity.ok()
                .body("success");
    }

    @GetMapping("/oauth2-login-fail")
    public void oauth2LoginFail(HttpServletResponse response) {
        String redirectUri = "http://ec2-43-200-47-43.ap-northeast-2.compute.amazonaws.com:3000/";

        try {
            response.sendRedirect(redirectUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 테스트 or 디버그 용 회원가입
    @PostMapping("/api/users/new")
    public ResponseEntity<String> registerUser(@RequestBody UserRegisterRequestDto dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        User user = User.createUser(dto);

        try {
            userService.join(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User Already Exists");
        }

        return ResponseEntity.ok()
                .body("success");
    }

    @GetMapping
    public LocalDateTime test() {
        return LocalDateTime.now();
    }
}
