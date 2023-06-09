package cos.peerna.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

	@Convert(converter = BooleanToYNConverter.class)
	private boolean isSolved;

	public static History createHistory(Problem problem) {
		History history = new History();
		history.time = LocalDate.now();
		history.problem = problem;
		history.isSolved = false;
		return history;
	}

	public History solve() {
		this.isSolved = true;
		return this;
	}
}
