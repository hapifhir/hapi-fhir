package ca.uhn.fhir.model.primitive;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeDtTest {

	@Test
	public void testEncode() {
		TimeDt dt = new TimeDt("11:33:01.123");
		assertEquals("11:33:01.123", dt.getValue());
	}
	
}
