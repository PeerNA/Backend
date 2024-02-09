package cos.peerna.domain.room.controller;

import cos.peerna.domain.history.repository.HistoryRepository;
import cos.peerna.domain.room.dto.ChatMessageReceiveDto;
import cos.peerna.domain.room.dto.ChatMessageSendDto;
import cos.peerna.domain.room.model.Chat;
import cos.peerna.domain.room.repository.ChatRepository;
import cos.peerna.global.security.dto.SessionUser;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final SimpMessagingTemplate template;
    private final ChatRepository chatRepository;
    private final HistoryRepository historyRepository;

    @MessageMapping("/chat/message")
    public void message(SimpMessageHeaderAccessor messageHeaderAccessor, ChatMessageReceiveDto receiveMessage) {
        SessionUser user = (SessionUser) messageHeaderAccessor.getSessionAttributes().get("user");
        ChatMessageSendDto sendMessage = ChatMessageSendDto.builder()
                .writerId(user.getId())
                .message(receiveMessage.getMessage())
                .build();
        template.convertAndSend("/chat/room/" + receiveMessage.getRoomId(), sendMessage);

        chatRepository.save(Chat.builder()
                .writerId(user.getId())
                .content(receiveMessage.getMessage())
                .history(historyRepository.findById(receiveMessage.getHistoryId()).orElse(null))
                .build());
    }
}
