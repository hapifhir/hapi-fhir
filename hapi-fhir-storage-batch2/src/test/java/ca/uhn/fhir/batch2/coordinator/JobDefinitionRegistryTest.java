package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
		} catch (InternalErrorException e) {
			assertEquals("HAPI-2043: Unknown job definition ID[" + jobDefinitionId + "] version[" + jobDefinitionVersion + "]", e.getMessage());
		}
	}


}
