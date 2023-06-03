package cos.peerna.domain;

import cos.peerna.controller.dto.ProblemRegisterRequestDto;
import cos.peerna.controller.dto.ReplyRegisterRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply {

    @Id @GeneratedValue
    @Column(name = "reply_id")
    private Long id;

    @NotNull
    private String answer;

    /**
     * History 의 생성 시점
     * 동료 매칭이 시작되면서 History 하나 생성
     * 그리고 History 번호를 클라이언트에 보냈다가
     * 클라이언트가 답안 제출 시에 History 번호를 같이 보내야 함
     * 이렇게 하지 않을 시, History 에 aUserId, bUserId, problem 등 어떤 필드가 있어도
     * 유니크한 History 를 찾아낼 수 없음
     * 하나의 문제를 A, B 유저가 2번 이상 풀 수 있기 때문
     */
    @NotNull @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id")
    private History history;

    @NotNull @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @NotNull @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "reply")
    private List<Likey> likes = new ArrayList<>();

    private Long likeCount;

    private boolean isRequested;

    public static Reply createReply(User user, History history, Problem problem, String answer) {
        Reply reply = new Reply();
        reply.answer = answer;
        reply.history = history;
        reply.problem = problem;
        reply.likeCount = 0L;
        reply.user = user;
        reply.isRequested = false;
        return reply;
    }

    public static void likeReply(Reply reply) {
        ++reply.likeCount;
    }

    public static void dislikeReply(Reply reply) {
        --reply.likeCount;
    }

    public static void requestReply(Reply reply) {
        reply.isRequested = true;
    }

//    id를 dto로 받는게 맞는가?

//    public static Reply createProblem(ProblemRegisterRequestDto dto) {
//        Reply reply = new Reply();
//        reply.question = dto.getQuestion();
//        reply.answer = dto.getAnswer();
//        reply.category = dto.getCategory();
//
//        return problem;
//    }
}
