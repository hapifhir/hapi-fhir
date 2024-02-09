package ca.uhn.fhir.context;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class VersionDstu3Test {

	@Test
	public void testVersion() {
		assertThat(FhirVersionEnum.DSTU3.getFhirVersionString()).isEqualTo("3.0.2");
	}
	
}
