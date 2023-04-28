package cos.peerna.controller;

import cos.peerna.controller.dto.RoomResponseDto;
import cos.peerna.domain.Room;
import cos.peerna.security.LoginUser;
import cos.peerna.security.dto.SessionUser;
import cos.peerna.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/api/match")
    public DeferredResult<ResponseEntity<RoomResponseDto>> match(@LoginUser SessionUser user) {
        DeferredResult<ResponseEntity<RoomResponseDto>> deferredResult = new DeferredResult<>();

        if (user == null) {
            deferredResult.setResult(null);
            return deferredResult;
        }

        roomService.match(user, deferredResult);

        return deferredResult;
        // setResult 안 하면 timeout -> 503 Service Unavailable
    }
}
