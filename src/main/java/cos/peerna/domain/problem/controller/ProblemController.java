package cos.peerna.domain.problem.controller;

import cos.peerna.domain.problem.dto.ProblemAnswerResponseDto;
import cos.peerna.domain.problem.dto.ProblemRegisterRequestDto;
import cos.peerna.domain.problem.dto.ProblemResponseDto;
import cos.peerna.domain.problem.model.Problem;
import cos.peerna.domain.problem.service.ProblemService;
import cos.peerna.domain.reply.dto.ReplyResponseDto;
import cos.peerna.domain.user.model.Category;
import cos.peerna.domain.keyword.model.Keyword;
import cos.peerna.domain.keyword.repository.KeywordRepository;
import cos.peerna.domain.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;
    private final ReplyService replyService;
    private final KeywordRepository keywordRepository;

    @PostMapping("/api/problems/new")
    public ResponseEntity<String> registerProblem(@RequestBody ProblemRegisterRequestDto dto) {
        String question = dto.getQuestion();
        String answer = dto.getAnswer();
        Category category = dto.getCategory();
        problemService.make(question, answer, category);

        return ResponseEntity.ok()
                .body("success");
    }

    @GetMapping("/api/problems")
    public ProblemAnswerResponseDto getProblemById(@RequestParam Long id) {
        String answer = problemService.getOneAnswer(id);

        return new ProblemAnswerResponseDto(answer);
    }

    @GetMapping("/api/problems/category")
    public ProblemResponseDto getProblemByCategory(@RequestParam Category category) {
        Problem problem = problemService.getRandomByCategory(category)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
        List<Keyword> keywordList = keywordRepository.findKeywordsByProblem(problem);
        List<String> keywords = keywordList.stream().map(Keyword::getName).collect(Collectors.toList());
        return ProblemResponseDto.builder()
                .problemId(problem.getId())
                .question(problem.getQuestion())
                .answer(problem.getAnswer())
                .category(problem.getCategory())
                .keywordList(keywords)
                .build();
    }

    @GetMapping("/api/problems/replies")
    public ReplyResponseDto getRepliesByProblem(@RequestParam Long problemId, @RequestParam @Nullable int page) {
        return replyService.getRepliesByProblem(problemId, page);
    }

}
