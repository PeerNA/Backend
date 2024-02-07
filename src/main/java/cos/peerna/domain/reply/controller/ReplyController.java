package cos.peerna.domain.reply.controller;

import cos.peerna.domain.reply.dto.ReplyRegisterRequestDto;
import cos.peerna.domain.reply.service.ReplyService;
import cos.peerna.global.security.LoginUser;
import cos.peerna.global.security.dto.SessionUser;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyController {

	private final ReplyService replyService;

	@PostMapping
	public ResponseEntity<Void> registerReply(@LoginUser SessionUser user, @RequestBody ReplyRegisterRequestDto dto) {
		String replyId = replyService.make(dto, user.getId());
		return ResponseEntity.created(URI.create(replyId)).build();
	}

	@PostMapping("/likey")
	public ResponseEntity<String> recommendReply(@LoginUser SessionUser user, @RequestParam Long replyId) {
		String likeyId = replyService.recommendReply(user, replyId);
		return ResponseEntity.created(URI.create(likeyId)).build();
	}

	@DeleteMapping("/likey")
	public ResponseEntity<Void> unrecommendReply(@LoginUser SessionUser user, @RequestParam Long replyId) {
		replyService.unrecommendReply(user, replyId);
		return ResponseEntity.noContent().build();
	}
}