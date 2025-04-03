/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.merge;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * This class contains code that is used to update source and target resources after the references are replaced.
 * This is the common functionality that is used in sync case and in the async case as the reduction step.
 */
public class MergeResourceHelper {

	private final IFhirResourceDao<Patient> myPatientDao;
	private final MergeProvenanceSvc myProvenanceSvc;

	public MergeResourceHelper(DaoRegistry theDaoRegistry, MergeProvenanceSvc theMergeProvenanceSvc) {
		myPatientDao = theDaoRegistry.getResourceDao(Patient.class);
		myProvenanceSvc = theMergeProvenanceSvc;
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

	public void updateMergedResourcesAndCreateProvenance(
			IHapiTransactionService myHapiTransactionService,
			IdDt theSourceResourceId,
			IdDt theTargetResourceId,
			List<Bundle> thePatchResultBundles,
			@Nullable Patient theResultResource,
			boolean theDeleteSource,
			RequestDetails theRequestDetails,
			Date theStartTime) {
		Patient sourceResource = myPatientDao.read(theSourceResourceId, theRequestDetails);
		Patient targetResource = myPatientDao.read(theTargetResourceId, theRequestDetails);

		updateMergedResourcesAndCreateProvenance(
				myHapiTransactionService,
				sourceResource,
				targetResource,
				thePatchResultBundles,
				theResultResource,
				theDeleteSource,
				theRequestDetails,
				theStartTime);
	}

	public Patient updateMergedResourcesAndCreateProvenance(
			IHapiTransactionService myHapiTransactionService,
			Patient theSourceResource,
			Patient theTargetResource,
			List<Bundle> thePatchResultBundles,
			@Nullable Patient theResultResource,
			boolean theDeleteSource,
			RequestDetails theRequestDetails,
			Date theStartTime) {

		AtomicReference<Patient> targetPatientAfterUpdate = new AtomicReference<>();
		myHapiTransactionService.withRequest(theRequestDetails).execute(() -> {
			Patient patientToUpdate = prepareTargetPatientForUpdate(
					theTargetResource, theSourceResource, theResultResource, theDeleteSource);

			targetPatientAfterUpdate.set(updateResource(patientToUpdate, theRequestDetails));
			Patient sourcePatientAfterUpdate = null;
			if (theDeleteSource) {
				deleteResource(theSourceResource, theRequestDetails);
			} else {
				prepareSourcePatientForUpdate(theSourceResource, theTargetResource);
				sourcePatientAfterUpdate = updateResource(theSourceResource, theRequestDetails);
			}

			myProvenanceSvc.createProvenance(
					targetPatientAfterUpdate.get().getIdElement(),
					theDeleteSource ? null : sourcePatientAfterUpdate.getIdElement(),
					thePatchResultBundles,
					theStartTime,
					theRequestDetails);
		});

		return targetPatientAfterUpdate.get();
	}

	public Patient prepareTargetPatientForUpdate(
			Patient theTargetResource,
			Patient theSourceResource,
			@Nullable Patient theResultResource,
			boolean theDeleteSource) {

		// if the client provided a result resource as input then use it to update the target resource
		if (theResultResource != null) {
			return theResultResource;
		}

		// client did not provide a result resource, we should update the target resource,
		// add the replaces link to the target resource, if the source resource is not to be deleted
		if (!theDeleteSource) {
			theTargetResource
					.addLink()
					.setType(Patient.LinkType.REPLACES)
					.setOther(new Reference(theSourceResource.getIdElement().toVersionless()));
		}

		// copy all identifiers from the source to the target
		copyIdentifiersAndMarkOld(theSourceResource, theTargetResource);

		return theTargetResource;
	}

	private void prepareSourcePatientForUpdate(Patient theSourceResource, Patient theTargetResource) {
		theSourceResource.setActive(false);
		theSourceResource
				.addLink()
				.setType(Patient.LinkType.REPLACEDBY)
				.setOther(new Reference(theTargetResource.getIdElement().toVersionless()));
	}

	/**
	 * Copies each identifier from theSourceResource to theTargetResource, after checking that theTargetResource does
	 * not already contain the source identifier. Marks the copied identifiers marked as old.
	 *
	 * @param theSourceResource the source resource to copy identifiers from
	 * @param theTargetResource the target resource to copy identifiers to
	 */
	private void copyIdentifiersAndMarkOld(Patient theSourceResource, Patient theTargetResource) {
		if (theSourceResource.hasIdentifier()) {
			List<Identifier> sourceIdentifiers = theSourceResource.getIdentifier();
			List<Identifier> targetIdentifiers = theTargetResource.getIdentifier();
			for (Identifier sourceIdentifier : sourceIdentifiers) {
				if (!containsIdentifier(targetIdentifiers, sourceIdentifier)) {
					Identifier copyOfSrcIdentifier = sourceIdentifier.copy();
					copyOfSrcIdentifier.setUse(Identifier.IdentifierUse.OLD);
					theTargetResource.addIdentifier(copyOfSrcIdentifier);
				}
			}
		}
	}

	/**
	 * Checks if theIdentifiers contains theIdentifier using equalsDeep
	 *
	 * @param theIdentifiers the list of identifiers
	 * @param theIdentifier  the identifier to check
	 * @return true if theIdentifiers contains theIdentifier, false otherwise
	 */
	private boolean containsIdentifier(List<Identifier> theIdentifiers, Identifier theIdentifier) {
		for (Identifier identifier : theIdentifiers) {
			if (identifier.equalsDeep(theIdentifier)) {
				return true;
			}
		}
		return false;
	}

	private Patient updateResource(Patient theResource, RequestDetails theRequestDetails) {
		DaoMethodOutcome outcome = myPatientDao.update(theResource, theRequestDetails);
		return (Patient) outcome.getResource();
	}

	private void deleteResource(Patient theResource, RequestDetails theRequestDetails) {
		myPatientDao.delete(theResource.getIdElement(), theRequestDetails);
	}
}
