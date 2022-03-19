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
