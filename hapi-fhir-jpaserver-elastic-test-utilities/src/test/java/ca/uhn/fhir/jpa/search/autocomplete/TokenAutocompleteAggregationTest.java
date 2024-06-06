package ca.uhn.fhir.jpa.search.autocomplete;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

			assertThatJson(myAggJson)
				.inPath("nested.path")
				.isEqualTo("nsp.combo-code");

			assertThatJson(myAggJson)
				.inPath("aggs.search.aggs.group_by_token.terms.field")
				.isEqualTo("nsp.combo-code.token.code-system");

			assertThatJson(myAggJson)
				.inPath("aggs.search.aggs.group_by_token.aggs.top_tags_hits.top_hits._source.includes[0]")
				.isEqualTo("nsp.combo-code");
		}

		@Test
		public void includesCount() {
			myCode = "combo-code";
			myCount = 77;

			buildAggregation();

			assertThatJson(myAggJson)
				.inPath("aggs.search.aggs.group_by_token.terms.size")
					.isEqualTo(77);
		}

		private void buildAggregation() {
			myAggJson = new TokenAutocompleteAggregation(myCode, myCount, null, null).toJsonAggregation().toString();
		}
	}

	@Nested
	public class ResultExtraction {
		//  Sample result from elastic for Observation.code
		String resultJson = "{ " +
								  "  \"doc_count\": 22770, " +
								  "  \"search\": { " +
								  "    \"doc_count\": 4, " +
								  "    \"group_by_token\": { " +
								  "      \"doc_count_error_upper_bound\": 0, " +
								  "      \"sum_other_doc_count\": 0, " +
								  "      \"buckets\": [ " +
								  "        { " +
								  "          \"key\": \"http://loinc.org|59460-6\", " +
								  "          \"doc_count\": 2, " +
								  "          \"top_tags_hits\": { " +
								  "            \"hits\": { " +
								  "              \"total\": { " +
								  "                \"value\": 2, " +
								  "                \"relation\": \"eq\" " +
								  "              }, " +
								  "              \"max_score\": 4.9845064e-05, " +
								  "              \"hits\": [ " +
								  "                { " +
								  "                  \"_index\": \"resourcetable-000001\", " +
								  "                  \"_type\": \"_doc\", " +
								  "                  \"_id\": \"1405280\", " +
								  "                  \"_nested\": { " +
								  "                    \"field\": \"nsp.code\", " +
								  "                    \"offset\": 0 " +
								  "                  }, " +
								  "                  \"_score\": 4.9845064e-05, " +
								  "                  \"_source\": { " +
								  "                    \"string\": { " +
								  "                      \"text\": \"Fall risk total [Morse Fall Scale]\" " +
								  "                    }, " +
								  "                    \"token\": { " +
								  "                      \"code\": \"59460-6\", " +
								  "                      \"system\": \"http://loinc.org\", " +
								  "                      \"code-system\": \"http://loinc.org|59460-6\" " +
								  "                    } " +
								  "                  } " +
								  "                } " +
								  "              ] " +
								  "            } " +
								  "          } " +
								  "        }, " +
								  "        { " +
								  "          \"key\": \"http://loinc.org|59461-4\", " +
								  "          \"doc_count\": 2, " +
								  "          \"top_tags_hits\": { " +
								  "            \"hits\": { " +
								  "              \"total\": { " +
								  "                \"value\": 2, " +
								  "                \"relation\": \"eq\" " +
								  "              }, " +
								  "              \"max_score\": 4.9845064e-05, " +
								  "              \"hits\": [ " +
								  "                { " +
								  "                  \"_index\": \"resourcetable-000001\", " +
								  "                  \"_type\": \"_doc\", " +
								  "                  \"_id\": \"1405281\", " +
								  "                  \"_nested\": { " +
								  "                    \"field\": \"nsp.code\", " +
								  "                    \"offset\": 0 " +
								  "                  }, " +
								  "                  \"_score\": 4.9845064e-05, " +
								  "                  \"_source\": { " +
								  "                    \"string\": { " +
								  "                      \"text\": \"Fall risk level [Morse Fall Scale]\" " +
								  "                    }, " +
								  "                    \"token\": { " +
								  "                      \"code\": \"59461-4\", " +
								  "                      \"system\": \"http://loinc.org\", " +
								  "                      \"code-system\": \"http://loinc.org|59461-4\" " +
								  "                    } " +
								  "                  } " +
								  "                } " +
								  "              ] " +
								  "            } " +
								  "          } " +
								  "        } " +
								  "      ] " +
								  "    } " +
								  "  } " +
								  "}";
		JsonObject parsedResult = new Gson().fromJson(resultJson, JsonObject.class);
		TokenAutocompleteAggregation myAutocompleteAggregation = new TokenAutocompleteAggregation("code", 22, null, null);

		@Test
		public void testResultExtraction() {

			List<TokenAutocompleteHit> hits = myAutocompleteAggregation.extractResults(parsedResult);

			assertThat(hits).hasSize(2);
		}

		@Test
		public void testBucketExtraction() {
			JsonObject bucket = parsedResult
				.getAsJsonObject("search")
				.getAsJsonObject("group_by_token")
				.getAsJsonArray("buckets")
				.get(0)
				.getAsJsonObject();

			TokenAutocompleteHit entry = myAutocompleteAggregation.bucketToEntry(bucket);
			assertEquals("http://loinc.org|59460-6", entry.mySystemCode);
			assertEquals("Fall risk total [Morse Fall Scale]", entry.myDisplayText);

		}

	}


}
