package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.Verdict;

public class VerdictTest {

	public void testToString() {
		Verdict v = new AuthorizationInterceptor.Verdict(PolicyEnum.ALLOW, new RuleImplOp("foo"));
		v.toString();
	}
	
}
