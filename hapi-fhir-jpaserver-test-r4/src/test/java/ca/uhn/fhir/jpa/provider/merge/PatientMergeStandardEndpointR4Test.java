// Created by claude-opus-4-6
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
import ca.uhn.fhir.jpa.merge.PatientMergeTestScenario;
import ca.uhn.fhir.merge.AbstractMergeOperationInputParameterNames;
import ca.uhn.fhir.merge.PatientMergeOperationInputParameterNames;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.Patient;

/**
 * Integration tests for the FHIR-standard Patient/$merge operation.
 *
 * <p>This test class validates the Patient-specific merge endpoint using Patient-specific
 * parameter names (e.g., "source-patient", "target-patient").
 *
 * <p>All common test methods are inherited from {@link AbstractGenericMergeR4Test}.
 * This class only configures the Patient-specific scenario, operation name, and parameter names.
 *
 * <p>For tests of the generic merge endpoint (Patient/$hapi.fhir.merge), see
 * {@link PatientMergeGenericEndpointR4Test}.
 */
public class PatientMergeStandardEndpointR4Test extends AbstractGenericMergeR4Test<Patient> {

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

	@Nonnull
	@Override
	protected String getOperationName() {
		return "$merge";
	}

	@Nonnull
	@Override
	protected AbstractMergeOperationInputParameterNames getParameterNames() {
		return new PatientMergeOperationInputParameterNames();
	}
}
