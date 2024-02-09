package ca.uhn.fhir.context;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FhirVersionEnumTest {

	@Test
	public void testGetVersionPermissive() {
		assertThat(FhirVersionEnum.forVersionString("1.0.0")).isEqualTo(FhirVersionEnum.DSTU2);
		assertThat(FhirVersionEnum.forVersionString("1.0.1")).isEqualTo(FhirVersionEnum.DSTU2);

		assertThat(FhirVersionEnum.forVersionString("3.0.1")).isEqualTo(FhirVersionEnum.DSTU3);
		assertThat(FhirVersionEnum.forVersionString("3.0.2")).isEqualTo(FhirVersionEnum.DSTU3);
		assertThat(FhirVersionEnum.forVersionString("DSTU3")).isEqualTo(FhirVersionEnum.DSTU3);
		assertThat(FhirVersionEnum.forVersionString("STU3")).isEqualTo(FhirVersionEnum.DSTU3);

		assertThat(FhirVersionEnum.forVersionString("4.0.0")).isEqualTo(FhirVersionEnum.R4);
		assertThat(FhirVersionEnum.forVersionString("4.0.1")).isEqualTo(FhirVersionEnum.R4);
		assertThat(FhirVersionEnum.forVersionString("R4")).isEqualTo(FhirVersionEnum.R4);
		assertThat(FhirVersionEnum.forVersionString("R4B")).isEqualTo(FhirVersionEnum.R4B);
		assertThat(FhirVersionEnum.forVersionString("4.3.0")).isEqualTo(FhirVersionEnum.R4B);

		assertThat(FhirVersionEnum.forVersionString("R5")).isEqualTo(FhirVersionEnum.R5);

	}


}
