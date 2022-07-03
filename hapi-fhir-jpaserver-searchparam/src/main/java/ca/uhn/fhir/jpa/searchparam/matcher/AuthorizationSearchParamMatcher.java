package ca.uhn.fhir.jpa.searchparam.matcher;

/*-
 * #%L
 * HAPI FHIR Search Parameters
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

import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthorizationSearchParamMatcher;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class AuthorizationSearchParamMatcher implements IAuthorizationSearchParamMatcher {
	private final SearchParamMatcher mySearchParamMatcher;

	public AuthorizationSearchParamMatcher(SearchParamMatcher mySearchParamMatcher) {
		this.mySearchParamMatcher = mySearchParamMatcher;
	}

	@Override
	public MatchResult match(String theCriteria, IBaseResource theResource) {
		try {
			InMemoryMatchResult inMemoryMatchResult = mySearchParamMatcher.match(theCriteria, theResource, null);
			if (!inMemoryMatchResult.supported()) {
				return new MatchResult(Match.UNSUPPORTED, inMemoryMatchResult.getUnsupportedReason());
			}
			if (inMemoryMatchResult.matched()) {
				return new MatchResult(Match.MATCH, null);
			} else {
				return new MatchResult(Match.NO_MATCH, null);
			}
		} catch (MatchUrlService.UnrecognizedSearchParameterException e) {
			// wipmb revisit this design
			// The matcher treats a bad expression as InvalidRequestException because it assumes it is during SearchParameter storage.
			// Instead, we adapt this to UNSUPPORTED during authorization.  We may be applying to all types, and this filter won't match.
			return new MatchResult(Match.UNSUPPORTED, e.getMessage());
		}
	}
}
