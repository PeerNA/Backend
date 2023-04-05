package cos.peerna.domain;

import cos.peerna.controller.dto.ProblemRegisterRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword {

    @Id @GeneratedValue
    @Column(name = "keyword_id")
    private Long id;

    @NotNull
    private String name;

    @NotNull @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    private Problem problem;

    public static Keyword createKeyword(String name, Problem problem) {
        Keyword keyword = new Keyword();
        keyword.name = name;
        keyword.problem = problem;

        return keyword;
    }

//    public static Keyword createKeyword(KeywordRegisterRequestDto dto) {
//        Keyword problem = new Keyword();
//        problem.question = dto.getQuestion();
//        problem.answer = dto.getAnswer();
//        problem.category = dto.getCategory();
//
//        return problem;
//    }
}
