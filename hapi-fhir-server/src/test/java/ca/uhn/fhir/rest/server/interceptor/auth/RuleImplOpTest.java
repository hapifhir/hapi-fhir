package ca.uhn.fhir.rest.server.interceptor.auth;

import org.junit.jupiter.api.Test;

public class RuleImplOpTest {

	@Test
	public void testToString() {
		new RuleImplOp("").toString();
	}
}
