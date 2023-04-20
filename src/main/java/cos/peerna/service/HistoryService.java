package cos.peerna.service;

import cos.peerna.controller.dto.HistoryResponseDto;
import cos.peerna.security.dto.SessionUser;
import cos.peerna.domain.History;
import cos.peerna.domain.Problem;
import cos.peerna.domain.Reply;
import cos.peerna.domain.User;
import cos.peerna.repository.HistoryRepository;
import cos.peerna.repository.ProblemRepository;
import cos.peerna.repository.ReplyRepository;
import cos.peerna.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final ProblemRepository problemRepository;

//    public void make(Problem problem) {
//        validateProblem(problem);
//        problemRepository.save(problem);
//    }

//    private void validateProblem(Problem problem) {
//        if (problemRepository.findProblemByQuestion(problem.getQuestion()).isPresent()) {
//            throw new IllegalArgumentException("This Question already exists.");
//        }
//    }

    public List<HistoryResponseDto> findUserHistory(SessionUser sessionUser, int page) {
        final int PAGE_SIZE = 8;

        User user = userRepository.findByEmail(sessionUser.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
        List<Reply> replyList = replyRepository.findRepliesByUser(user);
        List<HistoryResponseDto> historyResponseDtoList = new ArrayList<>(replyList.stream().map(r -> {
            return HistoryResponseDto.builder()
                    .historyId(r.getHistory().getId())
                    .problemId(r.getProblem().getId())
                    .question(r.getProblem().getQuestion())
                    .category(r.getProblem().getCategory())
                    .time(r.getHistory().getTime())
                    .build();
        }).toList());

        historyResponseDtoList.sort(HistoryResponseDto.builder().build().reversed());
        return historyResponseDtoList.stream().skip((long) PAGE_SIZE * page).limit(PAGE_SIZE).collect(Collectors.toList());
    }

    public void createHistory(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
        History history = History.createHistory(problem);
        historyRepository.save(history);
    }
}
