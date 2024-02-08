package ca.uhn.fhir.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.annotation.Nonnull;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

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
		assertThat(myConsumerCaptor.getAllValues().get(0)).isEqualTo(newIntRangeList(0, 10));
		assertThat(myConsumerCaptor.getAllValues().get(1)).isEqualTo(newIntRangeList(10, 20));
		assertThat(myConsumerCaptor.getAllValues().get(2)).isEqualTo(newIntRangeList(20, 30));
		assertThat(myConsumerCaptor.getAllValues().get(3)).isEqualTo(newIntRangeList(30, 35));

	}

	@Nonnull
	private static List<Integer> newIntRangeList(int startInclusive, int endExclusive) {
		List<Integer> input = IntStream.range(startInclusive, endExclusive).boxed().toList();
		return input;
	}

}
