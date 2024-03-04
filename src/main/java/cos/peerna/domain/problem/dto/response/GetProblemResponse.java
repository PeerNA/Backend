package cos.peerna.domain.problem.dto.response;

import cos.peerna.domain.keyword.model.Keyword;
import cos.peerna.domain.user.model.Category;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Validated
public record GetProblemResponse(
		Long problemId,
		String question,
		String answer,
		Category category
) {
	@Override
	public String toString() {
		return "GetProblemResponse{" +
				"problemId=" + problemId +
				", question='" + question + '\'' +
				", answer='" + answer + '\'' +
				", category='" + category + '\'' +
				'}';
	}

	public static GetProblemResponse of(Long problemId, String question, String answer, Category category) {
		return new GetProblemResponse(problemId, question, answer, category);
	}

}
