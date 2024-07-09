package ca.uhn.fhir.util;

import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TaskChunkerTest {

	@Mock
	private Consumer<List<Integer>> myConsumer;
	@Captor
	private ArgumentCaptor<List<Integer>> myConsumerCaptor;

	@Test
	public void testChunk() {
		// Setup
		List<Integer> input = newIntRangeList(0, 35);

		// Execute
		new TaskChunker<Integer>().chunk(input, 10, myConsumer);

		// Verify
		verify(myConsumer, times(4)).accept(myConsumerCaptor.capture());
		assertEquals(newIntRangeList(0, 10), myConsumerCaptor.getAllValues().get(0));
		assertEquals(newIntRangeList(10, 20), myConsumerCaptor.getAllValues().get(1));
		assertEquals(newIntRangeList(20, 30), myConsumerCaptor.getAllValues().get(2));
		assertEquals(newIntRangeList(30, 35), myConsumerCaptor.getAllValues().get(3));

	}

	@Nonnull
	private static List<Integer> newIntRangeList(int startInclusive, int endExclusive) {
		List<Integer> input = IntStream.range(startInclusive, endExclusive).boxed().toList();
		return input;
	}

}
