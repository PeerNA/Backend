package cos.peerna.controller.dto;

import cos.peerna.domain.Career;
import cos.peerna.domain.Category;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class UserPatchRequestDto {
	@Enumerated(EnumType.STRING)
	private Category priority1;
	@Enumerated(EnumType.STRING)
	private Category priority2;
	@Enumerated(EnumType.STRING)
	private Category priority3;

	@Enumerated(EnumType.STRING)
	private Career career;
}
