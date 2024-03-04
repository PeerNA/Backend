package cos.peerna.domain.reply.model;

import cos.peerna.domain.user.model.User;
import cos.peerna.domain.history.model.History;
import cos.peerna.domain.problem.model.Problem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply {

    @Id
    @GeneratedValue
    @Column(name = "reply_id")
    private Long id;

    @NotNull
    private String answer;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id")
    private History history;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "reply", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likey> likes = new ArrayList<>();

    private Long likeCount;

    @Builder
    private Reply(String answer, History history, Problem problem, User user) {
        this.answer = answer;
        this.history = history;
        this.problem = problem;
        this.user = user;
        this.likeCount = 0L;
    }

    @Builder(builderMethodName = "builderForRegister")
    public static Reply of(String answer, History history, Problem problem, User user) {
        return builder()
                .answer(answer)
                .history(history)
                .problem(problem)
                .user(user)
                .build();
    }

    public void modifyAnswer(String answer) {
        this.answer = answer;
    }

    public void likeReply() {
        ++this.likeCount;
    }

    public static void dislikeReply(Reply reply) {
        --reply.likeCount;
    }

}
