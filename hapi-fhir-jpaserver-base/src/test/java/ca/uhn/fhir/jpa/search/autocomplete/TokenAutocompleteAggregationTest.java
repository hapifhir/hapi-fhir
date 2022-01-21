package ca.uhn.fhir.jpa.search.autocomplete;

import org.junit.jupiter.api.Test;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.isJson;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.withJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class TokenAutocompleteAggregationTest {

	@Test
	public void testAggregationUsesSPName() {
		String aggJson = new TokenAutocompleteAggregation("combo-code").toJsonAggregation().toString();
		assertThat("terms field is sp", aggJson, isJson(withJsonPath("terms.field", equalTo("sp.combo-code.token.code-system"))));
		assertThat("fetched piece is sp", aggJson, isJson(withJsonPath("aggs.top_tags_hits.top_hits._source.includes[0]", equalTo("sp.combo-code"))));

	}

}
