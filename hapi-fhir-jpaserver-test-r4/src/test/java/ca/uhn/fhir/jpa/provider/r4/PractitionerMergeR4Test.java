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

import ca.uhn.fhir.jpa.merge.MergeTestScenario;
import ca.uhn.fhir.jpa.merge.PractitionerMergeTestScenario;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Practitioner;

/**
 * Integration tests for generic merge operations on Practitioner resources.
 *
 * <p>All common test methods are inherited from {@link AbstractGenericMergeR4Test}.
 * This class only configures the Practitioner-specific scenario and resource type name.
 *
 * <p>Test coverage includes:
 * - Basic merge matrix (16 tests): sync/async × delete/no-delete × preview/execute × with/without result-resource
 * - Identifier resolution (8 tests): source/target by ID or identifiers × sync/async
 * - Extension link validation (6 tests): replaces, replaced-by, no links before, deleted source, multiple types,
 * preserve target
 * - Edge cases and error handling (5 tests): missing source, missing target, source==target, non-existent identifiers
 * - Provenance validation (3 tests): sync, async, not created for preview
 * - Active field validation (2 tests): source.active set to false, target.active remains true
 * - Result resource validation (2 tests): overrides target data, preserves references
 *
 * <p>Total: 42 comprehensive tests for Practitioner merge operations.
 */
public class PractitionerMergeR4Test extends AbstractGenericMergeR4Test<Practitioner> {

	@Nonnull
	@Override
	protected MergeTestScenario<Practitioner> createScenario() {
		return new PractitionerMergeTestScenario(myDaoRegistry, myFhirContext, myLinkServiceFactory, mySrd);
	}

	@Nonnull
	@Override
	protected String getResourceTypeName() {
		return "Practitioner";
	}

	// All test methods are inherited from AbstractGenericMergeR4Test
	// Add Practitioner-specific tests here if needed
}
