package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Builds JSON aggregation block for ES query
 */
public class LastNAggregationBuilder {
	public static final String SP_CODE_TOKEN_CODE_SYSTEM = "sp.code.token.code-system";
	public static final String SP_DATE_DT_UPPER = "sp.date.dt.upper";
	private final SearchParameterMap myParams;

	public LastNAggregationBuilder(SearchParameterMap theParams) {
		myParams = theParams;
	}

	public JsonObject build() {
		return createGroupByTokenAggregator();
	}

	private JsonObject createGroupByTokenAggregator() {
		// Group by token aggregator
		JsonObject codeSystemTerm = new JsonObject();
		codeSystemTerm.addProperty("field", SP_CODE_TOKEN_CODE_SYSTEM);

		JsonObject mostRecentEffective = createRecentEffectiveAggregator();
		JsonObject subAggregation = new JsonObject();
		subAggregation.add("most_recent_effective", mostRecentEffective);

		JsonObject tokenAggregator = new JsonObject();
		tokenAggregator.add("terms", codeSystemTerm);
		tokenAggregator.add("aggregations", subAggregation);

		return tokenAggregator;
	}

	private JsonObject createRecentEffectiveAggregator() {
		// Top Hits aggregator on effective time
		JsonObject order = new JsonObject();
		order.addProperty("order", "desc");

		JsonObject sortDate = new JsonObject();
		sortDate.add(SP_DATE_DT_UPPER, order);

		JsonArray sortProperties = new JsonArray();
		sortProperties.add(sortDate);

		// We are only looking for observation ids so no need to pull the whole source
		JsonArray includes = new JsonArray();
		includes.add("myId");

		JsonObject topHits = new JsonObject();
		topHits.addProperty("size", getMaxParameter());
		topHits.add("sort", sortProperties);
		topHits.add("_source", includes);

		JsonObject recentEffective = new JsonObject();
		recentEffective.add("top_hits", topHits);
		return recentEffective;
	}

	private int getMaxParameter() {
		if (myParams.getLastNMax() == null) {
			return 1;
		} else {
			return myParams.getLastNMax();
		}
	}
}
