package cos.peerna.controller;

import cos.peerna.security.LoginUser;
import cos.peerna.security.dto.SessionUser;
import cos.peerna.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class TestController {

    private final NotificationService notificationService;

    @GetMapping("/login")
    public String getLoginForm() {
        return "loginPage";
    }

    @PostMapping("/api/fork")
    public void fork(@LoginUser SessionUser sessionUser) {
        notificationService.forkRepository(sessionUser.getToken(),
                "https://api.github.com/repos/ksundong/backend-interview-question/forks");
    }
}
