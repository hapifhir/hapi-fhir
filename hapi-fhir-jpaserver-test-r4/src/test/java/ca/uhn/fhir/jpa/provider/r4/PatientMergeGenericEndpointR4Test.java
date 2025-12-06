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
import ca.uhn.fhir.jpa.merge.PatientMergeTestScenario;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Patient;

/**
 * Integration tests for generic merge operations on Patient resources.
 *
 * <p>All common test methods are inherited from {@link AbstractGenericMergeR4Test}.
 * This class only configures the Patient-specific scenario and resource type name.
 *
 * <p>Test coverage includes:
 * - Basic merge matrix: sync/async × delete/no-delete × preview/execute × with/without result-resource
 * - Identifier resolution: source/target by ID or identifiers × sync/async
 * - Zero references edge case: deleteSource × async
 * - Identifier edge cases: empty source/target/both identifiers, result resource overrides
 * - Provenance agent interceptor: async/sync × single/multiple agents, empty agent error
 * - Error handling: missing source/target, source==target, non-existent identifiers, multiple matches,
 *   previously merged resources, resource limit exceeded, concurrent modification
 * - Active field validation: source.active set to false, target.active remains true
 *
 * <p><b>Total: 40+ comprehensive tests inherited from AbstractGenericMergeR4Test</b>
 *
 * <p>Comprehensive tests for Patient merge operations using the modern generic merge framework.
 */
public class PatientMergeGenericEndpointR4Test extends AbstractGenericMergeR4Test<Patient> {

	@Nonnull
	@Override
	protected AbstractMergeTestScenario<Patient> createScenario() {
		return new PatientMergeTestScenario(myDaoRegistry, myFhirContext, myLinkServiceFactory, mySrd, myHelper);
	}

	@Nonnull
	@Override
	protected String getResourceTypeName() {
		return "Patient";
	}
}
