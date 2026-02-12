// Created by Claude Code
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

import ca.uhn.fhir.jpa.merge.MergeOperationTestHelper;
import ca.uhn.fhir.jpa.merge.MergeTestParameters;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

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

	@Nested
	class PatientCompartmentTests {

		private static final String TEST_PATIENT_ID = "Patient/compartment-test-patient";

		@BeforeEach
		void beforePatientCompartment() {
			Patient patient = new Patient();
			patient.setId(TEST_PATIENT_ID);
			myClient.update().resource(patient).execute();
		}

		@ParameterizedTest
		@ValueSource(booleans = {true, false})
		void testMergeObservation_differentPatientCompartments_failsWithUnprocessableEntityException(boolean thePreview) {
			IIdType patient2Id = myClient.create().resource(new Patient()).execute().getId().toUnqualifiedVersionless();

			IIdType sourceId = createObservationWithPatient(TEST_PATIENT_ID, "source-id");
			IIdType targetId = createObservationWithPatient(patient2Id.getValue(), "target-id");

			MergeTestParameters params = new MergeTestParameters()
				.sourceResource(new Reference(sourceId))
				.targetResource(new Reference(targetId))
				.deleteSource(false)
				.preview(thePreview);

			String expectedMessage = String.format(
				"Source and target resources belong to different patients and cannot be merged. "
					+ "Source belongs to Patient/compartment-test-patient, Target belongs to Patient/%s.",
				patient2Id.getIdPart());
			myHelper.callMergeAndValidateException(
				"Observation",
				params,
				UnprocessableEntityException.class,
				expectedMessage);
		}

		static Stream<Arguments> sameOrNoPatientScenarios() {
			return Stream.of(
				Arguments.of("both reference same patient", TEST_PATIENT_ID, TEST_PATIENT_ID),
				Arguments.of("only source has patient ref", TEST_PATIENT_ID, null),
				Arguments.of("only target has patient ref", null, TEST_PATIENT_ID),
				Arguments.of("neither has patient ref", null, null)
			);
		}

		@ParameterizedTest(name = "{0}")
		@MethodSource("sameOrNoPatientScenarios")
		void testMergeObservation_sameOrNoPatientCompartment_succeeds(
				String theDescription, String theSourcePatientRef, String theTargetPatientRef) {
			IIdType sourceId = createObservationWithPatient(theSourcePatientRef, "source-id");
			IIdType targetId = createObservationWithPatient(theTargetPatientRef, "target-id");

			MergeTestParameters params = new MergeTestParameters()
				.sourceResource(new Reference(sourceId))
				.targetResource(new Reference(targetId))
				.deleteSource(false)
				.preview(false);

			Parameters outParams = myHelper.callMergeOperation("Observation", params, false);
			myHelper.validateSyncSuccessMessage(outParams);
		}

		private IIdType createObservationWithPatient(@Nullable String thePatientRef, @Nonnull String theIdentifierValue) {
			Observation obs = new Observation();
			obs.setStatus(Observation.ObservationStatus.FINAL);
			obs.getCode().addCoding().setSystem("http://loinc.org").setCode("test-code");
			if (thePatientRef != null) {
				obs.setSubject(new Reference(thePatientRef));
			}
			obs.addIdentifier(new Identifier().setSystem("http://test.org").setValue(theIdentifierValue));
			return myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();
		}
	}
}
