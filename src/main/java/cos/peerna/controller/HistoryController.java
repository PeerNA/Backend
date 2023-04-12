package cos.peerna.controller;

import cos.peerna.config.auth.LoginUser;
import cos.peerna.config.auth.dto.SessionUser;
import cos.peerna.controller.dto.ResponseDto;
import cos.peerna.domain.History;
import cos.peerna.service.HistoryService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HistoryController {
    private final HistoryService historyService;
    @GetMapping("/api/history")
    public List<History> findUserHistory(@LoginUser SessionUser user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No User Data");
        }
        return historyService.findUserHistory(user);
    }

    /**
     * 히스토리는 동료매칭이 되면서 혹은 다음 문제로 넘어가면서 생기기 때문에
     * 아래 api 는 실제 서비스에서 쓰일 확률은 적음
     * 원활한 테스트 환경을 위해 만듬
     */
    @PostMapping("/api/history/new")
    public ResponseDto createHistory(@NotNull @LoginUser SessionUser user, @RequestParam Long problemId) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No User Data");
        }
        historyService.createHistory(problemId);
        return new ResponseDto(200, "success");
    }
}