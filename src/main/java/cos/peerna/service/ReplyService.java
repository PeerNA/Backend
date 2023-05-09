package cos.peerna.service;

import cos.peerna.controller.dto.ReplyResponseDto;
import cos.peerna.security.dto.SessionUser;
import cos.peerna.controller.dto.ReplyRegisterRequestDto;
import cos.peerna.controller.dto.data.ReplyData;
import cos.peerna.domain.*;
import cos.peerna.repository.*;
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
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final LikeyRepository likeyRepository;
    private final HistoryRepository historyRepository;

    private final KeywordService keywordService;
    private final RoomRepository roomRepository;

    @Transactional
    public void make(ReplyRegisterRequestDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("No User Data"));
        Problem problem = problemRepository.findById(dto.getProblemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));
        History history = historyRepository.findById(dto.getHistoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "History Not Found"));

        Reply reply = Reply.createReply(user, history, problem, dto.getAnswer());
        replyRepository.save(reply);
        int numberOfUserInRoom = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room Not Found")).getConnectedUserIdList().size();
        int numberOfReplyOfHistory = replyRepository.findRepliesByHistory(history).size();
        if (numberOfUserInRoom <= numberOfReplyOfHistory) {
            history.solve();
        }

        /* 키워드 분석 및 KeywordRepository에 저장 */
        keywordService.analyze(dto.getAnswer(), dto.getProblemId());
    }

    public ReplyResponseDto getRepliesByProblem(Long problemId, int page) {
        final int PAGE_SIZE = 10;

        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem Not Found"));

        List<Reply> data = replyRepository.findRepliesByProblemOrderByLikeCountDesc(problem, PageRequest.of(page, PAGE_SIZE));

        List<ReplyData> replyData = data.stream()
                .map(r -> ReplyData.builder()
                        .replyId(r.getId())
                        .answer(r.getAnswer())
                        .userId(r.getUser().getId())
                        .likes(r.getLikeCount())
                        .imageUrl(r.getUser().getImageUrl())
                        .name(r.getUser().getName())
                        .build()).collect(Collectors.toList());

        return ReplyResponseDto.builder()
                .replyData(replyData)
                .totalCount(replyRepository.countByProblem(problem))
                .build();
    }

    public void recommendReply(SessionUser sessionUser, Long replyId) {
        User user = userRepository.findById(sessionUser.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reply Not Found"));

        likeyRepository.findLikeyByUserAndReply(user, reply).ifPresent(recommend -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already Recommended Reply");
        });
        Likey likey = Likey.createLikey(user, reply);
        likeyRepository.save(likey);
    }
}
