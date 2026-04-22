// Created by Claude Code
package ca.uhn.fhir.jpa.provider.merge;

/*-
 * #%L
 * HAPI FHIR JPA Server Test - R4
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

/**
 * Integration tests for generic undo-merge operations on Observation resources.
 *
 * <p>All common test methods are inherited from {@link AbstractGenericUndoMergeR4Test}.
 * This class only configures the Observation-specific scenario and resource type name.
 *
 * <p><b>Key difference from Practitioner</b>: Observation resources do NOT have
 * an "active" field, so the merge operation does not set source.active=false.
 */
public class ObservationUndoMergeR4Test extends AbstractGenericUndoMergeR4Test<Observation> {

	@Nonnull
	@Override
	protected AbstractMergeTestScenario<Observation> createScenario() {
		return new ObservationMergeTestScenario(myDaoRegistry, myFhirContext, myLinkServiceFactory, mySrd, myHelper);
	}

	@Nonnull
	@Override
	protected String getResourceTypeName() {
		return "Observation";
	}

	// All test methods are inherited from AbstractGenericUndoMergeR4Test
	// Add Observation-specific undo-merge tests here if needed
}
