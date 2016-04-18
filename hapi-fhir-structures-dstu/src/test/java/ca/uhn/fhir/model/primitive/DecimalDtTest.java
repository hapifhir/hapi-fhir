package ca.uhn.fhir.model.primitive;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.util.TestUtil;

public class DecimalDtTest {

	@Test
	public void testPrecision() {
		DecimalDt dt = new DecimalDt(2.03);
		assertEquals("2.03", dt.getValueAsString());
		
	}
	

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
