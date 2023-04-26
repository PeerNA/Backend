package cos.peerna.controller.dto;

import cos.peerna.domain.Category;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DetailHistoryResponseDto {
	private Long historyId;
	private Long problemId;
	private String problem;
	private Category category;
	private LocalDate time;
}
