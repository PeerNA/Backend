package cos.peerna.service;

import cos.peerna.config.auth.dto.SessionUser;
import cos.peerna.controller.dto.ReplyResponseDto;
import cos.peerna.domain.History;
import cos.peerna.domain.Reply;
import cos.peerna.domain.User;
import cos.peerna.repository.HistoryRepository;
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

//    public void make(Problem problem) {
//        validateProblem(problem);
//        problemRepository.save(problem);
//    }

//    private void validateProblem(Problem problem) {
//        if (problemRepository.findProblemByQuestion(problem.getQuestion()).isPresent()) {
//            throw new IllegalArgumentException("This Question already exists.");
//        }
//    }

    public List<History> findUserHistory(SessionUser sessionUser) {
        User user = userRepository.findByEmail(sessionUser.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));

        List<Reply> replyList = replyRepository.findRepliesByUser(user);
        List<History> historyList = new ArrayList<History>();

        return replyList.stream().map(Reply::getHistory).collect(Collectors.toList());
    }
}
