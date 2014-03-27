package ca.uhn.fhir.model.primitive;

import static org.junit.Assert.*;

import org.junit.Test;

public class DecimalDtTest {

	@Test
	public void testPrecision() {
		DecimalDt dt = new DecimalDt(2.03);
		assertEquals("2.03", dt.getValueAsString());
		
	}
	
}
