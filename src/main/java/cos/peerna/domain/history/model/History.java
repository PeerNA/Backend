package cos.peerna.domain.history.model;

import cos.peerna.domain.problem.model.Problem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class History {
	@Id @GeneratedValue
	@Column(name = "history_id")
	private Long id;

	private LocalDate time;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "problem_id")
	private Problem problem;

	public static History createHistory(Problem problem) {
		History history = new History();
		history.time = LocalDate.now();
		history.problem = problem;
		return history;
	}
}
