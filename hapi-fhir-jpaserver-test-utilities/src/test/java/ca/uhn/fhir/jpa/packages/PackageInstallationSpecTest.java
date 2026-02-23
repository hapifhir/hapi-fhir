package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class PackageInstallationSpecTest {

	@Test
	public void testExampleSupplier() throws IOException {
		PackageInstallationSpec output = new PackageInstallationSpec.ExampleSupplier().get();
		String json = JsonUtil.serialize(output);
		assertThat(json).contains("\"name\" : \"hl7.fhir.us.core\"");

		output = new PackageInstallationSpec.ExampleSupplier2().get();
		json = JsonUtil.serialize(output);
		assertThat(json).contains("\"packageUrl\" : \"classpath:/my-resources.tgz\"");
	}

	@ParameterizedTest
	@EnumSource(PackageInstallationSpec.VersionPolicyEnum.class)
	void testJsonSerializationWithVersionPolicy(PackageInstallationSpec.VersionPolicyEnum theVersionPolicy) throws Exception {
		// Given: A spec with versionPolicy set
		PackageInstallationSpec spec = new PackageInstallationSpec();
		spec.setName("test");
		spec.setVersion("1.0");
		spec.setVersionPolicy(theVersionPolicy);

		// When: Serialize
		String json = JsonUtil.serialize(spec);

		// Then: JSON contains the versionPolicy
		assertThat(json).contains("\"versionPolicy\" : \"" + theVersionPolicy + "\"");
	}

	@ParameterizedTest
	@MethodSource("versionPolicyDeserializationTestCases")
	void testJsonDeserializationWithVersionPolicy(
			PackageInstallationSpec.VersionPolicyEnum theInputPolicy,
			PackageInstallationSpec.VersionPolicyEnum theExpectedPolicy) throws Exception {
		// Build JSON with or without versionPolicy field
		String json = theInputPolicy != null
			? "{\"name\":\"test\",\"version\":\"1.0\",\"versionPolicy\":\"" + theInputPolicy + "\"}"
			: "{\"name\":\"test\",\"version\":\"1.0\"}";

		// When: Deserialize
		PackageInstallationSpec spec = JsonUtil.deserialize(json, PackageInstallationSpec.class);

		// Then: versionPolicy has expected value
		assertThat(spec.getVersionPolicy()).isEqualTo(theExpectedPolicy);
	}

	static Stream<Arguments> versionPolicyDeserializationTestCases() {
		return Stream.of(
			Arguments.of(PackageInstallationSpec.VersionPolicyEnum.SINGLE_VERSION, PackageInstallationSpec.VersionPolicyEnum.SINGLE_VERSION),
			Arguments.of(PackageInstallationSpec.VersionPolicyEnum.MULTI_VERSION, PackageInstallationSpec.VersionPolicyEnum.MULTI_VERSION),
			Arguments.of(null, PackageInstallationSpec.VersionPolicyEnum.MULTI_VERSION)  // missing defaults to MULTI_VERSION
		);
	}
}
