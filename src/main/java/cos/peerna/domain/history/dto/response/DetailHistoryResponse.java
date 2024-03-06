package cos.peerna.domain.history.dto.response;

import cos.peerna.domain.reply.dto.response.ReplyResponse;
import cos.peerna.domain.room.dto.ChatMessageSendDto;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetailHistoryResponse {
    private String question;
    private LocalDate time;
    private ReplyResponse mine;
    private ReplyResponse peer;
    private List<String> keyword;
    private List<ChatMessageSendDto> chat;

}