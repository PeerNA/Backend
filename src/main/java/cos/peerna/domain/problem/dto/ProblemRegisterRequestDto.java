package cos.peerna.domain.problem.dto;

import cos.peerna.domain.user.model.Category;
import lombok.Data;

@Data
public class ProblemRegisterRequestDto {
    private String question;
    private String answer;
    private Category category;
}



