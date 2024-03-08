package cos.peerna.domain.gpt.event;

import cos.peerna.domain.gpt.service.GptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GptEventListener {

    private final GptService gptService;

    @EventListener
    public void transactionalEventListenerAfterCommit(ReviewReplyEvent event) {
        log.info("Review Reply Event Published: {}", event);
        gptService.reviewReply(event);
    }
}
