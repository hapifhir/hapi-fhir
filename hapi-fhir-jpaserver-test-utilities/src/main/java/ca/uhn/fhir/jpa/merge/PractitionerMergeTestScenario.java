// Created by claude-sonnet-4-5
package ca.uhn.fhir.jpa.merge;

/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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

import ca.uhn.fhir.batch2.jobs.merge.ResourceLinkServiceFactory;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Reference;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Merge test scenario implementation for Practitioner resources.
 *
 * This scenario:
 * - Creates Practitioner resources with identifiers and active=true
 * - Creates referencing resources: PractitionerRole, Encounter, CarePlan
 * - Validates extension-based links (since Practitioner doesn't use native links)
 * - Validates active field behavior (set to false on merge source)
 */
public class PractitionerMergeTestScenario extends AbstractMergeTestScenario<Practitioner> {

	/**
	 * Create a new Practitioner merge test scenario.
	 *
	 * @param theDaoRegistry DAO registry for database operations
	 * @param theFhirContext FHIR context
	 * @param theLinkServiceFactory Factory for getting link services
	 * @param theRequestDetails Request details for DAO operations
	 * @param theHelper Helper for merge operations
	 */
	public PractitionerMergeTestScenario(
			@Nonnull DaoRegistry theDaoRegistry,
			@Nonnull FhirContext theFhirContext,
			@Nonnull ResourceLinkServiceFactory theLinkServiceFactory,
			@Nonnull RequestDetails theRequestDetails,
			@Nonnull MergeOperationTestHelper theHelper) {

		super(theDaoRegistry, theFhirContext, theLinkServiceFactory, theRequestDetails, theHelper);
	}

	@Nonnull
	public String getResourceTypeName() {
		return "Practitioner";
	}

	@Nonnull
	public Class<Practitioner> getResourceClass() {
		return Practitioner.class;
	}

	@Nonnull
	public Practitioner createResource(@Nonnull List<Identifier> theIdentifiers) {
		Practitioner p = new Practitioner();
		p.setActive(true);
		p.addName().setFamily("TestPractitioner");
		p.setIdentifier(theIdentifiers);
		return p;
	}

	@Nonnull
	public IBaseResource createReferencingResource(@Nonnull String theResourceType, @Nonnull IIdType theReferencedId) {

		switch (theResourceType) {
			case "PractitionerRole":
				PractitionerRole role = new PractitionerRole();
				role.setPractitioner(new Reference(theReferencedId));
				role.setActive(true);
				return role;

			case "Encounter":
				Encounter enc = new Encounter();
				enc.setStatus(Encounter.EncounterStatus.FINISHED);
				enc.addParticipant().setIndividual(new Reference(theReferencedId));
				return enc;

			case "CarePlan":
				CarePlan plan = new CarePlan();
				plan.setStatus(CarePlan.CarePlanStatus.ACTIVE);
				plan.setIntent(CarePlan.CarePlanIntent.PLAN);
				plan.addActivity().getDetail().addPerformer(new Reference(theReferencedId));
				return plan;

			default:
				throw new IllegalArgumentException(
						"Unsupported resource type for Practitioner references: " + theResourceType);
		}
	}

	@Nonnull
	@Override
	public IBaseResource createReferencingResource() {
		return createReferencingResource("PractitionerRole", getVersionlessSourceId());
	}

	public AbstractMergeTestScenario<Practitioner> withOneReferencingResource() {
		return withReferences(List.of(ReferencingTestResourceType.of("PractitionerRole", 1)));
	}

	public AbstractMergeTestScenario<Practitioner> withMultipleReferencingResources() {
		return withReferences(List.of(
				ReferencingTestResourceType.of("PractitionerRole", 3),
				ReferencingTestResourceType.of("Encounter", 2),
				ReferencingTestResourceType.of("CarePlan", 1)));
	}

	public AbstractMergeTestScenario<Practitioner> withMultipleReferencingResources(int theCount) {
		Validate.isTrue(theCount > 0, "Count must be greater than 0");
		if (theCount >= 3) {
			return withReferences(List.of(
					ReferencingTestResourceType.of("PractitionerRole", 1),
					ReferencingTestResourceType.of("Encounter", 1),
					ReferencingTestResourceType.of("CarePlan", theCount - 2)));
		} else if (theCount == 2) {
			return withReferences(List.of(
					ReferencingTestResourceType.of("PractitionerRole", 1),
					ReferencingTestResourceType.of("Encounter", 1)));
		} else {
			return withReferences(List.of(ReferencingTestResourceType.of("PractitionerRole", theCount)));
		}
	}

	@Override
	protected void assertActiveFieldIfSupported(@Nonnull Practitioner theResource, boolean theExpectedValue) {
		assertThat(theResource.getActive())
				.as("Practitioner active field should be " + theExpectedValue)
				.isEqualTo(theExpectedValue);
	}
}
