package cos.peerna.controller.dto;

import lombok.Data;

@Data
public class ReplyRegisterRequestDto {
	private String answer;
	private Long problemId;
	private Long historyId;
}
