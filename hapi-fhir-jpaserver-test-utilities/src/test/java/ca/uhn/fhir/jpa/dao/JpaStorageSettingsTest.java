package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.system.HapiSystemProperties;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JpaStorageSettingsTest {

	@Test
	public void testValidLogicalPattern() {
		new JpaStorageSettings().setTreatBaseUrlsAsLocal(new HashSet<>(List.of("http://foo")));
		new JpaStorageSettings().setTreatBaseUrlsAsLocal(new HashSet<>(List.of("http://foo*")));
	}

	@Test
	public void testInvalidLogicalPattern() {
		try {
			new JpaStorageSettings().setTreatBaseUrlsAsLocal(new HashSet<>(List.of("http://*foo")));
			fail("");
		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(1525) + "Base URL wildcard character (*) can only appear at the end of the string: http://*foo", e.getMessage());
		}
		try {
			new JpaStorageSettings().setTreatBaseUrlsAsLocal(new HashSet<>(List.of("http://foo**")));
			fail("");
		} catch (IllegalArgumentException e) {
			assertEquals(Msg.code(1525) + "Base URL wildcard character (*) can only appear at the end of the string: http://foo**", e.getMessage());
		}
	}

	@Test
	public void testDisableStatusBasedReindexUsingSystemProperty() {
		assertEquals(false, new JpaStorageSettings().isStatusBasedReindexingDisabled());
		HapiSystemProperties.disableStatusBasedReindex();
		assertEquals(true, new JpaStorageSettings().isStatusBasedReindexingDisabled());
		HapiSystemProperties.enableStatusBasedReindex();
	}

}
