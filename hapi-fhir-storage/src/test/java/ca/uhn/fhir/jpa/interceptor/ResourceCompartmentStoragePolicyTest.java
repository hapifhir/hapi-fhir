// Created by claude-opus-4-7
package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ResourceCompartmentStoragePolicyTest {

	@ParameterizedTest
	@ValueSource(strings = {
			"ALWAYS_USE_DEFAULT_PARTITION",
			"MANDATORY_SINGLE_COMPARTMENT",
			"OPTIONAL_SINGLE_COMPARTMENT",
			"NON_UNIQUE_COMPARTMENT_IN_DEFAULT",
			"ALWAYS_USE_PARTITION_ID/7"
	})
	void testParse_validPolicyName_returnsPolicyWithMatchingName(String thePolicyName) {
		assertThat(ResourceCompartmentStoragePolicy.parse(thePolicyName).getName())
				.isEqualTo(thePolicyName);
	}

	@Test
	void testParse_alwaysUsePartitionId_storesParsedPartitionId() {
		ResourceCompartmentStoragePolicy policy = ResourceCompartmentStoragePolicy.parse("ALWAYS_USE_PARTITION_ID/7");
		assertThat(policy.getAlwaysUsePartition()).isNotNull();
		assertThat(policy.getAlwaysUsePartition().getFirstPartitionIdOrNull()).isEqualTo(7);
	}

	@Test
	void testParse_alwaysUsePartitionIdWithNonNumericId_throwsIllegalArgumentException() {
		assertThatThrownBy(() -> ResourceCompartmentStoragePolicy.parse("ALWAYS_USE_PARTITION_ID/abc"))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining(Msg.code(2865))
			.hasMessageContaining("Invalid partition ID");
	}

	@Test
	void testParse_unknownPolicyName_throwsConfigurationException() {
		assertThatThrownBy(() -> ResourceCompartmentStoragePolicy.parse("NOT_A_POLICY"))
				.isInstanceOf(ConfigurationException.class)
				.hasMessageContaining(Msg.code(2871))
				.hasMessageContaining("Unknown policy name")
				.hasMessageContaining("NOT_A_POLICY");
	}

	@Test
	void testParse_emptyString_throwsConfigurationException() {
		assertThatThrownBy(() -> ResourceCompartmentStoragePolicy.parse(""))
				.isInstanceOf(ConfigurationException.class)
				.hasMessageContaining(Msg.code(2871))
				.hasMessageContaining("Unknown policy name");
	}
}
