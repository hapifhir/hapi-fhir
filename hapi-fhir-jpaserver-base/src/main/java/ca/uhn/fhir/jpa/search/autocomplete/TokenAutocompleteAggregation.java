package ca.uhn.fhir.jpa.search.autocomplete;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.dao.search.ExtendedLuceneClauseBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Compose the autocomplete aggregation, and parse the results.
 */
class TokenAutocompleteAggregation {
	static final String NESTED_AGG_NAME = "nestedTopNAgg";
	/**
	 * Aggregation template json.
	 *
	 * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations.html
	 */
	static final JsonObject AGGREGATION_TEMPLATE =
		new Gson().fromJson("{\n" +
			"            \"terms\": {\n" +
			"                \"field\": \"sp.TEMPLATE_DUMMY.token.code-system\",\n" +
			"                \"size\": 30,\n" +
			"                \"min_doc_count\": 1\n" +
			"            },\n" +
			"            \"aggs\": {\n" +
			"                \"" + NESTED_AGG_NAME + "\": {\n" +
			"                    \"top_hits\": {\n" +
			"                        \"_source\": {\n" +
			"                            \"includes\": [ \"sp.TEMPLATE_DUMMY\" ]\n" +
			"                        },\n" +
			"                        \"size\": 1\n" +
			"                    }\n" +
			"                }\n" +
			"        }}", JsonObject.class);

	static final Configuration configuration = Configuration
		.builder()
		.mappingProvider(new GsonMappingProvider())
		.jsonProvider(new GsonJsonProvider())
		.build();
	static final ParseContext parseContext = JsonPath.using(configuration);

	private final String mySpName;
	private final int myCount;

	public TokenAutocompleteAggregation(String theSpName, int theCount) {
		Validate.notEmpty(theSpName);
		Validate.isTrue(theCount>0, "count must be positive");
		mySpName = theSpName;
		myCount = theCount;
	}

	/**
	 * Generate the JSON for the ES aggregation query.
	 *
	 * @return the JSON
	 */
	JsonObject toJsonAggregation() {
		// clone and modify the template with the actual field names.
		JsonObject result = AGGREGATION_TEMPLATE.deepCopy();
		DocumentContext documentContext = parseContext.parse(result);
		documentContext.set("terms.field", ExtendedLuceneClauseBuilder.getTokenSystemCodeFieldPath(mySpName));
		documentContext.set("terms.size", myCount);
		documentContext.set("aggs." + NESTED_AGG_NAME + ".top_hits._source.includes[0]","sp." + mySpName);
		return result;
	}

	/**
	 * Extract hits from the aggregation buckets
	 *
	 * @param theAggregationResult the ES aggregation JSON
	 * @return A list of TokenAutocompleteHit, one per aggregation bucket.
	 */
	@Nonnull
	List<TokenAutocompleteHit> extractResults(@Nonnull JsonObject theAggregationResult) {
		Validate.notNull(theAggregationResult);

		JsonArray buckets = theAggregationResult.getAsJsonArray("buckets");
		List<TokenAutocompleteHit> result = StreamSupport.stream(buckets.spliterator(), false)
			.map(b-> bucketToEntry((JsonObject) b))
			.collect(Collectors.toList());

		return result;
	}

	/**
	 * Extract the result from the top-n aggregation bucket.
	 * The inner bucket contains matching hits
	 */
	@Nonnull
	TokenAutocompleteHit bucketToEntry(JsonObject theBucketJson) {
		// wrap the JsonObject for JSONPath.
		DocumentContext documentContext = parseContext.parse(theBucketJson);

	   // The outer bucket is keyed by the token value (i.e. "system|code").
		String bucketKey = documentContext.read("key", String.class);

		// The inner bucket has a hits array, and we only need the first.
		JsonObject spRootNode = documentContext.read(NESTED_AGG_NAME + ".hits.hits[0]._source.sp");
		// MB - JsonPath doesn't have placeholders, and I don't want to screw-up quoting mySpName, so read the JsonObject explicitly
		JsonObject spNode = spRootNode.getAsJsonObject(mySpName);
		JsonElement exactNode = spNode.get("string").getAsJsonObject().get("exact");
		String displayText;
		if (exactNode.isJsonArray()) {
			displayText = exactNode.getAsJsonArray().get(0).getAsString();
		} else {
			displayText = exactNode.getAsString();
		}

		return new TokenAutocompleteHit(bucketKey,displayText);
	}

}
