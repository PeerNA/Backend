package cos.peerna.domain.match.controller;

import cos.peerna.domain.match.model.Standby;
import cos.peerna.domain.match.service.MatchService;
import cos.peerna.domain.user.model.Category;
import cos.peerna.global.security.dto.SessionUser;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @MessageMapping("/match/join")
    @SendToUser("/match/join")
    public ResponseEntity<String> joinQueue(SimpMessageHeaderAccessor messageHeaderAccessor, String category) {
        SessionUser user = (SessionUser) messageHeaderAccessor.getSessionAttributes().get("user");

        Standby standby =  matchService.addStandby(user, Category.valueOf(category));
        if (standby == null) {
            return new ResponseEntity<>("already exist", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}