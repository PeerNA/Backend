package cos.peerna.domain.user.dto;

import cos.peerna.domain.user.model.Career;
import cos.peerna.domain.user.model.Category;
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
