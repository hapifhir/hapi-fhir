package ca.uhn.fhir.context;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FhirVersionEnumTest {

	@Test
	public void testGetVersionPermissive() {
		assertEquals(FhirVersionEnum.DSTU2, FhirVersionEnum.forVersionString("1.0.0"));
		assertEquals(FhirVersionEnum.DSTU2, FhirVersionEnum.forVersionString("1.0.1"));

		assertEquals(FhirVersionEnum.DSTU3, FhirVersionEnum.forVersionString("3.0.1"));
		assertEquals(FhirVersionEnum.DSTU3, FhirVersionEnum.forVersionString("3.0.2"));
		assertEquals(FhirVersionEnum.DSTU3, FhirVersionEnum.forVersionString("DSTU3"));
		assertEquals(FhirVersionEnum.DSTU3, FhirVersionEnum.forVersionString("STU3"));

		assertEquals(FhirVersionEnum.R4, FhirVersionEnum.forVersionString("4.0.0"));
		assertEquals(FhirVersionEnum.R4, FhirVersionEnum.forVersionString("4.0.1"));
		assertEquals(FhirVersionEnum.R4, FhirVersionEnum.forVersionString("R4"));

		assertEquals(FhirVersionEnum.R5, FhirVersionEnum.forVersionString("R5"));
	}


}
