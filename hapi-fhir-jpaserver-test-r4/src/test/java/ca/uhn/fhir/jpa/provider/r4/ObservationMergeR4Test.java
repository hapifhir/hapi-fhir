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
import ca.uhn.fhir.jpa.merge.ObservationMergeTestScenario;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.Test;

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
 * - Basic merge matrix: sync/async × delete/no-delete × preview/execute × with/without result-resource
 * - Identifier resolution: source/target by ID or identifiers × sync/async
 * - Extension link validation: replaces, replaced-by, no links before, deleted source, multiple types,
 * preserve target
 * - Edge cases and error handling: missing source, missing target, source==target, non-existent identifiers
 * - Provenance validation: sync, async, not created for preview
 * - Status field validation: source/target status handling (via scenario, not directly modified)
 * - Result resource validation: overrides target data, preserves references
 * - Observation-specific tests: DiagnosticReport references, all standard references
 *
 * <p>Comprehensive tests for Observation merge operations.
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
	// OBSERVATION-SPECIFIC TESTS
	// ================================================

	/**
	 * Test with DiagnosticReport.result references (most common for Observation).
	 */
	@Test
	void testMerge_observationSpecific_diagnosticReportResult() {
		// Setup
		AbstractMergeTestScenario<Observation> scenario = createScenario();
		scenario.withMultipleReferencingResources();
		scenario.createTestData();

		// Execute merge
		myHelper.callMergeOperation(scenario, false);

		// Validate all DiagnosticReport.result references updated
		scenario.assertReferencesUpdated("DiagnosticReport");
	}

	/**
	 * Test merge with all standard referencing resource types.
	 */
	@Test
	void testMerge_observationSpecific_allStandardReferences() {
		// Setup with standard references
		AbstractMergeTestScenario<Observation> scenario = createScenario();
		scenario.withMultipleReferencingResources();
		scenario.createTestData();

		// Execute merge
		myHelper.callMergeOperation(scenario, false);

		// Validate all reference types updated
		for (String resourceType : scenario.getReferencingResourceTypes()) {
			scenario.assertReferencesUpdated(resourceType);
		}
	}
}
