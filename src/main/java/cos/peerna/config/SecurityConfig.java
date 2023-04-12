package cos.peerna.config;

import cos.peerna.security.*;
import cos.peerna.config.auth.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity // spring security 설정들을 활성화시켜준다.
@Configuration
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable();
        http
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true);
        http
                .formLogin()
                .loginPage("/spring-security-login")
                .loginProcessingUrl("/api/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler);
        http.logout()
                .logoutUrl("/logout")   // 로그아웃 처리 URL (= form action url)
                //.logoutSuccessUrl("/login") // 로그아웃 성공 후 targetUrl,
                // logoutSuccessHandler 가 있다면 효과 없으므로 주석처리.
                .addLogoutHandler((request, response, authentication) -> {
                    // 사실 굳이 내가 세션 무효화하지 않아도 됨.
                    // LogoutFilter가 내부적으로 해줌.
                    HttpSession session = request.getSession();
                    if (session != null) {
                        session.invalidate();
                    }
                })  // 로그아웃 핸들러 추가
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.sendRedirect("/");
                }); // 로그아웃 성공 핸들러
        http
                .authenticationProvider(customAuthenticationProvider);
        http
                .headers().frameOptions().disable()
                .and()
                .oauth2Login()
                .failureUrl("/oauth2-login-fail")
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
        http
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint);
        return http.build();
    }


}
/**
 * csrf().disable() 하는 이유
 * 추후에 없앨 가능성 높음
 * https://velog.io/@woohobi/Spring-security-csrf%EB%9E%80
 
 * .maximumSessions(1)
 * 최대 허용 가능 세션 수, 5 하면 넷플릭스 마냥 5개 허용?

 * .maxSessionsPreventsLogin(true); 동시로그인 설정
 * true -> 동일한 계정으로 먼저 접근한 회원이 있다면 두 번째로 접근한 회원은 접근 불가능
 * false -> 반대로 동일한 계정으로 먼저 접근한 회원이 있고,
 * 두 번째 회원이 접근하면 첫 번째 회원의 세션을 만료시키고 두 번째 회원의 세션을 정상 로그인처리

 * .oauth2Login()
 * OAuth 2 로그인 기능에 대한 여러 설정의 진입점이다.

 * .userInfoEndpoint()
 * OAuth 2 로그인 성공 이후 사용자 정보를 가져올 떄의 설정들을 담당한다.

 * .userService(customOAuth2UserService);
 * 소셜 로그인 성공시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록한다.
 * 리소스 서버(소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자하는 기능을 명시할 수 있다.



 * .authorizeRequests()
 * URL별 권한 관리를 설정하는 옵션의 시작점이다. authorizeRequests()가 선언되어야만 antMatchers 옵션을 사용할 수 있다.

 * .antMatchers()
 * 권한 관리 대상을 지정하는 옵션이다.
 * URL, HTTP 메소드별로 관리가 가능하다. 지정된 URL들은 permitAll() 옵션을 통해 전체 열람 관한을 준다.
 * .antMatchers("/","/css/**","/images/**","/js/**","/h2-console?**").permitAll()

 * .antMatchers("/api/v1/**").hasRole(Role.USER.name())
 * "/api/v1/**" 주소를 가진 API는 USER 권한을 가진 사람만 가능하도록 한다.

 * .anyRequest().authenticated()
 * 설정된 값 이외의 나머지 URL들을 나타낸다. authenticated()를 추가하여
 * 나머지 URL들은 모두 인증된 사용자들(로그인한 사용자들)에게만 허용한다.

 * .logout().logoutSuccessUrl("/")
 * 로그아웃 기능에 대한 여러 설정의 진입점이다. 로그아웃 성공시 "/" 주소로 이동한다.
 **/