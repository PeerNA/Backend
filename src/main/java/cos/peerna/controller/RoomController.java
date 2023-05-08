package cos.peerna.controller;

import cos.peerna.controller.dto.RoomResponseDto;
import cos.peerna.security.LoginUser;
import cos.peerna.security.dto.SessionUser;
import cos.peerna.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;


@Slf4j
@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
//    private final SessionRepository<SessionUser> sessionRepository;
//    private final RedisOperationsSessionRepository sessionRepository;

    @GetMapping("/api/match")
    public DeferredResult<ResponseEntity<RoomResponseDto>> match(@LoginUser SessionUser user) {
        DeferredResult<ResponseEntity<RoomResponseDto>> deferredResult = new DeferredResult<>();

        if (user == null) {
            deferredResult.setResult(ResponseEntity.status(401).build());
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

//    @GetMapping("/api/match/cancel")

}
