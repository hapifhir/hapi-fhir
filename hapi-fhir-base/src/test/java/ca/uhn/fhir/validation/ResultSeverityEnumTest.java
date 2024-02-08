package ca.uhn.fhir.validation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResultSeverityEnumTest {

	@Test
	public void testOrdinals() {
		assertThat(ResultSeverityEnum.INFORMATION.ordinal()).isEqualTo(0);
		assertThat(ResultSeverityEnum.WARNING.ordinal()).isEqualTo(1);
		assertThat(ResultSeverityEnum.ERROR.ordinal()).isEqualTo(2);
		assertThat(ResultSeverityEnum.FATAL.ordinal()).isEqualTo(3);
		
	}
	
}
