package ca.uhn.fhir.rest.server;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleBundleProviderTest {

	@Test
	public void testPreferredPageSize() {
		SimpleBundleProvider p = new SimpleBundleProvider();
		assertThat(p.preferredPageSize()).isEqualTo(null);

		p.setPreferredPageSize(100);
		assertThat(p.preferredPageSize().intValue()).isEqualTo(100);
	}

}
