package ca.uhn.fhir.model.primitive;

import static org.junit.jupiter.api.Assertions.*;

import java.math.RoundingMode;

import org.junit.jupiter.api.Test;

public class DecimalDtTest {

	@Test
	public void testRoundWithMode() {
		DecimalDt dt = new DecimalDt("1.66666666");
		dt.round(3, RoundingMode.FLOOR);
		assertEquals("1.66", dt.getValueAsString());
	}
	
	@Test
	public void testGetValue() {
		DecimalDt dt = new DecimalDt("1.66666666");
		assertEquals(1, dt.getValueAsInteger());
		assertEquals("1.66666666", dt.getValueAsNumber().toString());
		assertEquals("1.66666666", dt.getValueAsString());
	}

	@Test
	public void testSetValue() {
		DecimalDt dt = new DecimalDt();
		dt.setValueAsInteger(123);
		assertEquals("123", dt.getValueAsString());
	}

	@Test
	public void testRound() {
		DecimalDt dt = new DecimalDt("1.66666666");
		dt.round(3);
		assertEquals("1.67", dt.getValueAsString());
	}
	
	@Test
	public void testCompareTo() {
		DecimalDt dt = new DecimalDt("1.66666666");
		assertEquals(1, dt.compareTo(null));
		assertEquals(1, dt.compareTo(new DecimalDt()));
		assertEquals(1, dt.compareTo(new DecimalDt("0.1")));
		assertEquals(-1, dt.compareTo(new DecimalDt("99")));
		assertEquals(0, dt.compareTo(new DecimalDt("1.66666666")));
		assertEquals(0, new DecimalDt().compareTo(new DecimalDt()));
		assertEquals(-1, new DecimalDt().compareTo(new DecimalDt("1.0")));
	}
	
	
}
