package cos.peerna.config.auth;

import cos.peerna.config.auth.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailService userDetailService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final HttpSession httpSession;
    // Session 에 Exception 정보를 전달 + entryPoint에서 확인하고 exception에 넣는다.

    /**
     * @param authentication the authentication request object.
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        SessionUser sessionUser = (SessionUser) userDetailService.loadUserByUsername(authentication.getName().toString());

        String reqPassword = authentication.getCredentials().toString();
        if (!passwordEncoder.matches(reqPassword, sessionUser.getPassword()))
            throw new BadCredentialsException("Dosen't match password");

        httpSession.setAttribute("user", sessionUser);

        return new UsernamePasswordAuthenticationToken(sessionUser, null, sessionUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
