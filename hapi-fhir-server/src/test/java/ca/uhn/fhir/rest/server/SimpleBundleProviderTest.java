package ca.uhn.fhir.rest.server;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleBundleProviderTest {

	@Test
	public void testPreferredPageSize() {
		SimpleBundleProvider p = new SimpleBundleProvider();
		assertEquals(null, p.preferredPageSize());

		p.setPreferredPageSize(100);
		assertEquals(100, p.preferredPageSize().intValue());
	}

}
