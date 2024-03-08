package cos.peerna.domain.gpt.service;

import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import cos.peerna.domain.gpt.event.ReviewReplyEvent;
import cos.peerna.domain.gpt.model.GPT;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GPTService {

    private final OpenAiService openAIService;
    private final SimpMessagingTemplate template;

    public void reviewReply(ReviewReplyEvent event) {
        /*
        사용자의 권한에 따른 gpt 모델 선택
         */
        openAIService.streamChatCompletion(ChatCompletionRequest.builder()
                .model(GPT.getModel())
                .messages(List.of(
                        new ChatMessage("system", GPT.getConcept(event.question())),
                        new ChatMessage("user", event.answer())
                ))
                .build())
                .doOnError(throwable -> sendErrorMessage(event.userId()))
                .blockingForEach(chunk -> sendChatMessage(chunk, event.userId()));
    }

    private void sendChatMessage(ChatCompletionChunk chunk, Long userId) {
        /*
        TODO: stream 이 끝나면, gpt 답변 전체를 저장
         */
        String message = chunk.getChoices().get(0).getMessage().getContent();
        if (message == null) {
            template.convertAndSend("/user/" + userId + "/gpt", GPT.getENDMessage());
            return;
        }
        template.convertAndSend("/user/" + userId + "/gpt", message);
    }

    private void sendErrorMessage(Long userId) {
        template.convertAndSend("/user/" + userId + "/gpt", GPT.getErrorMessage());
    }
}
