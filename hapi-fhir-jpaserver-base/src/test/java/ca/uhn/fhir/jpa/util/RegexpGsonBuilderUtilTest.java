package ca.uhn.fhir.jpa.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.Test;

import static ca.uhn.fhir.jpa.entity.TermConceptPropertyBinder.CONCEPT_PROPERTY_PREFIX_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegexpGsonBuilderUtilTest {

	private static final String PROP_NAME = "myValueString";

	// a valid regex string which breaks gson
	private static final String GSON_FAILING_REGEX = ".*\\^Donor$";


	@Test
	void testBuildUsingTestedClass() {
		String propertyValue = "propAAA";
		String expectedGson = "{\"regexp\":{\"P:" + PROP_NAME + "\":{\"value\":\"" + propertyValue + "\"}}}";

		assertThat(RegexpGsonBuilderUtil.toGson(
				CONCEPT_PROPERTY_PREFIX_NAME + PROP_NAME, propertyValue).toString()).isEqualTo(expectedGson);
	}

	/**
	 * This test demonstrates that valid regex strings break gson library.
	 * If one day this test fails would mean the library added support for this kind of strings
	 * so the tested class could be removed and the code using it just build a gson.JsonObject from a JSON string
	 */
	@Test
	void testShowGsonLibFailing() {
		String workingRegex = ".*[abc]?jk";
		String workingRegexQuery = "{'regexp':{'P:SYSTEM':{'value':'" + workingRegex + "'}}}";

		JsonObject jsonObj = new Gson().fromJson(workingRegexQuery, JsonObject.class);
		assertFalse(jsonObj.isJsonNull());

		// same json structure fails with some valid regex strings
		String failingRegexQuery = "{'regexp':{'P:SYSTEM':{'value':'" + GSON_FAILING_REGEX + "'}}}";

		JsonSyntaxException thrown = assertThrows(
			JsonSyntaxException.class,
			() -> new Gson().fromJson(failingRegexQuery, JsonObject.class));

		assertTrue(thrown.getCause() instanceof MalformedJsonException);
	}



}
