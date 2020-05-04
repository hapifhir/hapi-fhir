package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.Verdict;
import org.junit.Test;

public class VerdictTest {

	@Test
	public void testToString() {
		Verdict v = new AuthorizationInterceptor.Verdict(PolicyEnum.ALLOW, new RuleImplOp("foo"));
		v.toString();
	}
	
}
