package cos.peerna.controller;

import cos.peerna.config.auth.LoginUser;
import cos.peerna.config.auth.dto.SessionUser;
import cos.peerna.domain.History;
import cos.peerna.service.HistoryService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HistoryController {
    private final HistoryService historyService;
    @GetMapping("/api/history")
    public List<History> findUserHistory(@LoginUser SessionUser sessionUser, HttpServletResponse response) {
        if (sessionUser == null) {
            response.setStatus(401);
            return new ArrayList<History>();
        }
        return historyService.findUserHistory(sessionUser);
    }
}
