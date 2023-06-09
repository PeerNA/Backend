package cos.peerna.util;

import cos.peerna.controller.dto.ReplyRegisterRequestDto;
import cos.peerna.controller.dto.UserRegisterRequestDto;
import cos.peerna.domain.*;
import cos.peerna.repository.NotificationRepository;
import cos.peerna.repository.ReplyRepository;
import cos.peerna.repository.RoomRepository;
import cos.peerna.service.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;

    @PostConstruct
    public void initDB() {
        initService.initRedis();
        initService.initDB();
        initService.initDB1(); // User, Problem
        initService.initDB2(); // Reply
        initService.initDB3(); // Happhee, Mincheol Shin
        initService.initDb4();
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
        private final NotificationRepository notificationRepository;
        private final UserService userService;
        private final ProblemService problemService;
        private final BCryptPasswordEncoder passwordEncoder;
        private final ReplyService replyService;
        private final StringRedisTemplate redisTemplate;

        List<String> name_list;

        @Transactional
        public void initRedis() {
            redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
        }

        @Transactional
        public void initDB() {

            problemService.make("TCP와 UDP의 차이점에 대해서 설명해보세요.", "TCP는 연결 지향형 프로토콜이고 UDP는 데이터를 데이터그램단위로 전송하는 프로토콜입니다.", Category.NETWORK);
            problemService.make("TCP 3, 4 way handshake에 대해서 설명해보세요.", "TCP 3way handshake는 가상회선을 수립하는 단계입니다. 클라이언트는 서버에 요청을 전송할 수 있는지, 서버는 클라이언트에게 응답을 전송할 수 있는지 확인하는 과정입니다."
                    , Category.NETWORK);
            problemService.make("CORS가 무엇인지 설명해보세요.", "서로 다른 도메인간에 자원을 공유하는 것을 뜻합니다. 대부분의 브라우저에서는 이를 기본적으로 차단하며, 서버측에서 헤더를 통해서 사용가능한 자원을 알려줍니다.", Category.NETWORK);
            problemService.make("멀티스레드 프로그래밍이 무엇인지 설명해보세요.", "멀티스레드 프로그래밍은 하나의 프로세스에서 여러개의 스레드를 만들어 자원의 생성과 관리의 중복을 최소화하는 것을 멀티스레드 프로그래밍이라고 합니다.", Category.OS);
            problemService.make("트랜잭션이란 무엇인지 설명해주세요.", "트랜잭션이란 데이터베이스의 상태를 변화시키는 하나의 논리적인 작업 단위라고 할 수 있으며, 트랜잭션에는 여러개의 연산이 수행될 수 있습니다.", Category.RDB);
            problemService.make("트랜잭션의 특성에 대해서 설명해주세요.", "트랜잭션의 특성은 원자성, 일관성, 격리성, 지속성이 있습니다.", Category.RDB);
            problemService.make("List와 Set의 차이에 대해서 설명해주세요.", "List는 중복된 데이터를 저장하고 순서를 유지하는 선형 자료구조이고, Set은 중복되지 않은 데이터를 저장할 수 있고, 일반적으로 순서를 유지하지 않는 선형 자료구조입니다.(Set은 집합입니다., TreeSet과 같이 순서를 유지하는 Set도 존재합니다.)", Category.JAVA);
            problemService.make("제네릭이 무엇인지 설명해보세요.", "제네릭은 자바의 타입 안정성을 맡고 있습니다. 컴파일 과정에서 타입체크를 해주는 기능으로 객체의 타입을 컴파일 시에 체크하기 때문에 객체의 타입 안정성을 높이고 형변환의 번거로움을 줄여줍니다.", Category.JAVA);
            problemService.make("static이란 무엇일까요?", "static 키워드를 통해 생성된 정적멤버들은 PermGen 또는 Metaspace에 저장되며 저장된 메모리는 모든 객체가 공유하며 하나의 멤버를 어디서든지 참조할 수 있는 장점이 있습니다.", Category.JAVA);
            problemService.make("자바의 접근제어자에 대해서 설명해보세요.", "자바의 접근제어자는 public, protected, default, private가 있습니다. public은 어디서든 접근이 가능하고, protected는 같은 패키지나 상속받은 클래스에서 접근이 가능하고, default는 같은 패키지에서만 접근이 가능하고, private는 같은 클래스에서만 접근이 가능합니다.", Category.JAVA);
            problemService.make("Redis에 대해서 간단하게 설명해주세요.", "Redis는 key-value store NOSQL DB입니다. 싱글스레드로 동작하며 자료구조를 지원합니다. 그리고 다양한 용도로 사용될 수 있도록 다양한 기능을 지원합니다. 데이터의 스냅샷 혹은 AOF 로그를 통해 복구가 가능해서 어느정도 영속성도 보장됩니다.", Category.NOSQL);
            problemService.make("NOSQL이 무엇인지 설명해주세요.", "NOSQL은 Not Only SQL의 약자로, 관계형 데이터베이스가 아닌 다른 형태의 데이터베이스를 말합니다. 관계형 데이터베이스의 한계를 극복하기 위해 만들어졌습니다.", Category.NOSQL);
            problemService.make("HTTP와 HTTPS의 차이를 설명해주세요.", "HTTP는 따로 암호화 과정을 거치지 않기 때문에 중간에 패킷을 가로챌 수 있고, 수정할 수 있습니다. 따라서 보안이 취약해짐을 알 수 있습니다. 이를 보완하기 위해 나온 것이 HTTPS입니다. 중간에 암호화 계층을 거쳐서 패킷을 암호화합니다.", Category.NETWORK);
            problemService.make("Thread-safe 하다는 의미와 설계하는 법을 설명해보세요.", "두 개 이상의 스레드가 race condition에 들어가거나 같은 객체에 동시에 접근해도 연산결과의 정합성이 보장될 수 있게끔 메모리 가시성이 확보된 상태를 의미합니다.", Category.OS);
        }

        @Transactional
        public void initDB1() {
            name_list = new ArrayList<>();
            name_list.add("zoohi");
            name_list.add("ebang91");
            name_list.add("mingi1");
            name_list.add("yen001");
            name_list.add("ZIO-KIMM");
            name_list.add("anso333");
            name_list.add("jeongjiyeon");
            name_list.add("smart0505");
            name_list.add("koding00");
            name_list.add("juhyo");
            name_list.add("jungmin3899");
            name_list.add("darkerkang");
            name_list.add("quartz");
            name_list.add("sohee");
            for (int i = 1; i <= 14; i++) {
                userService.join(User.createUser(UserRegisterRequestDto.builder()
                        .id((long) i)
                        .name(name_list.get(i - 1))
                        .email("user" + i + "_email")
                        .password(passwordEncoder.encode("password"))
                        .build()));
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

                List<String> answer_list = new ArrayList<>();
                answer_list.add("멀티 스레드 프로그래밍에서 일반적으로 어떤 함수나 변수, 혹은 객체가 여러 스레드로부터 동시에 접근이 이루어져도 프로그램의 실행에 문제가 없음을 뜻한다.");
                answer_list.add("하나의 함수가 한 스레드로부터 호출되어 실행 중일 때, 다른 스레드가 그 함수를 호출하여 동시에 함께 실행되더라도 각 스레드에서의 함수의 수행 결과가 올바로 나오는 것으로 정의한다.");
                answer_list.add("여러 스레드에서 클래스 혹은 클래스의 객체에 동시에 접근해 수정하더라도 프로그램의 실행에 문제가 없음을 뜻한다.");
                answer_list.add("근본적으로 멀티 스레드 환경에서 안전하게 만드는 것을 말한다. 동시 접근하거나 등등");
                answer_list.add("보통 재진입 가능하게 만들어진 함수일 경우는 스레드 세이프이고, 전역 변수를 사용하지 않게끔 구성하게 된다.");
                answer_list.add("atomic한 성질을 지켜서 여러 스레드가 동시에 접근해도 문제가 없는 것을 말한다.");
                answer_list.add("스레드가 여러개 존재하는 것은 경제성 때문에 존재하는데, 여러 개 존재하면서 동시에 자원에 접근할 때 문제가 없어야 함을 의미한다.");
                answer_list.add("경합 조건이 발생했을 때 문제가 없어야 함을 의미한다.");
                answer_list.add("여러 쓰레드에 의해 코드가 실행되더라도 실행 결과의 correctness가 보장되는 것을 뜻합니다.");
                answer_list.add("thread-safe의 정확한 정의는 모르지만 mutex나 semaphore를 사용하여 여러 쓰레드가 동시에 접근하였을 때 문제가 없게 만드는 것을 의미한다.");
                answer_list.add("thread-safe를 가장 근본적으로 해결할 수 있는 방법은 싱글톤을 사용하는 것이다.");
                answer_list.add("컴퓨터에서 재진입성을 가진다고 하면 병렬로 안전하게 실행 가능함을 의미한다.");
                answer_list.add("Java에서는 synchronized 키워드를 사용하여 스레드 세이프를 보장한다.");
                answer_list.add("ConcurrentHashMap을 이용하게 된다면 Thread-safe를 보장하게 된다. 이는 병렬 환경에서의 Map을 사용할 때 중요하다.");

                for (int j = 1; j <= 14; j++) {
                    Problem problem = em.find(Problem.class, (long) j);
                    HashMap<Long, ConnectedUser> userMap = new HashMap<>();

                    Room room = roomRepository.save(Room.builder()
                            .connectedUserIds(new ArrayList<>(List.of(user1.getId(), user2.getId())))
                            .category(problem.getCategory())
                            .build());
                    userMap.put(user1.getId(), new ConnectedUser(user1.getId(), room.getId()));
                    userMap.put(user2.getId(), new ConnectedUser(user2.getId(), room.getId()));

                    History history = historyService.createHistory(problem.getId(), room.getId());

                    replyService.make(ReplyRegisterRequestDto.builder()
                            .answer(answer_list.get(i - 1))
                            .problemId(problem.getId())
                            .roomId(room.getId())
                            .historyId(history.getId())
                            .build(), user1.getId());
                }
            }
        }

        @Transactional
        public void initDB3() {
            User happhee = em.find(User.class, (long) 79238676);
            User mincshin = em.find(User.class, (long) 48898994);
            ArrayList<String> list = new ArrayList<>();
            list.add("TCP는 연결 지향형 프로토콜이고 UDP는 데이터를 데이터그램단위로 전송하는 프로토콜입니다.");
            list.add("TCP 3way handshake는 가상회선을 수립하는 단계입니다. 클라이언트는 서버에 요청을 전송할 수 있는지, 서버는 클라이언트에게 응답을 전송할 수 있는지 확인하는 과정입니다.");
            list.add("서로 다른 도메인간에 자원을 공유하는 것을 뜻합니다. 대부분의 브라우저에서는 이를 기본적으로 차단하며, 서버측에서 헤더를 통해서 사용가능한 자원을 알려줍니다.");
            list.add("멀티스레드 프로그래밍은 하나의 프로세스에서 여러개의 스레드를 만들어 자원의 생성과 관리의 중복을 최소화하는 것을 멀티스레드 프로그래밍이라고 합니다.");
            list.add("트랜잭션이란 데이터베이스의 상태를 변화시키는 하나의 논리적인 작업 단위라고 할 수 있으며, 트랜잭션에는 여러개의 연산이 수행될 수 있습니다.");
            list.add("트랜잭션의 특성은 원자성, 일관성, 격리성, 지속성이 있습니다.");
            list.add("List는 중복된 데이터를 저장하고 순서를 유지하는 선형 자료구조이고, Set은 중복되지 않은 데이터를 저장할 수 있고, 일반적으로 순서를 유지하지 않는 선형 자료구조입니다.(Set은 집합입니다., TreeSet과 같이 순서를 유지하는 Set도 존재합니다.)");
            list.add("제네릭은 자바의 타입 안정성을 맡고 있습니다. 컴파일 과정에서 타입체크를 해주는 기능으로 객체의 타입을 컴파일 시에 체크하기 때문에 객체의 타입 안정성을 높이고 형변환의 번거로움을 줄여줍니다.");
            list.add("static 키워드를 통해 생성된 정적멤버들은 PermGen 또는 Metaspace에 저장되며 저장된 메모리는 모든 객체가 공유하며 하나의 멤버를 어디서든지 참조할 수 있는 장점이 있습니다.");
            list.add("자바의 접근제어자는 public, protected, default, private가 있습니다. public은 어디서든 접근이 가능하고, protected는 같은 패키지나 상속받은 클래스에서 접근이 가능하고, default는 같은 패키지에서만 접근이 가능하고, private는 같은 클래스에서만 접근이 가능합니다.");
            list.add("Redis는 key-value store NOSQL DB입니다. 싱글스레드로 동작하며 자료구조를 지원합니다. 그리고 다양한 용도로 사용될 수 있도록 다양한 기능을 지원합니다. 데이터의 스냅샷 혹은 AOF 로그를 통해 복구가 가능해서 어느정도 영속성도 보장됩니다.");
            list.add("NOSQL은 Not Only SQL의 약자로, 관계형 데이터베이스가 아닌 다른 형태의 데이터베이스를 말합니다. 관계형 데이터베이스의 한계를 극복하기 위해 만들어졌습니다.");
            list.add("HTTP는 따로 암호화 과정을 거치지 않기 때문에 중간에 패킷을 가로챌 수 있고, 수정할 수 있습니다. 따라서 보안이 취약해짐을 알 수 있습니다. 이를 보완하기 위해 나온 것이 HTTPS입니다. 중간에 암호화 계층을 거쳐서 패킷을 암호화합니다.");
            list.add("두 개 이상의 스레드가 race condition에 들어가거나 같은 객체에 동시에 접근해도 연산결과의 정합성이 보장될 수 있게끔 메모리 가시성이 확보된 상태를 의미합니다.");

            ArrayList<String> list2 = new ArrayList<>();
            list2.add("TCP는 신뢰성 있는 데이터 전송을 지원하는 연결 지향형 프로토콜이고, UDP는 비연결 프로토콜로 신호 절차를 거치지 않습니다.");
            list2.add("3 Way-Handshake 란, 전송제어 프로토콜(TCP)에서 통신을 하는 장치간 서로 연결이 잘 되어있는지 확인하는 과정/방식입니다");
            list2.add("CORS는 Cross Origin Resource Sharing의 약자로, 도메인이 다른 서버의 자원을 공유하는 것을 말합니다.");
            list2.add("멀티쓰레드 프로그래밍은 말 그대로 쓰레드를 여러개 사용해서 실행하는 프로그래밍 기법입니다.");
            list2.add("데이터베이스의 쿼리(데이터베이스에 정보를 요청하는 것)를 가장 작은 단위의 업무로 쪼갠 것을 트랜잭션(Transaction)이라고 합니다.");
            list2.add("트랜잭션의 특성은 ACID로 원자성, 일관성, 격리성, 지속성이 있습니다.");
            list2.add("list는 중복값을 가질 수 있고, Set은 중복 값을 가질 수 없는 집합 자료구조입니다.");
            list2.add("제네릭은 컴파일 과정에서 타입 체크를 해주고 형변환의 번거로움을 줄여줍니다.");
            list2.add("static은 정적이라는 뜻으로 객체를 생성하지 않아도 사용할 수 있고 공유 자원으로써 사용됩니다.");
            list2.add("자바의 접근 제어자는 public, protected, default, private이 있습니다. 뒤로 갈수록 접근 범위가 좁아집니다.");
            list2.add("Redis는 key-value 형태의 NOSQL DB입니다. 싱글스레드로 동작하며 자료구조를 지원합니다.");
            list2.add("NOSQL은 Not Only SQL의 약자로, 관계형 데이터베이스가 아닌 다른 형태의 데이터베이스를 말합니다.");
            list2.add("HTTP는 암호화 과정이 없고 이를 보완하여 나온 것이 HTTPS 입니다.");
            list2.add("여러 스레드가 같은 자원에 동시에 접근할 때 race condition이 발생하게 되는데, 동시에 접근해도 정합성이 보장되도록 하는 상태를 말합니다.");

            for (int i = 1; i <= 14; i++) {
                Problem problem = em.find(Problem.class, (long) i);
                HashMap<Long, ConnectedUser> userMap = new HashMap<>();

                Room room = roomRepository.save(Room.builder()
                        .connectedUserIds(new ArrayList<>(List.of(mincshin.getId(), happhee.getId())))
                        .category(problem.getCategory())
                        .build());
                userMap.put(mincshin.getId(), new ConnectedUser(mincshin.getId(), room.getId()));
                userMap.put(happhee.getId(), new ConnectedUser(happhee.getId(), room.getId()));

                History history = historyService.createHistory(problem.getId(), room.getId());

                replyService.make(ReplyRegisterRequestDto.builder()
                        .answer(list.get(i - 1))
                        .problemId(problem.getId())
                        .historyId(history.getId())
                        .roomId(room.getId())
                        .build(), happhee.getId());
                replyService.make(ReplyRegisterRequestDto.builder()
                        .answer(list2.get(i - 1))
                        .problemId(problem.getId())
                        .historyId(history.getId())
                        .roomId(room.getId())
                        .build(), mincshin.getId());

            }
        }

        @Transactional
        public void initDb4() {
            User happhee = em.find(User.class, (long) 79238676);
            User mincshin = em.find(User.class, (long) 48898994);
            for (int i = 365; i < 393; i++) {
                Reply reply = em.find(Reply.class, (long) i);
                if (i % 2 == 0) {
                    Notification notification = Notification.builder()
                            .user(mincshin)
                            .reply(reply)
                            .type(NotificationType.PULL_REQ)
                            .msg("PR이 요청되었습니다." + i)
                            .build();
                    notificationRepository.save(notification);
                } else {
                    Notification notification = Notification.builder()
                            .user(happhee)
                            .reply(reply)
                            .type(NotificationType.PULL_REQ)
                            .msg("PR이 요청되었습니다." + i)
                            .build();
                    notificationRepository.save(notification);
                }
            }
            userService.follow(mincshin.getId(), happhee.getId());
            userService.follow(happhee.getId(), mincshin.getId());
            userService.follow(1L, happhee.getId());
            userService.follow(happhee.getId(), 2L);
            userService.follow(2L, happhee.getId());
            userService.follow(3L, happhee.getId());
            userService.follow(2L, mincshin.getId());
            userService.follow(3L, mincshin.getId());

        }
    }
}
