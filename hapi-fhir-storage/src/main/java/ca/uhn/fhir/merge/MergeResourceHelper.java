/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.merge;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import ca.uhn.fhir.util.TerserUtil;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Reference;

import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * This class contains code that is used to update source and target resources after the references are replaced.
 * This is the common functionality that is used in sync case and in the async case as the reduction step.
 */
public class MergeResourceHelper {

	private final DaoRegistry myDaoRegistry;
	private final MergeProvenanceSvc myProvenanceSvc;
	private final ResourceLinkServiceFactory myResourceLinkServiceFactory;
	private final FhirContext myFhirContext;
	private final FhirTerser myFhirTerser;

	public MergeResourceHelper(
			DaoRegistry theDaoRegistry,
			MergeProvenanceSvc theMergeProvenanceSvc,
			ResourceLinkServiceFactory theResourceLinkServiceFactory) {
		myDaoRegistry = theDaoRegistry;
		myProvenanceSvc = theMergeProvenanceSvc;
		myResourceLinkServiceFactory = theResourceLinkServiceFactory;
		myFhirContext = theDaoRegistry.getFhirContext();
		myFhirTerser = myFhirContext.newTerser();
	}

	public static int setResourceLimitFromParameter(
			JpaStorageSettings theStorageSettings, IPrimitiveType<Integer> theResourceLimit) {
		int retval = defaultIfNull(
				IPrimitiveType.toValueOrNull(theResourceLimit),
				ProviderConstants.OPERATION_REPLACE_REFERENCES_RESOURCE_LIMIT_DEFAULT);
		if (retval > theStorageSettings.getMaxTransactionEntriesForWrite()) {
			retval = theStorageSettings.getMaxTransactionEntriesForWrite();
		}
		return retval;
	}

	public static void addInfoToOperationOutcome(
			FhirContext theFhirContext,
			IBaseOperationOutcome theOutcome,
			String theDiagnosticMsg,
			String theDetailsText) {
		IBase issue =
				OperationOutcomeUtil.addIssue(theFhirContext, theOutcome, "information", theDiagnosticMsg, null, null);
		OperationOutcomeUtil.addDetailsToIssue(theFhirContext, issue, null, null, theDetailsText);
	}

	public static void addErrorToOperationOutcome(
			FhirContext theFhirContex, IBaseOperationOutcome theOutcome, String theDiagnosticMsg, String theCode) {
		OperationOutcomeUtil.addIssue(theFhirContex, theOutcome, "error", theDiagnosticMsg, null, theCode);
	}

	public DaoMethodOutcome updateMergedResourcesAfterReferencesReplaced(
			IBaseResource theSourceResource,
			IBaseResource theTargetResource,
			@Nullable IBaseResource theResultResource,
			boolean theIsDeleteSource,
			RequestDetails theRequestDetails) {

		IBaseResource targetToUpdate = prepareTargetResourceForUpdate(
				theTargetResource, theSourceResource, theResultResource, theIsDeleteSource);

		IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(theTargetResource.fhirType());
		DaoMethodOutcome targetOutcome = dao.update(targetToUpdate, theRequestDetails);
		if (!theIsDeleteSource) {
			prepareSourceResourceForUpdate(theSourceResource, theTargetResource);
			dao.update(theSourceResource, theRequestDetails);
		}

		return targetOutcome;
	}

	public void createProvenance(
			IBaseResource theSourceResource,
			IBaseResource theTargetResource,
			List<Bundle> thePatchResultBundles,
			boolean theIsDeleteSource,
			RequestDetails theRequestDetails,
			Date theStartTime,
			List<IProvenanceAgent> theProvenanceAgents,
			List<IBaseResource> theContainedResources) {

		IIdType sourceIdForProvenance = theSourceResource.getIdElement();
		if (theIsDeleteSource) {
			// If the source resource is to be deleted, increment the version id of the source resource to be put in the
			// provenance. Since the resource will be deleted after the provenance is created, its version will be
			// incremented by
			// the delete operation.
			sourceIdForProvenance = theSourceResource
					.getIdElement()
					.withVersion(Long.toString(sourceIdForProvenance.getVersionIdPartAsLong() + 1));
		}
		myProvenanceSvc.createProvenance(
				theTargetResource.getIdElement(),
				sourceIdForProvenance,
				thePatchResultBundles,
				theStartTime,
				theRequestDetails,
				theProvenanceAgents,
				theContainedResources);
	}

	/**
	 * Prepares the target resource for update by:
	 * 1. Using the provided result resource if supplied by the client
	 * 2. Adding a "replaces" link from target to source (if source is not being deleted)
	 * 3. Copying all identifiers from source to target and marking them as "old"
	 * <p>
	 *
	 * @param theTargetResource the target resource that will survive the merge
	 * @param theSourceResource the source resource being merged into the target
	 * @param theResultResource optional result resource provided by the client (may be null)
	 * @param theIsDeleteSource whether the source resource will be deleted after merge
	 * @return the resource to be updated (either the provided result resource or the modified target resource)
	 */
	public IBaseResource prepareTargetResourceForUpdate(
			IBaseResource theTargetResource,
			IBaseResource theSourceResource,
			@Nullable IBaseResource theResultResource,
			boolean theIsDeleteSource) {

		// if the client provided a result resource as input then use it to update the target resource
		if (theResultResource != null) {
			return theResultResource;
		}

		// client did not provide a result resource, we should update the target resource,
		// add the replaces link to the target resource, if the source resource is not to be deleted
		if (!theIsDeleteSource) {
			IResourceLinkService linkService = myResourceLinkServiceFactory.getServiceForResource(theTargetResource);
			Reference sourceRef = new Reference(theSourceResource.getIdElement().toVersionless());
			linkService.addReplacesLink(theTargetResource, sourceRef);
		}

		// copy all identifiers from the source to the target
		copyIdentifiersAndMarkOld(theSourceResource, theTargetResource);

		return theTargetResource;
	}

	/**
	 * Prepares the source resource for update by:
	 * 1. Setting active=false if the resource has an active field (e.g., Patient, Practitioner, Organization)
	 * 2. Adding a "replaced-by" link to the target resource
	 * <p>
	 * This method works generically with any resource type. For resources without an active field
	 * (like Observation), the active field setting is silently skipped.
	 *
	 * @param theSourceResource the source resource being merged (to be marked as inactive/replaced)
	 * @param theTargetResource the target resource that replaces the source
	 */
	private void prepareSourceResourceForUpdate(IBaseResource theSourceResource, IBaseResource theTargetResource) {
		// Set active=false if the resource has an active field
		// Note: Not all resource types have an 'active' field (e.g., Observation doesn't)
		if (myFhirTerser.fieldExists("active", theSourceResource)) {
			IPrimitiveType<?> activePrimitive =
					myFhirTerser.getSingleValueOrNull(theSourceResource, "active", IPrimitiveType.class);
			if (activePrimitive != null) {
				activePrimitive.setValueAsString("false");
			}
		}

		// Add replaced-by link using appropriate strategy (native Patient.link or extension-based)
		IResourceLinkService linkService = myResourceLinkServiceFactory.getServiceForResource(theSourceResource);
		Reference targetRef = new Reference(theTargetResource.getIdElement().toVersionless());
		linkService.addReplacedByLink(theSourceResource, targetRef);
	}

	/**
	 * Copies each identifier from theSourceResource to theTargetResource, after checking that theTargetResource does
	 * not already contain the source identifier. Marks the copied identifiers marked as old.
	 * <p>
	 *
	 * @param theSourceResource the source resource to copy identifiers from
	 * @param theTargetResource the target resource to copy identifiers to
	 */
	private void copyIdentifiersAndMarkOld(IBaseResource theSourceResource, IBaseResource theTargetResource) {
		// Get source identifiers (returns empty list if path doesn't exist or resource has no identifiers)
		List<IBase> sourceIdentifiers = myFhirTerser.getValues(theSourceResource, "identifier");

		if (sourceIdentifiers.isEmpty()) {
			// Resource doesn't have identifiers - skip
			return;
		}

		// Get target identifiers
		List<IBase> targetIdentifiers = myFhirTerser.getValues(theTargetResource, "identifier");

		// Copy each source identifier if not already in target
		for (IBase sourceIdentifier : sourceIdentifiers) {
			if (!containsIdentifier(targetIdentifiers, sourceIdentifier)) {
				// Add a new identifier to target and clone source data into it
				IBase newIdentifierAddedToTarget = myFhirTerser.addElement(theTargetResource, "identifier");
				myFhirTerser.cloneInto(sourceIdentifier, newIdentifierAddedToTarget, false);

				// Set use to OLD
				myFhirTerser.setElement(newIdentifierAddedToTarget, "use", "old");
			}
		}
	}

	/**
	 * Checks if theIdentifiers contains theIdentifier using deep equality comparison
	 *
	 * @param theIdentifiers the list of identifiers
	 * @param theIdentifier  the identifier to check
	 * @return true if theIdentifiers contains theIdentifier, false otherwise
	 */
	private boolean containsIdentifier(List<IBase> theIdentifiers, IBase theIdentifier) {
		for (IBase identifier : theIdentifiers) {
			if (TerserUtil.equals(identifier, theIdentifier)) {
				return true;
			}
		}
		return false;
	}
}
