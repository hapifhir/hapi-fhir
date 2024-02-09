package ca.uhn.fhir.rest.server.method;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MethodMatchEnumTest {

	@Test
	public void testOrder() {
		assertThat(MethodMatchEnum.NONE.ordinal()).isEqualTo(0);
		assertThat(MethodMatchEnum.APPROXIMATE.ordinal()).isEqualTo(1);
		assertThat(MethodMatchEnum.EXACT.ordinal()).isEqualTo(2);
	}

}
