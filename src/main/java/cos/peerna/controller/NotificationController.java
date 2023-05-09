package cos.peerna.controller;

import cos.peerna.controller.dto.NotificationResponseDto;
import cos.peerna.domain.User;
import cos.peerna.security.LoginUser;
import cos.peerna.security.dto.SessionUser;
import cos.peerna.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping("/api/notifications")
	public NotificationResponseDto getNotifications(@LoginUser SessionUser user) {
			return notificationService.getNotifications(user);
	}
}
