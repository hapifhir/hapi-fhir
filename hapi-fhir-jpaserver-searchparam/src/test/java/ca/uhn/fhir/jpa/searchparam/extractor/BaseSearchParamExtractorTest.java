package ca.uhn.fhir.jpa.searchparam.extractor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class BaseSearchParamExtractorTest {

    @Test
    public void testSplitPathsR4() {
        List<String> tokens =
                Arrays.asList(
                        BaseSearchParamExtractor.splitPathsR4("  aaa | bbb + '|' |   ccc  ddd  "));
        assertThat(tokens, contains("aaa", "bbb + '|'", "ccc  ddd"));
    }
}
