package cos.peerna.domain.history.dto;

import cos.peerna.domain.reply.dto.data.ReplyData;
import cos.peerna.domain.room.dto.ChatMessageSendDto;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class DetailHistoryResponseDto {
	private String question;
	private LocalDate time;
	private ReplyData mine;
	private ReplyData peer;
	private List<String> keyword;
	private List<ChatMessageSendDto> chat;

}
