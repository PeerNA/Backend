package cos.peerna.service;

import cos.peerna.controller.dto.KeywordRegisterRequestDto;
import cos.peerna.controller.dto.ProblemRegisterRequestDto;
import cos.peerna.domain.Keyword;
import cos.peerna.domain.Problem;
import cos.peerna.repository.KeywordRepository;
import cos.peerna.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequiredArgsConstructor
public class KeywordService {

	private final KeywordRepository keywordRepository;
	private final ProblemRepository problemRepository;

	public void make(KeywordRegisterRequestDto dto) {
		Problem problem = problemRepository.findById(dto.getProblemId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
		Keyword keyword = Keyword.createKeyword(dto.getName(), problem);
		keywordRepository.save(keyword);
	}
}
