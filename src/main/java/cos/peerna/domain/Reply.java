package cos.peerna.domain;

import cos.peerna.controller.dto.ProblemRegisterRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply {

    @Id @GeneratedValue
    @Column(name = "reply_id")
    private Long replyId;

    @NotNull
    private Long problemId;

    @NotNull
    private Long userId;

    @NotNull
    private String answer;

    @OneToOne
    @JoinColumn(name = "reply_id")
    private Like like;

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
