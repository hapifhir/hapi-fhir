package ca.uhn.fhir.jpa.batch2.mockjob.sendtostep;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import jakarta.annotation.Nonnull;

public class MockSendToStepWorkerLastStep extends BaseMockSendToStepWorker implements IJobStepWorker<MockSendToStepJobParameters, MockSendToStepJobModelJson, VoidModel> {

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<MockSendToStepJobParameters, MockSendToStepJobModelJson> theStepExecutionDetails, @Nonnull IJobDataSink<VoidModel> theDataSink) throws JobExecutionFailedException {
		super.captureMessage(theStepExecutionDetails);
		return RunOutcome.SUCCESS;
	}
}
