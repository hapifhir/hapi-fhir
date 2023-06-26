package ca.uhn.fhir.rest.server.interceptor.auth;

import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.Verdict;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VerdictTest {

	@Test
	public void testToString() {
		Verdict v = new AuthorizationInterceptor.Verdict(PolicyEnum.ALLOW, new RuleImplOp("foo"));
		assertEquals("AuthorizationInterceptor.Verdict[rule=foo,decision=ALLOW]", v.toString());
	}
	
}
