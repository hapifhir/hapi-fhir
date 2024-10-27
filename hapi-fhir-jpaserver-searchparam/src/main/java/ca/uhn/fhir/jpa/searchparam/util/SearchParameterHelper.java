/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.searchparam.util;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParameterCanonicalizer;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SearchParameterHelper {
	private final SearchParameterCanonicalizer mySearchParameterCanonicalizer;

	public SearchParameterHelper(SearchParameterCanonicalizer theSearchParameterCanonicalizer) {
		mySearchParameterCanonicalizer = theSearchParameterCanonicalizer;
	}

	public Optional<SearchParameterMap> buildSearchParameterMapFromCanonical(IBaseResource theRuntimeSearchParam) {
		RuntimeSearchParam canonicalSearchParam =
				mySearchParameterCanonicalizer.canonicalizeSearchParameter(theRuntimeSearchParam);
		if (canonicalSearchParam == null) {
			return Optional.empty();
		}

		SearchParameterMap retVal = SearchParameterMap.newSynchronous();

		String theCode = canonicalSearchParam.getName();
		List<String> theBases = List.copyOf(canonicalSearchParam.getBase());

		TokenAndListParam codeParam = new TokenAndListParam().addAnd(new TokenParam(theCode));
		TokenAndListParam basesParam = toTokenAndList(theBases);

		retVal.add("code", codeParam);
		retVal.add("base", basesParam);

		return Optional.of(retVal);
	}

	private TokenAndListParam toTokenAndList(List<String> theBases) {
		TokenAndListParam retVal = new TokenAndListParam();

		if (theBases != null) {

			TokenOrListParam tokenOrListParam = new TokenOrListParam();
			retVal.addAnd(tokenOrListParam);

			for (String next : theBases) {
				if (isNotBlank(next)) {
					tokenOrListParam.addOr(new TokenParam(next));
				}
			}
		}

		return retVal.getValuesAsQueryTokens().isEmpty() ? null : retVal;
	}
}
