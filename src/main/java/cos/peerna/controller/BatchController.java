package cos.peerna.controller;

import cos.peerna.config.job.ReqJob;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

import static org.quartz.JobBuilder.newJob;

@Slf4j
@Controller
public class BatchController {

	@Autowired
	private Scheduler scheduler;

	@PostConstruct
	public void start() {
		JobDetail ReqJobDetail = buildJobDetail(ReqJob.class, "신민철", "24시간주기 문제집 업데이트");
		try {
			scheduler.scheduleJob(ReqJobDetail, buildJobTrigger("1 * * * * ?"));
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
