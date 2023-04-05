package cos.peerna.service;

import cos.peerna.controller.dto.ProblemRegisterRequestDto;
import cos.peerna.domain.Category;
import cos.peerna.domain.Problem;
import cos.peerna.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    public void make(ProblemRegisterRequestDto dto) {
        Problem problem = Problem.createProblem(dto);
        validateProblem(problem);
        problemRepository.save(problem);
    }

    private void validateProblem(Problem problem) {
        if (problemRepository.findProblemByQuestion(problem.getQuestion()).isPresent()) {
            throw new IllegalArgumentException("This Question already exists.");
        }
    }

    public String getOneAnswer(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
        return problem.getAnswer();
    }

    public List<Problem> getAll() {
        return problemRepository.findAll();
    }

    public Optional<Problem> getRandomByCategory(Category category) {
        Optional<Problem> problem;
        List<Problem> problems = problemRepository.findProblemsByCategory(category);

        if (problems.isEmpty()) {
             problem = Optional.empty();
        } else {
            int randomElementIndex = ThreadLocalRandom.current().nextInt(problems.size()) % problems.size();
            problem = Optional.of(problems.get(randomElementIndex));
        }
        return problem;
    }

}

// 내가 Problem 방식대로 개발을 하게 되면
// Repository Bean을 Domain 상에서 주입을 받아야 돼
