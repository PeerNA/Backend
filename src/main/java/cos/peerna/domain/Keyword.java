package cos.peerna.domain;

import cos.peerna.controller.dto.ProblemRegisterRequestDto;
import jakarta.annotation.Resource;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.checkerframework.checker.units.qual.N;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword {

	@GeneratedValue
	@Id @Column(name = "keyword_id", nullable = false)
	private Long id;

	@Column(name = "name", nullable = false)
    private String name;

    @NotNull @Column(name = "count", nullable = false)
    private Long count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem", nullable = false)
    private Problem problem;

	@Builder
	public Keyword(String name, Long count, Problem problem) {
		this.name = name;
		this.count = count;
		this.problem = problem;
	}

	public static Keyword createKeyword(String kwd, Problem problem) {
		Keyword keyword = new Keyword();
		keyword.name = kwd;
		keyword.count = 1L;
		keyword.problem = problem;

		return keyword;
	}

	public static Keyword updateKeyword(Keyword keyword) {
		keyword.count += 1L;

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
