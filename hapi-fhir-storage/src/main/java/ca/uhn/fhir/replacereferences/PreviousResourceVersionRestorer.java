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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a class to restore resources to their previous versions based on the provided versioned resource references.
 * It is used in the context of undoing changes made by the $hapi.fhir.replace-references operation.
 */
public class PreviousResourceVersionRestorer {

	private static final Logger ourLog = LoggerFactory.getLogger(PreviousResourceVersionRestorer.class);

	private final HapiTransactionService myHapiTransactionService;
	private final DaoRegistry myDaoRegistry;

	public PreviousResourceVersionRestorer(
			DaoRegistry theDaoRegistry, HapiTransactionService theHapiTransactionService) {
		myDaoRegistry = theDaoRegistry;
		myHapiTransactionService = theHapiTransactionService;
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
	 * @param thePartitionId the partition ID for the operation
	 *
	 * @throws IllegalArgumentException if a given reference is versionless
	 * @throws ResourceVersionConflictException if the current version of the resource does not match the version specified in the reference.
	 */
	public void restoreToPreviousVersionsInTrx(
			List<Reference> theReferences, RequestDetails theRequestDetails, RequestPartitionId thePartitionId) {
		ourLog.info(
				"[DIAG] restoreToPreviousVersionsInTrx called with {} references, partitionId={}, requestDetails type={}",
				theReferences.size(),
				thePartitionId,
				theRequestDetails.getClass().getSimpleName());
		for (Reference ref : theReferences) {
			ourLog.info("[DIAG]   reference: {}", ref.getReference());
		}
		myHapiTransactionService
				.withRequest(theRequestDetails)
				.withRequestPartitionId(thePartitionId)
				.execute(() -> restoreToPreviousVersions(theReferences, theRequestDetails, thePartitionId));
	}

	private void restoreToPreviousVersions(
			List<Reference> theReferences, RequestDetails theRequestDetails, RequestPartitionId thePartitionId) {
		List<IIdType> idsToDelete = new ArrayList<>();
		for (Reference reference : theReferences) {
			String referenceStr = reference.getReference();
			IIdType referenceId = new IdDt(referenceStr);

			if (!referenceId.hasVersionIdPart()) {
				throw new IllegalArgumentException(
						Msg.code(2730) + "Reference does not have a version: " + referenceStr);
			}
			Long referenceVersion = referenceId.getVersionIdPartAsLong();

			ourLog.info("[DIAG] Processing reference: {} (version={})", referenceStr, referenceVersion);

			IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(referenceId.getResourceType());
			IBaseResource currentResource = null;
			try {
				currentResource = dao.read(referenceId.toUnqualifiedVersionless(), theRequestDetails);
				ourLog.info(
						"[DIAG]   dao.read({}) succeeded, current version={}, partition={}",
						referenceId.toUnqualifiedVersionless().getValue(),
						currentResource.getIdElement().getVersionIdPartAsLong(),
						RequestPartitionId.getPartitionIfAssigned(currentResource)
								.orElse(null));
			} catch (ResourceGoneException e) {
				IIdType deletedId = e.getResourceId();
				ourLog.info(
						"[DIAG]   dao.read({}) → ResourceGoneException, deletedId={}",
						referenceId.toUnqualifiedVersionless().getValue(),
						deletedId);
				if (deletedId == null || !deletedId.hasVersionIdPart()) {
					throw e;
				}
				long currentVersion = deletedId.getVersionIdPartAsLong();
				if (currentVersion != referenceVersion) {
					String msg = String.format(
							"The resource cannot be restored because the current version of resource %s (%s) does not match the expected version (%s)",
							referenceStr, currentVersion, referenceVersion);
					throw new ResourceVersionConflictException(Msg.code(2732) + msg);
				}
				ourLog.info("[DIAG]   Tombstoned resource version matches ({}), will undelete", currentVersion);
				// Version matches → resource is tombstoned at the expected version, proceed to undelete
			}

			if (currentResource != null) {
				Long currentVersion = currentResource.getIdElement().getVersionIdPartAsLong();
				if (!currentVersion.equals(referenceVersion)) {
					String msg = String.format(
							"The resource cannot be restored because the current version of resource %s (%s) does not match the expected version (%s)",
							referenceStr, currentVersion, referenceVersion);
					throw new ResourceVersionConflictException(Msg.code(2732) + msg);
				}

				if (referenceVersion == 1) {
					// Resource was created new by the operation (v1) — collect for bundle delete
					ourLog.info(
							"[DIAG]   v1 resource → collecting for delete: {}",
							referenceId.toUnqualifiedVersionless().getValue());
					idsToDelete.add(referenceId.toUnqualifiedVersionless());
					continue;
				}
			}

			long previousVersion = referenceVersion - 1;
			if (previousVersion < 1) {
				throw new IllegalArgumentException(Msg.code(2731)
						+ "Resource cannot be restored to a previous as the provided version is 1: " + referenceStr);
			}

			IIdType previousId = referenceId.withVersion(Long.toString(previousVersion));
			ourLog.info("[DIAG]   Reading previous version: {}", previousId.getValue());
			IBaseResource previousResource = dao.read(previousId, theRequestDetails);
			RequestPartitionId prevResPartition =
					(RequestPartitionId) previousResource.getUserData(Constants.RESOURCE_PARTITION_ID);
			ourLog.info(
					"[DIAG]   Previous resource loaded: id={}, partition={}, class={}",
					previousResource.getIdElement().getValue(),
					prevResPartition,
					previousResource.getClass().getSimpleName());
			previousResource.setId(previousResource.getIdElement().toUnqualifiedVersionless());

			ourLog.info(
					"[DIAG]   Calling dao.update({}) to restore to previous version",
					previousResource.getIdElement().getValue());
			DaoMethodOutcome updateOutcome = dao.update(previousResource, theRequestDetails);
			ourLog.info(
					"[DIAG]   dao.update completed: id={}, created={}, operationType={}",
					updateOutcome.getId(),
					updateOutcome.getCreated(),
					updateOutcome.getOperationType());
		}

		if (!idsToDelete.isEmpty()) {
			ourLog.info(
					"[DIAG] Executing delete bundle for {} v1 resources with partitionId={}",
					idsToDelete.size(),
					thePartitionId);
			for (IIdType id : idsToDelete) {
				ourLog.info("[DIAG]   Deleting: {}", id.getValue());
			}
			@SuppressWarnings("unchecked")
			IFhirSystemDao<Object, Object> systemDao = (IFhirSystemDao<Object, Object>) myDaoRegistry.getSystemDao();
			BundleBuilder deleteBuilder = new BundleBuilder(systemDao.getContext());
			for (IIdType id : idsToDelete) {
				deleteBuilder.addTransactionDeleteEntry(id);
			}
			systemDao.transactionNested(
					SystemRequestDetails.forRequestPartitionId(thePartitionId), deleteBuilder.getBundle());
			ourLog.info("[DIAG] Delete bundle completed");
		}
	}
}
