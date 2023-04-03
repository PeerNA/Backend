package cos.peerna.domain;

import cos.peerna.controller.dto.ProblemRegisterRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Problem {

    @Id @GeneratedValue
    @Column(name = "problem_id")
    private Long id;

    private String question;
    private String answer;
    @Enumerated(EnumType.STRING)
    private Category category;

    public static Problem createProblem(ProblemRegisterRequestDto dto) {
        Problem problem = new Problem();
        problem.question = dto.getQuestion();
        problem.answer = dto.getAnswer();
        problem.category = dto.getCategory();

        return problem;
    }
}
