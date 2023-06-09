package cos.peerna.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageReceiveDto {

    private Integer roomId;
    private Long writerId;
    private Long historyId;
    private String message;
}