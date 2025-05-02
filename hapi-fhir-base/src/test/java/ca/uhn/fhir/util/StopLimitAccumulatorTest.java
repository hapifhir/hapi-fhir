package ca.uhn.fhir.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StopLimitAccumulatorTest {

	@Test
	void testFromStreamAndLimit_withNoTruncation() {
		// setup
		Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5);
		int limit = 5;

		// execute
		StopLimitAccumulator<Integer> accumulator = StopLimitAccumulator.fromStreamAndLimit(stream, limit);

		// verify
		assertFalse(accumulator.isTruncated(), "The result should not be truncated");
		assertEquals(List.of(1, 2, 3, 4, 5), accumulator.getItemList(), "The list should contain all items within the limit");
	}

	@Test
	void testFromStreamAndLimit_withTruncation() {
		// setup
		Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6, 7);
		int limit = 5;

		// execute
		StopLimitAccumulator<Integer> accumulator = StopLimitAccumulator.fromStreamAndLimit(stream, limit);

		// verify
		assertTrue(accumulator.isTruncated(), "The result should be truncated");
		assertEquals(List.of(1, 2, 3, 4, 5), accumulator.getItemList(), "The list should contain only the items within the limit");
	}

	@Test
	void testFromStreamAndLimit_withEmptyStream() {
		// setup
		Stream<Integer> stream = Stream.empty();
		int limit = 5;

		// execute
		StopLimitAccumulator<Integer> accumulator = StopLimitAccumulator.fromStreamAndLimit(stream, limit);

		// verify
		assertFalse(accumulator.isTruncated(), "The result should not be truncated for an empty stream");
		assertTrue(accumulator.getItemList().isEmpty(), "The list should be empty");
	}

	@Test
	void testImmutabilityOfItemList() {
		// setup
		Stream<Integer> stream = Stream.of(1, 2, 3);
		int limit = 3;

		StopLimitAccumulator<Integer> accumulator = StopLimitAccumulator.fromStreamAndLimit(stream, limit);

		// execute and Assert
		List<Integer> itemList = accumulator.getItemList();
		assertThrows(UnsupportedOperationException.class, () -> itemList.add(4), "The list should be immutable");
	}
}
