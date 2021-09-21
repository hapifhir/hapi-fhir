package ca.uhn.fhir.jpa.batch.config;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class TestBatchConfig {

	@Bean
	public PlatformTransactionManager hapiTransactionManager() {
		return new ResourcelessTransactionManager();
	}

	@Bean
	public TaskExecutor jobLaunchingTaskExecutor() {
		ThreadPoolTaskExecutor asyncTaskExecutor = new ThreadPoolTaskExecutor();
		asyncTaskExecutor.setCorePoolSize(5);
		asyncTaskExecutor.setMaxPoolSize(10);
		asyncTaskExecutor.setQueueCapacity(500);
		asyncTaskExecutor.setThreadNamePrefix("JobLauncher-");
		asyncTaskExecutor.initialize();
		return asyncTaskExecutor;
	}

	@Bean
	public BatchConfigurer batchConfigurer() {
		return new NonPersistedBatchConfigurer();
	}
}
