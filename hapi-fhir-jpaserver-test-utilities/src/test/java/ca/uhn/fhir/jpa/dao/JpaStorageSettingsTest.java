package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.system.HapiSystemProperties;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static ca.uhn.fhir.jpa.api.config.JpaStorageSettings.MIN_SEARCH_URL_CLEANUP_BATCH_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

	@Test
	void testSearchUrlCleanupBatchSize_defaultAndBounds_areAccepted() {
		JpaStorageSettings settings = new JpaStorageSettings();
		assertThat(settings.getSearchUrlCleanupBatchSize())
				.isEqualTo(JpaStorageSettings.DEFAULT_SEARCH_URL_CLEANUP_BATCH_SIZE);

		settings.setSearchUrlCleanupBatchSize(MIN_SEARCH_URL_CLEANUP_BATCH_SIZE);
		assertThat(settings.getSearchUrlCleanupBatchSize()).isEqualTo(MIN_SEARCH_URL_CLEANUP_BATCH_SIZE);
		settings.setSearchUrlCleanupBatchSize(JpaStorageSettings.MAX_SEARCH_URL_CLEANUP_BATCH_SIZE);
		assertThat(settings.getSearchUrlCleanupBatchSize())
				.isEqualTo(JpaStorageSettings.MAX_SEARCH_URL_CLEANUP_BATCH_SIZE);
	}

	@Test
	void testSearchUrlCleanupBatchSize_outOfRange_isRejected() {
		JpaStorageSettings settings = new JpaStorageSettings();
		assertThatThrownBy(() -> settings.setSearchUrlCleanupBatchSize(MIN_SEARCH_URL_CLEANUP_BATCH_SIZE - 1))
				.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() ->
				settings.setSearchUrlCleanupBatchSize(JpaStorageSettings.MAX_SEARCH_URL_CLEANUP_BATCH_SIZE + 1))
				.isInstanceOf(IllegalArgumentException.class);
	}

}
