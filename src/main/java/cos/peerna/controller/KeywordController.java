package cos.peerna.controller;

import cos.peerna.controller.dto.KeywordRegisterRequestDto;
import cos.peerna.controller.dto.ResponseDto;
import cos.peerna.service.KeywordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KeywordController {

	private final KeywordService keywordService;

	@PostMapping("/api/keyword/new")
	public ResponseDto registerKeyword(@RequestBody KeywordRegisterRequestDto dto) {
		keywordService.make(dto);

		return new ResponseDto(200, "success");
	}
}
