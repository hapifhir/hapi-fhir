package ca.uhn.fhir.rest.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleBundleProviderTest {

	@Test
	public void testPreferredPageSize() {
		SimpleBundleProvider p = new SimpleBundleProvider();
		assertEquals(null, p.preferredPageSize());

		p.setPreferredPageSize(100);
		assertEquals(100, p.preferredPageSize().intValue());
	}

}
