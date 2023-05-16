package cos.peerna.controller;

import cos.peerna.controller.dto.ChatMessageReceiveDTO;
import cos.peerna.controller.dto.ChatMessageSendDTO;
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
    /*
    private final HttpSession httpSession;
    Http request 가 아니라 WebSocket 통신이라 위 방식은 불가능
     */

    // Client가 SEND할 수 있는 경로
    // stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
    //"/pub/chat/enter"


    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageReceiveDTO receiveMessage) {
        ChatMessageSendDTO sendMessage = new ChatMessageSendDTO(receiveMessage);
        log.debug("receiveMessage: {}", receiveMessage);
        template.convertAndSend("/sub/chat/room/" + sendMessage.getRoomId(), sendMessage);
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
