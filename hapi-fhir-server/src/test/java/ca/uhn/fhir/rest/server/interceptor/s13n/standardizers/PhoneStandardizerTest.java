package ca.uhn.fhir.rest.server.interceptor.s13n.standardizers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PhoneStandardizerTest {

	private IStandardizer myStandardizer = new PhoneStandardizer();

	// for rules refer to https://docs.google.com/document/d/1Vz0vYwdDsqu6WrkRyzNiBJDLGmWAej5g/edit#

	@Test
	public void testPhoneNumberStandartization() {
		assertThat(myStandardizer.standardize("(111) 222-33-33")).isEqualTo("111-222-3333");
		assertThat(myStandardizer.standardize("1 1 1 2 2 2 - 3 3 3 3 ")).isEqualTo("111-222-3333");
		assertThat(myStandardizer.standardize("111-222-3")).isEqualTo("111-222-3");
		assertThat(myStandardizer.standardize("111⅕-222-3")).isEqualTo("111-222-3");
		assertThat(myStandardizer.standardize("")).isEqualTo("");
	}
}
