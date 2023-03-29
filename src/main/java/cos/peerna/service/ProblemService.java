package cos.peerna.service;

import cos.peerna.domain.Category;
import cos.peerna.domain.Problem;
import cos.peerna.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    public void make(Problem problem) {
        validateProblem(problem);
        problemRepository.save(problem);
    }

    private void validateProblem(Problem problem) {
        if (problemRepository.findProblemByQuestion(problem.getQuestion()).isPresent()) {
            throw new IllegalArgumentException("This Question already exists.");
        }
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
