package cos.peerna.service;

import cos.peerna.controller.dto.DetailHistoryRequestDto;
import cos.peerna.controller.dto.DetailHistoryResponseDto;
import cos.peerna.controller.dto.HistoryResponseDto;
import cos.peerna.domain.History;
import cos.peerna.domain.Problem;
import cos.peerna.domain.Reply;
import cos.peerna.domain.User;
import cos.peerna.repository.HistoryRepository;
import cos.peerna.repository.ProblemRepository;
import cos.peerna.repository.ReplyRepository;
import cos.peerna.repository.UserRepository;
import cos.peerna.security.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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

        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
        List<Reply> replyList = replyRepository.findRepliesByUserOrderByIdDesc(user, PageRequest.of(page, PAGE_SIZE));

        return replyList.stream().map(reply -> {
            History history = reply.getHistory();
            Problem problem = reply.getProblem();
            return HistoryResponseDto.builder()
                    .historyId(history.getId())
                    .problemId(problem.getId())
                    .question(problem.getQuestion())
                    .category(problem.getCategory())
                    .time(history.getTime())
                    .build();
        }).collect(Collectors.toList());

//        return new ArrayList<>(replyList.stream().map(r -> HistoryResponseDto.builder()
//                .historyId(r.getHistory().getId())
//                .problemId(r.getProblem().getId())
//                .question(r.getProblem().getQuestion())
//                .category(r.getProblem().getCategory())
//                .time(r.getHistory().getTime())
//                .build()).toList());
    }

    public DetailHistoryResponseDto findDetailHistory(SessionUser user, DetailHistoryRequestDto dto) {
        History history = historyRepository.findById(dto.getHistoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "History Not Found"));
        Problem problem = history.getProblem();
        List<Reply> replyList = replyRepository.findRepliesByHistory(history);

        log.info("user: {}, first: {}", replyList.get(1).getUser().getId(), user.getId());

        if (replyList.get(1).getUser().getId().equals(user.getId()))
            Collections.reverse(replyList);

        return DetailHistoryResponseDto.builder()
                .bUserId(replyList.get(1).getUser().getId())
                .question(problem.getQuestion())
                .category(problem.getCategory())
                .time(history.getTime())
                .aUserAnswer(replyList.get(0).getAnswer())
                .bUserAnswer(replyList.get(1).getAnswer())
                .aUserNickname(replyList.get(0).getUser().getName())
                .bUserNickname(replyList.get(1).getUser().getName())
                .aUserImage(replyList.get(0).getUser().getImageUrl())
                .bUserImage(replyList.get(1).getUser().getImageUrl())
                .build();
    }

    public void createHistory(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
        History history = History.createHistory(problem);
        historyRepository.save(history);
    }
}
