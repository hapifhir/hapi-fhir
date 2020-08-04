package ca.uhn.fhir.context;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.util.TestUtil;

public class FhirVersionEnumTest {

	@Test
	public void testIsNewerThan() {
		assertFalse(FhirVersionEnum.DSTU1.isNewerThan(FhirVersionEnum.DSTU2));		
	}
	

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
