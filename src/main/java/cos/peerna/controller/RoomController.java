package cos.peerna.controller;

import cos.peerna.domain.Room;
import cos.peerna.security.LoginUser;
import cos.peerna.security.dto.SessionUser;
import cos.peerna.service.MatchService;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RoomController {

    private final MatchService matchService;

    @GetMapping("/api/match")
    public DeferredResult<ResponseEntity<Room>> match(@LoginUser SessionUser user) {
        DeferredResult<ResponseEntity<Room>> deferredResult = new DeferredResult<>();

        if (user == null) {
            ResponseEntity<Room> unauthorizedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            deferredResult.setResult(unauthorizedResponse);
            return deferredResult;
        }

        matchService.match(user, deferredResult);

        return deferredResult;
    }
}
