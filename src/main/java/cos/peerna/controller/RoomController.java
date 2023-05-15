package cos.peerna.controller;

import cos.peerna.controller.dto.DetailHistoryResponseDto;
import cos.peerna.controller.dto.MatchedUserDto;
import cos.peerna.controller.dto.RoomResponseDto;
import cos.peerna.domain.*;
import cos.peerna.repository.ConnectedUserRepository;
import cos.peerna.repository.HistoryRepository;
import cos.peerna.repository.RoomRepository;
import cos.peerna.repository.UserRepository;
import cos.peerna.security.LoginUser;
import cos.peerna.security.dto.SessionUser;
import cos.peerna.service.HistoryService;
import cos.peerna.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.server.ResponseStatusException;

import java.lang.module.ResolutionException;


@Slf4j
@RestController
@RequiredArgsConstructor
public class RoomController {

    private final UserRepository userRepository;
    private final RoomService roomService;
    private final RoomRepository roomRepository;
    private final HistoryService historyService;
    private final HistoryRepository historyRepository;
    private final ConnectedUserRepository connectedUserRepository;

    @GetMapping("/api/match")
    public DeferredResult<ResponseEntity<RoomResponseDto>> match(@LoginUser SessionUser user) {
        DeferredResult<ResponseEntity<RoomResponseDto>> deferredResult = new DeferredResult<>();

        if (user == null) {
            deferredResult.setResult(ResponseEntity.status(401).build());
            return deferredResult;
        }
        ConnectedUser connectedUser = connectedUserRepository.findById(user.getId()).orElse(null);
        if (connectedUser != null) {
            Room room = roomRepository.findById(connectedUser.getRoomId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room Not Found"));
            History history = historyRepository.findById(room.getHistoryIdList().get(0))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "History Not Found"));

            Long peerId = null;
            for (Long connectedUserId : room.getConnectedUserIdList()) {
                if (connectedUserId.equals(user.getId())) {
                    peerId = connectedUserId;
                    break;
                }
            }
            User peer = userRepository.findById(peerId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Peer Not Found"));
            Problem problem = history.getProblem();
            log.debug("loading problem: {}", problem.getQuestion());

            deferredResult.setResult(ResponseEntity.status(HttpStatus.CONFLICT).body(RoomResponseDto.builder()
                    .roomId(room.getId())
                    .historyId(history.getId())
                    .problem(problem)
                    .peer(new MatchedUserDto(peer))
                    .build()));
            return deferredResult;
        }

        roomService.match(user, deferredResult);

        return deferredResult;
        // setResult 안 하면 timeout -> 503 Service Unavailable
    }

    @GetMapping("/api/match/next")
    public DeferredResult<ResponseEntity<RoomResponseDto>> next(@LoginUser SessionUser user,
                                                                @RequestParam Integer roomId,
                                                                @RequestParam Long peerId) {

        DeferredResult<ResponseEntity<RoomResponseDto>> deferredResult = new DeferredResult<>();
        if (user == null) {
            deferredResult.setResult(ResponseEntity.status(401).build());
            return deferredResult;
        }

        if (peerId == 0)
            roomService.soloNext(user, roomId, deferredResult);
        else {
            try {
                roomService.duoNext(user, roomId, peerId, deferredResult);
            } catch (NullPointerException e) {
                deferredResult.setResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
            }

        }

        return deferredResult;
    }

    // match 대기열 취소
    @DeleteMapping("/api/match")
    public ResponseEntity<String> matchCancel(@LoginUser SessionUser user) {
        if (user == null)
            return ResponseEntity.status(401).build();

        return roomService.matchCancel(user);
    }

    // 방 나가기 (모두 나가면 방 삭제)
    @DeleteMapping("/api/room")
    public ResponseEntity<String> leave(@LoginUser SessionUser user, @RequestParam Integer roomId) {
        if (user == null)
            return ResponseEntity.status(401).build();

        return roomService.leave(user, roomId);
    }

    @GetMapping("/api/match/status")
    public DeferredResult<ResponseEntity<DetailHistoryResponseDto>> next(@LoginUser SessionUser user,
                                                                         @RequestParam Integer roomId) {

        DeferredResult<ResponseEntity<DetailHistoryResponseDto>> deferredResult = new DeferredResult<>();
        if (user == null) {
            deferredResult.setResult(ResponseEntity.status(401).build());
            return deferredResult;
        }
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            deferredResult.setResult(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
            return deferredResult;
        }

        History history = historyRepository.findById(room.getHistoryIdList().getLast())
                .orElseThrow(() -> new ResolutionException("History not found"));
        if (history.isSolved()) {
            deferredResult.setResult(ResponseEntity.ok(historyService.findDetailHistory(user, history.getId())));
        } else {
            deferredResult.setResult(ResponseEntity.accepted().build());
        }

        return deferredResult;
    }
}
