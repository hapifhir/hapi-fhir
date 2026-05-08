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

public class MockStep2Worker implements IJobStepWorker<MockJobParameters, MockStep1OutputType, VoidModel> {
	private static final Logger ourLog = LoggerFactory.getLogger(MockStep2Worker.class);

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<MockJobParameters, MockStep1OutputType> theStepExecutionDetails, @Nonnull IJobDataSink<VoidModel> theDataSink) throws JobExecutionFailedException {
		ourLog.info("Running MOCK JOB step 2");
		return RunOutcome.SUCCESS;
	}
}
