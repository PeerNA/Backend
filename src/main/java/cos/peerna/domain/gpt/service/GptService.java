package cos.peerna.domain.gpt.service;

import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import cos.peerna.domain.gpt.event.ReviewReplyEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GptService {

    private final OpenAiService openAIService;
    private final SimpMessagingTemplate template;

    public void reviewReply(ReviewReplyEvent event) {
        openAIService.streamChatCompletion(ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(
                        new ChatMessage("system", "너는 소프트웨어 기업의 면접관이고 사용자에게 " + event.question() + "라는 질문을 했어. 사용자의 답변을 듣고 나서 피드백을 줘"),
                        new ChatMessage("user", event.answer())
                ))
                .build())
                .blockingForEach(chunk -> sendChatMessage(chunk, event.userId()));
    }

    private void sendChatMessage(ChatCompletionChunk chunk, Long userId) {
        /*
        TODO: stream 이 끝나면, gpt 답변 전체를 저장
         */
        String message = chunk.getChoices().get(0).getMessage().getContent();
        if (message == null) {
            template.convertAndSend("/user/" + userId + "/gpt", "\n-----END MESSAGE-----\n");
            return;
        }
        template.convertAndSend("/user/" + userId + "/gpt", message);
    }
}
