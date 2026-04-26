package ca.uhn.fhir.jpa.batch2.mockjob;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockStep1Worker implements IJobStepWorker<MockJobParameters, VoidModel, MockStep1OutputType> {
	private static final Logger ourLog = LoggerFactory.getLogger(MockStep1Worker.class);

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<MockJobParameters, VoidModel> theStepExecutionDetails, @Nonnull IJobDataSink<MockStep1OutputType> theDataSink) throws JobExecutionFailedException {
		ourLog.info("Running MOCK JOB step 1");

		MockStep1OutputType output = new MockStep1OutputType();
		theDataSink.accept(output);

		return RunOutcome.SUCCESS;
	}
}
