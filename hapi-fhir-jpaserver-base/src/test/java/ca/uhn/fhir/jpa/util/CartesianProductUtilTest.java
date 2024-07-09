package ca.uhn.fhir.jpa.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartesianProductUtilTest {

	@Test
	public void testCalculateCartesianProductSize() {
		List<List<String>> input = List.of(
			List.of("A0", "A1"),
			List.of("B0", "B1", "B2"),
			List.of("C0", "C1")
		);
		assertEquals(12, CartesianProductUtil.calculateCartesianProductSize(input));
	}

	@Test
	public void testCalculateCartesianProductSizeEmpty() {
		List<List<String>> input = List.of();
		assertEquals(0, CartesianProductUtil.calculateCartesianProductSize(input));
	}

	@Test
	public void testCalculateCartesianProductSizeOverflow() {
		List<Integer> ranges = IntStream.range(0, 10001).boxed().toList();
		List<List<Integer>> input = IntStream.range(0, 20).boxed().map(t -> ranges).toList();
		assertThrows(ArithmeticException.class, () -> CartesianProductUtil.calculateCartesianProductSize(input));
	}

}
