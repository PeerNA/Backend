package cos.peerna.domain;

import cos.peerna.controller.dto.ProblemRegisterRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @NotNull @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @NotNull @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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
