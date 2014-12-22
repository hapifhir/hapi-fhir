package ca.uhn.fhir.context;

import static org.junit.Assert.*;

import org.junit.Test;

public class FhirVersionEnumTest {

	@Test
	public void testIsNewerThan() {
		assertTrue(FhirVersionEnum.DEV.isNewerThan(FhirVersionEnum.DSTU1));
		assertFalse(FhirVersionEnum.DSTU1.isNewerThan(FhirVersionEnum.DEV));		
		assertFalse(FhirVersionEnum.DEV.isNewerThan(FhirVersionEnum.DEV));		
	}
	
}
