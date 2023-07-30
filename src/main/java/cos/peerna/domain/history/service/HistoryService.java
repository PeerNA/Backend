package cos.peerna.domain.history.service;

import cos.peerna.domain.history.dto.DetailHistoryResponseDto;
import cos.peerna.domain.history.dto.HistoryResponseDto;
import cos.peerna.domain.history.model.History;
import cos.peerna.domain.history.repository.HistoryRepository;
import cos.peerna.domain.room.dto.ChatMessageSendDto;
import cos.peerna.domain.reply.dto.data.ReplyData;
import cos.peerna.domain.keyword.model.Keyword;
import cos.peerna.domain.keyword.repository.KeywordRepository;
import cos.peerna.domain.problem.model.Problem;
import cos.peerna.domain.problem.repository.ProblemRepository;
import cos.peerna.domain.reply.model.Reply;
import cos.peerna.domain.reply.repository.ReplyRepository;
import cos.peerna.domain.room.model.Chat;
import cos.peerna.domain.room.repository.ChatRepository;
import cos.peerna.domain.room.model.Room;
import cos.peerna.domain.room.repository.RoomRepository;
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
    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final ProblemRepository problemRepository;
    private final KeywordRepository keywordRepository;
    private final RoomRepository roomRepository;
    private final ChatRepository chatRepository;

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

    public DetailHistoryResponseDto findDetailHistory(SessionUser user, Long historyId) {
        History history = historyRepository.findById(historyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "History Not Found"));
        Problem problem = history.getProblem();
        List<Reply> replyList = replyRepository.findRepliesByHistory(history);
        List<Keyword> keywordList = keywordRepository.findKeywordsByProblemOrderByCountDesc(problem).subList(0, 3);

        ReplyData mine = null;
        ReplyData peer = null;

        for (Reply reply : replyList) {
            if (reply.getUser().getId().equals(user.getId())) {
                mine = ReplyData.builder()
                        .replyId(reply.getId())
                        .likes((long) reply.getLikes().size())
                        .answer(reply.getAnswer())
                        .user(reply.getUser())
                        .build();
            } else {
                peer = ReplyData.builder()
                        .replyId(reply.getId())
                        .likes((long) reply.getLikes().size())
                        .answer(reply.getAnswer())
                        .user(reply.getUser())
                        .build();
            }
        }

        List<Chat> chat = chatRepository.findAllByHistory(history);

        return DetailHistoryResponseDto.builder()
                .question(problem.getQuestion())
                .time(history.getTime())
                .mine(mine)
                .peer(peer)
                .keyword(keywordList.stream().map(Keyword::getName).collect(Collectors.toList()))
                .chat(chat.stream().map(ChatMessageSendDto::new).collect(Collectors.toList()))
                .build();
    }

    public History createHistory(Long problemId, Integer roomId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room Not Found"));
        History history = historyRepository.save(History.createHistory(problem));
        room.getHistoryIdList().add(history.getId());
        roomRepository.save(room);
        return history;
    }
}
