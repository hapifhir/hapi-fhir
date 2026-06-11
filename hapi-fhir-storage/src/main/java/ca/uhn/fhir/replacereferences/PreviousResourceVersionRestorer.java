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
package ca.uhn.fhir.replacereferences;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * This is a class to restore resources to their previous versions based on the provided versioned resource references.
 * It is used in the context of undoing changes made by the $hapi.fhir.replace-references operation.
 */
public class PreviousResourceVersionRestorer {

	private static final Logger ourLog = LoggerFactory.getLogger(PreviousResourceVersionRestorer.class);

	private final HapiTransactionService myHapiTransactionService;
	private final DaoRegistry myDaoRegistry;
	private final FhirContext myFhirContext;

	public PreviousResourceVersionRestorer(
			DaoRegistry theDaoRegistry, HapiTransactionService theHapiTransactionService) {
		myDaoRegistry = theDaoRegistry;
		myHapiTransactionService = theHapiTransactionService;
		myFhirContext = theDaoRegistry.getFhirContext();
	}

	/**
	 * Given a list of versioned resource references, this method restores each resource to its previous version
	 * if the resource's current version matches the version in the reference
	 * (i.e. the resource was not updated since the reference was created).
	 *
	 * Three cases are handled:
	 * <ul>
	 *   <li><b>Tombstoned resource</b> (ResourceGoneException): the exception carries the tombstone version.
	 *       If it matches the reference version, the resource is undeleted by restoring to version - 1.</li>
	 *   <li><b>Version-1 resource</b>: the resource was created new by the operation. It is deleted to undo.</li>
	 *   <li><b>Existing resource at version N &gt; 1</b>: normal case — restore to version N - 1.</li>
	 * </ul>
	 *
	 * This method is transactional and will attempt to restore all resources in a single transaction.
	 *
	 * Note that restoring updates a resource using its previous version's content,
	 * so it will actually cause a new version to be created (i.e. it does not rewrite the history).
	 * @param theReferences a list of versioned resource references to restore
	 * @param theRequestDetails the request details for the operation
	 *
	 * @throws IllegalArgumentException if a given reference is versionless
	 * @throws ResourceVersionConflictException if the current version of the resource does not match the version specified in the reference.
	 */
	public void restoreToPreviousVersionsInTrx(List<Reference> theReferences, RequestDetails theRequestDetails) {
		myHapiTransactionService
				.withRequest(theRequestDetails)
				.execute(() -> restoreToPreviousVersions(theReferences, theRequestDetails));
	}

	/**
	 * Same as {@link #restoreToPreviousVersionsInTrx(List, RequestDetails)} but pins the transaction to the
	 * given partition. When changing partitions requires a new transaction, this keeps every reference in
	 * the (single-partition) list within one transaction so the whole list is restored atomically.
	 *
	 * @param theReferences a list of versioned resource references that all live on {@code thePartitionId}
	 * @param thePartitionId the partition the references live on
	 * @param theRequestDetails the request details for the operation
	 */
	public void restoreToPreviousVersionsInTrx(
			List<Reference> theReferences, RequestPartitionId thePartitionId, RequestDetails theRequestDetails) {
		myHapiTransactionService
				.withRequest(theRequestDetails)
				.withRequestPartitionId(thePartitionId)
				.execute(() -> restoreToPreviousVersions(theReferences, theRequestDetails));
	}

	private void restoreToPreviousVersions(List<Reference> theReferences, RequestDetails theRequestDetails) {
		if (theReferences.isEmpty()) {
			ourLog.info("No resource references provided to restore; nothing to do.");
			return;
		}

		// Every reference yields exactly one bundle entry (a delete or an update), so the bundle is non-empty.
		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);

		for (Reference reference : theReferences) {
			String referenceStr = reference.getReference();
			IIdType referenceId = new IdDt(referenceStr);

			if (!referenceId.hasVersionIdPart()) {
				throw new IllegalArgumentException(
						Msg.code(2730) + "Reference does not have a version: " + referenceStr);
			}
			long referenceVersion = referenceId.getVersionIdPartAsLong();

			// Read the current resource
			IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(referenceId.getResourceType());
			IBaseResource currentResource = null;
			try {
				currentResource = dao.read(referenceId.toUnqualifiedVersionless(), theRequestDetails);
			} catch (ResourceGoneException e) {
				IIdType deletedId = e.getResourceId();
				if (deletedId == null || !deletedId.hasVersionIdPart()) {
					throw e;
				}
				long currentVersion = deletedId.getVersionIdPartAsLong();
				// Resource was deleted after the operation modified it — cannot safely undo.
				if (currentVersion != referenceVersion) {
					String msg = String.format(
							"The resource '%s' cannot be restored because it was deleted after the operation"
									+ " modified it (current version: %s, expected version: %s).",
							referenceStr, currentVersion, referenceVersion);
					throw new ResourceGoneException(Msg.code(2751) + msg);
				}
				// Version matches → resource is tombstoned at the expected version, proceed to undelete
			}

			// If resource exists, check its current version matches the expected version.
			// If not, the resource was updated since the reference was created, so the operation should fail.
			if (currentResource != null) {
				long currentVersion = currentResource.getIdElement().getVersionIdPartAsLong();
				if (currentVersion != referenceVersion) {
					String msg = String.format(
							"The resource cannot be restored because the current version of resource %s (%s) does not match the expected version (%s)",
							referenceStr, currentVersion, referenceVersion);
					throw new ResourceVersionConflictException(Msg.code(2732) + msg);
				}

				if (referenceVersion == 1) {
					// Resource was created new by the operation (v1) — delete it to undo
					bundleBuilder.addTransactionDeleteEntry(referenceId.toUnqualifiedVersionless());
					continue;
				}
			}

			// Restore previous version (version - 1)
			long previousVersion = referenceVersion - 1;
			if (previousVersion < 1) {
				throw new IllegalArgumentException(Msg.code(2731)
						+ "Resource cannot be restored to a previous as the provided version is 1: " + referenceStr);
			}

			IIdType previousId = referenceId.withVersion(Long.toString(previousVersion));
			IBaseResource previousResource = dao.read(previousId, theRequestDetails);
			previousResource.setId(previousResource.getIdElement().toUnqualifiedVersionless());
			// Restore the resource to its previous version's content via the transaction bundle
			bundleBuilder.addTransactionUpdateEntry(previousResource);
		}

		// Submit all restores (updates) and deletes as a single FHIR transaction. Processing them together
		// means references between the restored resources are resolved against the transaction's
		// pre-populated id map, independent of entry order — so a referrer can be restored before its
		// target, and two resources can even reference each other, without tripping
		// referential-integrity-on-write on a not-yet-restored (still tombstoned) target.
		myDaoRegistry.getSystemDao().transactionNested(theRequestDetails, bundleBuilder.getBundle());
	}
}
