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
@IdClass(KeywordPK.class)
public class Keyword {

    @NotNull
    @Id @Column(name = "name")
    private String name;

    @NotNull
    private Long count;

    @NotNull @ManyToOne(fetch = FetchType.LAZY)
    @Id @JoinColumn(name = "problem_id")
    private Problem problem;

//    public static Keyword createKeyword(KeywordRegisterRequestDto dto) {
//        Keyword problem = new Keyword();
//        problem.question = dto.getQuestion();
//        problem.answer = dto.getAnswer();
//        problem.category = dto.getCategory();
//
//        return problem;
//    }
}
