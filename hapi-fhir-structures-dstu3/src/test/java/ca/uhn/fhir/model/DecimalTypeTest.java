package ca.uhn.fhir.model;

import static org.junit.jupiter.api.Assertions.*;

import org.hl7.fhir.dstu3.model.DecimalType;
import org.junit.jupiter.api.Test;

public class DecimalTypeTest {

	@Test
	public void testDoubleValue() {
		DecimalType d = new DecimalType(1.2D);
		assertEquals("1.2", d.getValueAsString());
		
		d = new DecimalType();
		d.setValue(1.2D);
		assertEquals("1.2", d.getValueAsString());

		d = new DecimalType();
		d.setValue(10);
		assertEquals("10", d.getValueAsString());

		d = new DecimalType();
		d.setValue(10L);
		assertEquals("10", d.getValueAsString());

	}
	
}
