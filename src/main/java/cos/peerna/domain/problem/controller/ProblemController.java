package cos.peerna.domain.problem.controller;

import cos.peerna.domain.problem.dto.ProblemAnswerResponseDto;
import cos.peerna.domain.problem.dto.ProblemRegisterRequestDto;
import cos.peerna.domain.problem.dto.ProblemResponseDto;
import cos.peerna.domain.problem.service.ProblemService;
import cos.peerna.domain.reply.dto.ReplyResponseDto;
import cos.peerna.domain.user.model.Category;
import cos.peerna.domain.reply.service.ReplyService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/problem")
public class ProblemController {

    private static final String PROBLEM_PREFIX = "/api/problem/";

    private final ProblemService problemService;
    private final ReplyService replyService;

    @PostMapping
    public ResponseEntity<String> registerProblem(@RequestBody ProblemRegisterRequestDto dto) {
        Long problemId = problemService.make(dto.getQuestion(), dto.getAnswer(), dto.getCategory());
        URI location = URI.create(PROBLEM_PREFIX + problemId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{problemId}")
    public ResponseEntity<ProblemAnswerResponseDto> getProblemById(@PathVariable Long problemId) {
        return ResponseEntity.ok(problemService.getProblemById(problemId));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ProblemResponseDto> getProblemByCategory(@PathVariable Category category) {
        return ResponseEntity.ok(problemService.getProblemByCategory(category));
    }

    @GetMapping("/replies")
    public ResponseEntity<ReplyResponseDto> getRepliesByProblem(@RequestParam Long problemId, @RequestParam @Nullable int page) {
        return ResponseEntity.ok(replyService.getRepliesByProblem(problemId, page));
    }

}
