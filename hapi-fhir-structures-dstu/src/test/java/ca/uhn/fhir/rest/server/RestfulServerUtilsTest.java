package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.util.regex.Matcher;

import org.junit.Test;

public class RestfulServerUtilsTest {

	@Test
	public void testAcceptPattern() {
		Matcher m = RestfulServerUtils.ACCEPT_HEADER_PATTERN.matcher("application/json+fhir");
		assertTrue(m.find());
		assertEquals("application/json+fhir", m.group(0));
		assertEquals("application/json+fhir", m.group(1));
	}
	
}
