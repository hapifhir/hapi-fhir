package ca.uhn.fhir.model.primitive;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.util.TestUtil;


public class Base64BinaryDtTest {

	@Test
	public void testDecodeNull() {
		new Base64BinaryDt().setValueAsString(null);
	}
	

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
