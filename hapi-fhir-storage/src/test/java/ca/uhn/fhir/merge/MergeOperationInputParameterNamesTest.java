// Created by claude-opus-4-6
package ca.uhn.fhir.merge;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * These tests assert against hardcoded strings to guard against accidental renames
 * of FHIR operation parameter names. The parameter names are part of the wire protocol
 * (e.g., "source-patient", "target-resource") — changing them is a breaking change for
 * clients. If a constant in ProviderConstants is renamed, these tests will fail and
 * force a deliberate decision rather than a silent drift.
 */
class MergeOperationInputParameterNamesTest {

	@Test
	void testPatientParameterNames() {
		PatientMergeOperationInputParameterNames names = new PatientMergeOperationInputParameterNames();

		assertThat(names.getSourceResourceParameterName()).isEqualTo("source-patient");
		assertThat(names.getTargetResourceParameterName()).isEqualTo("target-patient");
		assertThat(names.getSourceIdentifiersParameterName()).isEqualTo("source-patient-identifier");
		assertThat(names.getTargetIdentifiersParameterName()).isEqualTo("target-patient-identifier");
		assertThat(names.getResultResourceParameterName()).isEqualTo("result-patient");
		assertThat(names.getDeleteSourceParameterName()).isEqualTo("delete-source");
	}

	@Test
	void testGenericParameterNames() {
		GenericMergeOperationInputParameterNames names = new GenericMergeOperationInputParameterNames();

		assertThat(names.getSourceResourceParameterName()).isEqualTo("source-resource");
		assertThat(names.getTargetResourceParameterName()).isEqualTo("target-resource");
		assertThat(names.getSourceIdentifiersParameterName()).isEqualTo("source-resource-identifier");
		assertThat(names.getTargetIdentifiersParameterName()).isEqualTo("target-resource-identifier");
		assertThat(names.getResultResourceParameterName()).isEqualTo("result-resource");
		assertThat(names.getDeleteSourceParameterName()).isEqualTo("delete-source");
	}
}
