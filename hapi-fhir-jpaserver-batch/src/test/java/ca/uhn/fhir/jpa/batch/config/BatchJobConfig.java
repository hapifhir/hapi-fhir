package ca.uhn.fhir.jpa.batch.config;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.test.concurrency.IPointcutLatch;
import ca.uhn.test.concurrency.PointcutLatch;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BatchJobConfig implements IPointcutLatch {

	@Autowired
	private JobBuilderFactory myJobBuilderFactory;

	@Autowired
	private StepBuilderFactory myStepBuilderFactory;

	private final PointcutLatch myPointcutLatch = new PointcutLatch("batchJobLatch");


	@Bean
	public Job datJob() {
		return myJobBuilderFactory.get("testJob")
			.start(testStep())
			.build();
	}

	@Bean
	public Step testStep() {
		return myStepBuilderFactory.get("testStep").tasklet((theStepContribution, theChunkContext) -> {
			System.out.println("woo!");
			myPointcutLatch.call(theChunkContext);
			return RepeatStatus.FINISHED;
		}).build();
	}

	@Override
	public void clear() {
		myPointcutLatch.clear();
	}

	@Override
	public void setExpectedCount(int count) {
		myPointcutLatch.setExpectedCount(count);
	}

	@Override
	public List<HookParams> awaitExpected() throws InterruptedException {
		return myPointcutLatch.awaitExpected();
	}
}
