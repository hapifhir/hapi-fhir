// Created by claude-sonnet-4-5
package ca.uhn.fhir.jpa.merge;

/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.merge.ResourceLinkServiceFactory;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Merge test scenario implementation for Patient resources.
 *
 * <p><b>Key characteristics</b>:
 * <ul>
 *   <li>Patient resources have an "active" field that must be set to false on merge source</li>
 *   <li>Common referencing resources include Encounter, Observation, and CarePlan</li>
 *   <li>References typically use Patient.subject pattern</li>
 * </ul>
 */
public class PatientMergeTestScenario extends AbstractMergeTestScenario<Patient> {

	/**
	 * Create a new Patient merge test scenario.
	 *
	 * @param theDaoRegistry DAO registry for database operations
	 * @param theFhirContext FHIR context
	 * @param theLinkServiceFactory Factory for getting link services
	 * @param theRequestDetails Request details for DAO operations
	 * @param theHelper Helper for merge operations
	 */
	public PatientMergeTestScenario(
			@Nonnull DaoRegistry theDaoRegistry,
			@Nonnull FhirContext theFhirContext,
			@Nonnull ResourceLinkServiceFactory theLinkServiceFactory,
			@Nonnull RequestDetails theRequestDetails,
			@Nonnull MergeOperationTestHelper theHelper) {

		super(theDaoRegistry, theFhirContext, theLinkServiceFactory, theRequestDetails, theHelper);
	}

	@Nonnull
	public String getResourceTypeName() {
		return "Patient";
	}

	@Nonnull
	public Class<Patient> getResourceClass() {
		return Patient.class;
	}

	@Nonnull
	public Patient createResource(@Nonnull List<Identifier> theIdentifiers) {
		Patient p = new Patient();
		p.setActive(true);
		p.addName().setFamily("TestPatient").addGiven("Test");
		p.setIdentifier(theIdentifiers);
		return p;
	}

	@Nonnull
	public IBaseResource createReferencingResource(@Nonnull String theResourceType, @Nonnull IIdType theReferencedId) {

		switch (theResourceType) {
			case "Encounter":
				Encounter enc = new Encounter();
				enc.setStatus(Encounter.EncounterStatus.FINISHED);
				enc.getSubject().setReference(theReferencedId.getValue());
				return enc;

			case "Observation":
				Observation obs = new Observation();
				obs.setStatus(Observation.ObservationStatus.FINAL);
				obs.getCode().addCoding().setSystem("http://loinc.org").setCode("test-code");
				obs.getSubject().setReference(theReferencedId.getValue());
				return obs;

			case "CarePlan":
				CarePlan plan = new CarePlan();
				plan.setStatus(CarePlan.CarePlanStatus.ACTIVE);
				plan.setIntent(CarePlan.CarePlanIntent.PLAN);
				plan.getSubject().setReference(theReferencedId.getValue());
				return plan;

			default:
				throw new IllegalArgumentException(
						"Unsupported resource type for Patient references: " + theResourceType);
		}
	}

	@Nonnull
	@Override
	public IBaseResource createReferencingResource() {
		return createReferencingResource("Encounter", getVersionlessSourceId());
	}

	public AbstractMergeTestScenario<Patient> withOneReferencingResource() {
		return withReferences(List.of(ReferencingTestResourceType.of("Encounter", 1)));
	}

	public AbstractMergeTestScenario<Patient> withMultipleReferencingResources() {
		return withReferences(List.of(
				ReferencingTestResourceType.of("Encounter", 3),
				ReferencingTestResourceType.of("Observation", 2),
				ReferencingTestResourceType.of("CarePlan", 1)));
	}

	public AbstractMergeTestScenario<Patient> withMultipleReferencingResources(int theCount) {
		Validate.isTrue(theCount > 0, "Count must be greater than 0");
		if (theCount >= 3) {
			return withReferences(List.of(
					ReferencingTestResourceType.of("Encounter", 1),
					ReferencingTestResourceType.of("Observation", 1),
					ReferencingTestResourceType.of("CarePlan", theCount - 2)));
		} else if (theCount == 2) {
			return withReferences(List.of(
					ReferencingTestResourceType.of("Encounter", 1), ReferencingTestResourceType.of("Observation", 1)));
		} else {
			return withReferences(List.of(ReferencingTestResourceType.of("Encounter", theCount)));
		}
	}

	@Override
	protected void assertActiveFieldIfSupported(@Nonnull Patient theResource, boolean theExpectedValue) {
		assertThat(theResource.getActive())
				.as("Patient active field should be " + theExpectedValue)
				.isEqualTo(theExpectedValue);
	}
}
