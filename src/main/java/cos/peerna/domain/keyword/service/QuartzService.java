package cos.peerna.domain.keyword.service;

import cos.peerna.domain.keyword.job.QuartzJob;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import static org.quartz.JobBuilder.newJob;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuartzService {

	private final Scheduler scheduler;

	@PostConstruct
	public void addJob() {
		JobDetail ReqJobDetail = buildJobDetail(QuartzJob.class, "신민철", "24시간주기 문제집 업데이트");
		try {
			scheduler.scheduleJob(ReqJobDetail, buildJobTrigger("0 0 * * * ?"));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public Trigger buildJobTrigger(String scheduleExp) {
		return TriggerBuilder.newTrigger()
				.withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp)).build();
	}

	public JobDetail buildJobDetail(Class job, String name, String work) {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("Dev", name);
		jobDataMap.put("Work", work);

		return newJob(job).withIdentity("scheduleJob")
				.usingJobData(jobDataMap)
				.build();
	}
}
