package ca.uhn.fhir.model.primitive;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.util.TestUtil;

public class DecimalDtTest {

	@Test
	public void testPrecision() {
		DecimalDt dt = new DecimalDt(2.03);
		assertEquals("2.03", dt.getValueAsString());
		
	}
	

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
