package ca.uhn.fhir.context;

import static org.junit.Assert.*;

import org.junit.Test;

public class FhirVersionEnumTest {

	@Test
	public void testIsNewerThan() {
		assertFalse(FhirVersionEnum.DSTU1.isNewerThan(FhirVersionEnum.DSTU2));		
	}
	
}
