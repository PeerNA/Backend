package cos.peerna.service;

import cos.peerna.domain.Category;
import cos.peerna.domain.Problem;
import cos.peerna.repository.HistoryRepository;
import cos.peerna.repository.KeywordRepository;
import cos.peerna.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final HistoryRepository historyRepository;
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
     * public List<Problem> getAll() {
     * return problemRepository.findAll();
     * }
     */
    public String getOneAnswer(Long id) {
        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
        return problem.getAnswer();
    }

    public List<Problem> getAll() {
        return problemRepository.findAll();
    }

    public Optional<Problem> getRandomByCategory(Category category) {
        Long categorySize = problemRepository.countByCategory(category);
        if (categorySize == 0) {
            return Optional.empty();
        } else {
            long randomElementIndex = ThreadLocalRandom.current().nextLong(1, categorySize+1) % (categorySize+1);
            return problemRepository.findById(randomElementIndex);
        }
    }

    public Problem getRandomByCategoryNonDuplicate(Category category, List<Long> historyIds) {
        Long categorySize = problemRepository.countByCategory(category);

        List<Long> problemIds = new ArrayList<>();
        for (Long historyId : historyIds) {
            problemIds.add(historyRepository.findById(historyId).orElse(null).getProblem().getId());
        }
        if (problemIds.size() == categorySize) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No More Problem");
        }

        Problem randomProblem = getRandomByCategory(category).orElse(null);

        while (problemIds.contains(randomProblem.getId())) {
            randomProblem = getRandomByCategory(category).orElse(null);
        }
        return randomProblem;
    }
}

