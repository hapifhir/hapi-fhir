package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.model.JobDefinition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


class JobDefinitionTest {
	private static final String JOB_DEF_ID = "Jeff";
	private static final String JOB_DESC = "Jeff is curious";

	@Test
	public void emptyBuilder_fails() {
		try {
			JobDefinition.newBuilder().build();
			fail();
		} catch (NullPointerException e) {
			assertEquals("No job parameters type was supplied", e.getMessage());
		}
	}

	@Test
	public void builder_no_steps() {
		try {
			JobDefinition.newBuilder()
				.setJobDefinitionId(JOB_DEF_ID)
				.setJobDescription(JOB_DESC)
				.setJobDefinitionVersion(1)
				.setParametersType(TestJobParameters.class)
				.build();
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("At least 2 steps must be supplied", e.getMessage());
		}
	}
}
