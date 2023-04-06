package cos.peerna.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class History {
	@Id @GeneratedValue
	@Column(name = "history_id")
	private Long id;

	private LocalTime time;

	@OneToOne(fetch = FetchType.LAZY)
	private Problem problem;

	public static History createHistory(Problem problem) {
		History history = new History();
		history.time = LocalTime.now();
		history.problem = problem;
		return history;
	}
}
