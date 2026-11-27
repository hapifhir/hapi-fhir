package ca.uhn.fhir.rest.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EncodingEnumTest {

	@Test
	public void getTypeWithoutCharset() {
		assertEquals("text/plain", EncodingEnum.getTypeWithoutCharset("text/plain"));
		assertEquals("text/plain", EncodingEnum.getTypeWithoutCharset("  text/plain"));
		assertEquals("text/plain", EncodingEnum.getTypeWithoutCharset("  text/plain; charset=utf-8"));
		assertEquals("text/plain", EncodingEnum.getTypeWithoutCharset("  text/plain  ; charset=utf-8"));
	}

	@Test
	public void forContentType_turtle() {
		assertEquals(EncodingEnum.RDF, EncodingEnum.forContentType(Constants.CT_RDF_TURTLE_NEW));
		assertEquals(EncodingEnum.RDF, EncodingEnum.forContentType(Constants.CT_RDF_TURTLE));
		assertEquals(EncodingEnum.RDF, EncodingEnum.forContentType(Constants.CT_RDF_TURTLE_LEGACY));
		assertEquals(EncodingEnum.RDF, EncodingEnum.forContentType("application/fhir turtle"));
		assertEquals(EncodingEnum.RDF, EncodingEnum.forContentTypeStrict(Constants.CT_RDF_TURTLE_NEW));
	}

	@Test
	public void getTypeWithSpace() {
		assertEquals("application/fhir+xml", EncodingEnum.getTypeWithoutCharset("application/fhir xml"));
		assertEquals("application/fhir+xml", EncodingEnum.getTypeWithoutCharset("application/fhir xml; charset=utf-8"));
		assertEquals("application/fhir+xml", EncodingEnum.getTypeWithoutCharset("application/fhir xml ; charset=utf-8"));
	}

}
