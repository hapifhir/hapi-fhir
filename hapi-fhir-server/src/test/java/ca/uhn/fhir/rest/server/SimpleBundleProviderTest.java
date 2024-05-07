package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleBundleProviderTest {

	@Test
	public void testPreferredPageSize() {
		SimpleBundleProvider p = new SimpleBundleProvider();
		assertNull(p.preferredPageSize());

		p.setPreferredPageSize(100);
		assertEquals(100, p.preferredPageSize().intValue());
	}

}
