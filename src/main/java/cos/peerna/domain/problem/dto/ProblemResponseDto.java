package cos.peerna.domain.problem.dto;

import cos.peerna.domain.user.model.Category;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProblemResponseDto {
    private Long     problemId;
    private String   question;
    private String   answer;
    private Category category;
    private List<String> keywordList;
}
