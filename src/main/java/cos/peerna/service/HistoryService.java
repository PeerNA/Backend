package cos.peerna.service;

import cos.peerna.config.auth.dto.SessionUser;
import cos.peerna.controller.dto.HistoryResponseDto;
import cos.peerna.controller.dto.ReplyResponseDto;
import cos.peerna.domain.History;
import cos.peerna.domain.Reply;
import cos.peerna.domain.User;
import cos.peerna.repository.HistoryRepository;
import cos.peerna.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

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

    public List<HistoryResponseDto> findUserHistory(SessionUser sessionUser) {
        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
        List<History> historyList =  historyRepository.findHistoriesByUser(user);

        List<HistoryResponseDto> historyResponseDtoList = new ArrayList<HistoryResponseDto>();

        for (History history : historyList) {
            List<Reply> replyList =  replyRepository.findRepliesByHistory(history);
            List<ReplyResponseDto> replyResponseDtoList = new ArrayList<ReplyResponseDto>();

            for (Reply reply : replyList) {
                ReplyResponseDto replyResponseDto = ReplyResponseDto.builder()
                        .replyId(reply.getId())
                        .userId(reply.getUser().getId())
                        .name(reply.getUser().getName())
                        .imageUrl(reply.getUser().getImageUrl())
                        .answer(reply.getAnswer())
                        .build();
                replyResponseDtoList.add(replyResponseDto);
            }
            HistoryResponseDto historyResponseDto = HistoryResponseDto.builder()
                    .historyId(history.getId())
                    .problem(history.getProblem())
                    .replyResponseDtoList(replyResponseDtoList)
                    .build();

            historyResponseDtoList.add(historyResponseDto);
        }

        return historyResponseDtoList;
    }
}
