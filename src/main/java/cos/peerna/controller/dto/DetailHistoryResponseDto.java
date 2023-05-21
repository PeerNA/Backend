package cos.peerna.controller.dto;

import cos.peerna.controller.dto.data.ReplyData;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class DetailHistoryResponseDto {
//	private Long peerId;
//	private String question;
//	private LocalDate time;
//	private List<Map<String, String>> userInfo;
//	private List<String> keyword;
//	private List<ChatMessageSendDto> chat;

	private String question;
	private LocalDate time;
	private ReplyData mine;
	private ReplyData peer;
	private List<String> keyword;
	private List<ChatMessageSendDto> chat;

}
