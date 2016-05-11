package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.util.regex.Matcher;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.util.TestUtil;

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
	
	@Test
	public void testParsePreferHeaderBad() {
		assertEquals(null, RestfulServerUtils.parsePreferHeader(null));
		assertEquals(null, RestfulServerUtils.parsePreferHeader(""));
		assertEquals(null, RestfulServerUtils.parsePreferHeader("foo"));
		assertEquals(null, RestfulServerUtils.parsePreferHeader("foo,bar"));

		assertEquals(null, RestfulServerUtils.parsePreferHeader("return"));
		assertEquals(null, RestfulServerUtils.parsePreferHeader("return="));
		assertEquals(null, RestfulServerUtils.parsePreferHeader("return= "));
		assertEquals(null, RestfulServerUtils.parsePreferHeader("return=  "));
		assertEquals(null, RestfulServerUtils.parsePreferHeader("return=   "));
		assertEquals(null, RestfulServerUtils.parsePreferHeader("return=    "));
		assertEquals(null, RestfulServerUtils.parsePreferHeader("return=     "));
		assertEquals(null, RestfulServerUtils.parsePreferHeader("return   =\"minimal"));
	}
	
	@Test
	public void testParsePreferHeaderGood() {
		assertEquals(PreferReturnEnum.MINIMAL, RestfulServerUtils.parsePreferHeader("return=minimal"));
		assertEquals(PreferReturnEnum.REPRESENTATION, RestfulServerUtils.parsePreferHeader("return=representation"));
		assertEquals(PreferReturnEnum.MINIMAL, RestfulServerUtils.parsePreferHeader("return   =  minimal    "));
		assertEquals(PreferReturnEnum.MINIMAL, RestfulServerUtils.parsePreferHeader("return   =  \"minimal\"    "));
		assertEquals(PreferReturnEnum.MINIMAL, RestfulServerUtils.parsePreferHeader("return   =\"minimal\""));
		assertEquals(PreferReturnEnum.MINIMAL, RestfulServerUtils.parsePreferHeader("return   =\"minimal\""));
	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
