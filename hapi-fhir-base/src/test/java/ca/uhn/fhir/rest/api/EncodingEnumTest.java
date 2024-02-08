package ca.uhn.fhir.rest.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EncodingEnumTest {

	@Test
	public void getTypeWithoutCharset() {
		assertThat(EncodingEnum.getTypeWithoutCharset("text/plain")).isEqualTo("text/plain");
		assertThat(EncodingEnum.getTypeWithoutCharset("  text/plain")).isEqualTo("text/plain");
		assertThat(EncodingEnum.getTypeWithoutCharset("  text/plain; charset=utf-8")).isEqualTo("text/plain");
		assertThat(EncodingEnum.getTypeWithoutCharset("  text/plain  ; charset=utf-8")).isEqualTo("text/plain");
	}

	@Test
	public void getTypeWithSpace() {
		assertThat(EncodingEnum.getTypeWithoutCharset("application/fhir xml")).isEqualTo("application/fhir+xml");
		assertThat(EncodingEnum.getTypeWithoutCharset("application/fhir xml; charset=utf-8")).isEqualTo("application/fhir+xml");
		assertThat(EncodingEnum.getTypeWithoutCharset("application/fhir xml ; charset=utf-8")).isEqualTo("application/fhir+xml");
	}

}
