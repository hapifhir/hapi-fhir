package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DaoConfigTest {

	@Test
	public void testValidLogicalPattern() {
		new DaoConfig().setTreatBaseUrlsAsLocal(new HashSet<String>(Arrays.asList("http://foo")));
		new DaoConfig().setTreatBaseUrlsAsLocal(new HashSet<String>(Arrays.asList("http://foo*")));
	}

	@Test
	public void testInvalidLogicalPattern() {
		try {
			new DaoConfig().setTreatBaseUrlsAsLocal(new HashSet<String>(Arrays.asList("http://*foo")));
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(1525) + "Base URL wildcard character (*) can only appear at the end of the string: http://*foo", e.getMessage());
		}
		try {
			new DaoConfig().setTreatBaseUrlsAsLocal(new HashSet<String>(Arrays.asList("http://foo**")));
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(1525) + "Base URL wildcard character (*) can only appear at the end of the string: http://foo**", e.getMessage());
		}
	}

	@Test
	public void testDisableStatusBasedReindexUsingSystemProperty() {
		assertEquals(false, new DaoConfig().isStatusBasedReindexingDisabled());
		System.setProperty(DaoConfig.DISABLE_STATUS_BASED_REINDEX, "true");
		assertEquals(true, new DaoConfig().isStatusBasedReindexingDisabled());
		System.clearProperty(DaoConfig.DISABLE_STATUS_BASED_REINDEX);
	}

}
