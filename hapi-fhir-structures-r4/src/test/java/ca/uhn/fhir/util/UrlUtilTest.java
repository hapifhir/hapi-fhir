package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


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
		assertNull(UrlUtil.parseUrl("http://hl7.org/fhir/ConceptMap/ussgfht-loincde?").getParams());
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
		// %20 is an encoded space character
		assertThat(UrlUtil.translateMatchUrl("Observation?names=homer%20simpson")).containsExactlyInAnyOrder(new BasicNameValuePair("names", "homer simpson"));

		// + is also an encoded space character
		assertThat(UrlUtil.translateMatchUrl("Observation?names=homer+simpson")).containsExactlyInAnyOrder(new BasicNameValuePair("names", "homer simpson"));
	}

	@Test
	public void testTranslateMatchUrl_UrlWithPlusSign() {
		// %2B is an encoded plus sign
		assertThat(UrlUtil.translateMatchUrl("Observation?names=homer%2Bsimpson")).containsExactlyInAnyOrder(new BasicNameValuePair("names", "homer+simpson"));
	}

	@Test
	public void testTranslateMatchUrl_UrlWithPipe() {
		// Real space
		assertThat(UrlUtil.translateMatchUrl("Observation?names=homer|simpson")).containsExactlyInAnyOrder(new BasicNameValuePair("names", "homer|simpson"));
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

	@Test
	public void testGetAboveUriCandidates_returnsUriList() {
		List<String> candidates = UrlUtil.getAboveUriCandidates("http://host/v1/v2/v3/v4");
		assertThat(candidates).hasSize(5);
		assertThat(candidates).containsExactlyInAnyOrder("http://host/v1/v2/v3/v4", "http://host/v1/v2/v3", "http://host/v1/v2", "http://host/v1", "http://host");
	}

	@Test
	public void testGetAboveUriCandidates_withHostOnly_returnsHostUri() {
		List<String> candidates = UrlUtil.getAboveUriCandidates("http://host");
		assertThat(candidates).hasSize(1);
		assertThat(candidates).containsExactlyInAnyOrder("http://host");
	}

	@Test
	public void testGetAboveUriCandidates_withFullUri_returnsUriList() {
		List<String> candidates = UrlUtil.getAboveUriCandidates("https://host.com:8080/path1/path2?name=name#name");
		assertThat(candidates).hasSize(3);
		assertThat(candidates).containsExactlyInAnyOrder("https://host.com:8080/path1/path2?name=name#name", "https://host.com:8080/path1", "https://host.com:8080");
	}

	@ParameterizedTest
	@ValueSource(strings = {"invalid_uri", "http://some-source/ with_invalid_uri", "http://"})
	public void testGetAboveUriCandidates_withInvalidURI_throwsException(String theUri) {
		try {
			UrlUtil.getAboveUriCandidates(theUri);
			fail();		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(2419) + "Provided URI is not valid: " + theUri, e.getMessage());
		}
	}
}
