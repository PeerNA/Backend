package cos.peerna.domain.room.controller;

import cos.peerna.domain.history.repository.HistoryRepository;
import cos.peerna.domain.room.dto.ChatMessageReceiveDto;
import cos.peerna.domain.room.dto.ChatMessageSendDto;
import cos.peerna.domain.room.model.Chat;
import cos.peerna.domain.room.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달
    private final ChatRepository chatRepository;
    private final HistoryRepository historyRepository;

    /*
    Client가 SEND할 수 있는 경로
     stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
    "/pub/chat/enter"
     */
    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageReceiveDto receiveMessage) {
        ChatMessageSendDto sendMessage = new ChatMessageSendDto(receiveMessage);
        template.convertAndSend("/sub/chat/room/" + receiveMessage.getRoomId(), sendMessage);

        log.debug("receiveMessage: {}", receiveMessage.getMessage());

        Chat chat = chatRepository.save(Chat.builder()
                .writerId(receiveMessage.getWriterId())
                .content(receiveMessage.getMessage())
                .history(historyRepository.findById(receiveMessage.getHistoryId()).orElse(null))
                .build());

    }

    /*
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDTO message) {
            message.setMessage(message.getWriterId() + "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping(value = "/chat/leave")
    public void leave(ChatMessageDTO message) {
        message.setMessage(message.getWriter() + "님이 채팅방을 나갔습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
     */

}
