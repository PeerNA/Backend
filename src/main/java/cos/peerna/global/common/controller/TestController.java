package cos.peerna.global.common.controller;

import cos.peerna.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class TestController {

    private final NotificationService notificationService;

    @GetMapping("/login")
    public String getLoginForm() {
        return "loginPage";
    }


}
