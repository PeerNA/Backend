package cos.peerna.domain.reply.controller;

import cos.peerna.domain.history.dto.response.HistoryResponse;
import cos.peerna.domain.reply.dto.request.RegisterReplyRequest;
import cos.peerna.domain.reply.dto.request.UpdateReplyRequest;
import cos.peerna.domain.reply.dto.response.ReplyResponse;
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

	/*
	getRepliesByProblem, findUserReplies 를 통합하는 것이 좋은지 의사판단
	 */
	@GetMapping("/problem")
	public ResponseEntity<ReplyWithPageInfoResponse> getRepliesByProblem(
			@RequestParam Long problemId,
			@RequestParam @Nullable int page) {
		return ResponseEntity.ok(replyService.findRepliesByProblem(problemId, page));
	}

	@GetMapping("/user")
    public ResponseEntity<List<ReplyResponse>> findUserReplies(
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "0") Long cursorId,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(replyService.findUserReplies(userId, cursorId, size));
    }

	@PostMapping
	public ResponseEntity<Void> registerReply(@LoginUser SessionUser user, @RequestBody RegisterReplyRequest request) {
		String replyId = replyService.make(request, user);
		return ResponseEntity.created(URI.create(replyId)).build();
	}

	@PatchMapping
	public ResponseEntity<Void> modifyReply(@LoginUser SessionUser user, @RequestBody UpdateReplyRequest request) {
		replyService.modify(request, user);
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