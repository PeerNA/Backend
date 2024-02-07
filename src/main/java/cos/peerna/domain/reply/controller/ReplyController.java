package cos.peerna.domain.reply.controller;

import cos.peerna.domain.reply.dto.ReplyRegisterRequestDto;
import cos.peerna.domain.reply.service.ReplyService;
import cos.peerna.global.security.LoginUser;
import cos.peerna.global.security.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reply")
public class ReplyController {

	private final ReplyService replyService;

	@PostMapping()
	public ResponseEntity<String> registerReply(@Nullable @LoginUser SessionUser user, @RequestBody ReplyRegisterRequestDto dto) {
		if (replyService.checkDuplicate(user.getId(), dto.getHistoryId())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Duplicate Reply");
		}
		replyService.make(dto, user.getId());

		return ResponseEntity.ok()
				.body("success");
	}

	@PostMapping("/likey")
	public ResponseEntity<String> recommendReply(@Nullable @LoginUser SessionUser user, @RequestParam Long replyId) {
		replyService.recommendReply(user, replyId);
		
		return ResponseEntity.ok()
				.body("success");
	}

	@DeleteMapping("/likey")
	public ResponseEntity<String> unrecommendReply(@Nullable @LoginUser SessionUser user, @RequestParam Long replyId) {
		replyService.unrecommendReply(user, replyId);

		return ResponseEntity.ok()
				.body("success");
	}
}
