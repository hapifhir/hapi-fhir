package ca.uhn.fhir.rest.server.util;

import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class MatchUrlUtilTest {

	// Created by Claude Fable 5
	@ParameterizedTest
	@CsvSource({
		"Patient?identifier=sys|val,                          true",
		"Patient?identifier=http://foo|1,                     true", // URL inside the token value
		"Patient?name=Smith,                                  true",
		"Patient,                                             false", // no query string
		"Patient/123,                                         false",
		"http://example.org/fhir/Patient?identifier=sys|val,  false",
		"https://example.org/fhir/Patient?identifier=sys|val, false",
		"urn:uuid:12345678-1234-1234-1234-123456789012,       false",
		"urn:oid:1.2.3?x=y,                                   false",
		"ftp://example.org/x?y=z,                             false", // any scheme'd value is absolute
		"mailto:foo@example.org?subject=x,                    false",
	})
	void testIsInlineMatchUrl(String theValue, boolean theExpected) {
		assertThat(MatchUrlUtil.isInlineMatchUrl(theValue)).isEqualTo(theExpected);
	}

	// Created by Claude Fable 5
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {" "})
	void testIsInlineMatchUrl_blankValues(String theValue) {
		assertThat(MatchUrlUtil.isInlineMatchUrl(theValue)).isFalse();
	}

	@Test
	public void testTranslateMatchUrl_UrlWithSpaces() {
		// %20 is an encoded space character
		assertThat(MatchUrlUtil.translateMatchUrl("Observation?names=homer%20simpson")).containsExactlyInAnyOrder(new BasicNameValuePair("names", "homer simpson"));

		// + is also an encoded space character
		assertThat(MatchUrlUtil.translateMatchUrl("Observation?names=homer+simpson")).containsExactlyInAnyOrder(new BasicNameValuePair("names", "homer simpson"));
	}

	@Test
	public void testTranslateMatchUrl_UrlWithPlusSign() {
		// %2B is an encoded plus sign
		assertThat(MatchUrlUtil.translateMatchUrl("Observation?names=homer%2Bsimpson")).containsExactlyInAnyOrder(new BasicNameValuePair("names", "homer+simpson"));
	}

	@Test
	public void testTranslateMatchUrl_UrlWithPipe() {
		// Real space
		assertThat(MatchUrlUtil.translateMatchUrl("Observation?names=homer|simpson")).containsExactlyInAnyOrder(new BasicNameValuePair("names", "homer|simpson"));
	}
}
