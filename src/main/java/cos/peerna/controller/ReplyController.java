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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReplyController {

	private final ReplyService replyService;

	@PostMapping("/api/reply/new")
	public ResponseDto registerReply(@RequestBody ReplyRegisterRequestDto dto, @Nullable @LoginUser SessionUser user) {
		if (user == null) {
			return new ResponseDto(400, "No User Data");
		}

		replyService.make(dto, user);

		return new ResponseDto(200, "success");
	}
}
