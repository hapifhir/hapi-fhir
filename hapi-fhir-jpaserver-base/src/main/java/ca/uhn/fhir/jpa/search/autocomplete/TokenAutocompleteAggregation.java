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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.IDX_STRING_TEXT;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.NESTED_SEARCH_PARAM_ROOT;

/**
 * Compose the autocomplete aggregation, and parse the results.
 */
class TokenAutocompleteAggregation {
	/**
	 * Aggregation template json.
	 *
	 * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations.html
	 */
	static final JsonObject AGGREGATION_TEMPLATE =
		new Gson().fromJson("" +
			"         {" +
			"            \"nested\": { \"path\": \"nsp.PLACEHOLDER\" }," +
			"            \"aggs\": {" +
			"                \"search\": {" +
			"                    \"filter\": {" +
			"                        \"bool\": {" +
			"                            \"must\": [" +
			"                                { \"match_bool_prefix\":" +
			"                                  { \"nsp.PLACEHOLDER.string.text\": {" +
			"                                      \"query\": \"Mors\"}" +
			"                                  }" +
			"                                }" +
			"                            ]" +
			"                        }" +
			"                    }," +
			"                    \"aggs\": {" +
			"                        \"group_by_token\": {" +
			"                            \"terms\": {" +
			"                                \"field\": \"nsp.PLACEHOLDER.token.code-system\"," +
			"                                \"size\": 30," +
			"                                \"min_doc_count\": 1," +
			"                                \"shard_min_doc_count\": 0," +
			"                                \"show_term_doc_count_error\": false" +
			"                            }," +
			"                            \"aggs\": {" +
			"                                \"top_tags_hits\": {" +
			"                                    \"top_hits\": {" +
			"                                        \"_source\": {" +
			"                                            \"includes\": [ \"nsp.PLACEHOLDER\" ]" +
			"                                        }," +
			"                                        \"size\": 1" +
			"                                    }" +
			"                                }" +
			"                            }" +
			"                        }" +
			"                    }" +
			"                }" +
			"            }" +
			"        }", JsonObject.class);

	static final Configuration configuration = Configuration
		.builder()
		.mappingProvider(new GsonMappingProvider())
		.jsonProvider(new GsonJsonProvider())
		.build();
	static final ParseContext parseContext = JsonPath.using(configuration);

	private final String mySpName;
	private final int myCount;
	private final JsonObject mySearch;

	public TokenAutocompleteAggregation(String theSpName, int theCount, String theSearchText, String theSearchModifier) {
		Validate.notEmpty(theSpName);
		Validate.isTrue(theCount>0, "count must be positive");
		Validate.isTrue("text".equalsIgnoreCase(theSearchModifier) || "".equals(theSearchModifier) || theSearchModifier == null, "Unsupported search modifier " + theSearchModifier);
		mySpName = theSpName;
		myCount = theCount;
		mySearch = makeSearch(theSearchText, theSearchModifier);
	}

	private JsonObject makeSearch(String theSearchText, String theSearchModifier) {
		theSearchText = StringUtils.defaultString(theSearchText);
		theSearchModifier = StringUtils.defaultString(theSearchModifier);

		if (StringUtils.isEmpty(theSearchText)) {
			return RawElasticJsonBuilder.makeMatchAllPredicate();
		} else if ("text".equalsIgnoreCase(theSearchModifier)) {
			return RawElasticJsonBuilder.makeMatchBoolPrefixPredicate(NESTED_SEARCH_PARAM_ROOT + "." + mySpName + ".string." + IDX_STRING_TEXT, theSearchText);
		} else {
			return RawElasticJsonBuilder.makeWildcardPredicate(NESTED_SEARCH_PARAM_ROOT + "." + mySpName + ".token.code", theSearchText + "*");
		}
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
		String nestedSearchParamPath = NESTED_SEARCH_PARAM_ROOT + "." + mySpName;
		documentContext.set("nested.path", nestedSearchParamPath);
		documentContext.set("aggs.search.filter.bool.must[0]", mySearch);
		documentContext.set("aggs.search.aggs.group_by_token.terms.field", NESTED_SEARCH_PARAM_ROOT + "." + mySpName + ".token" + ".code-system");
		documentContext.set("aggs.search.aggs.group_by_token.terms.size", myCount);
		documentContext.set("aggs.search.aggs.group_by_token.aggs.top_tags_hits.top_hits._source.includes[0]", nestedSearchParamPath);
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

		JsonArray buckets = theAggregationResult
			.getAsJsonObject("search")
			.getAsJsonObject("group_by_token")
			.getAsJsonArray("buckets");

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
		String displayText = documentContext.read("top_tags_hits.hits.hits[0]._source.string.text", String.class);

		return new TokenAutocompleteHit(bucketKey,displayText);
	}

}
