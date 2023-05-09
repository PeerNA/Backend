package cos.peerna.service;


import cos.peerna.controller.dto.NotificationResponseDto;
import cos.peerna.domain.Notification;
import cos.peerna.domain.User;
import cos.peerna.repository.NotificationRepository;
import cos.peerna.repository.UserRepository;
import cos.peerna.security.dto.SessionUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final UserRepository userRepository;

	public NotificationResponseDto getNotifications(SessionUser sessionUser) {
		User user = userRepository.findById(sessionUser.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
		List<Notification> notificationList = notificationRepository.findByUser(user);

		return NotificationResponseDto.builder()
				.notificationList(notificationList)
				.build();
	}
}
