package ca.uhn.fhir.util;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.util.UrlUtil.UrlParts;

public class UrlUtilTest {

	@Test
	public void testIsValid() {
		assertTrue(UrlUtil.isValid("http://foo"));
		assertTrue(UrlUtil.isValid("https://foo"));
		assertTrue(UrlUtil.isValid("HTTP://Foo"));
		assertTrue(UrlUtil.isValid("HTTPS://Foo"));
		
		assertFalse(UrlUtil.isValid("file://foo"));
		assertFalse(UrlUtil.isValid("://foo"));
		assertFalse(UrlUtil.isValid("http:/ss"));
		assertFalse(UrlUtil.isValid("http:/"));
		assertFalse(UrlUtil.isValid("http:"));
		assertFalse(UrlUtil.isValid("h"));
		assertFalse(UrlUtil.isValid(""));
		assertFalse(UrlUtil.isValid(null));
	}
	
	@Test
	public void testParseUrl() {
		assertEquals("ConceptMap", UrlUtil.parseUrl("http://hl7.org/fhir/ConceptMap/ussgfht-loincde").getResourceType());
		assertEquals("ConceptMap", UrlUtil.parseUrl("http://hl7.org/fhir/ConceptMap/ussgfht-loincde").getResourceType());
		assertEquals("ussgfht-loincde", UrlUtil.parseUrl("http://hl7.org/fhir/ConceptMap/ussgfht-loincde").getResourceId());
		assertEquals(null, UrlUtil.parseUrl("http://hl7.org/fhir/ConceptMap/ussgfht-loincde?").getParams());
		assertEquals("a=b", UrlUtil.parseUrl("http://hl7.org/fhir/ConceptMap/ussgfht-loincde?a=b").getParams());

		assertEquals("a=b", UrlUtil.parseUrl("ConceptMap/ussgfht-loincde?a=b").getParams());

	}
	
	@Test
	public void testConstructAbsoluteUrl() {
		assertEquals("http://foo/bar/baz", UrlUtil.constructAbsoluteUrl(null, "http://foo/bar/baz"));
		assertEquals("http://foo/bar/baz", UrlUtil.constructAbsoluteUrl("http://foo/bar/","baz"));
		assertEquals("http://foo/bar/baz/", UrlUtil.constructAbsoluteUrl("http://foo/bar/","baz/"));
		assertEquals("http://foo/bar/baz/", UrlUtil.constructAbsoluteUrl("http://foo/bar/","./baz/"));

		assertEquals("http://foo/baz/", UrlUtil.constructAbsoluteUrl("http://foo/bar/","../baz/"));
		assertEquals("http://foo/baz/", UrlUtil.constructAbsoluteUrl("http://foo/bar/","/baz/"));
	}

	
	
	@Test
	public void testConstructRelativeUrl() {
		assertEquals("http://foo/bar/baz", UrlUtil.constructRelativeUrl("http://boo/far/faz", "http://foo/bar/baz"));
		assertEquals("http://foo/bar/baz", UrlUtil.constructRelativeUrl("http://foo/far/faz", "http://foo/bar/baz"));
		assertEquals("baz", UrlUtil.constructRelativeUrl("http://foo/bar/boo", "http://foo/bar/baz"));
	}
	
}
