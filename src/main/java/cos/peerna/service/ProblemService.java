package cos.peerna.service;

import cos.peerna.controller.dto.ProblemResponseDto;
import cos.peerna.domain.Category;
import cos.peerna.domain.Keyword;
import cos.peerna.domain.Problem;
import cos.peerna.repository.KeywordRepository;
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
    private final KeywordRepository keywordRepository;

    public void make(Problem problem) {
        validateProblem(problem);
        problemRepository.save(problem);
    }

    private void validateProblem(Problem problem) {
        if (problemRepository.findProblemByQuestion(problem.getQuestion()).isPresent()) {
            throw new IllegalArgumentException("This Question already exists.");
        }
    }

    /**
     * List<ProblemResponseDto>  를 반환 하도록 바꾸기
        public List<Problem> getAll() {
            return problemRepository.findAll();
        }
    */

    public Optional<ProblemResponseDto> getRandomByCategory(Category category) {
        List<Problem> problems = problemRepository.findProblemsByCategory(category);

        if (problems.isEmpty()) {
             return Optional.empty();
        } else {
            int randomElementIndex = ThreadLocalRandom.current().nextInt(problems.size()) % problems.size();
            Problem problem = problems.get(randomElementIndex);
            List<Keyword> keywordList = keywordRepository.findKeywordsByProblem(problem);
            return Optional.of(ProblemResponseDto.builder()
                    .problemId(problem.getId())
                    .answer(problem.getAnswer())
                    .category(problem.getCategory())
                    .keywordList(keywordList)
                    .build());
        }
    }

}
