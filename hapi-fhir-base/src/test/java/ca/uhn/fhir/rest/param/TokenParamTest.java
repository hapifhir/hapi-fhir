package ca.uhn.fhir.rest.param;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokenParamTest {
	@Test
	public void testEquals() {
		TokenParam tokenParam1 = new TokenParam("foo", "bar");
		TokenParam tokenParam2 = new TokenParam("foo", "bar");
		assertEquals(tokenParam1, tokenParam2);
	}
}
