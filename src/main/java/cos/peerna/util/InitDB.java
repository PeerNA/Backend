package cos.peerna.util;

import cos.peerna.controller.dto.UserRegisterRequestDto;
import cos.peerna.domain.*;
import cos.peerna.repository.HistoryRepository;
import cos.peerna.repository.ReplyRepository;
import cos.peerna.repository.RoomRepository;
import cos.peerna.service.KeywordService;
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
        private final KeywordService keywordService;
        private final ReplyRepository replyRepository;
        private final RoomRepository roomRepository;
        private final HistoryRepository historyRepository;
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
            for (int i = 1; i <= 13; i++) {
                User user1 = em.find(User.class, (long) i);
                User user2 = em.find(User.class, (long) i + 1);
                for (int j = 1; j <= 14; j++) {
                    Problem problem = em.find(Problem.class, (long) j);
                    Room room = roomRepository.save(Room.builder()
                            .user1(user1)
                            .user2(user2)
                            .category(problem.getCategory())
                            .build());
                    History history = historyRepository.save(History.createHistory(problem, room));

                    replyRepository.save(Reply.createReply(user1, history, problem, "user:" + i + ", problem:" + i));
                    replyRepository.save(Reply.createReply(user2, history, problem, "user:" + i + 1 + ", problem:" + i + 1));
                }
            }
        }

        @Transactional
        public void initDB3() {
            User happhee = em.find(User.class, (long) 79238676);
            User mincshin = em.find(User.class, (long) 48898994);

            for (int i = 1; i <= 14; i++) {
                Problem problem = em.find(Problem.class, (long) i);
                Room room = roomRepository.save(Room.builder()
                        .user1(happhee)
                        .user2(mincshin)
                        .category(problem.getCategory())
                        .build());

                History history = historyRepository.save(History.createHistory(problem, room));

                Reply reply1 = replyRepository.save(Reply.createReply(happhee, history, problem, "안녕하세요 테스트를 위한 텍스트입니다. 텍스트, DB, SQL"));
                Reply reply2 = replyRepository.save(Reply.createReply(mincshin, history, problem, "안경, 돌, 망치"));

                em.flush();

                keywordService.analyze(reply1.getAnswer(), (long) i);
                keywordService.analyze(reply2.getAnswer(), (long) i);
            }
        }
    }
}
