package ca.uhn.fhir.rest.server;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IncomingRequestAddressStrategyTest {

	@Test
	public void testRequestWithNull() {
		IncomingRequestAddressStrategy s = new IncomingRequestAddressStrategy();
		String result = s.determineServerBase(null, null);
		assertThat(result).isNull();
	}

}
