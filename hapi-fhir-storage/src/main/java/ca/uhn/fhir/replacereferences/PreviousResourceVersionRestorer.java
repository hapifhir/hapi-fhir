/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.replacereferences;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Reference;

import java.util.List;
import java.util.Set;

/**
 * This is a class to restore resources to their previous versions based on the provided versioned resource references.
 * It is used in the context of undoing changes made by the $hapi.fhir.replace-references operation.
 */
public class PreviousResourceVersionRestorer {

	private final HapiTransactionService myHapiTransactionService;
	private final DaoRegistry myDaoRegistry;

	public PreviousResourceVersionRestorer(
			DaoRegistry theDaoRegistry, HapiTransactionService theHapiTransactionService) {
		myDaoRegistry = theDaoRegistry;
		myHapiTransactionService = theHapiTransactionService;
	}

	/**
	 * Given a list of versioned resource references, this method restores each resource to its previous version
	 * if the resource's current version is the same as specified in the given reference
	 * (i.e. the resource was not updated since the reference was created).
	 *
	 * This method is transactional and will attempt to restore all resources in a single transaction.
	 *
	 * Note that this method updates a resource using its previous version's content,
	 * so it will actually cause a new version to be created (i.e. it does not rewrite the history).
	 *
	 * @throws IllegalArgumentException if a given reference is versionless
	 * @throws IllegalArgumentException a given reference has version 1, so it cannot have a previous version to restore to.
	 * @throws ResourceVersionConflictException if the current version of the resource does not match the version specified in the reference.
	 */
	public void restoreToPreviousVersionsInTrx(
			List<Reference> theReferences,
			Set<Reference> theReferencesToUndelete,
			RequestDetails theRequestDetails,
			RequestPartitionId thePartitionId) {
		myHapiTransactionService
				.withRequest(theRequestDetails)
				.withRequestPartitionId(thePartitionId)
				.execute(() -> restoreToPreviousVersions(theReferences, theReferencesToUndelete, theRequestDetails));
	}

	private void restoreToPreviousVersions(
			List<Reference> theReferences, Set<Reference> theReferencesToUndelete, RequestDetails theRequestDetails) {
		for (Reference reference : theReferences) {
			String referenceStr = reference.getReference();
			IIdType referenceId = new IdDt(referenceStr);

			if (!referenceId.hasVersionIdPart()) {
				throw new IllegalArgumentException(
						Msg.code(2730) + "Reference does not have a version: " + referenceStr);
			}
			Long referenceVersion = referenceId.getVersionIdPartAsLong();

			// Restore previous version (version - 1)
			long previousVersion = referenceVersion - 1;
			if (previousVersion < 1) {
				throw new IllegalArgumentException(Msg.code(2731)
						+ "Resource cannot be restored to a previous as the provided version is 1: " + referenceStr);
			}

			// Read the current resource
			IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(referenceId.getResourceType());
			IBaseResource currentResource = null;
			try {
				currentResource = dao.read(referenceId.toUnqualifiedVersionless(), theRequestDetails);
			} catch (ResourceGoneException e) {
				if (!theReferencesToUndelete.contains(reference)) {
					//FIXME EMRE: update msg.code
					// enhance error message to include the reference of the deleted resource that cannot be restored
					String msg = String.format("The resource '%s' cannot be restored because it was deleted. %s", referenceStr, e.getMessage());
					throw new ResourceGoneException(Msg.code(1234) + msg);
				}
			}

			// Check current version, if current resource version exists, the resource wasn't deleted
			if (currentResource != null) {
				Long currentVersion = currentResource.getIdElement().getVersionIdPartAsLong();
				if (!currentVersion.equals(referenceVersion)) {
					String msg = String.format(
							"The resource cannot be restored because the current version of resource %s (%s) does not match the expected version (%s)",
							referenceStr, currentVersion, referenceVersion);
					throw new ResourceVersionConflictException(Msg.code(2732) + msg);
				}
			}

			IIdType previousId = referenceId.withVersion(Long.toString(previousVersion));
			IBaseResource previousResource = dao.read(previousId, theRequestDetails);
			previousResource.setId(previousResource.getIdElement().toUnqualifiedVersionless());

			// Update the resource to the previous version's content
			dao.update(previousResource, theRequestDetails);
		}
	}
}
