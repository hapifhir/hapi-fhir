package ca.uhn.fhir.jpa.search.builder;

import com.google.common.collect.Streams;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.*;

class SearchQueryExecutorsTest {

	@Test
	public void adaptFromLongArrayYieldsAllValues() {
		List<Long> listWithValues = Arrays.asList(1L,2L,3L,4L,5L);

		ISearchQueryExecutor queryExecutor = SearchQueryExecutors.from(listWithValues);

		assertThat(drain(queryExecutor), contains(1L,2L,3L,4L,5L));
	}

	@Test
	public void limitedCountDropsTrailingTest() {
		// given
		List<Long> vals = Arrays.asList(1L,2L,3L,4L,5L);
		ISearchQueryExecutor target = SearchQueryExecutors.from(vals);

		ISearchQueryExecutor queryExecutor = SearchQueryExecutors.limited(target, 3);

		assertThat(drain(queryExecutor), contains(1L,2L,3L));
	}

	@Test
	public void limitedCountExhaustsBeforeLimitOkTest() {
		// given
		List<Long> vals = Arrays.asList(1L,2L,3L);
		ISearchQueryExecutor target = SearchQueryExecutors.from(vals);

		ISearchQueryExecutor queryExecutor = SearchQueryExecutors.limited(target, 5);

		assertThat(drain(queryExecutor), contains(1L,2L,3L));
	}


	private List<Long> drain(ISearchQueryExecutor theQueryExecutor) {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(theQueryExecutor, 0), false)
			.collect(Collectors.toList());
	}


}
