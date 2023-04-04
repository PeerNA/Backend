package cos.peerna.controller;

import cos.peerna.controller.dto.ProblemRegisterRequestDto;
import cos.peerna.controller.dto.ResponseDto;
import cos.peerna.domain.Category;
import cos.peerna.domain.Problem;
import cos.peerna.service.ProblemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @PostMapping("/api/problems/new")
    public ResponseDto registerProblem(@RequestBody ProblemRegisterRequestDto dto) {
        Problem problem = Problem.createProblem(dto);
        problemService.make(problem);

        return new ResponseDto(200, "success");
    }

    @GetMapping("/api/problems/category")
    public Problem getProblemByCategory(@RequestParam Category category) {
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
