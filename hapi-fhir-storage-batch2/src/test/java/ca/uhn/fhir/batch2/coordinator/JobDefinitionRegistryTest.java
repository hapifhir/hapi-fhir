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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
class JobDefinitionRegistryTest {
	private JobDefinitionRegistry mySvc;

	@Mock
	private IJobStepWorker<TestJobParameters, VoidModel, TestJobStep2InputType> myFirstStep;
	@Mock
	private IJobStepWorker<TestJobParameters, TestJobStep2InputType, VoidModel> myLastStep;

	private JobDefinition<TestJobParameters> myJobDefinition1;
	private JobDefinition<TestJobParameters> myJobDefinition2;

	@BeforeEach
	void beforeEach() {
		myJobDefinition1 = JobDefinition
			.newBuilder()
			.setJobDefinitionId("A")
			.setJobDefinitionVersion(1)
			.setJobDescription("the description")
			.setParametersType(TestJobParameters.class)
			.addFirstStep("S1", "S1", TestJobStep2InputType.class, myFirstStep)
			.addLastStep("S2", "S2", myLastStep)
			.build();
		myJobDefinition2 = JobDefinition
			.newBuilder()
			.setJobDefinitionId("A")
			.setJobDefinitionVersion(2)
			.setJobDescription("the description")
			.setParametersType(TestJobParameters.class)
			.addFirstStep("S1", "S1", TestJobStep2InputType.class, myFirstStep)
			.addLastStep("S2", "S2", myLastStep)
			.build();

		mySvc = new JobDefinitionRegistry();

		mySvc.addJobDefinition(myJobDefinition1);
		mySvc.addJobDefinition(myJobDefinition2);
	}

	@Test
	void testGetLatestJobDefinition() {
		assertEquals(2, mySvc.getLatestJobDefinition("A").orElseThrow(IllegalArgumentException::new).getJobDefinitionVersion());
	}

	@Test
	void testGetJobDefinition() {
		assertEquals(1, mySvc.getJobDefinition("A", 1).orElseThrow(IllegalArgumentException::new).getJobDefinitionVersion());
		assertEquals(2, mySvc.getJobDefinition("A", 2).orElseThrow(IllegalArgumentException::new).getJobDefinitionVersion());
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
			fail();
		} catch (ConfigurationException e) {
			assertEquals("HAPI-2047: Multiple definitions for job[A] version: 2", e.getMessage());
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
			fail();
		} catch (ConfigurationException e) {
			assertEquals("HAPI-2046: Duplicate step[S1] in definition[A] version: 3", e.getMessage());
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
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("No step ID specified", e.getMessage());
		}

	}

	@Test
	public void getJobDefinitionOrThrowException() {
		String jobDefinitionId = "Ranch Dressing Expert";
		int jobDefinitionVersion = 12;
		try {
			mySvc.getJobDefinitionOrThrowException(jobDefinitionId, jobDefinitionVersion);
			fail();
		} catch (JobExecutionFailedException e) {
			assertEquals("HAPI-2043: Unknown job definition ID[" + jobDefinitionId + "] version[" + jobDefinitionVersion + "]", e.getMessage());
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

	@Test
	void getJobDefinitionShouldReturnJobDefinitionWhenItIsRegistered() {
		// setup
		final JobDefinitionRegistry fixture = new JobDefinitionRegistry();
		fixture.addJobDefinition(myJobDefinition1);
		fixture.addJobDefinition(myJobDefinition2);
		// execute
		final Optional<JobDefinition<?>> actual = fixture.getJobDefinition("A", 2);
		// validate
		assertThat(actual).isNotEmpty().contains(myJobDefinition2);
	}

	@Test
	void getJobDefinitionShouldReturnEmptyOptionalWhenJobDefinitionsRegistered() {
		// setup
		final JobDefinitionRegistry fixture = new JobDefinitionRegistry();
		// execute
		final Optional<JobDefinition<?>> actual = fixture.getJobDefinition("A", 1);
		// validate
		assertThat(actual).isEmpty();
	}

	@Test
	void getJobDefinitionShouldReturnJobDefinitionWhenJobDefinitionsRegisteredWithDifferentVersion() {
		// setup
		final JobDefinitionRegistry fixture = new JobDefinitionRegistry();
		fixture.addJobDefinition(myJobDefinition1);
		// execute
		final Optional<JobDefinition<?>> actual = fixture.getJobDefinition("A", 2);
		// validate
		assertThat(actual).isEmpty();
	}

}
