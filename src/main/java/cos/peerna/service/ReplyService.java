package cos.peerna.service;

import cos.peerna.config.auth.dto.SessionUser;
import cos.peerna.controller.dto.ReplyRegisterRequestDto;
import cos.peerna.controller.dto.ReplyResponseDto;
import cos.peerna.domain.*;
import cos.peerna.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final LikeyRepository likeyRepository;
    private final HistoryRepository historyRepository;

    public void make(ReplyRegisterRequestDto dto, SessionUser sessionUser) {
        User user = userRepository.findByEmail(sessionUser.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("No User Data"));
        Problem problem = problemRepository.findById(dto.getProblemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
        History history = historyRepository.findById(dto.getHistoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "History Not Found"));

        Reply reply = Reply.createReply(user, history, problem, dto.getAnswer());
        replyRepository.save(reply);
    }

    public List<ReplyResponseDto> getRepliesByProblem(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
        return replyRepository.findRepliesByProblem(problem).stream()
                .map(r -> ReplyResponseDto.builder()
                        .replyId(r.getId())
                        .answer(r.getAnswer())
                        .userId(r.getUser().getId())
                        .imageUrl(r.getUser().getImageUrl())
                        .name(r.getUser().getName())
                        .build()).collect(Collectors.toList());
    }

    public void recommendReply(SessionUser sessionUser, Long replyId) {
        User user = userRepository.findByEmail(sessionUser.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply Not Found"));

        likeyRepository.findLikeyByUserAndUser(user, reply).ifPresent(recommend -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already Recommended Reply");
        });
        Likey likey = Likey.createLikey(user, reply);
        likeyRepository.save(likey);
    }
}
