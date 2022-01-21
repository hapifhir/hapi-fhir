package ca.uhn.fhir.jpa.search.autocomplete;

import ca.uhn.fhir.jpa.dao.search.ExtendedLuceneClauseBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
public class TokenAutocompleteAggregation {
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

	final String mySpName;

	// wipmb
	public TokenAutocompleteAggregation(String theSpName) {
		mySpName = theSpName;
	}

	public JsonObject toJsonAggregation() {
		JsonObject result = AGGREGATION_TEMPLATE.deepCopy();
		DocumentContext documentContext = parseContext.parse(result);
		documentContext.set("terms.field", ExtendedLuceneClauseBuilder.getTokenSystemCodeFieldPath(mySpName));
		documentContext.set("aggs." + NESTED_AGG_NAME + ".top_hits._source.includes[0]","sp." + mySpName);
		return result;
	}

	@Nonnull
	public List<AutocompleteResultEntry> extractResults(@Nonnull JsonObject theAggregationResult) {
		Validate.notNull(theAggregationResult);

		JsonArray buckets = theAggregationResult.getAsJsonArray("buckets");
		List<AutocompleteResultEntry> result = StreamSupport.stream(buckets.spliterator(), false)
			.map(b-> bucketToEntry((JsonObject) b))
			.collect(Collectors.toList());

		return result;
	}

	/**
	 * Extract the results from the aggregation bucket
	 *
	 * @param theBucketJson
	 * @return
	 */
	@Nonnull
	AutocompleteResultEntry bucketToEntry(JsonObject theBucketJson) {
		// wrap the JsonObject for JSONPath.
		DocumentContext documentContext = parseContext.parse(theBucketJson);

		// jsonpath caches the paths, so keep it simple
		String bucketKey = documentContext.read("key", String.class);

		JsonObject spRootNode = documentContext.read(NESTED_AGG_NAME + ".hits.hits[0]._source.sp");
		// MB - JsonPath doesn't have placeholders, and I don't want to screw-up quoting mySpName, so read it explicitly
		JsonObject spNode = spRootNode.getAsJsonObject(mySpName);
		String displayText = parseContext.parse(spNode).read("string.exact", String.class);

		return new AutocompleteResultEntry(bucketKey,displayText);
	}

}
