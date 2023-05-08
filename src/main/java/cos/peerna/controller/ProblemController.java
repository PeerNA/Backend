package cos.peerna.controller;

import cos.peerna.controller.dto.*;
import cos.peerna.controller.dto.data.ReplyData;
import cos.peerna.domain.Category;
import cos.peerna.domain.Keyword;
import cos.peerna.domain.Problem;
import cos.peerna.repository.KeywordRepository;
import cos.peerna.service.ProblemService;
import cos.peerna.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


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
        return ProblemResponseDto.builder()
                .problemId(problem.getId())
                .question(problem.getQuestion())
                .answer(problem.getAnswer())
                .category(problem.getCategory())
                .keywordList(keywordList)
                .build();
    }

    @GetMapping("/api/problems/replies")
    public ReplyResponseDto getRepliesByProblem(@RequestParam Long problemId, @RequestParam @Nullable int page) {
        return replyService.getRepliesByProblem(problemId, page);
    }

}
