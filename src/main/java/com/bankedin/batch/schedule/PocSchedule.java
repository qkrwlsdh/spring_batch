package com.bankedin.batch.schedule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@EnableScheduling
@Component
@RequiredArgsConstructor
public class PocSchedule {

	private final JobLauncher jobLauncher;
	private final ApplicationContext applicationContext;
	String pTransDt = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

	@Autowired
	public PocSchedule(ApplicationContext applicationContext, JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
		this.applicationContext = applicationContext;
	}

	/**
	 * @Scheduled(cron = "초 분 24H day month week ")
	 * @Scheduled(cron = "0/10 * * * * ? ") : 매 10초마다 실행
	 * @Scheduled(cron = "0 0 22 1 * ? ") : 매월 1일 정각 밤 10시에 실행
	 * @Scheduled(cron = "0 0 2 ? * 1 ") : 매주 일요일 새벽 2시에
	 * @Scheduled(cron = "0 0 2 * * ? ") : 매일 새벽 2시
	 */
	@Scheduled(cron = "0 0 2 * * ?")
	public void boardDailyStatsSchedule() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
		Job job = applicationContext.getBean("boardJob", Job.class);

		JobParameters jobParameters = new JobParametersBuilder()
				.addLong("timestamp", System.currentTimeMillis())
				.addString("pTransDt", pTransDt)
				.toJobParameters();
		jobLauncher.run(job, jobParameters);
	}
}
