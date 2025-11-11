// Created by Claude Code
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

import ca.uhn.fhir.jpa.merge.MergeOperationTestHelper;
import ca.uhn.fhir.jpa.merge.MergeTestParameters;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

/**
 * Tests for edge cases in merge operations that don't fit the generic resource pattern.
 * These tests validate error handling for unusual scenarios like resource types without identifiers.
 */
public class MergeEdgeCaseR4Test extends BaseResourceProviderR4Test {

	@Autowired
	protected Batch2JobHelper myBatch2JobHelper;

	protected MergeOperationTestHelper myHelper;

	@BeforeEach
	public void beforeMergeEdgeCase() {
		myHelper = new MergeOperationTestHelper(myClient, myBatch2JobHelper, myFhirContext);
	}

	@Test
	void testMerge_resourceTypeWithoutIdentifierElement_failsWithUnprocessableEntityException() {
		// Setup: Build parameters for OperationOutcome (a resource type that doesn't have 'identifier' element)
		MergeTestParameters params = new MergeTestParameters()
			.sourceResource(new Reference("OperationOutcome/source-id"))
			.targetResource(new Reference("OperationOutcome/target-id"))
			.deleteSource(false)
			.preview(false);

		// Execute and validate error
		myHelper.callMergeAndValidateException(
			"OperationOutcome",
			params,
			UnprocessableEntityException.class,
			"Merge operation cannot be performed on resource type 'OperationOutcome' because it does not have an 'identifier' element.");
	}

	static Stream<Arguments> resourceTypeMismatchScenarios() {
		return Stream.of(
			Arguments.of("Patient", "Practitioner", "Patient", List.of("The request was for \"Patient\" type but source-resource contains a reference for \"Practitioner\" type")),
			Arguments.of("Patient", "Patient", "Practitioner", List.of("The request was for \"Patient\" type but target-resource contains a reference for \"Practitioner\" type")),
			Arguments.of("Practitioner", "Patient", "Practitioner", List.of("The request was for \"Practitioner\" type but source-resource contains a reference for \"Patient\" type")),
			Arguments.of("Practitioner", "Practitioner", "Patient", List.of("The request was for \"Practitioner\" type but target-resource contains a reference for \"Patient\" type")),
			Arguments.of("Practitioner", "Patient", "Patient", List.of(
				"The request was for \"Practitioner\" type but source-resource contains a reference for \"Patient\" type",
				"The request was for \"Practitioner\" type but target-resource contains a reference for \"Patient\" type"
			))
		);
	}

	@ParameterizedTest
	@MethodSource("resourceTypeMismatchScenarios")
	void testMerge_resourceTypeMismatch_failsWithInvalidRequestException(
			String endpointResourceType,
			String sourceResourceType,
			String targetResourceType,
			List<String> expectedErrorMessages) {
		// Setup: Create both Patient and Practitioner resources upfront
		Patient patient = new Patient();
		IIdType patientId = myClient.create()
			.resource(patient)
			.execute()
			.getId()
			.toUnqualifiedVersionless();

		Practitioner practitioner = new Practitioner();
		IIdType practitionerId = myClient.create()
			.resource(practitioner)
			.execute()
			.getId()
			.toUnqualifiedVersionless();

		// Determine which IDs to use based on test parameters
		IIdType sourceId = "Patient".equals(sourceResourceType) ? patientId : practitionerId;
		IIdType targetId = "Patient".equals(targetResourceType) ? patientId : practitionerId;

		// Build parameters
		MergeTestParameters params = new MergeTestParameters()
			.sourceResource(new Reference(sourceId))
			.targetResource(new Reference(targetId))
			.deleteSource(false)
			.preview(false);

		// Execute and validate error
		myHelper.callMergeAndValidateException(
			endpointResourceType,
			params,
			InvalidRequestException.class,
			expectedErrorMessages.toArray(new String[0]));
	}
}
