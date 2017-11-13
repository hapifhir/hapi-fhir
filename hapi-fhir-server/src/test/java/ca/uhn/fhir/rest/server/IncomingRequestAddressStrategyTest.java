package ca.uhn.fhir.rest.server;

import org.junit.Test;

import static org.junit.Assert.*;

public class IncomingRequestAddressStrategyTest {

	@Test
	public void testRequestWithNull() {
		IncomingRequestAddressStrategy s = new IncomingRequestAddressStrategy();
		String result = s.determineServerBase(null, null);
		assertNull(result);
	}

}
