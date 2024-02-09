package ca.uhn.fhir.model.primitive;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.RoundingMode;

import org.junit.jupiter.api.Test;

public class DecimalDtTest {

	@Test
	public void testRoundWithMode() {
		DecimalDt dt = new DecimalDt("1.66666666");
		dt.round(3, RoundingMode.FLOOR);
		assertThat(dt.getValueAsString()).isEqualTo("1.66");
	}
	
	@Test
	public void testGetValue() {
		DecimalDt dt = new DecimalDt("1.66666666");
		assertThat(dt.getValueAsInteger()).isEqualTo(1);
		assertThat(dt.getValueAsNumber().toString()).isEqualTo("1.66666666");
		assertThat(dt.getValueAsString()).isEqualTo("1.66666666");
	}

	@Test
	public void testSetValue() {
		DecimalDt dt = new DecimalDt();
		dt.setValueAsInteger(123);
		assertThat(dt.getValueAsString()).isEqualTo("123");
	}

	@Test
	public void testRound() {
		DecimalDt dt = new DecimalDt("1.66666666");
		dt.round(3);
		assertThat(dt.getValueAsString()).isEqualTo("1.67");
	}
	
	@Test
	public void testCompareTo() {
		DecimalDt dt = new DecimalDt("1.66666666");
		assertThat(dt.compareTo(null)).isEqualTo(1);
		assertThat(dt.compareTo(new DecimalDt())).isEqualTo(1);
		assertThat(dt.compareTo(new DecimalDt("0.1"))).isEqualTo(1);
		assertThat(dt.compareTo(new DecimalDt("99"))).isEqualTo(-1);
		assertThat(dt.compareTo(new DecimalDt("1.66666666"))).isEqualTo(0);
		assertThat(new DecimalDt().compareTo(new DecimalDt())).isEqualTo(0);
		assertThat(new DecimalDt().compareTo(new DecimalDt("1.0"))).isEqualTo(-1);
	}
	
	
}
