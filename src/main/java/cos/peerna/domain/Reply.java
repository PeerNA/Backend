package cos.peerna.domain;

import cos.peerna.controller.dto.ProblemRegisterRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply {

    @Id @GeneratedValue
    @Column(name = "id")
    private Long id;

    private String question;
    private String answer;
    @Enumerated(EnumType.STRING)
    private Category category;

    public static Reply createProblem(ProblemRegisterRequestDto dto) {
        Reply problem = new Reply();
        problem.question = dto.getQuestion();
        problem.answer = dto.getAnswer();
        problem.category = dto.getCategory();

        return problem;
    }
}
