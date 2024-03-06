package cos.peerna.domain.reply.service;

import cos.peerna.domain.github.event.CommitReplyEvent;
import cos.peerna.domain.history.model.History;
import cos.peerna.domain.history.repository.HistoryRepository;
import cos.peerna.domain.keyword.service.KeywordService;
import cos.peerna.domain.problem.model.Problem;
import cos.peerna.domain.problem.repository.ProblemRepository;
import cos.peerna.domain.reply.dto.request.RegisterReplyRequest;
import cos.peerna.domain.reply.dto.request.UpdateReplyRequest;
import cos.peerna.domain.reply.dto.response.GetReplyWithProfileResponse;
import cos.peerna.domain.reply.model.Likey;
import cos.peerna.domain.reply.model.Reply;
import cos.peerna.domain.reply.repository.LikeyRepository;
import cos.peerna.domain.reply.repository.ReplyRepository;
import cos.peerna.domain.user.model.User;
import cos.peerna.domain.user.repository.UserRepository;
import cos.peerna.domain.user.service.UserService;
import cos.peerna.global.security.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final KeywordService keywordService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public String make(RegisterReplyRequest dto, SessionUser sessionUser) {
        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new UsernameNotFoundException("No User Data"));
        Problem problem = problemRepository.findById(dto.problemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
        History history = historyRepository.save(History.createHistory(problem));
        Reply reply = replyRepository.save(Reply.builderForRegister()
                .answer(dto.answer())
                .history(history)
                .problem(problem)
                .user(user)
                .build());

        keywordService.analyze(dto.answer(), dto.problemId());

        if (user.getGithubRepo() != null) {
            eventPublisher.publishEvent(CommitReplyEvent.of(
                    sessionUser.getLogin(), sessionUser.getToken(), problem, dto.answer()));
        }
        /*
        TODO: user.getGithubRepo() == null 일 때, 유저에게 GithubRepo를 등록하라는 메시지 전달
         */

        return String.valueOf(reply.getId());
    }

    @Transactional
    public void modify(UpdateReplyRequest dto, SessionUser sessionUser) {
        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new UsernameNotFoundException("No User Data"));
        Problem problem = problemRepository.findById(dto.problemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
        Reply reply = replyRepository.findFirstByUserAndProblemOrderByIdDesc(user, problem)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply Not Found"));

        reply.modifyAnswer(dto.answer());
        keywordService.analyze(dto.answer(), dto.problemId());

        if (user.getGithubRepo() != null) {
            eventPublisher.publishEvent(CommitReplyEvent.of(
                    sessionUser.getLogin(), sessionUser.getToken(), problem, dto.answer()));
        }
        /*
        TODO: user.getGithubRepo() == null 일 때, 유저에게 GithubRepo를 등록하라는 메시지 전달
         */
    }

    public List<GetReplyWithProfileResponse> getRepliesByProblem(Long problemId, int page) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));

        List<Reply> replies = replyRepository.findRepliesByProblemOrderByLikeCountDesc(
                problem, PageRequest.of(page, PAGE_SIZE));

        List<GetReplyWithProfileResponse> replyData = replies.stream()
                .map(r -> GetReplyWithProfileResponse.from(
                        r.getId(), r.getLikeCount(), r.getAnswer(),
                        r.getUser().getId(), r.getUser().getName(), r.getUser().getImageUrl())
                        ).collect(Collectors.toList());

        return replyData;
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
