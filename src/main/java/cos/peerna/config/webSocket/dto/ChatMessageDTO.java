package cos.peerna.config.webSocket.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDTO {

    private Integer roomId;
    private Integer writerId;
    private String message;
}