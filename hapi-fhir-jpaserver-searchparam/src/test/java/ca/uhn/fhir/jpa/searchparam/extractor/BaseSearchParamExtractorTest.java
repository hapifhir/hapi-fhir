package ca.uhn.fhir.jpa.searchparam.extractor;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BaseSearchParamExtractorTest {

	@Test
	public void testSplitPathsR4() {
		List<String> tokens = Arrays.asList(BaseSearchParamExtractor.splitPathsR4("  aaa | bbb + '|' |   ccc  ddd  "));
		assertThat(tokens).containsExactly("aaa", "bbb + '|'", "ccc  ddd");

	}
}
