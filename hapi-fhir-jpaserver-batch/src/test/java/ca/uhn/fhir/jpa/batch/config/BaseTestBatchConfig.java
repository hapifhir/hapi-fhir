package ca.uhn.fhir.jpa.batch.config;

import ca.uhn.fhir.jpa.batch.svc.DummyService;
import ca.uhn.fhir.jpa.config.TestJpaConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BaseTestBatchConfig extends DefaultBatchConfigurer {

	@Autowired
	private JpaTransactionManager myPlatformTransactionManager;


	@Autowired
	private JobBuilderFactory myJobBuilderFactory;

	@Autowired
	private StepBuilderFactory myStepBuilderFactory;


	@Override
	public PlatformTransactionManager getTransactionManager() {
		return myPlatformTransactionManager;
	}

	@Bean
	public DummyService myDummyService() {
		return new DummyService();
	}

	@Bean
	public Job testJob() {
		return myJobBuilderFactory.get("testJob")
			.start(testStep())
			.build();
	}

	@Bean
	public Step testStep() {
		return myStepBuilderFactory.get("testStep").tasklet((theStepContribution, theChunkContext) -> {
			System.out.println("woo!");
			return RepeatStatus.FINISHED;
		}).build();
	}
}
