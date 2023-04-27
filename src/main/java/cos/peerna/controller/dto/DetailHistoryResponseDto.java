package cos.peerna.controller.dto;

import cos.peerna.domain.Category;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DetailHistoryResponseDto {
	private Long bUserId;
	private String question;
	private Category category;
	private LocalDate time;
	private String aUserAnswer;
	private String bUserAnswer;
	private String aUserNickname;
	private String bUserNickname;
	private String aUserImage;
	private String bUserImage;
}
