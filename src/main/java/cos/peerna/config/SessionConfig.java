package cos.peerna.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600) // 1시간 (해당 어노테이션 사용시 timeout application.yml 로 설정 불가능)
//@Configuration
public class SessionConfig {

//    @Bean
//    public CookieSerializer cookieSerializer() {
//        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
//        serializer.setCookieName("CORINSESSIONID");// JSESSIONID 가 기본
//        serializer.setCookiePath("/");
//        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$"); // 호출한 도메인으로 설정됨
//        serializer.setCookieMaxAge(-1); // -1 = 브라우저 닫을 때까지 유지
//        return serializer;
//    }

}
