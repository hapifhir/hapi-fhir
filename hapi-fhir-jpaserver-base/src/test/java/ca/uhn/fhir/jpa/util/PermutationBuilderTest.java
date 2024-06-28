package ca.uhn.fhir.jpa.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PermutationBuilderTest {

	@Test
	public void testCalculatePermutations() {
		List<List<String>> input = List.of(
			List.of("A0", "A1"),
			List.of("B0", "B1", "B2"),
			List.of("C0", "C1")
		);

		List<List<String>> output = PermutationBuilder.calculatePermutations(input);
		assertThat(output).containsExactlyInAnyOrder(
			List.of("A0", "B0", "C0"),
			List.of("A1", "B0", "C0"),
			List.of("A0", "B1", "C0"),
			List.of("A1", "B1", "C0"),
			List.of("A0", "B2", "C0"),
			List.of("A1", "B2", "C0"),
			List.of("A0", "B0", "C1"),
			List.of("A1", "B0", "C1"),
			List.of("A0", "B1", "C1"),
			List.of("A1", "B1", "C1"),
			List.of("A0", "B2", "C1"),
			List.of("A1", "B2", "C1")
		);
	}


	@Test
	public void testCalculatePermutationCount() {
		List<List<String>> input = List.of(
			List.of("A0", "A1"),
			List.of("B0", "B1", "B2"),
			List.of("C0", "C1")
		);
		assertEquals(12, PermutationBuilder.calculatePermutationCount(input));
	}

	@Test
	public void testCalculatePermutationCountEmpty() {
		List<List<String>> input = List.of();
		assertEquals(0, PermutationBuilder.calculatePermutationCount(input));
	}

	@Test
	public void testCalculatePermutationCountOverflow() {
		List<Integer> ranges = IntStream.range(0, 10001).boxed().toList();
		List<List<Integer>> input = IntStream.range(0, 20).boxed().map(t -> ranges).toList();
		assertThrows(ArithmeticException.class, () -> PermutationBuilder.calculatePermutationCount(input));
	}

}
