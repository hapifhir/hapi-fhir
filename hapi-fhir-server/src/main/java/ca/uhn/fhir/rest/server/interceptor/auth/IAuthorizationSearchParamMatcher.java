package ca.uhn.fhir.rest.server.interceptor.auth;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Adapt the InMemoryMatcher to support authorization filters in {@link FhirQueryRuleImpl}.
 */
public interface IAuthorizationSearchParamMatcher {
	MatchResult match(String theCriteria, IBaseResource theResource);

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
		@Nonnull private final Match myMatch;
		@Nullable private final String myUnsupportedReason;

		public static MatchResult makeMatched() {
			return new MatchResult(Match.MATCH, null);
		}

		public static MatchResult makeUnmatched() {
			return new MatchResult(Match.NO_MATCH, null);
		}

		public static MatchResult makeUnsupported(@Nonnull String theReason) {
			return new MatchResult(Match.UNSUPPORTED, theReason);
		}

		private MatchResult(Match myMatch, String myUnsupportedReason) {
			this.myMatch = myMatch;
			this.myUnsupportedReason = myUnsupportedReason;
		}

		public Match getMatch() {
			return myMatch;
		}

		public String getUnsupportedReason() {
			return myUnsupportedReason;
		}

	}
}
