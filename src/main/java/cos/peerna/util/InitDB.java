package cos.peerna.util;

import cos.peerna.controller.dto.ReplyRegisterRequestDto;
import cos.peerna.controller.dto.UserRegisterRequestDto;
import cos.peerna.domain.*;
import cos.peerna.repository.ReplyRepository;
import cos.peerna.repository.RoomRepository;
import cos.peerna.service.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        private final HistoryService historyService;
        private final ReplyRepository replyRepository;
        private final RoomRepository roomRepository;
        private final UserService userService;
        private final ProblemService problemService;
        private final BCryptPasswordEncoder passwordEncoder;
        private final ReplyService replyService;

        @Transactional
        public void initDB1() {
            for (int i = 1; i <= 14; i++) {
                userService.join(User.createUser(UserRegisterRequestDto.builder()
                        .id((long) i)
                        .name("user" + i + "_name")
                        .email("user" + i + "_email")
                        .password(passwordEncoder.encode("password"))
                        .build()));
                problemService.make("question" + i, "answer" + i, Category.OS);
            }
            userService.join(User.builder()
                    .email("ghdtj7@naver.com")
                    .name("Happhee")
                    .imageUrl("https://avatars.githubusercontent.com/u/79238676?v=4")
                    .role(Role.MENTEE)
                    .bio("Happhee")
                    .id((long) 79238676)
                    .build());
            userService.join(User.builder()
                    .email("smc9919@sju.ac.kr")
                    .name("Mincheol Shin")
                    .imageUrl("https://avatars.githubusercontent.com/u/48898994?v=4")
                    .role(Role.MENTEE)
                    .bio("Undergraduate of Sejong Univ since 2018.")
                    .id((long) 48898994)
                    .build());
            userService.join(User.builder()
                    .email("sseunghun99@naver.com")
                    .name("Seunghun Song")
                    .imageUrl("https://avatars.githubusercontent.com/u/45088611?v=4")
                    .role(Role.MENTEE)
                    .bio("Sejong Univ.")
                    .id((long) 45088611)
                    .build());
        }

        @Transactional
        public void initDB2() {
            for (int i = 1; i <= 13; i++) {
                User user1 = em.find(User.class, (long) i);
                User user2 = em.find(User.class, (long) i + 1);
                for (int j = 1; j <= 14; j++) {
                    Problem problem = em.find(Problem.class, (long) j);
                    HashMap<Long, ConnectedUser> userMap = new HashMap<>();
                    userMap.put(user1.getId(), new ConnectedUser(user1.getId()));
                    userMap.put(user2.getId(), new ConnectedUser(user2.getId()));

                    Room room = roomRepository.save(Room.builder()
                            .connectedUserIds(new ArrayList<>(List.of(user1.getId(), user2.getId())))
                            .category(problem.getCategory())
                            .build());

                    History history = historyService.createHistory(problem.getId(), room.getId());

                    replyService.make(ReplyRegisterRequestDto.builder()
                            .answer("user:" + i + ", problem:" + i)
                            .problemId(problem.getId())
                            .historyId(history.getId())
                            .roomId(room.getId())
                            .build(), user1.getId());
                    replyService.make(ReplyRegisterRequestDto.builder()
                            .answer("user:" + i + 1 + ", problem:" + i + 1)
                            .problemId(problem.getId())
                            .historyId(history.getId())
                            .roomId(room.getId())
                            .build(), user2.getId());
                }
            }
        }

        @Transactional
        public void initDB3() {
            User happhee = em.find(User.class, (long) 79238676);
            User mincshin = em.find(User.class, (long) 48898994);

            for (int i = 1; i <= 14; i++) {
                Problem problem = em.find(Problem.class, (long) i);
                HashMap<Long, ConnectedUser> userMap = new HashMap<>();
                userMap.put(mincshin.getId(), new ConnectedUser(mincshin.getId()));
                userMap.put(happhee.getId(), new ConnectedUser(happhee.getId()));

                Room room = roomRepository.save(Room.builder()
                        .connectedUserIds(new ArrayList<>(List.of(mincshin.getId(), happhee.getId())))
                        .category(problem.getCategory())
                        .build());

                History history = historyService.createHistory(problem.getId(), room.getId());

                replyService.make(ReplyRegisterRequestDto.builder()
                        .answer("안녕하세요 테스트를 위한 텍스트입니다. 텍스트, DB, SQL")
                        .problemId(problem.getId())
                        .historyId(history.getId())
                        .roomId(room.getId())
                        .build(), happhee.getId());
                replyService.make(ReplyRegisterRequestDto.builder()
                        .answer("안경, 돌, 망치")
                        .problemId(problem.getId())
                        .historyId(history.getId())
                        .roomId(room.getId())
                        .build(), mincshin.getId());

            }
        }
    }
}
