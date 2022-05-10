package ca.uhn.fhir.util;

import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UrlUtilTest {

	@Test
	public void testNormalizeCanonicalUrl() {
		assertEquals("http://foo", UrlUtil.normalizeCanonicalUrlForComparison("http://foo/"));
		assertEquals("http://foo", UrlUtil.normalizeCanonicalUrlForComparison("http://foo"));
		assertEquals("http://foo", UrlUtil.normalizeCanonicalUrlForComparison("http://foo|1.23"));
		assertEquals("http://foo", UrlUtil.normalizeCanonicalUrlForComparison("http://foo|1.23#333"));
		assertEquals("abc", UrlUtil.normalizeCanonicalUrlForComparison("abc"));
		assertEquals("abc", UrlUtil.normalizeCanonicalUrlForComparison("abc/"));
	}

	@Test
	public void testConstructAbsoluteUrl() {
		assertEquals("http://foo/bar/baz", UrlUtil.constructAbsoluteUrl(null, "http://foo/bar/baz"));
		assertEquals("http://foo/bar/baz", UrlUtil.constructAbsoluteUrl("http://foo/bar/", "baz"));
		assertEquals("http://foo/bar/baz/", UrlUtil.constructAbsoluteUrl("http://foo/bar/", "baz/"));
		assertEquals("http://foo/bar/baz/", UrlUtil.constructAbsoluteUrl("http://foo/bar/", "./baz/"));

		assertEquals("http://foo/baz/", UrlUtil.constructAbsoluteUrl("http://foo/bar/", "../baz/"));
		assertEquals("http://foo/baz/", UrlUtil.constructAbsoluteUrl("http://foo/bar/", "/baz/"));
	}

	@Test
	public void testConstructRelativeUrl() {
		assertEquals("http://foo/bar/baz", UrlUtil.constructRelativeUrl("http://boo/far/faz", "http://foo/bar/baz"));
		assertEquals("http://foo/bar/baz", UrlUtil.constructRelativeUrl("http://foo/far/faz", "http://foo/bar/baz"));
		assertEquals("baz", UrlUtil.constructRelativeUrl("http://foo/bar/boo", "http://foo/bar/baz"));
	}

	@Test
	public void testEscape() {
		assertEquals("A%20B", UrlUtil.escapeUrlParam("A B"));
		assertEquals("A%2BB", UrlUtil.escapeUrlParam("A+B"));
	}

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
		assertEquals("a=b", UrlUtil.parseUrl("/ConceptMap?a=b").getParams());
		assertEquals("a=b", UrlUtil.parseUrl("/ConceptMap/ussgfht-loincde?a=b").getParams());

	}

	@Test
	public void testSanitize() {
		assertEquals(" &apos; ", UrlUtil.sanitizeUrlPart(" ' "));
		assertEquals(" &lt; ", UrlUtil.sanitizeUrlPart(" < "));
		assertEquals(" &gt; ", UrlUtil.sanitizeUrlPart(" > "));
		assertEquals(" &quot; ", UrlUtil.sanitizeUrlPart(" \" "));
		assertEquals(" &#10; ", UrlUtil.sanitizeUrlPart(" \n "));
		assertEquals(" &#13; ", UrlUtil.sanitizeUrlPart(" \r "));
		assertEquals("  ", UrlUtil.sanitizeUrlPart(" \0 "));
	}

	@Test
	public void testTranslateMatchUrl_UrlWithSpaces() {
		// Real space
		assertThat(UrlUtil.translateMatchUrl("Observation?names=homer%20simpson"),
			containsInAnyOrder(new BasicNameValuePair("names", "homer simpson")));

		// Treat + as an actual + and not a space
		assertThat(UrlUtil.translateMatchUrl("Observation?names=homer+simpson"),
			containsInAnyOrder(new BasicNameValuePair("names", "homer+simpson")));

	}

	@Test
	public void testTranslateMatchUrl_UrlWithPipe() {
		// Real space
		assertThat(UrlUtil.translateMatchUrl("Observation?names=homer|simpson"),
			containsInAnyOrder(new BasicNameValuePair("names", "homer|simpson")));
	}

}
