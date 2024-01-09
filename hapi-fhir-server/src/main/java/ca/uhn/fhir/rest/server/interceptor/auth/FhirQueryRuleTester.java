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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Tester that a resource matches a provided query filter.
 */
public class FhirQueryRuleTester implements IAuthRuleTester {
	private final String myQueryParameters;

	public FhirQueryRuleTester(String theQueryParameters) {
		myQueryParameters = theQueryParameters;
	}

	@Override
	public boolean matches(RuleTestRequest theRuleTestRequest) {
		return checkMatch(theRuleTestRequest);
	}

	@Override
	public boolean matchesOutput(RuleTestRequest theRuleTestRequest) {
		return checkMatch(theRuleTestRequest);
	}

	private boolean checkMatch(RuleTestRequest theRuleTestRequest) {
		// look for a matcher
		IAuthorizationSearchParamMatcher matcher = theRuleTestRequest.ruleApplier.getSearchParamMatcher();
		if (matcher == null) {
			theRuleTestRequest
					.ruleApplier
					.getTroubleshootingLog()
					.warn("No matcher provided.  Can't apply filter permission.");
			return false;
		}

		// this is a bit weird.
		// A tester narrows a rule -- i.e. a rule only applies if the main logic matches AND the testers all match
		// But this rule would have matched without this tester, so true means abstain.
		if (theRuleTestRequest.resource == null) {
			// we aren't looking at a resource yet.  treat as no-op
			return true;
		}

		// we use the target type since the rule might apply to all types, a type set, or instances, and that has
		// already been checked.
		IAuthorizationSearchParamMatcher.MatchResult mr = matcher.match(
				theRuleTestRequest.resource.fhirType() + "?" + myQueryParameters, theRuleTestRequest.resource);

		switch (mr.match) {
			case MATCH:
				return true;
			case UNSUPPORTED:
				theRuleTestRequest
						.ruleApplier
						.getTroubleshootingLog()
						.warn("Unsupported matcher expression {}: {}.", myQueryParameters, mr.unsupportedReason);
				// unsupported doesn't match unless this is a deny request, and we need to be safe!
				return (theRuleTestRequest.mode == PolicyEnum.DENY);
			case NO_MATCH:
			default:
				return false;
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("filter", myQueryParameters)
				.toString();
	}
}
