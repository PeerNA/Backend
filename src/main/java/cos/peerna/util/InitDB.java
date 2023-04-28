package cos.peerna.util;

import cos.peerna.controller.dto.UserRegisterRequestDto;
import cos.peerna.domain.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;

    @PostConstruct
    public void initDB() {
        initService.initDB1(); // User, Problem
        initService.initDB2(); // Reply
        initService.initDB3(); // Happhee, Mincheol Shin
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        private final BCryptPasswordEncoder passwordEncoder;

        @Transactional
        public void initDB1() {
            for (int i = 1; i <= 14; i++) {
                User user = User.createUser(UserRegisterRequestDto.builder()
                        .id((long) i)
                        .name("user" + i + "_name")
                        .email("user" + i + "_email")
                        .password(passwordEncoder.encode("password"))
                        .build());
                Problem problem = Problem.createProblem("question" + i, "answer" + i, Category.OS);
                em.persist(user);
                em.persist(problem);
            }
            User happhee = User.builder()
                    .email("ghdtj7@naver.com")
                    .name("Happhee")
                    .imageUrl("https://avatars.githubusercontent.com/u/79238676?v=4")
                    .role(Role.MENTEE)
                    .bio("Happhee")
                    .id((long) 79238676)
                    .build();
            em.persist(happhee);
            User mincshin = User.builder()
                    .email("smc9919@sju.ac.kr")
                    .name("Mincheol Shin")
                    .imageUrl("https://avatars.githubusercontent.com/u/48898994?v=4")
                    .role(Role.MENTEE)
                    .bio("Undergraduate of Sejong Univ since 2018.")
                    .id((long) 48898994)
                    .build();
            em.persist(mincshin);
            User seunghso = User.builder()
                    .email("sseunghun99@naver.com")
                    .name("Seunghun Song")
                    .imageUrl("https://avatars.githubusercontent.com/u/45088611?v=4")
                    .role(Role.MENTEE)
                    .bio("Sejong Univ.")
                    .id((long) 45088611)
                    .build();
            em.persist(seunghso);
        }

        @Transactional
        public void initDB2() {
//            for (int i = 1; i <= 14; i++) {
//                User user = em.find(User.class, (long) i);
//                for (int j = 1; j <= 14; j++) {
//                    Problem problem = em.find(Problem.class, (long) j);
//                    History history = History.createHistory(problem);
//                    em.persist(history);
//
//                    Reply reply = Reply.createReply(user, history, problem, "user:" + i + ", problem:" + i);
//                    em.persist(reply);
//                }
//            }
        }

        @Transactional
        public void initDB3() {
//            User happhee = em.find(User.class, (long) 79238676);
//            User mincshin = em.find(User.class, (long) 48898994);
//
//            for (int i = 1; i <= 14; i++) {
//                Problem problem = em.find(Problem.class, (long) i);
//                History history = History.createHistory(problem);
//                em.persist(history);
//
//                Reply reply1 = Reply.createReply(happhee, history, problem, "user:Happhee" + ", problem:" + i);
//                Reply reply2 = Reply.createReply(mincshin, history, problem, "user:mincshin" + ", problem:" + i);
//                em.persist(reply1);
//                em.persist(reply2);
//            }
        }
    }
}
