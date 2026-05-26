package ca.uhn.fhir.jpa.batch2.mockjob.building;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.jpa.batch2.mockjob.MockStepOutputType;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockBuildingJobStep2Worker implements IJobStepWorker<MockBuildingJobParameters, MockStepOutputType, VoidModel> {
	private static final Logger ourLog = LoggerFactory.getLogger(MockBuildingJobStep2Worker.class);

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<MockBuildingJobParameters, MockStepOutputType> theStepExecutionDetails, @Nonnull IJobDataSink<VoidModel> theDataSink) throws JobExecutionFailedException {
		ourLog.info("Running MOCK JOB step 2");
		return RunOutcome.SUCCESS;
	}
}
