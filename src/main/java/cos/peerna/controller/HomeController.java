package cos.peerna.controller;

import cos.peerna.security.LoginUser;
import cos.peerna.security.dto.SessionUser;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// http://localhost:8080/login 연동 로그인 모음
// http://localhost:8080/oauth2/authorization/google 구글 연동 로그인
// GET https://github.com/login/oauth/authorize Github 연동 로그인
@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public Object home(@Nullable @LoginUser SessionUser user) {
        if (user == null) {
            return "로그인을 해주세요";
        }
        return user;
    }

    @GetMapping("/ping")
    public String pong() {
        return "pong!";
    }

}
