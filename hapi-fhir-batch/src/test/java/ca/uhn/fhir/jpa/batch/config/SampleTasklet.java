package ca.uhn.fhir.jpa.batch.config;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class SampleTasklet implements Tasklet {
	@Override
	public RepeatStatus execute(StepContribution theStepContribution, ChunkContext theChunkContext) throws Exception {
		System.out.println("woo");
		return RepeatStatus.FINISHED;
	}
}
