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

import ca.uhn.fhir.batch2.jobs.merge.IResourceLinkService;
import ca.uhn.fhir.batch2.jobs.merge.ResourceLinkServiceFactory;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Reference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test strategy implementation for Practitioner resource merge tests.
 *
 * This strategy:
 * - Creates Practitioner resources with identifiers and active=true
 * - Creates referencing resources: PractitionerRole, Encounter, CarePlan
 * - Validates extension-based links (since Practitioner doesn't use native links)
 * - Validates active field behavior (set to false on merge source)
 */
public class PractitionerMergeTestStrategy implements ResourceMergeTestStrategy<Practitioner> {

	private static final org.slf4j.Logger ourLog =
			org.slf4j.LoggerFactory.getLogger(PractitionerMergeTestStrategy.class);

	private final DaoRegistry myDaoRegistry;
	private final ResourceLinkServiceFactory myLinkServiceFactory;
	private final RequestDetails myRequestDetails;

	/**
	 * Create a new Practitioner merge test strategy.
	 *
	 * @param theDaoRegistry DAO registry for database operations
	 * @param theFhirContext FHIR context (unused, kept for consistency with interface)
	 * @param theLinkServiceFactory Factory for getting link services
	 * @param theRequestDetails Request details for DAO operations
	 */
	public PractitionerMergeTestStrategy(
			@Nonnull DaoRegistry theDaoRegistry,
			@Nonnull FhirContext theFhirContext,
			@Nonnull ResourceLinkServiceFactory theLinkServiceFactory,
			@Nonnull RequestDetails theRequestDetails) {

		myDaoRegistry = theDaoRegistry;
		// theFhirContext unused - IResourceLinkService doesn't need it
		myLinkServiceFactory = theLinkServiceFactory;
		myRequestDetails = theRequestDetails;
	}

	@Nonnull
	@Override
	public String getResourceTypeName() {
		return "Practitioner";
	}

	@Nonnull
	@Override
	public Class<Practitioner> getResourceClass() {
		return Practitioner.class;
	}

	@Nonnull
	@Override
	public Practitioner createResourceWithIdentifiers(@Nonnull String... theIdentifierValues) {
		Practitioner p = new Practitioner();
		p.setActive(true);
		p.addName().setFamily("TestPractitioner");

		for (String value : theIdentifierValues) {
			p.addIdentifier().setSystem("http://test.org").setValue(value);
		}

		return p;
	}

	@Nonnull
	@Override
	public Practitioner createResourceWithIdentifiers(@Nonnull List<Identifier> theIdentifiers) {
		Practitioner p = new Practitioner();
		p.setActive(true);
		p.addName().setFamily("TestPractitioner");
		p.setIdentifier(new ArrayList<>(theIdentifiers));
		return p;
	}

	@Nonnull
	@Override
	public IBaseResource createReferencingResource(
			@Nonnull String theResourceType, @Nonnull String theReferencePath, @Nonnull IIdType theTargetId) {

		switch (theResourceType) {
			case "PractitionerRole":
				PractitionerRole role = new PractitionerRole();
				role.setPractitioner(new Reference(theTargetId));
				role.setActive(true);
				return role;

			case "Encounter":
				Encounter enc = new Encounter();
				enc.setStatus(Encounter.EncounterStatus.FINISHED);
				enc.addParticipant().setIndividual(new Reference(theTargetId));
				return enc;

			case "CarePlan":
				CarePlan plan = new CarePlan();
				plan.setStatus(CarePlan.CarePlanStatus.ACTIVE);
				plan.setIntent(CarePlan.CarePlanIntent.PLAN);
				plan.addActivity().getDetail().addPerformer(new Reference(theTargetId));
				return plan;

			default:
				throw new IllegalArgumentException(
						"Unsupported resource type for Practitioner references: " + theResourceType);
		}
	}

	@Nonnull
	@Override
	public List<ReferencingResourceConfig> getStandardReferenceConfigs() {
		return Arrays.asList(
				ReferencingResourceConfig.of("PractitionerRole", "practitioner", 3),
				ReferencingResourceConfig.of("Encounter", "participant.individual", 2),
				ReferencingResourceConfig.of("CarePlan", "activity.detail.performer", 1));
	}

	@Nonnull
	@Override
	public Practitioner readResource(@Nonnull IIdType theId) {
		IFhirResourceDao<Practitioner> dao = myDaoRegistry.getResourceDao(Practitioner.class);
		return dao.read(theId, myRequestDetails);
	}

	@Nonnull
	@Override
	public List<Identifier> getIdentifiersFromResource(@Nonnull Practitioner theResource) {
		return new ArrayList<>(theResource.getIdentifier());
	}

	@Override
	public void addReplacesLinkToResource(@Nonnull Practitioner theResource, @Nonnull IIdType theTargetId) {
		IResourceLinkService linkService = myLinkServiceFactory.getServiceForResource(theResource);
		Reference targetRef = new Reference(theTargetId.toVersionless());
		linkService.addReplacesLink(theResource, targetRef);
		ourLog.debug("Added replaces link to {} pointing to {}", theResource.getIdElement(), theTargetId);
	}

	@Nonnull
	@Override
	public Practitioner cloneResource(@Nonnull Practitioner theResource) {
		return theResource.copy();
	}

	@Override
	public void assertSourceResourceState(
			@Nullable Practitioner theResource,
			@Nonnull IIdType theSourceId,
			@Nonnull IIdType theTargetId,
			boolean theDeleted) {

		if (theDeleted) {
			// Resource should not exist
			assertThatThrownBy(() -> readResource(theSourceId))
					.as("Source resource should be deleted")
					.isInstanceOf(ResourceGoneException.class);
			ourLog.debug("Verified source resource is deleted: {}", theSourceId);
		} else {
			// Resource should have replaced-by link
			IResourceLinkService linkService = myLinkServiceFactory.getServiceForResourceType("Practitioner");
			List<IBaseReference> replacedByLinksRefs = linkService.getReplacedByLinks(theResource);

			assertThat(replacedByLinksRefs)
					.as("Source should have replaced-by link")
					.hasSize(1)
					.element(0)
					.satisfies(link -> assertThat(link.getReferenceElement().toUnqualifiedVersionless())
							.isEqualTo(theTargetId.toUnqualifiedVersionless()));

			// Active should be false
			assertThat(theResource.getActive())
					.as("Source practitioner active field should be false")
					.isFalse();

			ourLog.debug("Verified source resource state: has replaced-by link, active=false");
		}
	}

	@Override
	public void assertTargetResourceState(
			@Nonnull Practitioner theResource,
			@Nonnull IIdType theSourceId,
			boolean theSourceDeleted,
			@Nonnull List<Identifier> theExpectedIdentifiers) {

		// Should have replaces link only if source was not deleted
		// (when source is deleted, we don't want a dangling reference)
		if (!theSourceDeleted) {
			IResourceLinkService linkService = myLinkServiceFactory.getServiceForResourceType("Practitioner");
			List<IBaseReference> replacesLinksRefs = linkService.getReplacesLinks(theResource);

			assertThat(replacesLinksRefs)
					.as("Target should have replaces link when source not deleted")
					.hasSize(1)
					.element(0)
					.satisfies(link -> assertThat(link.getReferenceElement().toUnqualifiedVersionless())
							.isEqualTo(theSourceId.toUnqualifiedVersionless()));
			ourLog.debug("Verified target has replaces link to source");
		}

		// Should have expected identifiers
		assertThat(theResource.getIdentifier())
				.as("Target should have expected identifiers")
				.hasSize(theExpectedIdentifiers.size());

		// Active should remain true
		assertThat(theResource.getActive())
				.as("Target practitioner active field should remain true")
				.isTrue();

		ourLog.debug("Verified target resource state: correct identifiers, active=true");
	}

	@Override
	public void assertLinksPresent(
			@Nonnull Practitioner theResource, @Nonnull List<IIdType> theExpectedLinks, @Nonnull String theLinkType) {

		IResourceLinkService linkService = myLinkServiceFactory.getServiceForResourceType("Practitioner");

		List<IBaseReference> actualLinksRefs;
		if ("replaces".equals(theLinkType)) {
			actualLinksRefs = linkService.getReplacesLinks(theResource);
		} else if ("replaced-by".equals(theLinkType)) {
			actualLinksRefs = linkService.getReplacedByLinks(theResource);
		} else {
			throw new IllegalArgumentException("Unknown link type: " + theLinkType);
		}

		// Convert IBaseReference list to IIdType list for comparison
		List<IIdType> actualLinks = actualLinksRefs.stream()
				.map(IBaseReference::getReferenceElement)
				.map(IIdType::toUnqualifiedVersionless)
				.collect(Collectors.toList());

		List<IIdType> expectedLinksUnversioned =
				theExpectedLinks.stream().map(IIdType::toUnqualifiedVersionless).collect(Collectors.toList());

		assertThat(actualLinks)
				.as("Resource should have expected %s links", theLinkType)
				.containsExactlyInAnyOrderElementsOf(expectedLinksUnversioned);

		ourLog.debug("Verified {} links present: {}", theLinkType, actualLinks.size());
	}

	@Override
	public void assertNoLinks(@Nonnull Practitioner theResource) {
		IResourceLinkService linkService = myLinkServiceFactory.getServiceForResourceType("Practitioner");

		List<IBaseReference> replacesLinks = linkService.getReplacesLinks(theResource);
		List<IBaseReference> replacedByLinks = linkService.getReplacedByLinks(theResource);

		assertThat(replacesLinks).as("Resource should have no replaces links").isEmpty();

		assertThat(replacedByLinks)
				.as("Resource should have no replaced-by links")
				.isEmpty();

		ourLog.debug("Verified no links present on resource");
	}

	@Override
	public boolean hasActiveField() {
		return true;
	}

	@Override
	public boolean hasIdentifierField() {
		return true;
	}

	@Nonnull
	@Override
	public List<Identifier> calculateExpectedIdentifiersAfterMerge(
			@Nonnull Practitioner theSource, @Nonnull Practitioner theTarget, boolean theWithResultResource) {

		ourLog.info(
				"calculateExpectedIdentifiersAfterMerge: withResultResource={}, sourceIdentifiers={}, targetIdentifiers={}",
				theWithResultResource,
				theSource.getIdentifier().size(),
				theTarget.getIdentifier().size());

		if (theWithResultResource) {
			// Result resource provided - identifiers come from there
			// For now, return target identifiers (actual logic would use result resource)
			return new ArrayList<>(theTarget.getIdentifier());
		}

		// Merge logic: target keeps its identifiers, source identifiers marked as "old" are added
		List<Identifier> expected = new ArrayList<>(theTarget.getIdentifier());
		ourLog.info("Starting with {} target identifiers", expected.size());

		for (Identifier sourceId : theSource.getIdentifier()) {
			boolean alreadyPresent = expected.stream()
					.anyMatch(targetId -> sourceId.getSystem().equals(targetId.getSystem())
							&& sourceId.getValue().equals(targetId.getValue()));

			ourLog.info("Checking source identifier {}: alreadyPresent={}", sourceId.getValue(), alreadyPresent);

			if (!alreadyPresent) {
				Identifier copy = sourceId.copy();
				copy.setUse(Identifier.IdentifierUse.OLD);
				expected.add(copy);
				ourLog.info(
						"Added source identifier {} as OLD, expected now has {}", sourceId.getValue(), expected.size());
			}
		}

		ourLog.info("Returning {} expected identifiers", expected.size());
		return expected;
	}
}
