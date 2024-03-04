package cos.peerna.domain.reply.controller;

import cos.peerna.domain.reply.dto.request.RegisterReplyRequest;
import cos.peerna.domain.reply.dto.request.UpdateReplyRequest;
import cos.peerna.domain.reply.dto.response.GetReplyWithProfileResponse;
import cos.peerna.domain.reply.service.ReplyService;
import cos.peerna.global.security.LoginUser;
import cos.peerna.global.security.dto.SessionUser;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyController {

	private final ReplyService replyService;

	@GetMapping
	public ResponseEntity<List<GetReplyWithProfileResponse>> getRepliesByProblem(
			@RequestParam Long problemId, @RequestParam @Nullable int page) {
		return ResponseEntity.ok(replyService.getRepliesByProblem(problemId, page));
	}

	@PostMapping
	public ResponseEntity<Void> registerReply(@LoginUser SessionUser user, @RequestBody RegisterReplyRequest request) {
		String replyId = replyService.make(request, user.getId());
		return ResponseEntity.created(URI.create(replyId)).build();
	}

	@PatchMapping
	public ResponseEntity<Void> modifyReply(@LoginUser SessionUser user, @RequestBody UpdateReplyRequest request) {
		replyService.modify(request, user.getId());
		return ResponseEntity.noContent().build();
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