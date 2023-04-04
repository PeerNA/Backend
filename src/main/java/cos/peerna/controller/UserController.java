package cos.peerna.controller;

import cos.peerna.config.auth.LoginUser;
import cos.peerna.config.auth.dto.SessionUser;
import cos.peerna.controller.dto.UserRegisterRequestDto;
import cos.peerna.controller.dto.ResponseDto;
import cos.peerna.domain.Career;
import cos.peerna.domain.Interest;
import cos.peerna.domain.User;
import cos.peerna.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/api/users/logout")
    public Object logout(@LoginUser SessionUser sessionUser, HttpServletResponse response) {
        if (sessionUser == null) {
            response.setStatus(401);
        }
        return sessionUser;
    }
    @PostMapping("/api/users/new")
    public ResponseDto registerUser(@RequestBody UserRegisterRequestDto dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        User user = User.createUser(dto);
        userService.join(user);

        return new ResponseDto(200, "success");
    }
    @GetMapping("/api/users/signout")
    public void signout(@LoginUser SessionUser sessionUser, HttpServletResponse response) {
        if (sessionUser == null) {
            response.setStatus(401);
        }
        userService.delete(sessionUser);
    }

    @GetMapping("/api/users/info")
    public Object userStatus(@LoginUser SessionUser sessionUser, HttpServletResponse response) {
        if (sessionUser == null) {
            response.setStatus(401);
        }
        return sessionUser;
    }

    @PostMapping("/api/users/info")
    public ResponseDto updateInfo(@LoginUser SessionUser sessionUser, Interest interest, Career career) {
        if (sessionUser == null) {
            return new ResponseDto(401, "no login data");
        }
        userService.updateCondition(sessionUser, interest, career);

        return new ResponseDto(200, "success");
    }

    @GetMapping("/oauth2-login-fail")
    public void oauth2LoginFail(HttpServletResponse response) {
        String redirectUri = "http://localhost:3000/";

        try {
            response.sendRedirect(redirectUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
