package ca.uhn.fhir.jpa.searchparam.extractor;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.*;

class BaseSearchParamExtractorTest {

	@Test
	public void testSplitPathsR4() {
		List<String> tokens = Arrays.asList(BaseSearchParamExtractor.splitPathsR4("  aaa | bbb + '|' |   ccc  ddd  "));
		assertThat(tokens, contains("aaa", "bbb + '|'", "ccc  ddd"));

	}
}
