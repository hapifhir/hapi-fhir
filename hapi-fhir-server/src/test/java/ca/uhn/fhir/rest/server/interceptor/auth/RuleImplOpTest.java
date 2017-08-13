package ca.uhn.fhir.rest.server.interceptor.auth;

import org.junit.Test;

public class RuleImplOpTest {

	@Test
	public void testToString() {
		new RuleImplOp("").toString();
	}
}
