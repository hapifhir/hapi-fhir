package ca.uhn.fhir.batch2.model;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

class StatusEnumTest {
	@Test
	public void testEndedStatuses() {
		assertThat(StatusEnum.getEndedStatuses(), containsInAnyOrder(StatusEnum.COMPLETED, StatusEnum.FAILED, StatusEnum.CANCELLED, StatusEnum.ERRORED));
	}
	@Test
	public void testNotEndedStatuses() {
		assertThat(StatusEnum.getNotEndedStatuses(), containsInAnyOrder(StatusEnum.QUEUED, StatusEnum.IN_PROGRESS));
	}
}
