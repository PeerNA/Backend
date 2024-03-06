package cos.peerna.global.common.controller;

import cos.peerna.global.security.LoginUser;
import cos.peerna.global.security.dto.SessionUser;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// http://localhost:8080/login 연동 로그인 모음
// http://localhost:8080/oauth2/authorization/google 구글 연동 로그인
// GET https://github.com/login/oauth/authorize Github 연동 로그인
@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pageTitle", "피어나");
        return "pages/index";
    }

    @GetMapping("/study/solo")
    public String soloStudy(@Nullable @LoginUser SessionUser user, Model model) {
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("userId", user.getId());
        model.addAttribute("userName", user.getName());
        model.addAttribute("userImage", user.getImageUrl());
        model.addAttribute("pageTitle", "Study - Solo");
        return "pages/study/solo";
    }

    @GetMapping("/mypage")
    public String myPage(@Nullable @LoginUser SessionUser user, Model model) {
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("userId", user.getId());
        model.addAttribute("userName", user.getName());
        model.addAttribute("userImage", user.getImageUrl());
        model.addAttribute("userEmail", user.getEmail());
        model.addAttribute("userImage", user.getImageUrl());
        model.addAttribute("pageTitle", "My Page - 피어나");
        return "pages/user/mypage";
    }

    /*
     NOTICE: 아래 페이지들은 테스트를 위해 존재. 실제 서비스에서는 제거해야함..
     */
    @GetMapping("/login")
    public String login() {
        return "pages/user/login";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "pages/user/signUp";
    }
}
