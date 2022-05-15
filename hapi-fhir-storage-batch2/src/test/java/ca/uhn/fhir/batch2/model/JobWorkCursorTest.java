package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.batch2.impl.BaseBatch2Test;
import ca.uhn.fhir.batch2.impl.TestJobParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
