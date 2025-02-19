package ca.uhn.fhir.rest.server.util;

import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MatchUrlUtilTest {

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
