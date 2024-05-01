package ca.uhn.fhir.batch2.model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class WorkChunkStatusEnumTest {
	@ParameterizedTest
	@EnumSource(WorkChunkStatusEnum.class)
	void allStatesExceptCOMPLETEDareIncomplete(WorkChunkStatusEnum theEnum) {
		if (theEnum == WorkChunkStatusEnum.COMPLETED) {
			assertFalse(theEnum.isIncomplete());
		} else {
			assertTrue(theEnum.isIncomplete());
		}
	}

	@ParameterizedTest
	@EnumSource(WorkChunkStatusEnum.class)
	void allowedPriorStates_matchesNextStates(WorkChunkStatusEnum theEnum) {
		Arrays.stream(WorkChunkStatusEnum.values()).forEach(nextPrior->{
			if (nextPrior.getNextStates().contains(theEnum)) {
				assertThat(theEnum.getPriorStates()).as("is prior").contains(nextPrior);
			} else {
				assertThat("is not prior", theEnum.getPriorStates(), not(hasItem(nextPrior)));
			}
		});
	}
}
