package ca.uhn.fhir.jpa.provider;

import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.UriType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.provider.BaseJpaResourceProviderCodeSystem.applyVersionToSystem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

/**
 * {@literal $lookup} and {@literal $subsumes} take the code system version as its own operation parameter and
 * hand the DAO layer a single {@literal system|version} identifier, which is what applyVersionToSystem builds.
 */
// Created by Claude Opus 5
public class BaseJpaResourceProviderCodeSystemTest {

	private static final String SYSTEM = "http://example.org/fhir/CodeSystem/colour";
	private static final String VERSION = "1.0.0";
	private static final String OTHER_VERSION = "1.0.1";

	private static Stream<Arguments> systemsAndVersions() {
		return Stream.of(
			Arguments.of(SYSTEM, VERSION, SYSTEM + "|" + VERSION, "System and version are joined"),
			Arguments.of(SYSTEM, null, SYSTEM, "No version leaves the system alone"),
			Arguments.of(SYSTEM, "", SYSTEM, "Blank version leaves the system alone"),
			Arguments.of(SYSTEM + "|" + VERSION, OTHER_VERSION, SYSTEM + "|" + VERSION,
				"A system which already names a version does not get a second one"));
	}

	@ParameterizedTest(name = "{3}")
	@MethodSource("systemsAndVersions")
	public void applyVersionToSystem_withDifferentSystemsAndVersions_setsTheVersionedSystem(String theSystem,
																						   String theVersion, String theExpectedSystem, String theMessage) {
		// Setup
		IPrimitiveType<String> system = new UriType(theSystem);

		// Test
		applyVersionToSystem(system, theVersion == null ? null : new StringType(theVersion));

		// Verify
		assertThat(system.getValueAsString()).as(theMessage).isEqualTo(theExpectedSystem);
	}

	@Test
	public void applyVersionToSystem_withoutASystem_doesNothing() {
		assertThatNoException().isThrownBy(() -> applyVersionToSystem(null, new StringType(VERSION)));
	}
}
