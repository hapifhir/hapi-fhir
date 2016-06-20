package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.Verdict;

abstract class BaseRule implements IAuthRule {
	private String myName;
	private PolicyEnum myMode;

	BaseRule(String theRuleName) {
		myName = theRuleName;
	}
	
	@Override
	public String getName() {
		return myName;
	}
	
	public void setMode(PolicyEnum theRuleMode) {
		myMode = theRuleMode;
	}

	Verdict newVerdict() {
		return new Verdict(myMode, this);
	}

	public PolicyEnum getMode() {
		return myMode;
	}

}
