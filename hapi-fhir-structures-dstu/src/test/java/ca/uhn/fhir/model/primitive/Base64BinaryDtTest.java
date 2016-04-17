package ca.uhn.fhir.model.primitive;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.util.TestUtil;


public class Base64BinaryDtTest {

	@Test
	public void testDecodeNull() {
		new Base64BinaryDt().setValueAsString(null);
	}
	

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
