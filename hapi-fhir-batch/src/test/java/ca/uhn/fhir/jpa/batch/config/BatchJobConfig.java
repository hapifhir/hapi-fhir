package ca.uhn.fhir.jpa.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchJobConfig {

	@Autowired
	private JobBuilderFactory myJobBuilderFactory;

	@Autowired
	private StepBuilderFactory myStepBuilderFactory;


	@Bean
	public Job testJob() {
		return myJobBuilderFactory.get("testJob")
			.start(testStep())
			.build();
	}

	@Bean
	public Step testStep() {
		return myStepBuilderFactory.get("testStep")
			.tasklet(sampleTasklet())
			.build();
	}

	@Bean
	@StepScope
	public Tasklet sampleTasklet() {
		return new SampleTasklet();
	}

	@Bean
	@StepScope
	public SampleItemReader reader() {
		return new SampleItemReader();
	}

	@Bean
	public ItemWriter<String> simpleWriter() {
		return theList -> theList.forEach(System.out::println);
	}
}
