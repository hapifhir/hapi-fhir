package ca.uhn.fhir.jpa.batch2.mockjob.sendtostep;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import jakarta.annotation.Nonnull;

public class MockSendToStepWorkerFirstStep extends BaseMockSendToStepWorker implements IJobStepWorker<MockSendToStepJobParameters, VoidModel, MockSendToStepJobModelJson> {

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<MockSendToStepJobParameters, VoidModel> theStepExecutionDetails, @Nonnull IJobDataSink<MockSendToStepJobModelJson> theDataSink) throws JobExecutionFailedException {
		super.sendDataToSubsequentSteps(theStepExecutionDetails, theDataSink);
		return RunOutcome.SUCCESS;
	}

}
