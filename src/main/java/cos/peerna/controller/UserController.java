package cos.peerna.controller;

import cos.peerna.security.LoginUser;
import cos.peerna.security.dto.SessionUser;
import cos.peerna.controller.dto.UserRegisterRequestDto;
import cos.peerna.controller.dto.ResponseDto;
import cos.peerna.domain.Career;
import cos.peerna.domain.Interest;
import cos.peerna.domain.User;
import cos.peerna.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/api/users/new")
    public ResponseDto registerUser(@RequestBody UserRegisterRequestDto dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        User user = User.createUser(dto);
        userService.join(user);

        return new ResponseDto(200, "success");
    }
    @GetMapping("/api/users/signout")
    public void signOut(@LoginUser SessionUser user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No User Data");
        }
        userService.delete(user);
    }

    @GetMapping("/api/users/info")
    public Object userStatus(@LoginUser SessionUser user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No User Data");
        }
        return user;
    }

    @PostMapping("/api/users/info")
    public ResponseDto updateInfo(@LoginUser SessionUser user, Interest interest, Career career) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No User Data");
        }
        userService.updateCondition(user, interest, career);

        return new ResponseDto(200, "success");
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

}
