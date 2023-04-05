package cos.peerna.controller;

import cos.peerna.controller.dto.ReplyRegisterRequestDto;
import cos.peerna.controller.dto.ResponseDto;
import cos.peerna.domain.Reply;
import cos.peerna.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReplyController {

	private final ReplyService replyService;

	@PostMapping("/api/reply/new")
	public ResponseDto registerReply(@RequestBody ReplyRegisterRequestDto dto) {
		replyService.make(dto);

		return new ResponseDto(200, "success");
	}
}
