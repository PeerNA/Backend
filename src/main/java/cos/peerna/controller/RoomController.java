package cos.peerna.controller;

import cos.peerna.controller.dto.RoomResponseDto;
import cos.peerna.repository.ConnectedUserRepository;
import cos.peerna.security.LoginUser;
import cos.peerna.security.dto.SessionUser;
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


@Slf4j
@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final ConnectedUserRepository connectedUserRepository;

    @GetMapping("/api/match")
    public DeferredResult<ResponseEntity<RoomResponseDto>> match(@LoginUser SessionUser user) {
        DeferredResult<ResponseEntity<RoomResponseDto>> deferredResult = new DeferredResult<>();

        if (user == null) {
            deferredResult.setResult(ResponseEntity.status(401).build());
            return deferredResult;
        }
        if (connectedUserRepository.findById(user.getId()).orElse(null) != null) {
            deferredResult.setResult(ResponseEntity.status(HttpStatus.CONFLICT).build());
            return deferredResult;
        }

        roomService.match(user, deferredResult);

        return deferredResult;
        // setResult 안 하면 timeout -> 503 Service Unavailable
    }

    @GetMapping("/api/match/next")
    public DeferredResult<ResponseEntity<RoomResponseDto>> next(@LoginUser SessionUser user,
                                                                @RequestParam Long roomId,
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

    // 방 삭제
    @DeleteMapping("/api/room")
    public ResponseEntity<String> leave(@LoginUser SessionUser user, @RequestParam Long roomId) {
        if (user == null)
            return ResponseEntity.status(401).build();

        return roomService.leave(user, roomId);
    }

}
