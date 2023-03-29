package cos.peerna.domain;

import cos.peerna.controller.dto.ProblemRegisterRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword {

    @Id @GeneratedValue
    @Column(name = "problem_id")
    private Long problemId;

    private String name;
    private Long cnt;

//    public static Keyword createKeyword(KeywordRegisterRequestDto dto) {
//        Keyword problem = new Keyword();
//        problem.question = dto.getQuestion();
//        problem.answer = dto.getAnswer();
//        problem.category = dto.getCategory();
//
//        return problem;
//    }
}
