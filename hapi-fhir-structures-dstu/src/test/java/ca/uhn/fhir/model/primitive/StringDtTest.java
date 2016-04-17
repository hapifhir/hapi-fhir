package ca.uhn.fhir.model.primitive;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.util.TestUtil;

public class StringDtTest {

	@Test
	public void testBlank() {
		
		assertTrue(new StringDt("").isEmpty());
		
	}
	

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
