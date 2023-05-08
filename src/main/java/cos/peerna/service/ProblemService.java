package cos.peerna.service;

import cos.peerna.domain.Category;
import cos.peerna.domain.Keyword;
import cos.peerna.domain.Problem;
import cos.peerna.repository.KeywordRepository;
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
    private final KeywordRepository keywordRepository;

    public void make(String question, String answer, Category category) {
        Problem problem = Problem.createProblem(question, answer, category);
        validateProblem(problem);
        problemRepository.save(problem);
    }

    private void validateProblem(Problem problem) {
        problemRepository.findProblemByQuestion(problem.getQuestion()).ifPresent(p -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already Exist Problem");
        });
    }

    /**
     * List<ProblemResponseDto>  를 반환 하도록 바꾸기
        public List<Problem> getAll() {
            return problemRepository.findAll();
        }
    */
    public String getOneAnswer(Long id) {
        Problem problem = problemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
        return problem.getAnswer();
    }

    public List<Problem> getAll() {
        return problemRepository.findAll();
    }

    public Optional<Problem> getRandomByCategory(Category category) {
        List<Problem> problems = problemRepository.findProblemsByCategory(category);

        if (problems.isEmpty()) {
             return Optional.empty();
        } else {
            int randomElementIndex = ThreadLocalRandom.current().nextInt(problems.size()) % problems.size();
            Problem problem = problems.get(randomElementIndex);

            return Optional.of(problem);
        }
    }
}

