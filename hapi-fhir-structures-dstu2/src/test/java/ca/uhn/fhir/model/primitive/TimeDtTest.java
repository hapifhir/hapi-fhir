package ca.uhn.fhir.model.primitive;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeDtTest {

	@Test
	public void testEncode() {
		TimeDt dt = new TimeDt("11:33:01.123");
		assertEquals("11:33:01.123", dt.getValue());
	}
	
}
