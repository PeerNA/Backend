package cos.peerna.service;

<<<<<<< HEAD
import cos.peerna.controller.dto.ProblemResponseDto;
=======
import cos.peerna.controller.dto.ProblemRegisterRequestDto;
>>>>>>> 22eab42d075d9fa141db4c0f836ad8d9e5e6a6bb
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

// 내가 Problem 방식대로 개발을 하게 되면
// Repository Bean을 Domain 상에서 주입을 받아야 돼
