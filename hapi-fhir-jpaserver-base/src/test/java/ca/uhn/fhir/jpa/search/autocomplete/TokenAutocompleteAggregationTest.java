package ca.uhn.fhir.jpa.search.autocomplete;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.isJson;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.withJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

class TokenAutocompleteAggregationTest {

	@Nested
	public class AggregationQueryContents {
		String myCode;
		int myCount = 30;
		String myAggJson;

		@Test
		public void includesSPName() {
			myCode = "combo-code";

			buildAggregation();
			assertThat("terms field is sp", myAggJson, isJson(withJsonPath("terms.field", equalTo("sp.combo-code.token.code-system"))));
			assertThat("fetched piece is sp", myAggJson, isJson(withJsonPath("aggs.nestedTopNAgg.top_hits._source.includes[0]", equalTo("sp.combo-code"))));
		}

		@Test
		public void includesCount() {
			myCode = "combo-code";
			myCount = 77;

			buildAggregation();
			assertThat("terms field is sp", myAggJson, isJson(withJsonPath("terms.size", equalTo(77))));
		}

		private void buildAggregation() {
			myAggJson = new TokenAutocompleteAggregation(myCode, myCount).toJsonAggregation().toString();
		}
	}

	@Nested
	public class ResultExtraction {
		//  Sample result from elastic for Observation.code
		String resultJson = "" +
			"{ \"doc_count_error_upper_bound\":0,\"sum_other_doc_count\":0," +
			"  \"buckets\": [" +
			"  { \"key\": \"http://loinc.org|88262-1\"," +
			"    \"doc_count\":3," +
			"    \"nestedTopNAgg\": " +
			"      { \"hits\":" +
			"         { \"total\":{\"value\":3,\"relation\":\"eq\"}, \"max_score\":1.0," +
			"           \"hits\":[" +
			"             { \"_index\":\"resourcetable-000001\",\"_type\":\"_doc\",\"_id\":\"13\",\"_score\":1.0," +
			"               \"_source\":{\"sp\":{\"code\":" +
			"                      { \"string\":{\"exact\":\"Gram positive blood culture panel by Probe in Positive blood culture\",\"text\":\"Gram positive blood culture panel by Probe in Positive blood culture\",\"norm\":\"Gram positive blood culture panel by Probe in Positive blood culture\"}," +
			"                        \"token\":{\"code\":\"88262-1\",\"system\":\"http://loinc.org\",\"code-system\":\"http://loinc.org|88262-1\"}}}}}]}}}," +
			// a second result
			"{\"key\":\"http://loinc.org|4544-3\",\"doc_count\":1,\"nestedTopNAgg\":{\"hits\":{\"total\":{\"value\":1,\"relation\":\"eq\"},\"max_score\":1.0,\"hits\":[{\"_index\":\"resourcetable-000001\",\"_type\":\"_doc\",\"_id\":\"12\",\"_score\":1.0,\"_source\":{\"sp\":{\"code\":{\"string\":{\"exact\":\"Hematocrit [Volume Fraction] of Blood by Automated count\",\"text\":\"Hematocrit [Volume Fraction] of Blood by Automated count\",\"norm\":\"Hematocrit [Volume Fraction] of Blood by Automated count\"},\"token\":{\"code\":\"4544-3\",\"system\":\"http://loinc.org\",\"code-system\":\"http://loinc.org|4544-3\"}}}}}]}}}," +
			"{\"key\":\"http://loinc.org|4548-4\",\"doc_count\":1,\"nestedTopNAgg\":{\"hits\":{\"total\":{\"value\":1,\"relation\":\"eq\"},\"max_score\":1.0,\"hits\":[{\"_index\":\"resourcetable-000001\",\"_type\":\"_doc\",\"_id\":\"11\",\"_score\":1.0,\"_source\":{\"sp\":{\"code\":{\"string\":{\"exact\":\"Hemoglobin A1c/Hemoglobin.total in Blood\",\"text\":\"Hemoglobin A1c/Hemoglobin.total in Blood\",\"norm\":\"Hemoglobin A1c/Hemoglobin.total in Blood\"},\"token\":{\"code\":\"4548-4\",\"system\":\"http://loinc.org\",\"code-system\":\"http://loinc.org|4548-4\"}}}}}]}}}" +
			"]}";
		JsonObject parsedResult = new Gson().fromJson(resultJson, JsonObject.class);
		TokenAutocompleteAggregation myAutocompleteAggregation = new TokenAutocompleteAggregation("code", 22);

		@Test
		public void testResultExtraction() {

			List<TokenAutocompleteHit> hits = myAutocompleteAggregation.extractResults(parsedResult);

			assertThat(hits, is(not(empty())));
			assertThat(hits, (hasSize(3)));
		}

		@Test
		public void testBucketExtraction() {
			JsonObject bucket = (JsonObject) parsedResult.getAsJsonArray("buckets").get(0);

			TokenAutocompleteHit entry = myAutocompleteAggregation.bucketToEntry(bucket);
			assertThat(entry.mySystemCode, equalTo("http://loinc.org|88262-1"));
			assertThat(entry.myDisplayText, equalTo("Gram positive blood culture panel by Probe in Positive blood culture"));

		}

		/**
		 * wipmb until we move to nested, we may have multiple Coding in a code.  This is broken.
		 */
		@Test
		public void testMultiValuedBucketExtraction() {
			JsonObject bucket = new Gson().fromJson("{" +
				"  \"key\": \"http://loinc.org|2708-6\"," +
				"  \"doc_count\": 14," +
				"  \"nestedTopNAgg\": {" +
				"    \"hits\": {" +
				"      \"total\": {" +
				"        \"value\": 14," +
				"        \"relation\": \"eq\"" +
				"      }," +
				"      \"max_score\": 1.0000025," +
				"      \"hits\": [" +
				"        {" +
				"          \"_index\": \"resourcetable-000001\"," +
				"          \"_type\": \"_doc\"," +
				"          \"_id\": \"1393284\"," +
				"          \"_score\": 1.0000025," +
				"          \"_source\": {" +
				"            \"sp\": {" +
				"              \"code\": {" +
				"                \"string\": {" +
				"                  \"exact\": [" +
				"                    \"Oxygen saturation in Arterial blood by Pulse oximetry\"," +
				"                    \"Oxygen saturation in Arterial blood\"" +
				"                  ]" +
				"                }," +
				"                \"token\": {" +
				"                  \"code-system\": [" +
				"                    \"http://loinc.org|2708-6\"," +
				"                    \"http://loinc.org|59408-5\"" +
				"                  ]" +
				"                }" +
				"              }" +
				"            }" +
				"          }" +
				"        }" +
				"      ]" +
				"    }" +
				"  }" +
				"}", JsonObject.class);

			TokenAutocompleteHit entry = myAutocompleteAggregation.bucketToEntry(bucket);
			assertThat(entry.mySystemCode, equalTo("http://loinc.org|2708-6"));
			assertThat(entry.myDisplayText, equalTo("Oxygen saturation in Arterial blood by Pulse oximetry"));

		}
	}


}
