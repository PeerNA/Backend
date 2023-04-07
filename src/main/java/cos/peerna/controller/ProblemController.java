package cos.peerna.controller;

import cos.peerna.controller.dto.*;
import cos.peerna.domain.Category;
import cos.peerna.domain.Keyword;
import cos.peerna.domain.Problem;
import cos.peerna.domain.Reply;
import cos.peerna.service.ProblemService;
import cos.peerna.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;
    private final ReplyService replyService;

    @PostMapping("/api/problems/new")
    public ResponseDto registerProblem(@RequestBody ProblemRegisterRequestDto dto) {
        problemService.make(dto);

        return new ResponseDto(200, "success");
    }

    @GetMapping("/api/problems")
    public ProblemAnswerResponseDto getProblemById(@RequestParam Long id) {
        String answer = problemService.getOneAnswer(id);

        return new ProblemAnswerResponseDto(answer);
    }

    @GetMapping("/api/problems/category")
    public ProblemResponseDto getProblemByCategory(@RequestParam Category category) {
        return problemService.getRandomByCategory(category)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
    }

    @GetMapping("/api/problems/replies")
    public List<ReplyResponseDto> getRepliesByProblem(@RequestParam Long problemId) {
        return replyService.getRepliesByProblem(problemId);
    }

}
