package cos.peerna.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageReceiveDTO {

    private Integer roomId;
    private Long writerId;
    private String message;
}