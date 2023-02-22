package ca.uhn.fhir.batch2.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StatusEnumTest {
	@Test
	public void testEndedStatuses() {
		assertThat(StatusEnum.getEndedStatuses(), containsInAnyOrder(StatusEnum.COMPLETED, StatusEnum.FAILED, StatusEnum.CANCELLED, StatusEnum.ERRORED));
	}
	@Test
	public void testNotEndedStatuses() {
		assertThat(StatusEnum.getNotEndedStatuses(), containsInAnyOrder(StatusEnum.QUEUED, StatusEnum.IN_PROGRESS, StatusEnum.FINALIZE));
	}

	@ParameterizedTest
	@CsvSource({
		"QUEUED, QUEUED, true",
		"QUEUED, IN_PROGRESS, true",
		"QUEUED, COMPLETED, true",
		"QUEUED, CANCELLED, true",
		"QUEUED, ERRORED, true",
		"QUEUED, FAILED, true",

		"IN_PROGRESS, QUEUED, false",
		"IN_PROGRESS, IN_PROGRESS, true",
		"IN_PROGRESS, COMPLETED, true",
		"IN_PROGRESS, CANCELLED, true",
		"IN_PROGRESS, ERRORED, true",
		"IN_PROGRESS, FAILED, true",

		"COMPLETED, QUEUED, false",
		"COMPLETED, IN_PROGRESS, false",
		"COMPLETED, COMPLETED, true",
		"COMPLETED, CANCELLED, false",
		"COMPLETED, ERRORED, false",
		"COMPLETED, FAILED, false",

		"CANCELLED, QUEUED, false",
		"CANCELLED, IN_PROGRESS, false",
		"CANCELLED, COMPLETED, false",
		"CANCELLED, CANCELLED, true",
		"CANCELLED, ERRORED, false",
		"CANCELLED, FAILED, false",

		"ERRORED, QUEUED, false",
		"ERRORED, IN_PROGRESS, false",
		"ERRORED, COMPLETED, true",
		"ERRORED, CANCELLED, true",
		"ERRORED, ERRORED, true",
		"ERRORED, FAILED, true",

		"FAILED, QUEUED, false",
		"FAILED, IN_PROGRESS, false",
		"FAILED, COMPLETED, false",
		"FAILED, CANCELLED, false",
		"FAILED, ERRORED, false",
		"FAILED, FAILED, true",
		"FINALIZE, COMPLETED, true",
		"FINALIZE, IN_PROGRESS, false",
		"FINALIZE, QUEUED, false",
		"FINALIZE, FAILED, true",
		"FINALIZE, ERRORED, true",
	})
	public void testStateTransition(StatusEnum origStatus, StatusEnum newStatus, boolean expected) {
		assertEquals(expected, StatusEnum.isLegalStateTransition(origStatus, newStatus));
		if (expected) {
			assertThat(StatusEnum.ourFromStates.get(newStatus), hasItem(origStatus));
			assertThat(StatusEnum.ourToStates.get(origStatus), hasItem(newStatus));
		} else {
			assertThat(StatusEnum.ourFromStates.get(newStatus), not(hasItem(origStatus)));
			assertThat(StatusEnum.ourToStates.get(origStatus), not(hasItem(newStatus)));
		}
	}

	@ParameterizedTest
	@EnumSource(StatusEnum.class)
	public void testCancellableStates(StatusEnum theState) {
		assertEquals(StatusEnum.ourFromStates.get(StatusEnum.CANCELLED).contains(theState), theState.isCancellable()
			|| theState == StatusEnum.CANCELLED); // hack: isLegalStateTransition() always returns true for self-transition
	}

	@Test
	public void testEnumSize() {
		assertEquals(7, StatusEnum.values().length, "Update testStateTransition() with new cases");
	}
}
