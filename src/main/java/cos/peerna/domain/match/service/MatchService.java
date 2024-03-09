package cos.peerna.domain.match.service;

import static org.quartz.JobBuilder.newJob;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cos.peerna.domain.match.job.MatchJob;
import cos.peerna.domain.match.model.Standby;
import cos.peerna.domain.room.event.CreateRoomEvent;
import cos.peerna.domain.user.model.Category;
import cos.peerna.global.security.dto.SessionUser;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {
    private static final Class JOB_CLASS = MatchJob.class;
    private static final String DEVELOPER_NAME = "송승훈";
    private static final String JOB_DESCRIPTION = "동료 매칭";
    private static final String SCHEDULE_EXPRESSION = "*/5 * * * * ?";
    private static final String JOB_IDENTITY = "Dev";
    private static final String JOB_WORK = "Work";
    private final Scheduler scheduler;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final ApplicationEventPublisher eventPublisher;

    @PostConstruct
    public void scheduleJob() {
        JobDetail jobDetail = buildJobDetail(JOB_CLASS, DEVELOPER_NAME, JOB_DESCRIPTION);
        try {
            scheduler.scheduleJob(jobDetail, buildJobTrigger(SCHEDULE_EXPRESSION));
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
    }

    public Standby addStandby(SessionUser user, Category category) {
        Standby standby = Standby.builder()
                .id(user.getId())
                .score(user.getScore())
                .createdAt(LocalDateTime.now())
                .build();

        try {
            String json = objectMapper.writeValueAsString(standby);
            double score = standby.getScore().doubleValue();
            redisTemplate.opsForZSet().add("standby:" + category, json, score);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
        return standby;
    }

    @Async
    public void duoMatching(Category category) throws JsonProcessingException {
        List<Standby> standbyList = findStandby(category);
        if (standbyList.size() < 2) {
            return;
        }
        while (standbyList.size() >= 2) {
            Standby standby = standbyList.remove(0);
            for (int i = 0; i < standbyList.size(); i++) {
                Standby target = standbyList.get(i);
                if (!standby.isMatchable(target))
                    break;
                if (target.isMatchable(standby)) {
                    log.debug("[" + standby.getId() + "] and [" + target.getId() + "] are matched!");
                    standbyList.remove(i);
                    redisTemplate.opsForZSet().remove("standby:" + category.name(), objectMapper.writeValueAsString(standby));
                    redisTemplate.opsForZSet().remove("standby:" + category.name(), objectMapper.writeValueAsString(target));
                    eventPublisher.publishEvent(CreateRoomEvent.of(new HashMap<>() {{
                        put(standby.getId(), standby.getScore());
                        put(target.getId(), target.getScore());
                    }}, category));
                    break;
                }
            }
        }
    }

    private Trigger buildJobTrigger(String scheduleExp) {
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp)).build();
    }

    private JobDetail buildJobDetail(Class job, String name, String work) {
        return newJob(job).withIdentity("scheduleJob")
                .usingJobData(newJobDataMap(name, work))
                .build();
    }

    private JobDataMap newJobDataMap(String name, String work) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(JOB_IDENTITY, name);
        jobDataMap.put(JOB_WORK, work);
        return jobDataMap;
    }

    private List<Standby> findStandby(Category category) throws JsonProcessingException {
        Set<String> standbyStrSet = redisTemplate.opsForZSet().range("standby:" + category.name(), 0, -1);
        List<Standby> standbyList = new ArrayList<>();
        for (String standbyStr : standbyStrSet) {
            Standby standby = objectMapper.readValue(standbyStr, Standby.class);
            standbyList.add(standby);
        }
        return standbyList;
    }
}