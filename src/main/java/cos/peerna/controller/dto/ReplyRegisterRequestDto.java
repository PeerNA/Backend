package cos.peerna.controller.dto;

import lombok.*;

@Data
public class ReplyRegisterRequestDto {
	private String answer;
	private Long problemId;
	private Long historyId;
	private Long roomId;

	@Builder
	public ReplyRegisterRequestDto(String answer, Long problemId, Long historyId, Long roomId) {
		this.answer = answer;
		this.problemId = problemId;
		this.historyId = historyId;
		this.roomId = roomId;
	}
}
