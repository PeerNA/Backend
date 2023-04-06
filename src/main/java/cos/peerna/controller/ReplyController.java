package cos.peerna.controller;

import cos.peerna.config.auth.LoginUser;
import cos.peerna.config.auth.dto.SessionUser;
import cos.peerna.controller.dto.ReplyRegisterRequestDto;
import cos.peerna.controller.dto.ResponseDto;
import cos.peerna.domain.Reply;
import cos.peerna.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReplyController {

	private final ReplyService replyService;

	@PostMapping("/api/reply/new")
	public ResponseDto registerReply(@Nullable @LoginUser SessionUser user, @RequestBody ReplyRegisterRequestDto dto) {
		if (user == null) {
			return new ResponseDto(401, "No User Data");
		}

		replyService.make(dto, user);

		return new ResponseDto(200, "success");
	}

	@GetMapping("/api/reply/likey")
	public ResponseDto recommendReply(@Nullable @LoginUser SessionUser user, @RequestParam Long replyId) {
		if (user == null) {
			return new ResponseDto(401, "No User Data");
		}
		replyService.recommendReply(user, replyId);
		return new ResponseDto(200, "success");
	}
}
