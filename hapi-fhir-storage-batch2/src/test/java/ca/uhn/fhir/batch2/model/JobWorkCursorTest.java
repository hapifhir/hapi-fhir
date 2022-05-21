package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.batch2.BaseBatch2Test;
import ca.uhn.fhir.batch2.coordinator.TestJobParameters;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

class JobWorkCursorTest extends BaseBatch2Test {

	private JobDefinition<TestJobParameters> myDefinition;

	@BeforeEach
	public void before() {
		myDefinition = createJobDefinition();
	}

	@Test
	public void createCursorStep1() {
		// setup
		JobWorkNotification workNotification = new JobWorkNotification();
		workNotification.setTargetStepId(STEP_1);

		// execute
		JobWorkCursor<TestJobParameters, ?, ?> cursor = JobWorkCursor.fromJobDefinitionAndWorkNotification(myDefinition, workNotification);

		// verify
		assertCursor(cursor, true, false, STEP_1, STEP_2);
	}

	@Test
	public void createCursorStep2() {
		// setup
		JobWorkNotification workNotification = new JobWorkNotification();
		workNotification.setTargetStepId(STEP_2);

		// execute
		JobWorkCursor<TestJobParameters, ?, ?> cursor = JobWorkCursor.fromJobDefinitionAndWorkNotification(myDefinition, workNotification);

		// verify
		assertCursor(cursor, false, false, STEP_2, STEP_3);
	}

	@Test
	public void createCursorStep3() {
		// setup
		JobWorkNotification workNotification = new JobWorkNotification();
		workNotification.setTargetStepId(STEP_3);

		// execute
		JobWorkCursor<TestJobParameters, ?, ?> cursor = JobWorkCursor.fromJobDefinitionAndWorkNotification(myDefinition, workNotification);

		// verify
		assertCursor(cursor, false, true, STEP_3, null);
	}

	@Test
	public void unknownStep() {
		// setup
		JobWorkNotification workNotification = new JobWorkNotification();
		String targetStepId = "Made a searching and fearless moral inventory of ourselves";
		workNotification.setTargetStepId(targetStepId);

		// execute
		try {
			JobWorkCursor.fromJobDefinitionAndWorkNotification(myDefinition, workNotification);

			// verify
			fail();
		} catch (InternalErrorException e) {
			assertEquals("HAPI-2042: Unknown step[" + targetStepId + "] for job definition ID[JOB_DEFINITION_ID] version[1]", e.getMessage());
		}
	}

	private void assertCursor(JobWorkCursor<TestJobParameters,?,?> theCursor, boolean theExpectedIsFirstStep, boolean theExpectedIsFinalStep, String theExpectedCurrentStep, String theExpectedNextStep) {
		assertEquals(theExpectedIsFirstStep, theCursor.isFirstStep);
		assertEquals(theExpectedIsFinalStep, theCursor.isFinalStep());
		assertEquals(theExpectedCurrentStep, theCursor.currentStep.getStepId());
		if (theExpectedNextStep == null) {
			assertNull(theCursor.nextStep);
		} else {
			assertEquals(theExpectedNextStep, theCursor.nextStep.getStepId());
		}
		assertEquals(myDefinition.getJobDefinitionId(), theCursor.jobDefinition.getJobDefinitionId());
		assertEquals(myDefinition.getJobDefinitionVersion(), theCursor.jobDefinition.getJobDefinitionVersion());
	}
}
