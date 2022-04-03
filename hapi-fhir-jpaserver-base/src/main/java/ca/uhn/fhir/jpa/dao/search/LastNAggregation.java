package ca.uhn.fhir.jpa.dao.search;

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

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.SEARCH_PARAM_ROOT;

/**
 * Builds lastN aggregation, and parse the results
 */
public class LastNAggregation {
	static final String SP_SUBJECT = SEARCH_PARAM_ROOT + ".subject.reference.value";
	private static final String SP_CODE_TOKEN_CODE_AND_SYSTEM = SEARCH_PARAM_ROOT + ".code.token.code-system";
	private static final String SP_DATE_DT_UPPER = SEARCH_PARAM_ROOT + ".date.dt.upper";
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
			"{" +
				"   \"terms\":{" +
				"      \"field\":\"" + SP_CODE_TOKEN_CODE_AND_SYSTEM + "\"," +
				"      \"size\":10000," +
				"      \"min_doc_count\":1" +
				"   }," +
				"   \"aggs\":{" +
				"      \"" + MOST_RECENT_EFFECTIVE_SUB_AGGREGATION + "\":{" +
				"         \"top_hits\":{" +
				"            \"size\":" + myLastNMax + "," +
				"            \"sort\":[" +
				"               {" +
				"                  \"" + SP_DATE_DT_UPPER + "\":{" +
				"                     \"order\":\"desc\"" +
				"                  }" +
				"               }" +
				"            ]," +
				"            \"_source\":[" +
				"               \"myId\"" +
				"            ]" +
				"         }" +
				"      }" +
				"   }" +
				"}", JsonObject.class);
		if (myAggregateOnSubject) {
			lastNAggregation = myJsonParser.fromJson(
				"{" +
					"  \"terms\": {" +
					"    \"field\": \"" + SP_SUBJECT + "\"," +
					"    \"size\": 10000," +
					"    \"min_doc_count\": 1" +
					"  }," +
					"  \"aggs\": {" +
					"    \"" + GROUP_BY_CODE_SYSTEM_SUB_AGGREGATION + "\": " + myJsonParser.toJson(lastNAggregation) + "" +
					"  }" +
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
