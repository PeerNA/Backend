package cos.peerna.domain.match.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import cos.peerna.domain.match.service.MatchService;
import cos.peerna.domain.user.model.Category;
import lombok.RequiredArgsConstructor;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchJob extends QuartzJobBean implements InterruptableJob {

    private final RedisTemplate<String, String> redisTemplate;
    private final MatchService matchService;

    @Override
    public void interrupt() throws UnableToInterruptJobException {
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        for (Category category : Category.values()) {
            try {
                matchService.duoMatching(category);
            } catch (JsonProcessingException e) {
                redisTemplate.delete("standby:" + category.name());
            }
        }
    }
}