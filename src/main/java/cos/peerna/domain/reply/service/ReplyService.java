package cos.peerna.domain.reply.service;

import cos.peerna.domain.history.model.History;
import cos.peerna.domain.history.repository.HistoryRepository;
import cos.peerna.domain.keyword.service.KeywordService;
import cos.peerna.domain.problem.model.Problem;
import cos.peerna.domain.problem.repository.ProblemRepository;
import cos.peerna.domain.reply.dto.data.ReplyData;
import cos.peerna.domain.reply.dto.ReplyRegisterRequestDto;
import cos.peerna.domain.reply.dto.ReplyResponseDto;
import cos.peerna.domain.reply.model.Likey;
import cos.peerna.domain.reply.model.Reply;
import cos.peerna.domain.reply.repository.LikeyRepository;
import cos.peerna.domain.reply.repository.ReplyRepository;
import cos.peerna.domain.room.repository.RoomRepository;
import cos.peerna.domain.user.model.User;
import cos.peerna.domain.user.repository.UserRepository;
import cos.peerna.domain.user.service.UserService;
import cos.peerna.global.security.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyService {

    private static final int PAGE_SIZE = 10;

    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final LikeyRepository likeyRepository;
    private final HistoryRepository historyRepository;
    private final RoomRepository roomRepository;

    private final KeywordService keywordService;
    private final UserService userService;

    @Transactional
    public String make(ReplyRegisterRequestDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("No User Data"));
        Problem problem = problemRepository.findById(dto.getProblemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
        History history = historyRepository.findById(dto.getHistoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "History Not Found"));

        Reply reply = replyRepository.save(Reply.createReply(user, history, problem, dto.getAnswer()));
        int numberOfUserInRoom = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room Not Found"))
                .getConnectedUserIdList().size();
        int numberOfReplyOfHistory = replyRepository.findRepliesByHistory(history).size();
        if (numberOfUserInRoom <= numberOfReplyOfHistory) {
            history.solve();
        }

        /* 키워드 분석 및 KeywordRepository에 저장 */
        keywordService.analyze(dto.getAnswer(), dto.getProblemId());

        return String.valueOf(reply.getId());
    }

    public ReplyResponseDto getRepliesByProblem(Long problemId, int page) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));

        List<Reply> replies = replyRepository.findRepliesByProblemOrderByLikeCountDesc(problem, PageRequest.of(page, PAGE_SIZE));

        List<ReplyData> replyData = replies.stream()
                .map(r -> ReplyData.builder()
                        .replyId(r.getId())
                        .answer(r.getAnswer())
                        .likes(r.getLikeCount())
                        .user(r.getUser())
                        .build()).collect(Collectors.toList());

        return ReplyResponseDto.builder()
                .replyData(replyData)
                .totalCount(replyRepository.countByProblem(problem))
                .build();
    }

    @Transactional
    public String recommendReply(SessionUser sessionUser, Long replyId) {
        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply Not Found"));

        userService.checkForbiddenUser(user, reply.getUser().getId());
        if (isAlreadyLikedReply(user, reply)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already Recommended Reply");
        }

        Likey newLikey = likeyRepository.save(Likey.builder()
                .user(user)
                .reply(reply)
                .build());

        reply.likeReply();
        user.addScore(10);

        return String.valueOf(newLikey.getId());
    }

    private boolean isAlreadyLikedReply(User user, Reply reply) {
        return likeyRepository.findLikeyByUserAndReply(user, reply).isPresent();
    }

    @Transactional
    public void unrecommendReply(SessionUser sessionUser, Long replyId) {
        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply Not Found"));
        if (!isAlreadyLikedReply(user, reply)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Not Recommended Reply");
        }

        Reply.dislikeReply(reply);

        likeyRepository.findLikeyByUserAndReply(user, reply).ifPresent(likeyRepository::delete);
    }

}
