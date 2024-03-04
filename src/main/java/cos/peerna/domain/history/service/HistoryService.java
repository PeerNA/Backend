package cos.peerna.domain.history.service;

import cos.peerna.domain.history.dto.HistoryResponseDto;
import cos.peerna.domain.history.model.History;
import cos.peerna.domain.problem.model.Problem;
import cos.peerna.domain.reply.model.Reply;
import cos.peerna.domain.reply.repository.ReplyRepository;
import cos.peerna.domain.user.model.User;
import cos.peerna.domain.user.repository.UserRepository;
import cos.peerna.global.security.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class HistoryService {
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;

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
    }
}
