package ca.uhn.fhir.util;

import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
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
		return IntStream.range(startInclusive, endExclusive).boxed().toList();
	}

	@Test
	void testIteratorChunk() {
	    // given
		Iterator<Integer> iter = List.of(1,2,3,4,5,6,7,8,9).iterator();
		ArrayList<List<Integer>> result = new ArrayList<>();

	    // when
		new TaskChunker<Integer>().chunk(iter, 3, result::add);

	    // then
	    assertEquals(List.of(List.of(1,2,3), List.of(4,5,6), List.of(7,8,9)), result);
	}

}
