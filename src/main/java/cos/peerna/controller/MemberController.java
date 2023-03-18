package cos.peerna.controller;

import cos.peerna.controller.dto.MemberRegisterRequestDto;
import cos.peerna.controller.dto.ResponseDto;
import cos.peerna.domain.Member;
import cos.peerna.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/api/members/new")
    public ResponseDto registerMember(@RequestBody MemberRegisterRequestDto dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        Member member = Member.createMember(dto);
        memberService.join(member);

        return new ResponseDto(200, "success");
    }

    @GetMapping("/oauth2-login-fail")
    public void oauth2LoginFail(HttpServletResponse response) {
        String redirectUri = "http://ec2-3-35-151-197.ap-northeast-2.compute.amazonaws.com:3000/";

        try {
            response.sendRedirect(redirectUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
