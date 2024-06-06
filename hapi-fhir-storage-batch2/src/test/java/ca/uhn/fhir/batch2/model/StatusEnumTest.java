package ca.uhn.fhir.batch2.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StatusEnumTest {
	@Test
	public void testEndedStatuses() {
		assertThat(StatusEnum.getEndedStatuses()).containsExactlyInAnyOrder(StatusEnum.COMPLETED, StatusEnum.FAILED, StatusEnum.CANCELLED);
	}
	@Test
	public void testNotEndedStatuses() {
		assertThat(StatusEnum.getNotEndedStatuses()).containsExactlyInAnyOrder(StatusEnum.QUEUED, StatusEnum.IN_PROGRESS, StatusEnum.ERRORED, StatusEnum.FINALIZE);
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
		"COMPLETED, COMPLETED, false",
		"COMPLETED, CANCELLED, false",
		"COMPLETED, ERRORED, false",
		"COMPLETED, FAILED, false",

		"CANCELLED, QUEUED, false",
		"CANCELLED, IN_PROGRESS, false",
		"CANCELLED, COMPLETED, false",
		"CANCELLED, CANCELLED, false",
		"CANCELLED, ERRORED, false",
		"CANCELLED, FAILED, false",

		"ERRORED, QUEUED, false",
		"ERRORED, IN_PROGRESS, true",
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
			assertThat(StatusEnum.ourFromStates.get(newStatus)).contains(origStatus);
			assertThat(StatusEnum.ourToStates.get(origStatus)).contains(newStatus);
		} else {
			assertThat(StatusEnum.ourFromStates.get(newStatus)).doesNotContain(origStatus);
			assertThat(StatusEnum.ourToStates.get(origStatus)).doesNotContain(newStatus);
		}
	}

	@ParameterizedTest
	@EnumSource(StatusEnum.class)
	public void testCancellableStates(StatusEnum theState) {
		assertEquals(StatusEnum.ourFromStates.get(StatusEnum.CANCELLED).contains(theState), theState.isCancellable());
	}

	@Test
	public void testEnumSize() {
		assertThat(StatusEnum.values().length).as("Update testStateTransition() with new cases").isEqualTo(7);
	}
}
