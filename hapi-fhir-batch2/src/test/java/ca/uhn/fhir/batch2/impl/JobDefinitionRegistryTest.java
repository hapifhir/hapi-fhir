package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.model.JobDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class JobDefinitionRegistryTest {

	private JobDefinitionRegistry mySvc;

	@BeforeEach
	void beforeEach() {
		mySvc = new JobDefinitionRegistry();

		mySvc.addJobDefinition(JobDefinition
			.newBuilder()
				.setJobDefinitionId("A")
				.setJobDefinitionVersion(1)
				.addStep("S1", "S1", mock(IJobStepWorker.class))
				.addStep("S2", "S2", mock(IJobStepWorker.class))
			.build());

		mySvc.addJobDefinition(JobDefinition
			.newBuilder()
			.setJobDefinitionId("A")
			.setJobDefinitionVersion(2)
			.addStep("S1", "S1", mock(IJobStepWorker.class))
			.addStep("S2", "S2", mock(IJobStepWorker.class))
			.build());
	}

	@Test
	void testGetLatestJobDefinition() {
		assertEquals(2, mySvc.getLatestJobDefinition("A").orElseThrow(()->new IllegalArgumentException()).getJobDefinitionVersion());
	}

	@Test
	void testGetJobDefinition() {
		assertEquals(1, mySvc.getJobDefinition("A", 1).orElseThrow(()->new IllegalArgumentException()).getJobDefinitionVersion());
		assertEquals(2, mySvc.getJobDefinition("A", 2).orElseThrow(()->new IllegalArgumentException()).getJobDefinitionVersion());
	}
}
