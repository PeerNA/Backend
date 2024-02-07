package cos.peerna.domain.history.controller;

import cos.peerna.domain.history.service.HistoryService;
import cos.peerna.domain.history.dto.DetailHistoryResponseDto;
import cos.peerna.domain.history.dto.HistoryResponseDto;
import cos.peerna.global.security.LoginUser;
import cos.peerna.global.security.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/history")
public class HistoryController {
    private final HistoryService historyService;
    @GetMapping()
    public List<HistoryResponseDto> findUserHistory(@LoginUser SessionUser user, @RequestParam int page) {
        return historyService.findUserHistory(user, page);
    }

    @GetMapping("/detail")
    public DetailHistoryResponseDto findDetailHistory(@LoginUser SessionUser user, @RequestParam Long historyId) {
        return historyService.findDetailHistory(user, historyId);
    }
}
