package cos.peerna.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class ChatMessageSendDto {

    private Integer roomId;
    private Long writerId;
    private String message;
    private LocalTime time;

    public ChatMessageSendDto(ChatMessageReceiveDto receiveMessage) {
        this.roomId = receiveMessage.getRoomId();
        this.writerId = receiveMessage.getWriterId();
        this.message = receiveMessage.getMessage();
        this.time = LocalTime.now();
    }
}