package cos.peerna.domain.problem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProblemAnswerResponseDto {
	private String answer;

	public static ProblemAnswerResponseDto of(String answer) {
		return new ProblemAnswerResponseDto(answer);
	}
}
