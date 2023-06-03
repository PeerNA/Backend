package cos.peerna.controller;

import cos.peerna.controller.dto.NotificationResponseDto;
import cos.peerna.domain.User;
import cos.peerna.repository.UserRepository;
import cos.peerna.security.LoginUser;
import cos.peerna.security.dto.SessionUser;
import cos.peerna.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
	private final UserRepository userRepository;

    @GetMapping("/api/notifications")
    public NotificationResponseDto getNotifications(@LoginUser SessionUser user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        return notificationService.getNotifications(user);
    }

    /* DTO로 변경하여 클라이언트에서 GitHub REST API 호출할 때 캐스팅해줘야 할 데이터 전달해야 함 */
    @PostMapping("/api/notification/accept")
    public ResponseEntity<String> acceptNotification(@LoginUser SessionUser user, @RequestParam Long notificationId) {
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("No User Data");
		}

        notificationService.acceptNotification(user, notificationId);

        return ResponseEntity.ok()
                .body("success");
    }

    @DeleteMapping("/api/notification")
    public ResponseEntity<String> deleteNotification(@LoginUser SessionUser sessionUser, @RequestParam Long notificationId) {
		if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("No User Data");
		}
		User user = userRepository.findById(sessionUser.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저가 없습니다."));
        notificationService.deleteNotification(user, notificationId);

        return ResponseEntity.ok()
                .body("success");
    }

    @DeleteMapping("/api/notifications")
    public ResponseEntity<String> deleteAllNotification(@LoginUser SessionUser sessionUser) {
		if (sessionUser == null) {
			            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("No User Data");
		}
        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저가 없습니다."));
        notificationService.deleteAllNotification(user);

        return ResponseEntity.ok()
                .body("success");
    }


//	@PostMapping("/api/fork")
//	public void fork(@LoginUser SessionUser sessionUser) {
//		notificationService.forkRepository(sessionUser.getToken(),
//				"https://api.github.com/repos/koding1/maple_web/forks");
//	}
//
//	@PostMapping("/api/branch")
//	public void createBranch(@LoginUser SessionUser sessionUser) {
//		notificationService.createBranch(sessionUser.getToken(),
//				"https://api.github.com/repos/its-sky/maple_web/git/refs");
//	}
//
//	@GetMapping("/api/content")
//	public void getContent(@LoginUser SessionUser sessionUser) {
////		notificationService.getContentAndPush(sessionUser.getToken(),
////				"https://api.github.com/repos/ksundong/backend-interview-question/readme", "TCP와 UDP의 차이점에 대해서 설명해보세요.", "테스트 Test 입니다");
//		notificationService.getContentAndPush(sessionUser,
//				"https://api.github.com/repos/its-sky/maple_web", "TCP와 UDP의 차이점에 대해서 설명해보세요.", "테스트 Test 입니다");
//	}
//
//	@GetMapping("/api/getSha")
//	public void getSha(@LoginUser SessionUser sessionUser) {
//		notificationService.getShaHash(sessionUser.getToken(),
//				"https://api.github.com/repos/its-sky/maple_web/git/ref/heads/main");
//	}
}
