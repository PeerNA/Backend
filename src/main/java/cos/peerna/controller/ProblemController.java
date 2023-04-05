package cos.peerna.controller;

import cos.peerna.controller.dto.ProblemRegisterRequestDto;
import cos.peerna.controller.dto.ProblemResponseDto;
import cos.peerna.controller.dto.ResponseDto;
import cos.peerna.controller.dto.ProblemAnswerResponseDto;
import cos.peerna.domain.Category;
import cos.peerna.domain.Keyword;
import cos.peerna.domain.Problem;
import cos.peerna.service.ProblemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;



@Slf4j
@RestController
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

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
        log.debug("getProblemByCategory()");

        return problemService.getRandomByCategory(category)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
    }

    @GetMapping("/script")
    public void runScript() {
        try {
            new ProcessBuilder("python", "script.py").start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
