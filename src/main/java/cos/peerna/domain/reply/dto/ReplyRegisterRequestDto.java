package cos.peerna.domain.reply.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReplyRegisterRequestDto {
	public String answer;
	public Long problemId;
	public Long historyId;
	public Integer roomId;

	@Builder
	public ReplyRegisterRequestDto(String answer, Long problemId, Long historyId, Integer roomId) {
		this.answer = answer;
		this.problemId = problemId;
		this.historyId = historyId;
		this.roomId = roomId;
	}
}
