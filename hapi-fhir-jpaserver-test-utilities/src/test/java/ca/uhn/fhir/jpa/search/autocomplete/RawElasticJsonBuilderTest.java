package ca.uhn.fhir.jpa.search.autocomplete;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RawElasticJsonBuilderTest {

	JsonObject myJson;

	@Test
	public void matchAll() {
	    myJson = RawElasticJsonBuilder.makeMatchAllPredicate();

		assertJson("""
			{"match_all": {}}""");
	}

	@Test
	public void wildcard() {
		myJson = RawElasticJsonBuilder.makeWildcardPredicate("a.field", "pattern_text");

		assertJson("""
				{ "wildcard": {  
				   "a.field": { 
				   	"value": "pattern_text"
				}}}""");
	}

	@Test
	public void matchBoolPrefix() {
		myJson = RawElasticJsonBuilder.makeMatchBoolPrefixPredicate("a.field", "query_text");

		assertJson("""
			{ "match_bool_prefix" : {
			  "a.field" : "query_text"
			}}""");
	}

	private void assertJson(String expected) {
		assertEquals(JsonParser.parseString(expected).getAsJsonObject(), myJson);
	}


}
