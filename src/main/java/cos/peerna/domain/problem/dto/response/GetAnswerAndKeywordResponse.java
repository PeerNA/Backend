package cos.peerna.domain.problem.dto.response;

import cos.peerna.domain.keyword.model.Keyword;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Validated
public record GetAnswerAndKeywordResponse(
		String answer,
		List<Keyword> keywords
) {
	@Override
	public String toString() {
		return "GetAnswerAndKeywordResponse{" +
				"answer='" + answer + '\'' +
				", keywords=" + keywords +
				'}';
	}

	public static GetAnswerAndKeywordResponse of(String answer, List<Keyword> keywords) {
		return new GetAnswerAndKeywordResponse(answer, keywords);
	}
}
