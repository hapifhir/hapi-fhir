package ca.uhn.fhir.jpa.dao.index;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Regression tests for {@link DaoSearchParamSynchronizer#subtract(java.util.Collection, java.util.Collection)}.
 * <p>
 * Before the fix, this method called {@code theToSubtract.contains(next)} inside a loop over
 * {@code theSubtractFrom}. When {@code theToSubtract} was an {@code ArrayList} (the typical
 * call site for {@code paramsToAdd} in {@link DaoSearchParamSynchronizer#synchronize}, which
 * passes the existing-params collection as the second argument), the operation was O(N^2).
 * <p>
 * For large code systems (e.g. NDC, ~10^6 {@code ResourceIndexedSearchParam*} rows) this caused
 * multi-hour CPU spins on re-PUT. After the fix, {@code theToSubtract} is wrapped in a HashSet
 * (when not already a Set), giving O(N) overall.
 */
class DaoSearchParamSynchronizerSubtractTest {

	@Test
	void subtract_emptyInput_returnsEmptyList() {
		List<Long> result = DaoSearchParamSynchronizer.subtract(
				Collections.emptyList(), List.of(1L, 2L, 3L));
		assertThat(result).isEmpty();
	}

	@Test
	void subtract_noOverlap_returnsAllOfSubtractFrom() {
		List<Long> subtractFrom = List.of(1L, 2L, 3L);
		List<Long> toSubtract = List.of(10L, 20L, 30L);

		List<Long> result = DaoSearchParamSynchronizer.subtract(subtractFrom, toSubtract);

		assertThat(result).containsExactlyElementsOf(subtractFrom);
	}

	@Test
	void subtract_fullOverlap_returnsEmptyList() {
		List<Long> subtractFrom = List.of(1L, 2L, 3L);
		List<Long> toSubtract = List.of(3L, 2L, 1L);

		List<Long> result = DaoSearchParamSynchronizer.subtract(subtractFrom, toSubtract);

		assertThat(result).isEmpty();
	}

	@Test
	void subtract_partialOverlap_returnsOnlyNonOverlapping() {
		List<Long> subtractFrom = List.of(1L, 2L, 3L, 4L, 5L);
		List<Long> toSubtract = List.of(2L, 4L);

		List<Long> result = DaoSearchParamSynchronizer.subtract(subtractFrom, toSubtract);

		assertThat(result).containsExactly(1L, 3L, 5L);
	}

	@Test
	void subtract_preservesOrderOfSubtractFrom() {
		List<Long> subtractFrom = List.of(5L, 3L, 4L, 1L, 2L);
		List<Long> toSubtract = List.of(3L, 1L);

		List<Long> result = DaoSearchParamSynchronizer.subtract(subtractFrom, toSubtract);

		assertThat(result).containsExactly(5L, 4L, 2L);
	}

	/**
	 * Regression guard for the O(N^2) bug. With the buggy ArrayList.contains() loop, this case
	 * (N=100,000 with the second argument being an ArrayList) was measured at ~42 sec on a
	 * developer machine (Java 21, 4 GB heap). With the fix it runs in well under 100 ms. We
	 * assert under a generous 5 sec ceiling so the test stays stable across CI hardware while
	 * still catching a regression.
	 */
	@Test
	void subtract_largeArrayListInput_completesInLinearTime() {
		final int N = 100_000;
		List<Long> subtractFrom = new ArrayList<>(N);
		List<Long> toSubtractArrayList = new ArrayList<>(N);
		for (long i = 0; i < N; i++) {
			subtractFrom.add(i);
			// 50% overlap: even values are in both lists, odd values are only in subtractFrom.
			if (i % 2 == 0) {
				toSubtractArrayList.add(i);
			} else {
				toSubtractArrayList.add(i + N); // disjoint value
			}
		}

		long startNs = System.nanoTime();
		List<Long> result = DaoSearchParamSynchronizer.subtract(subtractFrom, toSubtractArrayList);
		long elapsedMs = (System.nanoTime() - startNs) / 1_000_000;

		assertThat(result)
				.as("Half the elements (the odd values) should remain")
				.hasSize(N / 2);
		assertThat(elapsedMs)
				.as("subtract took %d ms for N=%d; expected <5000 ms. The O(N^2) bug may have regressed.", elapsedMs, N)
				.isLessThan(5_000);
	}

	@Test
	void subtract_setInput_isHandledCorrectly() {
		List<Long> subtractFrom = List.of(1L, 2L, 3L, 4L, 5L);
		Set<Long> toSubtractSet = new HashSet<>(List.of(2L, 4L));

		List<Long> result = DaoSearchParamSynchronizer.subtract(subtractFrom, toSubtractSet);

		assertThat(result).containsExactly(1L, 3L, 5L);
	}
}