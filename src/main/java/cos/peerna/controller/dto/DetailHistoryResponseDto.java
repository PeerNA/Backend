package cos.peerna.controller.dto;

import cos.peerna.domain.Category;
import cos.peerna.domain.Chat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class DetailHistoryResponseDto {
	private Long peerId;
	private String question;
	private LocalDate time;
	private List<Map<String, String>> userInfo;
	private List<String> keyword;
	private List<ChatMessageSendDto> chat;

	/*
	private String question;
	private LocalDate date;

	private List<String> keyword;
	private List<ChatMessageSendDto> chat;
	 */
	class Re
}
