package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UrlUtilTest {

	private final FhirContext myCtx = FhirContext.forR4Cached();

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
	public void testUnescape() {
		assertAll(
			() -> assertEquals(Constants.CT_JSON, UrlUtil.unescape(Constants.CT_JSON)),
			() -> assertEquals(Constants.CT_NDJSON, UrlUtil.unescape(Constants.CT_NDJSON)),
			() -> assertEquals(Constants.CT_XML, UrlUtil.unescape(Constants.CT_XML)),
			() -> assertEquals(Constants.CT_XML_PATCH, UrlUtil.unescape(Constants.CT_XML_PATCH)),
			() -> assertEquals(Constants.CT_APPLICATION_GZIP, UrlUtil.unescape(Constants.CT_APPLICATION_GZIP)),
			() -> assertEquals(Constants.CT_RDF_TURTLE, UrlUtil.unescape(Constants.CT_RDF_TURTLE)),
			() -> assertEquals(Constants.CT_FHIR_JSON, UrlUtil.unescape(Constants.CT_FHIR_JSON)),
			() -> assertEquals(Constants.CT_FHIR_NDJSON, UrlUtil.unescape(Constants.CT_FHIR_NDJSON))
		);
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

	@ParameterizedTest
	@CsvSource({
		"null,    null",
		"null,    urn:uuid:12345",
		"Patient, Patient",
		"Patient, Patient?",
		"Patient, Patient?identifier=foo",
		"Patient, /Patient",
		"Patient, /Patient?",
		"Patient, /Patient?identifier=foo",
		"Patient, http://foo/base/Patient?identifier=foo",
		"Patient, http://foo/base/Patient/1",
		"Patient, http://foo/base/Patient/1/_history/2",
		"Patient, /Patient/1",
		"Patient, /Patient/1/_history/2",
		"Patient, Patient/1",
		"Patient, Patient/1/_history/2",
	})
	public void testDetermineResourceTypeInResourceUrl(String theExpected, String theUrl) {
		String url = theUrl;
		if (url.equals("null")) {
			url = null;
		}

		String actual = UrlUtil.determineResourceTypeInResourceUrl(myCtx, url);

		if (theExpected.equals("null")) {
			assertNull(actual);
		} else {
			assertEquals(theExpected, actual);
		}
	}

}
