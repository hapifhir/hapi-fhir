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
package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthorizationSearchParamMatcher;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adapter from {@link SearchParamMatcher} to our authorization version.
 */
public class AuthorizationSearchParamMatcher implements IAuthorizationSearchParamMatcher {
	private static final Logger ourLog = LoggerFactory.getLogger(AuthorizationSearchParamMatcher.class);
	private final SearchParamMatcher mySearchParamMatcher;

	public AuthorizationSearchParamMatcher(SearchParamMatcher mySearchParamMatcher) {
		this.mySearchParamMatcher = mySearchParamMatcher;
	}

	@Override
	public MatchResult match(String theQueryParameters, IBaseResource theResource) {
		try {
			InMemoryMatchResult inMemoryMatchResult = mySearchParamMatcher.match(theQueryParameters, theResource, null);
			if (!inMemoryMatchResult.supported()) {
				return MatchResult.buildUnsupported(inMemoryMatchResult.getUnsupportedReason());
			}
			if (inMemoryMatchResult.matched()) {
				return MatchResult.buildMatched();
			} else {
				return MatchResult.buildUnmatched();
			}
		} catch (MatchUrlService.UnrecognizedSearchParameterException e) {
			// The matcher treats a bad expression as InvalidRequestException because
			// it assumes it is during SearchParameter storage.
			// Instead, we adapt this to UNSUPPORTED during authorization.
			// We may be applying to all types, and this filter won't match.
			ourLog.info("Unsupported filter {} applied to resource: {}", theQueryParameters, e.getMessage());
			return MatchResult.buildUnsupported(e.getMessage());
		}
	}
}
