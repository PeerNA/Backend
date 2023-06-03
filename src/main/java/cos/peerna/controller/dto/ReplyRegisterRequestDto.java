package cos.peerna.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReplyRegisterRequestDto {
	private String answer;
	private Long problemId;
	private Long historyId;
	private Integer roomId;

	@Builder
	public ReplyRegisterRequestDto(String answer, Long problemId, Long historyId, Integer roomId) {
		this.answer = answer;
		this.problemId = problemId;
		this.historyId = historyId;
		this.roomId = roomId;
	}
}
