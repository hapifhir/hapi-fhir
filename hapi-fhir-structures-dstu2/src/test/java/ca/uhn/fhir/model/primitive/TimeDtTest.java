package ca.uhn.fhir.model.primitive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class TimeDtTest {

	@Test
	public void testEncode() {
		TimeDt dt = new TimeDt("11:33:01.123");
		assertEquals("11:33:01.123", dt.getValue());
	}
	
}
