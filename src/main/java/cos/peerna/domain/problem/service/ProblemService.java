package cos.peerna.domain.problem.service;

import cos.peerna.domain.problem.dto.response.AnswerAndKeywordResponse;
import cos.peerna.domain.problem.dto.response.ProblemResponse;
import cos.peerna.domain.problem.model.Problem;
import cos.peerna.domain.problem.model.ProblemAnswerKeywords;
import cos.peerna.domain.problem.repository.ProblemRepository;
import cos.peerna.domain.user.model.Category;
import cos.peerna.domain.history.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final HistoryRepository historyRepository;

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

    public AnswerAndKeywordResponse getAnswerAndKeywordByProblemId(Long problemId) {
        ProblemAnswerKeywords answerKeywords = problemRepository.findAnswerAndKeywordsById(problemId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
        return AnswerAndKeywordResponse.of(answerKeywords.getAnswer(), answerKeywords.getKeywords());
    }

    public List<ProblemResponse> findProblemsByCategory(Category category, Long cursorId, int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by("id").ascending());

        List<Problem> problems = problemRepository.findByCategoryAndIdGreaterThanOrderByIdAsc(category, cursorId, pageable);
        List<ProblemResponse> problemResponseDtos = new ArrayList<>();
        for (Problem problem : problems) {
            problemResponseDtos.add(ProblemResponse.of(
                    problem.getId(), problem.getQuestion(), problem.getAnswer(), problem.getCategory()));
        }
        return problemResponseDtos;
    }
}

