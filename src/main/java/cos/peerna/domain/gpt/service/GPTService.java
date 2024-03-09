package cos.peerna.domain.gpt.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import cos.peerna.domain.gpt.dto.request.SendMessageRequest;
import cos.peerna.domain.gpt.event.ReviewReplyEvent;
import cos.peerna.domain.gpt.model.GPT;
import cos.peerna.global.security.dto.SessionUser;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GPTService {

    private final OpenAiService openAIService;
    private final SimpMessagingTemplate template;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void reviewReply(ReviewReplyEvent event) {
        /*
        TODO: 사용자의 권한에 따른 gpt 모델 선택
         */
        ChatMessage systemMessage = new ChatMessage("system", GPT.getConcept(event.question()));
        ChatMessage userMessage = new ChatMessage("user", event.answer());
        openAIService.streamChatCompletion(ChatCompletionRequest.builder()
                        .model(GPT.getModel())
                        .messages(List.of(
                                systemMessage,
                                userMessage
                        ))
                        .build())
                .doOnError(throwable -> sendErrorMessage(event.userId()))
                .blockingForEach(chunk -> sendChatMessage(chunk, event.userId()));
        redisTemplate.opsForList().rightPush(String.valueOf(event.historyId()), systemMessage);
        redisTemplate.opsForList().rightPush(String.valueOf(event.historyId()), userMessage);
    }

    public void sendMessage(SessionUser user, SendMessageRequest request) {
        List<ChatMessage> messages = getChatMessages(user);
        ChatMessage assistantMessage = new ChatMessage("assistant", request.lastGPTMessage());
        ChatMessage userMessage = new ChatMessage("user", request.message());
        messages.add(assistantMessage);
        messages.add(userMessage);

        log.debug("GPTService.sendMessage: messages: {}", messages);

        openAIService.streamChatCompletion(ChatCompletionRequest.builder()
                        .model(GPT.getModel())
                        .messages(messages)
                        .build())
                .doOnError(throwable -> sendErrorMessage(user.getId()))
                .blockingForEach(chunk -> sendChatMessage(chunk, user.getId()));
        redisTemplate.opsForList().rightPush(String.valueOf(user.getHistoryId()), userMessage);
    }

    private List<ChatMessage> getChatMessages(SessionUser user) {
        if (user.getHistoryId() == null) {
            throw new NotFoundException("historyId is null");
        }
        List<Object> messageObjects = redisTemplate.opsForList().range(String.valueOf(user.getHistoryId()), 0, -1);
        List<ChatMessage> messages = new ArrayList<>();
        if (messageObjects == null) {
            throw new NotFoundException("messageObjects is null");
        }
        for (Object messageObject : messageObjects) {
            ChatMessage chatMessage = objectMapper.convertValue(messageObject, ChatMessage.class);
            messages.add(chatMessage);
        }
        return messages;
    }

    private void sendChatMessage(ChatCompletionChunk chunk, Long userId) {
        /*
        TODO: stream 이 끝나면, gpt 답변 전체를 저장
        TODO: gpt에게서 오는 chunk의 순서가 보장되지 않음
         */
        String message = chunk.getChoices().get(0).getMessage().getContent();
        if (message == null) {
            template.convertAndSend("/user/" + userId + "/gpt", GPT.getENDMessage());
            return;
        }
        template.convertAndSend("/user/" + userId + "/gpt", message);

//        ChatCompletionChoice choice = chunk.getChoices().get(0);
//        template.convertAndSend("/user/" + userId + "/gpt", choice);
    }

    private void sendErrorMessage(Long userId) {
        template.convertAndSend("/user/" + userId + "/gpt", GPT.getErrorMessage());
    }
}
