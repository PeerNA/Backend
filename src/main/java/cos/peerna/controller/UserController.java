package cos.peerna.controller;

import cos.peerna.config.auth.LoginUser;
import cos.peerna.config.auth.dto.SessionUser;
import cos.peerna.controller.dto.UserRegisterRequestDto;
import cos.peerna.controller.dto.ResponseDto;
import cos.peerna.domain.Career;
import cos.peerna.domain.Interest;
import cos.peerna.domain.User;
import cos.peerna.service.UserService;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/api/users/update")
    public ResponseDto updateUser(@LoginUser SessionUser user, Interest interest, Career career) {
        if (user == null) {
            return new ResponseDto(400, "No User Data");
        }

        userService.updateCondition(user, interest, career);

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
