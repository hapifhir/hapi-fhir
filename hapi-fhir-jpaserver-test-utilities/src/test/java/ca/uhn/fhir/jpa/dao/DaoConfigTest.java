package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.system.HapiSystemProperties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DaoConfigTest {

	@Test
	public void testDisableStatusBasedReindexUsingSystemProperty() {
		assertEquals(false, new DaoConfig().isStatusBasedReindexingDisabled());
		HapiSystemProperties.disableStatusBasedReindex();
		assertEquals(true, new DaoConfig().isStatusBasedReindexingDisabled());
		HapiSystemProperties.enableStatusBasedReindex();
	}

}
