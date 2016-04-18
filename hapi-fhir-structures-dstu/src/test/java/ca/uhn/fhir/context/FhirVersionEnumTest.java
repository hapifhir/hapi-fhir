package ca.uhn.fhir.context;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.util.TestUtil;

public class FhirVersionEnumTest {

	@Test
	public void testIsNewerThan() {
		assertFalse(FhirVersionEnum.DSTU1.isNewerThan(FhirVersionEnum.DSTU2));		
	}
	

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
