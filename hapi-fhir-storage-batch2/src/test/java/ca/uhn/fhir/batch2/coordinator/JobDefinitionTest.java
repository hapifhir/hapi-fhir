package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.model.api.IModelJson;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


class JobDefinitionTest extends BaseBatch2Test {
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

	@Test
	void stepWeight_FailOnZeroWeight() {
		assertThatThrownBy(()->{
			super.createJobDefinition(b -> {
				b.setStepWeightForProgressCalculator(STEP_1, 0.0);
			});
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("step weight can not be <= 0.0");
	}

	@Test
	void stepWeight_FailOnAllWeightConsumedByNotAllSteps() {
		assertThatThrownBy(()->{
			super.createJobDefinition(b -> {
				b.setStepWeightForProgressCalculator(STEP_1, 0.5);
				b.setStepWeightForProgressCalculator(STEP_2, 0.5);
				// Nothing left for step 3
			});
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("no remaining room for steps: [STEP_3]");
	}

	@Test
	void stepWeight_FailOnAllWeightAddsUpToMoreThanOne() {
		assertThatThrownBy(()->{
			super.createJobDefinition(b -> {
				b.setStepWeightForProgressCalculator(STEP_1, 0.49);
				b.setStepWeightForProgressCalculator(STEP_2, 0.49);
				// Adds up to more than 1.0
				b.setStepWeightForProgressCalculator(STEP_3, 0.2);
			});
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Combined step weights can not be greater than 1.0, but was 1.18");
	}

}
