// Created by claude-sonnet-4-5
package ca.uhn.fhir.jpa.provider.r4;

/*-
 * #%L
 * HAPI FHIR JPA Server Test - R4
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.merge.AbstractMergeTestScenario;
import ca.uhn.fhir.jpa.merge.MergeTestParameters;
import ca.uhn.fhir.jpa.merge.ObservationMergeTestScenario;
import ca.uhn.fhir.jpa.merge.ReferencingResourceConfig;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for generic merge operations on Observation resources.
 *
 * <p>All common test methods are inherited from {@link AbstractGenericMergeR4Test}.
 * This class configures the Observation-specific scenario and adds observation-specific tests.
 *
 * <p><b>Key difference from Practitioner</b>: Observation resources do NOT have
 * an "active" field, so the merge operation does not set source.active=false.
 *
 * <p>Test coverage includes:
 * - Basic merge matrix (16 tests): sync/async × delete/no-delete × preview/execute × with/without result-resource
 * - Identifier resolution (8 tests): source/target by ID or identifiers × sync/async
 * - Extension link validation (6 tests): replaces, replaced-by, no links before, deleted source, multiple types,
 * preserve target
 * - Edge cases and error handling (5 tests): missing source, missing target, source==target, non-existent identifiers
 * - Provenance validation (3 tests): sync, async, not created for preview
 * - Status field validation (2 tests): source/target status handling (via scenario, not directly modified)
 * - Result resource validation (2 tests): overrides target data, preserves references
 * - Observation-specific tests (3 tests): DiagnosticReport references, LOINC identifiers, all standard references
 *
 * <p>Total: 45 comprehensive tests for Observation merge operations.
 */
public class ObservationMergeR4Test extends AbstractGenericMergeR4Test<Observation> {

	@Nonnull
	@Override
	protected AbstractMergeTestScenario<Observation> createScenario() {
		return new ObservationMergeTestScenario(myDaoRegistry, myFhirContext, myLinkServiceFactory, mySrd);
	}

	@Nonnull
	@Override
	protected String getResourceTypeName() {
		return "Observation";
	}

	// ================================================
	// OBSERVATION-SPECIFIC TESTS (3 tests)
	// ================================================

	/**
	 * Test with DiagnosticReport.result references (most common for Observation).
	 */
	@Test
	void testMerge_observationSpecific_diagnosticReportResult() {
		// Setup
		AbstractMergeTestScenario<Observation> scenario = createScenario();
		scenario.withReferences(ReferencingResourceConfig.of("DiagnosticReport", "result", 10));
		scenario.createTestData();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Observation", params, false);

		// Validate all DiagnosticReport.result references updated
		scenario.assertReferencesUpdated("DiagnosticReport");
	}

	/**
	 * Test identifier merging with LOINC codes (common in Observation).
	 */
	@Test
	void testMerge_observationSpecific_loincCodeIdentifiers() {
		// Setup with LOINC-like identifiers
		AbstractMergeTestScenario<Observation> scenario = createScenario();
		scenario.withSourceIdentifiers("8867-4", "2339-0");
		scenario.withTargetIdentifiers("8867-4", "94531-1");
		scenario.withReferences(ReferencingResourceConfig.of("DiagnosticReport", "result", 3));
		scenario.createTestData();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Observation", params, false);

		// Validate target has all identifiers
		Observation target = scenario.readResource(scenario.getTargetId());
		List<Identifier> targetIdentifiers = scenario.getIdentifiersFromResource(target);

		// Should have original target identifiers + non-duplicate source identifiers
		assertThat(targetIdentifiers).hasSizeGreaterThanOrEqualTo(3);
	}

	/**
	 * Test merge with all standard referencing resource types.
	 */
	@Test
	void testMerge_observationSpecific_allStandardReferences() {
		// Setup with standard references
		AbstractMergeTestScenario<Observation> scenario = createScenario();
		scenario.withStandardReferences();
		scenario.createTestData();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Observation", params, false);

		// Validate all reference types updated
		for (String resourceType : scenario.getReferencingResourceTypes()) {
			scenario.assertReferencesUpdated(resourceType);
		}
	}
}
