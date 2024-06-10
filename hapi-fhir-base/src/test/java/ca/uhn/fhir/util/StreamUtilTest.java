package ca.uhn.fhir.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class StreamUtilTest {

	@ParameterizedTest
	@MethodSource("streamPartitionTestCases")
	void testStreamPartitionBy4(String theCase, List<Integer> theInput, List<List<Integer>> theOutput) {
		List<List<Integer>> result = StreamUtil.partition(theInput.stream(), 4).toList();

		assertThat(result).as(theCase).isEqualTo(theOutput);
	}

	static Object[][] streamPartitionTestCases() {
		return new Object[][]{
			{
				"empty list produces empty stream",
				List.of(),
				List.of()
			},
			{
				"short list produces single chunk",
				List.of(1, 2, 3),
				List.of(List.of(1, 2, 3))
			},
			{
				"longer list produces several chunks",
				List.of(1, 2, 3, 1, 2, 3, 1, 2, 3),
				List.of(List.of(1, 2, 3, 1), List.of(2, 3, 1, 2), List.of(3))
			},
			{
				"even size produces even chunks",
				List.of(1, 2, 3,4,5,6,7,8),
				List.of(List.of(1, 2, 3,4), List.of(5,6,7,8))
			},
		};
	}

	@Test
	void testStreamPartitionClosesOriginalStream() {
	    // given
		AtomicBoolean closed = new AtomicBoolean(false);
		Stream<Integer> baseStream = Stream.of(1, 2, 3).onClose(()->closed.set(true));

		// when
		StreamUtil.partition(baseStream, 2).close();

		// then
			assertThat(closed.get()).as("partition closed underlying stream").isTrue();
	}


}
