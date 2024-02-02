package cos.peerna.domain.reply.model;

import cos.peerna.domain.user.model.User;
import cos.peerna.domain.history.model.History;
import cos.peerna.domain.problem.model.Problem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
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

    public void likeReply() {
        ++this.likeCount;
    }

    public static void dislikeReply(Reply reply) {
        --reply.likeCount;
    }

}
