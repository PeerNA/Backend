package cos.peerna.domain.history.controller;

import cos.peerna.domain.history.service.HistoryService;
import cos.peerna.domain.history.dto.HistoryResponseDto;
import cos.peerna.global.security.LoginUser;
import cos.peerna.global.security.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/history")
public class HistoryController {
    private final HistoryService historyService;
    @GetMapping
    public ResponseEntity<List<HistoryResponseDto>> findUserHistory(@LoginUser SessionUser user, @RequestParam int page) {
        return ResponseEntity.ok(historyService.findUserHistory(user, page));
    }
}
