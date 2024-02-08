package cos.peerna.global.common.controller;

import cos.peerna.domain.user.dto.UserRegisterRequestDto;
import cos.peerna.domain.user.model.Role;
import cos.peerna.domain.user.model.User;
import cos.peerna.domain.user.repository.UserRepository;
import cos.peerna.global.security.LoginUser;
import cos.peerna.global.security.dto.SessionUser;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.crypto.password.PasswordEncoder;

// http://localhost:8080/login 연동 로그인 모음
// http://localhost:8080/oauth2/authorization/google 구글 연동 로그인
// GET https://github.com/login/oauth/authorize Github 연동 로그인
@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final UserRepository userRepository;

    @GetMapping
    public String index(@Nullable @LoginUser SessionUser user, Model model) {
        if (user == null) {
            return "login";
        }
        model.addAttribute("userId", user.getId());
        model.addAttribute("userName", user.getName());
        model.addAttribute("userImage", user.getImageUrl());
        return "index";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signUp";
    }

    @PostMapping("/callback")
    public String callback() {
        return "redirect:/";
    }
}
