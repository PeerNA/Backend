package cos.peerna.domain.problem.service;

import cos.peerna.domain.problem.dto.ProblemAnswerResponseDto;
import cos.peerna.domain.problem.dto.ProblemResponseDto;
import cos.peerna.domain.problem.model.Problem;
import cos.peerna.domain.problem.model.ProblemIdMapping;
import cos.peerna.domain.problem.repository.ProblemRepository;
import cos.peerna.domain.user.model.Category;
import cos.peerna.domain.history.repository.HistoryRepository;
import cos.peerna.domain.keyword.repository.KeywordRepository;
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
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final HistoryRepository historyRepository;
    private final KeywordRepository keywordRepository;

    @Transactional
    public Long make(String question, String answer, Category category) {
        validateProblem(question);
        Problem newProblem = problemRepository.save(Problem.createProblem(question, answer, category));
        return newProblem.getId();
    }

    private void validateProblem(String question) {
        problemRepository.findProblemByQuestion(question).ifPresent(p -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already Exist Problem");
        });
    }

    public ProblemAnswerResponseDto getProblemById(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
        return ProblemAnswerResponseDto.of(problem.getAnswer());
    }

    public ProblemResponseDto getProblemByCategory(Category category) {
        Problem problem = getRandomByCategory(category);
        List<String> keywords = keywordRepository.findKeywordsByProblem(problem).stream().map(keyword -> keyword.getName()).toList();
        return ProblemResponseDto.builder()
                .problemId(problem.getId())
                .question(problem.getQuestion())
                .answer(problem.getAnswer())
                .category(problem.getCategory())
                .keywordList(keywords)
                .build();
    }

    public Problem getRandomByCategory(Category category) {
        List<ProblemIdMapping> problemIdList = problemRepository.findAllByCategory(category);
        int categorySize = problemIdList.size();

        if (categorySize == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found");
        } else {
            int randomNumber = ThreadLocalRandom.current().nextInt(1, categorySize + 1) % (categorySize + 1);
            long randomElementIndex = problemIdList.get(randomNumber-1).getId();
            return problemRepository.findById(randomElementIndex).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
        }
    }

    public Problem getRandomByCategoryNonDuplicate(Category category, List<Long> historyIds) {
        List<Long> solvedProblemIds = new ArrayList<>();
        for (Long historyId : historyIds) {
            solvedProblemIds.add(historyRepository.findById(historyId).orElse(null).getProblem().getId());
        }

        List<Long> noDuplicateIds = new ArrayList<>();
         for (ProblemIdMapping dto : problemRepository.findAllByCategory(category)) {
            if (!solvedProblemIds.contains(dto.getId())) {
                noDuplicateIds.add(dto.getId());
            }
        }
        if (noDuplicateIds.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No More Problem");
        }
        Long randomProblemId = noDuplicateIds.get((int) (ThreadLocalRandom.current().nextLong(noDuplicateIds.size())));

        return problemRepository.findById(randomProblemId).orElse(null);
    }
}

