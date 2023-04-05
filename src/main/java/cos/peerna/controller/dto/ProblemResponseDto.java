package cos.peerna.controller.dto;

import cos.peerna.domain.Category;
import cos.peerna.domain.Keyword;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProblemResponseDto {
    private Long     problemId;
    private String   answer;
    private Category category;
    private List<Keyword> keywordList;
}



