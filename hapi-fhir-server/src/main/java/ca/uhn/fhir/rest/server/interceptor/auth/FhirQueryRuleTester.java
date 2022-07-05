package ca.uhn.fhir.rest.server.interceptor.auth;

public class FhirQueryRuleTester implements IAuthRuleTester{
	private final String myType;
	private final String myFilter;

	public FhirQueryRuleTester(String theType, String theFilter) {
		myType = theType;
		myFilter = theFilter;
	}

	@Override
	public boolean matches(RuleTestRequest theRuleTestRequest) {
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
				theRuleTestRequest.ruleApplier.getTroubleshootingLog().warn("Unsupported matcher expression {}: {}.  Abstaining.", myFilter, mr.getUnsupportedReason());
				// unsupported doesn't match unless this is a deny request, and we need to be safe!
				return (theRuleTestRequest.mode == PolicyEnum.DENY);
			case NO_MATCH:
			default:
				return false;
		}
	}
}
