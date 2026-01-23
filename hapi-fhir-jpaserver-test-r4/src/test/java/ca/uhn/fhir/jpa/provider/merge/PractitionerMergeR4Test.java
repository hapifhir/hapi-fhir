// Created by claude-sonnet-4-5
package ca.uhn.fhir.jpa.provider.merge;

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
import ca.uhn.fhir.jpa.merge.PractitionerMergeTestScenario;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Practitioner;

/**
 * Integration tests for generic merge operations on Practitioner resources.
 *
 * <p>All common test methods are inherited from {@link AbstractGenericMergeR4Test}.
 * This class only configures the Practitioner-specific scenario and resource type name.
 */
public class PractitionerMergeR4Test extends AbstractGenericMergeR4Test<Practitioner> {

	@Nonnull
	@Override
	protected AbstractMergeTestScenario<Practitioner> createScenario() {
		return new PractitionerMergeTestScenario(myDaoRegistry, myFhirContext, myLinkServiceFactory, mySrd, myHelper);
	}

	@Nonnull
	@Override
	protected String getResourceTypeName() {
		return "Practitioner";
	}

	// All test methods are inherited from AbstractGenericMergeR4Test
	// Add Practitioner-specific tests here if needed
}
