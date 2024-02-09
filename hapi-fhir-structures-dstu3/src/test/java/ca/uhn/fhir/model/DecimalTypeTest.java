package ca.uhn.fhir.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.hl7.fhir.dstu3.model.DecimalType;
import org.junit.jupiter.api.Test;

public class DecimalTypeTest {

	@Test
	public void testDoubleValue() {
		DecimalType d = new DecimalType(1.2D);
		assertThat(d.getValueAsString()).isEqualTo("1.2");
		
		d = new DecimalType();
		d.setValue(1.2D);
		assertThat(d.getValueAsString()).isEqualTo("1.2");

		d = new DecimalType();
		d.setValue(10);
		assertThat(d.getValueAsString()).isEqualTo("10");

		d = new DecimalType();
		d.setValue(10L);
		assertThat(d.getValueAsString()).isEqualTo("10");

	}
	
}
