package cos.peerna.controller;

import cos.peerna.security.LoginUser;
import cos.peerna.security.dto.SessionUser;
import cos.peerna.controller.dto.ReplyRegisterRequestDto;
import cos.peerna.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReplyController {

	private final ReplyService replyService;

	@PostMapping("/api/reply/new")
	public ResponseEntity<String> registerReply(@Nullable @LoginUser SessionUser user, @RequestBody ReplyRegisterRequestDto dto) {
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("No User Data");
		}
		if (replyService.checkDuplicate(user.getId(), dto.getHistoryId())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Duplicate Reply");
		}
		replyService.make(dto, user.getId());

		return ResponseEntity.ok()
				.body("success");
	}

	@PostMapping("/api/reply/likey")
	public ResponseEntity<String> recommendReply(@Nullable @LoginUser SessionUser user, @RequestParam Long replyId) {
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("No User Data");
		}
		replyService.recommendReply(user, replyId);
		
		return ResponseEntity.ok()
				.body("success");
	}

	@DeleteMapping("/api/reply/dislikey")
	public ResponseEntity<String> unrecommendReply(@Nullable @LoginUser SessionUser user, @RequestParam Long replyId) {
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body("No User Data");
		}
		replyService.unrecommendReply(user, replyId);

		return ResponseEntity.ok()
				.body("success");
	}
}
