package cos.peerna.service;

import cos.peerna.controller.dto.DetailHistoryResponseDto;
import cos.peerna.controller.dto.HistoryResponseDto;
import cos.peerna.domain.*;
import cos.peerna.repository.*;
import cos.peerna.security.dto.SessionUser;
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
        List<Map<String, String>> userInfo = new ArrayList<>();
        Map<String, String> mine = new HashMap<>();

        if (replyList.get(1).getUser().getId().equals(user.getId()))
            Collections.reverse(replyList);

        mine.put("answer", replyList.get(0).getAnswer());
        mine.put("userName", replyList.get(0).getUser().getName());
        mine.put("imageUrl", replyList.get(0).getUser().getImageUrl());

        Map<String, String> peer = new HashMap<>();
        peer.put("answer", replyList.get(1).getAnswer());
        peer.put("userName", replyList.get(1).getUser().getName());
        peer.put("imageUrl", replyList.get(1).getUser().getImageUrl());

        userInfo.add(mine);
        userInfo.add(peer);

        return DetailHistoryResponseDto.builder()
                .peerId(replyList.get(1).getUser().getId())
                .question(problem.getQuestion())
                .time(history.getTime())
                .userInfo(userInfo)
                .keyword(keywordList.stream().map(Keyword::getName).collect(Collectors.toList()))
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
