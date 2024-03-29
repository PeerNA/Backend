package cos.peerna.domain.problem.model;

import cos.peerna.domain.user.model.Category;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Problem {

    @Id @GeneratedValue
    @Column(name = "problem_id")
    private Long id;

    @Column(length = 1000)
    private String question;

    @Column(length = 1000)
    private String answer;
    @Enumerated(EnumType.STRING)
    private Category category;

    public static Problem createProblem(String question, String answer, Category category) {
        Problem problem = new Problem();
        problem.question = question;
        problem.answer = answer;
        problem.category = category;

        return problem;
    }
}
