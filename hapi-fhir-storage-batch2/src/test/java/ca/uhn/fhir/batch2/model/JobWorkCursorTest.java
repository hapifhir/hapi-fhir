package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.batch2.coordinator.BaseBatch2Test;
import ca.uhn.fhir.batch2.coordinator.TestJobParameters;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.fail;

class JobWorkCursorTest extends BaseBatch2Test {

	private JobDefinition<TestJobParameters> myDefinition;

	@BeforeEach
	public void before() {
		myDefinition = createJobDefinition();
	}

	@Test
	public void createCursorStep1() {
		// execute
		JobWorkCursor<TestJobParameters, ?, ?> cursor = JobWorkCursor.fromJobDefinitionAndRequestedStepId(myDefinition, STEP_1);

		// verify
		assertCursor(cursor, true, false, STEP_1, STEP_2);
	}

	@Test
	public void createCursorStep2() {
		// execute
		JobWorkCursor<TestJobParameters, ?, ?> cursor = JobWorkCursor.fromJobDefinitionAndRequestedStepId(myDefinition, STEP_2);

		// verify
		assertCursor(cursor, false, false, STEP_2, STEP_3);
	}

	@Test
	public void createCursorStep3() {
		// execute
		JobWorkCursor<TestJobParameters, ?, ?> cursor = JobWorkCursor.fromJobDefinitionAndRequestedStepId(myDefinition, STEP_3);

		// verify
		assertCursor(cursor, false, true, STEP_3, null);
	}

	@Test
	public void unknownStep() {
		// setup
		String targetStepId = "Made a searching and fearless moral inventory of ourselves";

		// execute
		try {
			JobWorkCursor.fromJobDefinitionAndRequestedStepId(myDefinition, targetStepId);

			// verify
			fail("");
		} catch (InternalErrorException e) {
			assertThat(e.getMessage()).isEqualTo("HAPI-2042: Unknown step[" + targetStepId + "] for job definition ID[JOB_DEFINITION_ID] version[1]");
		}
	}

	private void assertCursor(JobWorkCursor<TestJobParameters,?,?> theCursor, boolean theExpectedIsFirstStep, boolean theExpectedIsFinalStep, String theExpectedCurrentStep, String theExpectedNextStep) {
		assertThat(theCursor.isFirstStep).isEqualTo(theExpectedIsFirstStep);
		assertThat(theCursor.isFinalStep()).isEqualTo(theExpectedIsFinalStep);
		assertThat(theCursor.currentStep.getStepId()).isEqualTo(theExpectedCurrentStep);
		if (theExpectedNextStep == null) {
			assertThat(theCursor.nextStep).isNull();
		} else {
			assertThat(theCursor.nextStep.getStepId()).isEqualTo(theExpectedNextStep);
		}
		assertThat(theCursor.jobDefinition.getJobDefinitionId()).isEqualTo(myDefinition.getJobDefinitionId());
		assertThat(theCursor.jobDefinition.getJobDefinitionVersion()).isEqualTo(myDefinition.getJobDefinitionVersion());
	}
}
