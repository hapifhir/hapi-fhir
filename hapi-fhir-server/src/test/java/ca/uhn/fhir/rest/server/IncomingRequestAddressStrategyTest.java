package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IncomingRequestAddressStrategyTest {

	@Test
	public void testRequestWithNull() {
		IncomingRequestAddressStrategy s = new IncomingRequestAddressStrategy();
		String result = s.determineServerBase(null, null);
		assertNull(result);
	}

}
