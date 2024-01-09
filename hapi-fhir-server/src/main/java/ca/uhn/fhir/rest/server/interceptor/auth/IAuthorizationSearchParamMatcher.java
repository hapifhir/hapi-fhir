/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server.interceptor.auth;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Adapt the InMemoryMatcher to support authorization filters in {@link FhirQueryRuleTester}.
 * Exists because filters may be applied to resources that don't support all paramters, and UNSUPPORTED
 * has a different meaning during authorization.
 */
public interface IAuthorizationSearchParamMatcher {
	/**
	 * Calculate if the resource would match the fhir query parameters.
	 * @param theQueryParameters e.g. "category=laboratory"
	 * @param theResource the target of the comparison
	 */
	MatchResult match(String theQueryParameters, IBaseResource theResource);

	/**
	 * Match outcomes.
	 */
	enum Match {
		MATCH,
		NO_MATCH,
		/** Used for contexts without matcher infrastructure like hybrid providers */
		UNSUPPORTED
	}

	class MatchResult {
		// fake record pattern
		/** match result */
		@Nonnull
		public final Match match;
		/** the reason for the UNSUPPORTED result */
		@Nullable
		public final String unsupportedReason;

		public static MatchResult buildMatched() {
			return new MatchResult(Match.MATCH, null);
		}

		public static MatchResult buildUnmatched() {
			return new MatchResult(Match.NO_MATCH, null);
		}

		public static MatchResult buildUnsupported(@Nonnull String theReason) {
			return new MatchResult(Match.UNSUPPORTED, theReason);
		}

		private MatchResult(Match myMatch, String myUnsupportedReason) {
			this.match = myMatch;
			this.unsupportedReason = myUnsupportedReason;
		}
	}
}
