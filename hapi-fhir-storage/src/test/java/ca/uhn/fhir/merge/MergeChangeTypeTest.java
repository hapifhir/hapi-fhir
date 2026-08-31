package ca.uhn.fhir.merge;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Created by Claude Opus 4.8
class MergeChangeTypeTest {

	@Test
	void undoOrder_undeletesBeforeUpdatesBeforeDeletes() {
		assertThat(MergeChangeType.DELETE.getUndoOrder())
				.isLessThan(MergeChangeType.UPDATE.getUndoOrder());
		assertThat(MergeChangeType.UPDATE.getUndoOrder())
				.isLessThan(MergeChangeType.CREATE.getUndoOrder());
	}

	@ParameterizedTest
	@EnumSource(MergeChangeType.class)
	void fromCode_roundTripsWithGetCode(MergeChangeType theChangeType) {
		String code = theChangeType.getCode();
		assertThat(code).isNotBlank();
		assertThat(MergeChangeType.fromCode(code)).isEqualTo(theChangeType);
	}

	@Test
	void fromCode_invalidCode_throws() {
		assertThatThrownBy(() -> MergeChangeType.fromCode("bogus"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("Invalid change type");
	}
}
