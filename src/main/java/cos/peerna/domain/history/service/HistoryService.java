package cos.peerna.domain.history.service;

import cos.peerna.domain.history.dto.response.DetailHistoryResponse;
import cos.peerna.domain.history.dto.response.HistoryResponse;
import cos.peerna.domain.history.model.History;
import cos.peerna.domain.history.repository.HistoryRepository;
import cos.peerna.domain.keyword.model.Keyword;
import cos.peerna.domain.keyword.repository.KeywordRepository;
import cos.peerna.domain.problem.model.Problem;
import cos.peerna.domain.problem.repository.ProblemRepository;
import cos.peerna.domain.reply.dto.response.ReplyResponse;
import cos.peerna.domain.room.dto.ChatMessageSendDto;
import cos.peerna.domain.reply.model.Reply;
import cos.peerna.domain.reply.repository.ReplyRepository;
import cos.peerna.domain.room.model.Chat;
import cos.peerna.domain.room.model.Room;
import cos.peerna.domain.room.repository.ChatRepository;
import cos.peerna.domain.room.repository.RoomRepository;
import cos.peerna.domain.user.repository.UserRepository;
import cos.peerna.global.security.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final HistoryRepository historyRepository;
    private final KeywordRepository keywordRepository;
    private final ChatRepository chatRepository;
    private final ProblemRepository problemRepository;
    private final RoomRepository roomRepository;

    public List<HistoryResponse> findUserHistory(Long userId, Long cursorId, int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.ASC, "id"));
        List<Reply> replyList = replyRepository.findRepliesByUserIdOrderByIdAsc(userId, cursorId, pageable);

        return replyList.stream().map(reply -> {
            History history = reply.getHistory();
            Problem problem = reply.getProblem();
            return HistoryResponse.builder()
                    .historyId(history.getId())
                    .problemId(problem.getId())
                    .question(problem.getQuestion())
                    .category(problem.getCategory())
                    .time(history.getTime())
                    .build();
        }).collect(Collectors.toList());
    }

    public DetailHistoryResponse findDetailHistory(SessionUser user, Long historyId) {
        History history = historyRepository.findByIdWithProblem(historyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "History Not Found"));
        Problem problem = history.getProblem();
        List<Reply> replyList = replyRepository.findRepliesWithUserByHistoryOrderByHistoryIdDesc(history);
        List<Keyword> keywordList = keywordRepository.findTop3KeywordsByProblemOrderByCountDesc(problem);

        ReplyResponse mine = null;
        ReplyResponse peer = null;

        for (Reply reply : replyList) {
            if (reply.getUser().getId().equals(user.getId())) {
                mine = ReplyResponse.builder()
                        .replyId(reply.getId())
                        .likeCount((long) reply.getLikeCount())
                        .answer(reply.getAnswer())
                        .userId(reply.getUser().getId())
                        .userName(reply.getUser().getName())
                        .userImage(reply.getUser().getImageUrl())
                        .build();
            } else {
                peer = ReplyResponse.builder()
                        .replyId(reply.getId())
                        .likeCount((long) reply.getLikes().size())
                        .answer(reply.getAnswer())
                        .userId(reply.getUser().getId())
                        .userName(reply.getUser().getName())
                        .userImage(reply.getUser().getImageUrl())
                        .build();
            }
        }

        List<Chat> chat = chatRepository.findAllByHistory(history);

        return DetailHistoryResponse.builder()
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
