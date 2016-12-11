package ca.uhn.fhir.context;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VersionDstu3Test {

	@Test
	public void testVersion() {
		assertEquals("1.8.0", FhirVersionEnum.DSTU3.getFhirVersionString());
	}
	
}
