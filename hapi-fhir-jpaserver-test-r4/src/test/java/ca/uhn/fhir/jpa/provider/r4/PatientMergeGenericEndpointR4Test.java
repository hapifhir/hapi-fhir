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
 * Integration tests for the Patient/$hapi.fhir.merge operation.
 *
 * <p>This test class validates the HAPI FHIR generic merge endpoint that works with any
 * resource type, using generic parameter names (e.g., "source-resource", "target-resource").
 *
 * <p>All common test methods are inherited from {@link AbstractGenericMergeR4Test}.
 * This class only configures the Patient-specific scenario and resource type name.
 *
 * <p>For tests of the standard FHIR Patient/$merge endpoint, see {@link PatientMergeR4Test}.
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
