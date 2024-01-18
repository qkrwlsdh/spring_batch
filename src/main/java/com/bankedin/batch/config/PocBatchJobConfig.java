package com.bankedin.batch.config;

import java.time.LocalDateTime;
import java.util.List;

import com.bankedin.batch.entity.DailyEntity;
import com.bankedin.batch.repository.BoardRepository;
import com.bankedin.batch.repository.DailyRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class PocBatchJobConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final DailyRepository dailyRepository;
	private final BoardRepository boardRepository;

	/**
	 * 일별 게시물 통계를 위한 Job
	 */
	@Bean
	@Transactional
	public Job boardJob() {
		return new JobBuilder("dailyStatsJob", jobRepository)
				.start(dailyStatsStep())
				.build();
	}

	/**
	 * Tasklet을 사용한 일별 게시물 통계 Step
	 * Tasklet은 단일 태스크를 수행하는 데 사용되는 인터페이스이며, 비교적 간단한 배치 작업을 구현할 때 유용
	 */
	@Bean
	@JobScope
	Step dailyStatsStep() {
		return new StepBuilder("dailyStatsStep", jobRepository)
				.tasklet(dailyStatsTasklet(), transactionManager)
				.build();
	}

	/**
	 * Tasklet을 사용한 일별 게시물 통계 수행
	 */
	@Bean
	Tasklet dailyStatsTasklet() {
		return (contribution, chunkContext) -> {
			// 현재 날짜 및 시간 가져오기
			LocalDateTime now = LocalDateTime.now();
			// 오늘 날짜로 시간을 00:00:00으로 설정
			LocalDateTime today = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
			// 시작 날짜
			LocalDateTime startDate = LocalDateTime.of(2024, 1, 15, 0, 0, 0);

			List<Object[]> dailyBoardCounts = boardRepository.countDailyBoardByDateRange(startDate, today);

			for (Object[] result : dailyBoardCounts) {
				String date = (String) result[0];
				long count = (long) result[1];

				DailyEntity dailyEntity = new DailyEntity();
				dailyEntity.setDay(date);
				dailyEntity.setBoardCnt((int) count);

				dailyRepository.save(dailyEntity);
			}

			return RepeatStatus.FINISHED;
		};
	}
}
