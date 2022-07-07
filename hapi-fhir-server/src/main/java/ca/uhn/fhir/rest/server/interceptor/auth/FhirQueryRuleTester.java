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

public class FhirQueryRuleTester implements IAuthRuleTester {
	private final String myType;
	private final String myFilter;

	public FhirQueryRuleTester(String theType, String theFilter) {
		myType = theType;
		myFilter = theFilter;
	}

	@Override
	public boolean matches(RuleTestRequest theRuleTestRequest) {
		// wipmb placeholder until we get to writes
		return true;
	}

	@Override
	public boolean matchesOutput(RuleTestRequest theRuleTestRequest) {

		// look for a matcher
		IAuthorizationSearchParamMatcher matcher = theRuleTestRequest.ruleApplier.getSearchParamMatcher();
		if (matcher == null) {
			return false;
		}

		// wipmb myType, or target type?  Is this just query filter, or do we bring * into it?
		IAuthorizationSearchParamMatcher.MatchResult mr = matcher.match(myType + "?" + myFilter, theRuleTestRequest.resource);

		switch (mr.getMatch()) {
			case MATCH:
				return true;
			case UNSUPPORTED:
				theRuleTestRequest.ruleApplier.getTroubleshootingLog().warn("Unsupported matcher expression {}: {}.", myFilter, mr.getUnsupportedReason());
				// unsupported doesn't match unless this is a deny request, and we need to be safe!
				return (theRuleTestRequest.mode == PolicyEnum.DENY);
			case NO_MATCH:
			default:
				return false;
		}
	}
}
