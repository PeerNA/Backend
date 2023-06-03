package cos.peerna.controller.dto;

import cos.peerna.domain.Chat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class ChatMessageSendDto {

    private Long writerId;
    private String message;
    private LocalTime time;

    public ChatMessageSendDto(ChatMessageReceiveDto receiveMessage) {
        this.writerId = receiveMessage.getWriterId();
        this.message = receiveMessage.getMessage();
        this.time = LocalTime.now();
    }

    public ChatMessageSendDto(Chat chat) {
        this.writerId = chat.getWriterId();
        this.message = chat.getContent();
        this.time = chat.getTime();
    }
}