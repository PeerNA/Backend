package cos.peerna.controller.dto;

import cos.peerna.domain.Category;
import lombok.Data;

@Data
public class ProblemRegisterRequestDto {
    private String question;
    private String answer;
    private Category category;
}



