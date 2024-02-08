package ca.uhn.fhir.system;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;
import uk.org.webcompere.systemstubs.properties.SystemProperties;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SystemStubsExtension.class)
class HapiSystemPropertiesTest {

	@SystemStub
	private SystemProperties mySystemProperties;

	@Test
	void isHapiClientKeepResponsesEnabled() {
		// setup
		mySystemProperties.set(HapiSystemProperties.HAPI_CLIENT_KEEPRESPONSES, Boolean.TRUE);
		// execute
		final boolean actual = HapiSystemProperties.isHapiClientKeepResponsesEnabled();
		// validate
		assertThat(actual).isTrue();
	}

	@Test
	void isTestModeEnabled() {
		// setup
		mySystemProperties.set(HapiSystemProperties.TEST_MODE, Boolean.TRUE);
		// execute
		final boolean actual = HapiSystemProperties.isTestModeEnabled();
		// validate
		assertThat(actual).isTrue();
	}

	@Test
	void isUnitTestModeEnabled() {
		// setup
		mySystemProperties.set(HapiSystemProperties.UNIT_TEST_MODE, Boolean.TRUE);
		// execute
		final boolean actual = HapiSystemProperties.isUnitTestModeEnabled();
		// validate
		assertThat(actual).isTrue();
	}

	@Test
	void isUnitTestCaptureStackEnabled() {
		// setup
		mySystemProperties.set(HapiSystemProperties.UNIT_TEST_CAPTURE_STACK, Boolean.TRUE);
		// execute
		final boolean actual = HapiSystemProperties.isUnitTestCaptureStackEnabled();
		// validate
		assertThat(actual).isTrue();
	}

	@Test
	void isDisableStatusBasedReindex() {
		// setup
		mySystemProperties.set(HapiSystemProperties.DISABLE_STATUS_BASED_REINDEX, Boolean.TRUE);
		// execute
		final boolean actual = HapiSystemProperties.isDisableStatusBasedReindex();
		// validate
		assertThat(actual).isTrue();
	}

	@Test
	void isSuppressHapiFhirVersionLogEnabled() {
		// setup
		mySystemProperties.set(HapiSystemProperties.SUPPRESS_HAPI_FHIR_VERSION_LOG, Boolean.TRUE);
		// execute
		final boolean actual = HapiSystemProperties.isSuppressHapiFhirVersionLogEnabled();
		// validate
		assertThat(actual).isTrue();
	}
}
