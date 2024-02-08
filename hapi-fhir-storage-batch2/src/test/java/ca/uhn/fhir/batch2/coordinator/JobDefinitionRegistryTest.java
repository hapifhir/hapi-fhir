package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
class JobDefinitionRegistryTest {

	private JobDefinitionRegistry mySvc;

	@Mock
	private IJobStepWorker<TestJobParameters, VoidModel, TestJobStep2InputType> myFirstStep;
	@Mock
	private IJobStepWorker<TestJobParameters, TestJobStep2InputType, VoidModel> myLastStep;

	@BeforeEach
	void beforeEach() {
		mySvc = new JobDefinitionRegistry();

		mySvc.addJobDefinition(JobDefinition
			.newBuilder()
			.setJobDefinitionId("A")
			.setJobDefinitionVersion(1)
			.setJobDescription("the description")
			.setParametersType(TestJobParameters.class)
			.addFirstStep("S1", "S1", TestJobStep2InputType.class, myFirstStep)
			.addLastStep("S2", "S2", myLastStep)
			.build());

		mySvc.addJobDefinition(JobDefinition
			.newBuilder()
			.setJobDefinitionId("A")
			.setJobDefinitionVersion(2)
			.setJobDescription("the description")
			.setParametersType(TestJobParameters.class)
			.addFirstStep("S1", "S1", TestJobStep2InputType.class, myFirstStep)
			.addLastStep("S2", "S2", myLastStep)
			.build());
	}

	@Test
	void testGetLatestJobDefinition() {
		assertThat(mySvc.getLatestJobDefinition("A").orElseThrow(IllegalArgumentException::new).getJobDefinitionVersion()).isEqualTo(2);
	}

	@Test
	void testGetJobDefinition() {
		assertThat(mySvc.getJobDefinition("A", 1).orElseThrow(IllegalArgumentException::new).getJobDefinitionVersion()).isEqualTo(1);
		assertThat(mySvc.getJobDefinition("A", 2).orElseThrow(IllegalArgumentException::new).getJobDefinitionVersion()).isEqualTo(2);
	}


	@Test
	void testEnsureStepsHaveUniqueIds() {

		try {
			mySvc.addJobDefinition(JobDefinition
				.newBuilder()
				.setJobDefinitionId("A")
				.setJobDefinitionVersion(2)
				.setJobDescription("The description")
				.setParametersType(TestJobParameters.class)
				.addFirstStep("S1", "S1", TestJobStep2InputType.class, myFirstStep)
				.addLastStep("S2", "S2", myLastStep)
				.build());
			fail("");
		} catch (ConfigurationException e) {
			assertThat(e.getMessage()).isEqualTo("HAPI-2047: Multiple definitions for job[A] version: 2");
		}

		try {
			mySvc.addJobDefinition(JobDefinition
				.newBuilder()
				.setJobDefinitionId("A")
				.setJobDefinitionVersion(3)
				.setJobDescription("The description")
				.setParametersType(TestJobParameters.class)
				.addFirstStep("S1", "S1", TestJobStep2InputType.class, myFirstStep)
				.addLastStep("S1", "S2", myLastStep)
				.build());
			fail("");
		} catch (ConfigurationException e) {
			assertThat(e.getMessage()).isEqualTo("HAPI-2046: Duplicate step[S1] in definition[A] version: 3");
		}

		try {
			mySvc.addJobDefinition(JobDefinition
				.newBuilder()
				.setJobDefinitionId("A")
				.setJobDefinitionVersion(2)
				.setParametersType(TestJobParameters.class)
				.addFirstStep("S1", "S1", TestJobStep2InputType.class, myFirstStep)
				.addLastStep("", "S2", myLastStep)
				.build());
			fail("");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage()).isEqualTo("No step ID specified");
		}

	}

	@Test
	public void getJobDefinitionOrThrowException() {
		String jobDefinitionId = "Ranch Dressing Expert";
		int jobDefinitionVersion = 12;
		try {
			mySvc.getJobDefinitionOrThrowException(jobDefinitionId, jobDefinitionVersion);
			fail("");
		} catch (JobExecutionFailedException e) {
			assertThat(e.getMessage()).isEqualTo("HAPI-2043: Unknown job definition ID[" + jobDefinitionId + "] version[" + jobDefinitionVersion + "]");
		}
	}

	@Test
	public void testRemoveJobDefinition() {
		mySvc.removeJobDefinition("A", 1);

		assertThat(mySvc.getJobDefinitionIds()).containsExactlyInAnyOrder("A");
		assertThat(mySvc.getJobDefinitionVersions("A")).containsExactlyInAnyOrder(2);

		mySvc.removeJobDefinition("A", 2);
		assertThat(mySvc.getJobDefinitionIds()).isEmpty();
	}


}
