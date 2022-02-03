package ca.uhn.fhir.jpa.dao.search;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.hibernate.search.engine.search.aggregation.AggregationKey;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Builds lastN aggregation, and parse the results
 */
public class LastNAggregation {
	static final String SP_SUBJECT = "sp.subject.reference.value";
	private static final String SP_CODE_TOKEN_CODE_AND_SYSTEM = "sp.code.token.code-system";
	private static final String SP_DATE_DT_UPPER = "sp.date.dt.upper";
	private static final String GROUP_BY_CODE_SYSTEM_SUB_AGGREGATION = "group_by_code_system";
	private static final String MOST_RECENT_EFFECTIVE_SUB_AGGREGATION = "most_recent_effective";

	private final int myLastNMax;
	private final boolean myAggregateOnSubject;
	private final Gson myJsonParser = new Gson();

	public LastNAggregation(int theLastNMax, boolean theAggregateOnSubject) {
		myLastNMax = theLastNMax;
		myAggregateOnSubject = theAggregateOnSubject;
	}

	/**
	 * Aggregation template json.
	 * <p>
	 * https://www.elastic.co/guide/en/elasticsearch/reference/current/search-aggregations.html
	 */
	public JsonObject toAggregation() {
		JsonObject lastNAggregation = myJsonParser.fromJson(
			"{\n" +
				"   \"terms\":{\n" +
				"      \"field\":\"" + SP_CODE_TOKEN_CODE_AND_SYSTEM + "\",\n" +
				"      \"size\":100,\n" +
				"      \"min_doc_count\":1\n" +
				"   },\n" +
				"   \"aggs\":{\n" +
				"      \"" + MOST_RECENT_EFFECTIVE_SUB_AGGREGATION + "\":{\n" +
				"         \"top_hits\":{\n" +
				"            \"size\":" + myLastNMax + ",\n" +
				"            \"sort\":[\n" +
				"               {\n" +
				"                  \"" + SP_DATE_DT_UPPER + "\":{\n" +
				"                     \"order\":\"desc\"\n" +
				"                  }\n" +
				"               }\n" +
				"            ],\n" +
				"            \"_source\":[\n" +
				"               \"myId\"\n" +
				"            ]\n" +
				"         }\n" +
				"      }\n" +
				"   }\n" +
				"}", JsonObject.class);
		if (myAggregateOnSubject) {
			lastNAggregation = myJsonParser.fromJson(
				"{\n" +
					"  \"terms\": {\n" +
					"    \"field\": \"" + SP_SUBJECT + "\",\n" +
					"    \"size\": 100,\n" +
					"    \"min_doc_count\": 1\n" +
					"  },\n" +
					"  \"aggs\": {\n" +
					"    \"" + GROUP_BY_CODE_SYSTEM_SUB_AGGREGATION + "\": " + myJsonParser.toJson(lastNAggregation) + "\n" +
					"  }\n" +
					"}", JsonObject.class);
		}
		return lastNAggregation;
	}

	/**
	 * Parses the JSONObject aggregation result from ES to extract observation resource ids
	 * E.g aggregation result payload
	 * <pre>
	 * {@code
	 * {
	 *   "doc_count_error_upper_bound": 0,
	 *   "sum_other_doc_count": 0,
	 *   "buckets": [
	 *     {
	 *       "key": "http://mycode.com|code0",
	 *       "doc_count": 45,
	 *       "most_recent_effective": {
	 *         "hits": {
	 *           "total": {
	 *             "value": 45,
	 *             "relation": "eq"
	 *           },
	 *           "max_score": null,
	 *           "hits": [
	 *             {
	 *               "_index": "resourcetable-000001",
	 *               "_type": "_doc",
	 *               "_id": "48",
	 *               "_score": null,
	 *               "_source": {
	 *                 "myId": 48
	 *               },
	 *               "sort": [
	 *                 1643673125112
	 *               ]
	 *             }
	 *           ]
	 *         }
	 *       }
	 *     },
	 *     {
	 *       "key": "http://mycode.com|code1",
	 *       "doc_count": 30,
	 *       "most_recent_effective": {
	 *         "hits": {
	 *           "total": {
	 *             "value": 30,
	 *             "relation": "eq"
	 *           },
	 *           "max_score": null,
	 *           "hits": [
	 *             {
	 *               "_index": "resourcetable-000001",
	 *               "_type": "_doc",
	 *               "_id": "58",
	 *               "_score": null,
	 *               "_source": {
	 *                 "myId": 58
	 *               },
	 *               "sort": [
	 *                 1643673125112
	 *               ]
	 *             }
	 *           ]
	 *         }
	 *       }
	 *     }
	 *   ]
	 * }
	 * }
	 * </pre>
	 */
	public List<Long> extractResourceIds(@Nonnull JsonObject theAggregationResult) {
		Stream<JsonObject> resultBuckets = Stream.of(theAggregationResult);

		// was it grouped by subject?
		if (myAggregateOnSubject) {
			resultBuckets = StreamSupport.stream(theAggregationResult.getAsJsonArray("buckets").spliterator(), false)
				.map(bucket -> bucket.getAsJsonObject().getAsJsonObject(GROUP_BY_CODE_SYSTEM_SUB_AGGREGATION));
		}

		return resultBuckets
			.flatMap(grouping -> StreamSupport.stream(grouping.getAsJsonArray("buckets").spliterator(), false))
			.flatMap(bucket -> {
				JsonArray hits = bucket.getAsJsonObject()
					.getAsJsonObject(MOST_RECENT_EFFECTIVE_SUB_AGGREGATION)
					.getAsJsonObject("hits")
					.getAsJsonArray("hits");
				return StreamSupport.stream(hits.spliterator(), false);
			})
			.map(hit -> hit.getAsJsonObject().getAsJsonObject("_source").get("myId").getAsLong())
			.collect(Collectors.toList());
	}
}
