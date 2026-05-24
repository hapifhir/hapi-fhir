package ca.uhn.fhir.jpa.batch2.mockjob.sendtostep;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import jakarta.annotation.Nonnull;

public class MockSendToStepWorkerIntermediateStep extends BaseMockSendToStepWorker implements IJobStepWorker<MockSendToStepJobParameters, MockSendToStepJobModelJson, MockSendToStepJobModelJson> {

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<MockSendToStepJobParameters, MockSendToStepJobModelJson> theStepExecutionDetails, @Nonnull IJobDataSink<MockSendToStepJobModelJson> theDataSink) throws JobExecutionFailedException {
		super.captureMessage(theStepExecutionDetails);
		super.sendDataToSubsequentSteps(theStepExecutionDetails, theDataSink);
		return RunOutcome.SUCCESS;
	}
}
