package cos.peerna.controller;

import cos.peerna.controller.dto.NotificationResponseDto;
import cos.peerna.domain.User;
import cos.peerna.security.LoginUser;
import cos.peerna.security.dto.SessionUser;
import cos.peerna.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	/* DTO로 변경하여 클라이언트에서 GitHub REST API 호출할 때 캐스팅해줘야 할 데이터 전달해야 함 */
	@GetMapping("/api/notification/accept")
	public void acceptNotification(@LoginUser SessionUser user, @RequestParam Long notificationId) {
		notificationService.acceptNotification(user, notificationId);
	}
}
