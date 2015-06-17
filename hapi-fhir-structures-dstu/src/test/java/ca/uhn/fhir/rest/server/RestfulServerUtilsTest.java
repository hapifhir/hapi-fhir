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
	
	@Test
	public void testAcceptPattern2() {
		Matcher m = RestfulServerUtils.ACCEPT_HEADER_PATTERN.matcher("text/plain, " + Constants.CT_FHIR_JSON);
		assertTrue(m.find());
		assertEquals("text/plain,", m.group(0));
		assertEquals("text/plain", m.group(1));
		
		assertTrue(m.find());
		assertEquals(" application/json+fhir", m.group(0));
		assertEquals("application/json+fhir", m.group(1));
	}
	
}
