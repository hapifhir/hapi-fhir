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

public class MockBuildingJobStep1Worker implements IJobStepWorker<MockJobParameters, VoidModel, MockStepOutputType> {
	private static final Logger ourLog = LoggerFactory.getLogger(MockBuildingJobStep1Worker.class);

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<MockJobParameters, VoidModel> theStepExecutionDetails, @Nonnull IJobDataSink<MockStepOutputType> theDataSink) throws JobExecutionFailedException {
		ourLog.info("Running MOCK JOB step 1");

		MockStepOutputType output = new MockStepOutputType();
		theDataSink.accept(output);

		return RunOutcome.SUCCESS;
	}
}
