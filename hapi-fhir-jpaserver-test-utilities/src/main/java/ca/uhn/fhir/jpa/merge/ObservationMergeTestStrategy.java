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
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Reference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test strategy implementation for Observation resource merge tests.
 *
 * This strategy:
 * - Creates Observation resources with identifiers and status=FINAL
 * - Creates referencing resources: DiagnosticReport, Procedure, MedicationRequest
 * - Validates extension-based links (since Observation doesn't use native links)
 * - Does NOT validate active field (Observation has status instead)
 *
 * <p><b>Key difference from Practitioner</b>: Observation resources do NOT have
 * an "active" field, so merge operations should not attempt to set active=false
 * on the source resource.</p>
 */
public class ObservationMergeTestStrategy implements ResourceMergeTestStrategy<Observation> {

	private static final org.slf4j.Logger ourLog =
			org.slf4j.LoggerFactory.getLogger(ObservationMergeTestStrategy.class);

	private final DaoRegistry myDaoRegistry;
	private final ResourceLinkServiceFactory myLinkServiceFactory;
	private final RequestDetails myRequestDetails;

	/**
	 * Create a new Observation merge test strategy.
	 *
	 * @param theDaoRegistry DAO registry for database operations
	 * @param theFhirContext FHIR context (unused, kept for consistency with interface)
	 * @param theLinkServiceFactory Factory for getting link services
	 * @param theRequestDetails Request details for DAO operations
	 */
	public ObservationMergeTestStrategy(
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
		return "Observation";
	}

	@Nonnull
	@Override
	public Class<Observation> getResourceClass() {
		return Observation.class;
	}

	@Nonnull
	@Override
	public Observation createResourceWithIdentifiers(@Nonnull String... theIdentifierValues) {
		Observation obs = new Observation();
		obs.setStatus(Observation.ObservationStatus.FINAL);
		obs.getCode().addCoding().setSystem("http://loinc.org").setCode("test-code");

		for (String value : theIdentifierValues) {
			obs.addIdentifier().setSystem("http://test.org").setValue(value);
		}

		return obs;
	}

	@Nonnull
	@Override
	public Observation createResourceWithIdentifiers(@Nonnull List<Identifier> theIdentifiers) {
		Observation obs = new Observation();
		obs.setStatus(Observation.ObservationStatus.FINAL);
		obs.getCode().addCoding().setSystem("http://loinc.org").setCode("test-code");
		obs.setIdentifier(new ArrayList<>(theIdentifiers));
		return obs;
	}

	@Nonnull
	@Override
	public IBaseResource createReferencingResource(
			@Nonnull String theResourceType, @Nonnull String theReferencePath, @Nonnull IIdType theTargetId) {

		switch (theResourceType) {
			case "DiagnosticReport":
				DiagnosticReport report = new DiagnosticReport();
				report.setStatus(DiagnosticReport.DiagnosticReportStatus.FINAL);
				report.getCode().addCoding().setSystem("http://loinc.org").setCode("report-code");
				report.addResult(new Reference(theTargetId));
				return report;

			case "Procedure":
				Procedure proc = new Procedure();
				proc.setStatus(Procedure.ProcedureStatus.COMPLETED);
				proc.getCode().addCoding().setSystem("http://snomed.info/sct").setCode("procedure-code");
				proc.addReport(new Reference(theTargetId));
				return proc;

			case "MedicationRequest":
				MedicationRequest med = new MedicationRequest();
				med.setStatus(MedicationRequest.MedicationRequestStatus.ACTIVE);
				med.setIntent(MedicationRequest.MedicationRequestIntent.ORDER);
				med.addSupportingInformation(new Reference(theTargetId));
				return med;

			default:
				throw new IllegalArgumentException(
						"Unsupported resource type for Observation references: " + theResourceType);
		}
	}

	@Nonnull
	@Override
	public List<ReferencingResourceConfig> getStandardReferenceConfigs() {
		return Arrays.asList(ReferencingResourceConfig.of("DiagnosticReport", "result", 5));
	}

	@Nonnull
	@Override
	public Observation readResource(@Nonnull IIdType theId) {
		IFhirResourceDao<Observation> dao = myDaoRegistry.getResourceDao(Observation.class);
		return dao.read(theId, myRequestDetails);
	}

	@Nonnull
	@Override
	public List<Identifier> getIdentifiersFromResource(@Nonnull Observation theResource) {
		return new ArrayList<>(theResource.getIdentifier());
	}

	@Override
	public void addReplacesLinkToResource(@Nonnull Observation theResource, @Nonnull IIdType theTargetId) {
		IResourceLinkService linkService = myLinkServiceFactory.getServiceForResource(theResource);
		Reference targetRef = new Reference(theTargetId.toVersionless());
		linkService.addReplacesLink(theResource, targetRef);
		ourLog.debug("Added replaces link to {} pointing to {}", theResource.getIdElement(), theTargetId);
	}

	@Nonnull
	@Override
	public Observation cloneResource(@Nonnull Observation theResource) {
		return theResource.copy();
	}

	@Override
	public void assertSourceResourceState(
			@Nullable Observation theResource,
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
			IResourceLinkService linkService = myLinkServiceFactory.getServiceForResourceType("Observation");
			List<IBaseReference> replacedByLinksRefs = linkService.getReplacedByLinks(theResource);

			assertThat(replacedByLinksRefs)
					.as("Source should have replaced-by link")
					.hasSize(1)
					.element(0)
					.satisfies(link -> assertThat(link.getReferenceElement().toUnqualifiedVersionless())
							.isEqualTo(theTargetId.toUnqualifiedVersionless()));

			// NOTE: Observation does NOT have active field - we do not check it
			ourLog.debug("Verified source resource state: has replaced-by link (no active field for Observation)");
		}
	}

	@Override
	public void assertTargetResourceState(
			@Nonnull Observation theResource,
			@Nonnull IIdType theSourceId,
			boolean theSourceDeleted,
			@Nonnull List<Identifier> theExpectedIdentifiers) {

		// Should have replaces link only if source was not deleted
		// (when source is deleted, we don't want a dangling reference)
		if (!theSourceDeleted) {
			IResourceLinkService linkService = myLinkServiceFactory.getServiceForResourceType("Observation");
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

		// NOTE: Observation does NOT have active field - we do not check it
		ourLog.debug("Verified target resource state: correct identifiers (no active field for Observation)");
	}

	@Override
	public void assertLinksPresent(
			@Nonnull Observation theResource, @Nonnull List<IIdType> theExpectedLinks, @Nonnull String theLinkType) {

		IResourceLinkService linkService = myLinkServiceFactory.getServiceForResourceType("Observation");

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
	public void assertNoLinks(@Nonnull Observation theResource) {
		IResourceLinkService linkService = myLinkServiceFactory.getServiceForResourceType("Observation");

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
		// KEY DIFFERENCE: Observation does NOT have active field
		return false;
	}

	@Override
	public boolean hasIdentifierField() {
		return true;
	}

	@Nonnull
	@Override
	public List<Identifier> calculateExpectedIdentifiersAfterMerge(
			@Nonnull Observation theSource, @Nonnull Observation theTarget, boolean theWithResultResource) {

		if (theWithResultResource) {
			// Result resource provided - identifiers come from there
			// For now, return target identifiers (actual logic would use result resource)
			return new ArrayList<>(theTarget.getIdentifier());
		}

		// Merge logic: target keeps its identifiers, source identifiers marked as "old" are added
		List<Identifier> expected = new ArrayList<>(theTarget.getIdentifier());

		for (Identifier sourceId : theSource.getIdentifier()) {
			boolean alreadyPresent = expected.stream()
					.anyMatch(targetId -> sourceId.getSystem().equals(targetId.getSystem())
							&& sourceId.getValue().equals(targetId.getValue()));

			if (!alreadyPresent) {
				Identifier copy = sourceId.copy();
				copy.setUse(Identifier.IdentifierUse.OLD);
				expected.add(copy);
			}
		}

		return expected;
	}
}
