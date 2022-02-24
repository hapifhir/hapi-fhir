package ca.uhn.fhir.jpa.search;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ElasticsearchNestedQueryBuilderUtilTest {

	private static final String NESTED_KEY_PROP_NAME = "myKey";
	private static final String NESTED_VALUE_PROP_NAME = "myValueString";
	private static final String NESTED_OBJECT_NAME = "myProperties";

	// a valid regex string which breaks gson
	private static final String GSON_FAILING_REGEX = ".*\\^Donor$";


	private ElasticsearchNestedQueryBuilderUtil testedUtil;


	@Test
	void testBuildUsingTestedClass() {
		String propertyValue = "propAAA";

		testedUtil = new ElasticsearchNestedQueryBuilderUtil(
			NESTED_OBJECT_NAME, NESTED_KEY_PROP_NAME, propertyValue, NESTED_VALUE_PROP_NAME, GSON_FAILING_REGEX);

		assertFalse(testedUtil.toGson().isJsonNull());

		JsonObject generatedJson = testedUtil.toGson();
		JsonObject nestedJO = generatedJson.getAsJsonObject("nested");
		assertEquals(NESTED_OBJECT_NAME, nestedJO.get("path").getAsString());

		JsonObject queryJO = nestedJO.getAsJsonObject("query");
		JsonObject boolJO = queryJO.getAsJsonObject("bool");
		JsonArray mustJA = boolJO.getAsJsonArray("must");

		JsonObject matchPropKeyJE = (JsonObject) mustJA.get(0);
		JsonObject propKeyJO = matchPropKeyJE.getAsJsonObject("match");
		assertEquals(propertyValue, propKeyJO.get(testedUtil.getNestedPropertyKeyPath()).getAsString());

		JsonObject regexpPropValueJE = (JsonObject) mustJA.get(1);
		JsonObject propValueJO = regexpPropValueJE.getAsJsonObject("regexp");
		assertEquals(GSON_FAILING_REGEX, propValueJO.get(testedUtil.getNestedPropertyValuePath()).getAsString());
	}


	/**
	 * This test demonstrates that valid regex strings break gson library.
	 * If one day this test fails would mean the library added support for this kind of strings
	 * so the tested class could be removed and the code using it just build a gson.JsonObject from a JSON string
	 */
	@Test
	void testShowGsonLibFailing() {
		String workingRegex = ".*[abc]?jk";
		String workingNestedQuery =
			"{'nested': { 'path': 'myProperties', 'query': { 'bool': { 'must': [" +
				"{'match': {'myProperties.myKey': 'propAAA' }}," +
				"{'regexp': {'myProperties.myValueString': '" + workingRegex + "'}}" +
				"]}}}}";

		JsonObject jsonObj = new Gson().fromJson(workingNestedQuery, JsonObject.class);
		assertFalse(jsonObj.isJsonNull());

		// same json structure fails with some valid regex strings
		String nestedQuery =
			"{'nested': { 'path': 'myProperties', 'query': { 'bool': { 'must': [" +
				"{'match': {'myProperties.myKey': 'propAAA' }}," +
				"{'regexp': {'myProperties.myValueString': '" + GSON_FAILING_REGEX + "'}}" +
				"]}}}}";

//		MalformedJsonException thrown = assertThrows(
		JsonSyntaxException thrown = assertThrows(
			JsonSyntaxException.class,
			() -> new Gson().fromJson(nestedQuery, JsonObject.class));

		assertTrue(thrown.getCause() instanceof MalformedJsonException);
	}

}
