package cos.peerna.global.common.controller;

import cos.peerna.domain.history.dto.response.DetailHistoryResponse;
import cos.peerna.domain.history.service.HistoryService;
import cos.peerna.domain.reply.service.ReplyService;
import cos.peerna.domain.room.service.RoomService;
import cos.peerna.domain.user.model.Category;
import cos.peerna.global.security.LoginUser;
import cos.peerna.global.security.dto.SessionUser;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

// http://localhost:8080/login 연동 로그인 모음
// http://localhost:8080/oauth2/authorization/google 구글 연동 로그인
// GET https://github.com/login/oauth/authorize Github 연동 로그인
@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ReplyService replyService;
    private final HistoryService historyService;
    private final RoomService roomService;

    @GetMapping("/")
    public String index(@Nullable @LoginUser SessionUser user, Model model) {
        model.addAttribute("userId", user == null ? null : user.getId());
        model.addAttribute("userName", user == null ? "Guest" : user.getName());
        model.addAttribute("userImage", user == null ?
                "https://avatars.githubusercontent.com/u/0?v=4" : user.getImageUrl());
        model.addAttribute("pageTitle", "피어나");
        return "pages/index";
    }

    @GetMapping("/reply/solo")
    public String soloStudy(@Nullable @LoginUser SessionUser user, Model model) {
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("userId", user.getId());
        model.addAttribute("userName", user.getName());
        model.addAttribute("userImage", user.getImageUrl());
        model.addAttribute("pageTitle", "Study - Solo");
        return "pages/reply/solo";
    }

    @GetMapping("/reply/multi")
    public String multiStudy(@Nullable @LoginUser SessionUser user, Model model,
                             @RequestParam("roomId") Integer roomId) {
        if (user == null || !roomService.isConnectedUser(roomId, user.getId())) {
            return "redirect:/";
        }
        Category category = roomService.findById(roomId).getCategory();
        model.addAttribute("userId", user.getId());
        model.addAttribute("userName", user.getName());
        model.addAttribute("userImage", user.getImageUrl());
        model.addAttribute("pageTitle", "Study - Multi");
        model.addAttribute("category", category);
        return "pages/reply/multi";
    }

    @GetMapping("/reply/latest")
    public String latestReplies(@Nullable @LoginUser SessionUser user, Model model) {
        model.addAttribute("userId", user == null ? null : user.getId());
        model.addAttribute("userName", user == null ? "Guest" : user.getName());
        model.addAttribute("userImage", user == null ?
                "https://avatars.githubusercontent.com/u/0?v=4" : user.getImageUrl());
        model.addAttribute("pageTitle", "최신 사람 답변 모음 - 피어나");

        return "pages/reply/latest";
    }

    @GetMapping("/reply/others")
    public String othersReplies(@Nullable @LoginUser SessionUser user, Model model) {
        model.addAttribute("userId", user == null ? null : user.getId());
        model.addAttribute("userName", user == null ? "Guest" : user.getName());
        model.addAttribute("userImage", user == null ?
                "https://avatars.githubusercontent.com/u/0?v=4" : user.getImageUrl());
        model.addAttribute("pageTitle", "다른 사람 답변 모음 - 피어나");

        return "pages/reply/others";
    }

    @GetMapping("/mypage")
    public String myPage(@Nullable @LoginUser SessionUser user, Model model) {
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("userId", user.getId());
        model.addAttribute("userName", user.getName());
        model.addAttribute("userImage", user.getImageUrl());
        model.addAttribute("userEmail", user.getEmail());
        model.addAttribute("userImage", user.getImageUrl());
        model.addAttribute("pageTitle", "My Page - 피어나");
        return "pages/user/mypage";
    }

    @GetMapping("/reply/{id}")
    public String reply(@Nullable @LoginUser SessionUser user, Model model,
                        @Nullable @PathVariable("id") Long historyId) {
        model.addAttribute("pageTitle", "Reply - 피어나");
        model.addAttribute("userId", user == null ? null : user.getId());
        model.addAttribute("userName", user == null ? "Guest" : user.getName());
        model.addAttribute("userImage", user == null ?
                "https://avatars.githubusercontent.com/u/0?v=4" : user.getImageUrl());

        DetailHistoryResponse detailHistory = historyService.findDetailHistory(historyId);
        model.addAttribute("history", detailHistory);

        return "pages/reply/view";
    }

    /*
     NOTICE: 아래 페이지들은 테스트를 위해 존재. 실제 서비스에서는 제거해야함..
     */
    @GetMapping("/login")
    public String login() {
        return "pages/user/login";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "pages/user/signUp";
    }
}
