package cos.peerna.controller.dto;

<<<<<<< HEAD
=======
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
>>>>>>> 8e6fb1e6c067671dac60ccb3db784c48583cd06e

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
