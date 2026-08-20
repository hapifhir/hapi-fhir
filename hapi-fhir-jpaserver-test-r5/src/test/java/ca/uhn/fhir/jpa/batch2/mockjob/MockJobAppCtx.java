package ca.uhn.fhir.jpa.batch2.mockjob;

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.batch2.mockjob.building.MockBuildingJobParameters;
import ca.uhn.fhir.jpa.batch2.mockjob.building.MockBuildingJobStep1Worker;
import ca.uhn.fhir.jpa.batch2.mockjob.building.MockBuildingJobStep2Worker;
import ca.uhn.fhir.jpa.batch2.mockjob.sendtostep.MockSendToStepJobModelJson;
import ca.uhn.fhir.jpa.batch2.mockjob.sendtostep.MockSendToStepJobParameters;
import ca.uhn.fhir.jpa.batch2.mockjob.sendtostep.MockSendToStepWorkerFirstStep;
import ca.uhn.fhir.jpa.batch2.mockjob.sendtostep.MockSendToStepWorkerIntermediateStep;
import ca.uhn.fhir.jpa.batch2.mockjob.sendtostep.MockSendToStepWorkerLastStep;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MockJobAppCtx {

	public static final String BUILDING_JOB = "mock-building-job";
	public static final String SEND_TO_FUTURE_STEP_JOB_GATED = "mock-send-to-future-step-job-gated";
	public static final String SEND_TO_FUTURE_STEP_JOB_ASYNC = "mock-send-to-future-step-job-async";
	public static final String STEP_0 = "step-0";
	public static final String STEP_1 = "step-1";
	public static final String STEP_2 = "step-2";
	public static final String STEP_3 = "step-3";

	@Bean
	public JobDefinition<MockBuildingJobParameters> getBuildingJobDefinition() {
		return JobDefinition.newBuilder()
			.setParametersType(MockBuildingJobParameters.class)
			.setInitialStatus(StatusEnum.BUILDING)
			.setJobDefinitionId(BUILDING_JOB)
			.setJobDefinitionVersion(1)
			.setJobDescription("Mock job definition with an initial BUILDING status")
			.setParametersValidator(new MockJobParametersValidator<>())
			.gatedExecution()
			.addFirstStep(STEP_0, "Step 1", MockStepOutputType.class, new MockBuildingJobStep1Worker())
			.addLastStep(STEP_1, "Step 2", new MockBuildingJobStep2Worker())
			.build();
	}

	@Bean(name = SEND_TO_FUTURE_STEP_JOB_GATED)
	public JobDefinition<MockSendToStepJobParameters> getSendToFutureStepJobDefinitionGated() {
		boolean gatedExecution = true;
		return getMockSendToStepJobParametersJobDefinition(SEND_TO_FUTURE_STEP_JOB_GATED, gatedExecution);
	}

	@Bean(name = SEND_TO_FUTURE_STEP_JOB_ASYNC)
	public JobDefinition<MockSendToStepJobParameters> getSendToFutureStepJobDefinitionAsync() {
		boolean gatedExecution = false;
		return getMockSendToStepJobParametersJobDefinition(SEND_TO_FUTURE_STEP_JOB_ASYNC, gatedExecution);
	}

	private static JobDefinition<MockSendToStepJobParameters> getMockSendToStepJobParametersJobDefinition(String theId, boolean gatedExecution) {
		return JobDefinition.newBuilder()
			.setParametersType(MockSendToStepJobParameters.class)
			.setJobDefinitionId(theId)
			.setJobDefinitionVersion(1)
			.setJobDescription("Mock job for testing send-to-future-step")
			.setParametersValidator(new MockJobParametersValidator<>())
			.gatedExecution(gatedExecution)
			.addFirstStep(STEP_0, "Step 0", MockSendToStepJobModelJson.class, new MockSendToStepWorkerFirstStep())
			.addIntermediateStep(STEP_1, "Step 1", MockSendToStepJobModelJson.class, new MockSendToStepWorkerIntermediateStep())
			.addIntermediateStep(STEP_2, "Step 2", MockSendToStepJobModelJson.class, new MockSendToStepWorkerIntermediateStep())
			.addLastStep(STEP_3, "Step 3", new MockSendToStepWorkerLastStep())
			.build();
	}


}
