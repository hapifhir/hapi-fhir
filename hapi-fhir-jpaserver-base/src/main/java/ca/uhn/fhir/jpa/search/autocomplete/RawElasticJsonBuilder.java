package ca.uhn.fhir.jpa.search.autocomplete;

import com.google.gson.JsonObject;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;

public class RawElasticJsonBuilder {
	@Nonnull
	static JsonObject makeMatchBoolPrefixPredicate(String theFieldName, String queryText) {

		JsonObject matchBoolBody = new JsonObject();
		matchBoolBody.addProperty(theFieldName, queryText);

		JsonObject predicate = new JsonObject();
		predicate.add("match_bool_prefix", matchBoolBody);
		return predicate;
	}

	public static JsonObject makeWildcardPredicate(String theFieldName, String theQueryText) {
		Validate.notEmpty(theQueryText);

		JsonObject params = new JsonObject();
		params.addProperty("value", theQueryText);

		JsonObject wildcardBody = new JsonObject();
		wildcardBody.add(theFieldName, params);

		JsonObject predicate = new JsonObject();
		predicate.add("wildcard", wildcardBody);
		return predicate;
	}

	@Nonnull
	public static JsonObject makeMatchAllPredicate() {
		JsonObject o = new JsonObject();
		o.add("match_all", new JsonObject());
		return o;
	}
}
