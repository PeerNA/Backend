//package cos.peerna.controller;
//
//import cos.peerna.controller.dto.KeywordRegisterRequestDto;
//import cos.peerna.controller.dto.ReplyRegisterRequestDto;
//import cos.peerna.controller.dto.ResponseDto;
//import cos.peerna.domain.Keyword;
//import cos.peerna.security.LoginUser;
//import cos.peerna.security.dto.SessionUser;
//import cos.peerna.service.KeywordService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.lang.Nullable;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//
//@Slf4j
//@RestController
//@RequiredArgsConstructor
//public class KeywordController {
//
//	private final KeywordService keywordService;
//
//	@PostMapping("/api/keyword/new")
//	public ResponseDto registerReply(@Nullable @LoginUser SessionUser user, @RequestBody KeywordRegisterRequestDto dto) {
//		if (user == null) {
//			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No User Data");
//		}
//		keywordService.analyze(dto.getAnswer(), dto.getProblemId());
//
//		return new ResponseDto(200, "success");
//	}
//
//	@GetMapping("/api/keyword")
//	public Keyword findKeyword(@Nullable @LoginUser SessionUser user, @RequestBody KeywordRegisterRequestDto dto) {
//		if (user == null) {
//			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No User Data");
//		}
//		return keywordService.findOne(dto);
//	}
//}

/* deprecated */
