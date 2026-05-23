package ca.uhn.fhir.jpa.batch2.mockjob;

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.StatusEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MockJobAppCtx {

	public static final String BUILDING_JOB = "mock-building-job";
	public static final String SEND_TO_FUTURE_STEP_JOB = "mock-send-to-future-step-job";

	@Bean
	public JobDefinition<MockJobParameters> getBuildingJobDefinition() {
		return JobDefinition.newBuilder()
			.setParametersType(MockJobParameters.class)
			.setInitialStatus(StatusEnum.BUILDING)
			.setJobDefinitionId(BUILDING_JOB)
			.setJobDefinitionVersion(1)
			.setJobDescription("Mock job definition with an initial BUILDING status")
			.setParametersValidator(new MockJobParametersValidator())
			.addFirstStep("step-1", "Step 1", MockStepOutputType.class, new MockBuildingJobStep1Worker())
			.addLastStep("step-2", "Step 2", new MockBuildingJobStep2Worker())
			.build();
	}

	@Bean
	public JobDefinition<MockJobParameters> getSendToFutureStepJobDefinition() {
		return JobDefinition.newBuilder()
			.setParametersType(MockJobParameters.class)
			.setInitialStatus(StatusEnum.BUILDING)
			.setJobDefinitionId(BUILDING_JOB)
			.setJobDefinitionVersion(1)
			.setJobDescription("Mock job definition with an initial BUILDING status")
			.setParametersValidator(new MockJobParametersValidator())
			.addFirstStep("step-1", "Step 1", MockStepOutputType.class, new MockBuildingJobStep1Worker())
			.addLastStep("step-2", "Step 2", new MockBuildingJobStep2Worker())
			.build();
	}


}
