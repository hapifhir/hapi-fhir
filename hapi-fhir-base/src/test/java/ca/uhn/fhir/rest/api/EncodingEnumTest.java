package ca.uhn.fhir.rest.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EncodingEnumTest {

	@Test
	public void getTypeWithoutCharset() {
		assertEquals("text/plain", EncodingEnum.getTypeWithoutCharset("text/plain"));
		assertEquals("text/plain", EncodingEnum.getTypeWithoutCharset("  text/plain"));
		assertEquals("text/plain", EncodingEnum.getTypeWithoutCharset("  text/plain; charset=utf-8"));
		assertEquals("text/plain", EncodingEnum.getTypeWithoutCharset("  text/plain  ; charset=utf-8"));
	}

	@Test
	public void getTypeWithSpace() {
		assertEquals("application/fhir+xml", EncodingEnum.getTypeWithoutCharset("application/fhir xml"));
		assertEquals("application/fhir+xml", EncodingEnum.getTypeWithoutCharset("application/fhir xml; charset=utf-8"));
		assertEquals("application/fhir+xml", EncodingEnum.getTypeWithoutCharset("application/fhir xml ; charset=utf-8"));
	}

}
