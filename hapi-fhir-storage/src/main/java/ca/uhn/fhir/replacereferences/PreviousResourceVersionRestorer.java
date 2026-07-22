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
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.delete.DeleteConflictUtil;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.merge.MergeResourceHelper;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.BundleBuilder;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;

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
	private final FhirContext myFhirContext;
	private final PartitionSettings myPartitionSettings;

	public PreviousResourceVersionRestorer(
			DaoRegistry theDaoRegistry,
			HapiTransactionService theHapiTransactionService,
			PartitionSettings thePartitionSettings) {
		myDaoRegistry = theDaoRegistry;
		myHapiTransactionService = theHapiTransactionService;
		myFhirContext = theDaoRegistry.getFhirContext();
		myPartitionSettings = thePartitionSettings;
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
	 * @throws InternalErrorException if no explicit partition is provided and all-partition search is not supported
	 *     (e.g. MegaScale) — the caller must use the partition-pinned overload on such platforms, since the
	 *     unpinned fan-out reads are unreliable there (they can resolve through ESR surrogate rows on other shards).
	 */
	public void restoreToPreviousVersionsInTrx(List<Reference> theReferences, RequestDetails theRequestDetails) {
		// Without an explicit partition the pre-reads (and deletes) fan out across all shards. That is only legitimate
		// on platforms that support all-partition search (single-database HAPI). On platforms that do not (MegaScale),
		// the unpinned reads can resolve through another shard's ESR surrogate row and fail, so the scattered mode is
		// refused outright — the caller must supply the partition via the partition-pinned overload.
		if (!myPartitionSettings.isAllPartitionSearchSupported()) {
			throw new InternalErrorException(Msg.code(2997)
					+ "Cannot restore resources to their previous versions without an explicit partition because"
					+ " all-partition search is not supported on this server; the partition the resources live on must"
					+ " be supplied.");
		}
		// Run under an allPartitions context so the references (which may span partitions) are resolved against
		// every partition, mirroring how the forward cross-partition merge submits its bundle. When a
		// transaction-partitioning interceptor is present it then repartitions the bundle per resource; otherwise
		// (single database) the whole bundle runs as one transaction.
		myHapiTransactionService
				.withRequest(theRequestDetails)
				.withRequestPartitionId(RequestPartitionId.allPartitions())
				.execute(() -> restoreToPreviousVersions(theReferences, theRequestDetails, null));
	}

	/**
	 * Same as {@link #restoreToPreviousVersionsInTrx(List, RequestDetails)} but for a list of references that all
	 * live on the given partition. The transaction is pinned to that partition, and — unlike the allPartitions
	 * overload, whose pre-reads fan out across every shard under MegaScale — the pre-reads are pinned to it too.
	 * That keeps them off other shards' ESR surrogate rows (same FHIR id, externally-stored body), whose
	 * resolution ignores the requested version and can fail the read (e.g. throw Gone for a tombstoned original
	 * even though the requested previous version exists on the correct shard).
	 *
	 * @param theReferences a list of versioned resource references that all live on {@code thePartitionId}
	 * @param thePartitionId the partition the referenced resources live on
	 * @param theRequestDetails the request details for the operation
	 */
	public void restoreToPreviousVersionsInTrx(
			List<Reference> theReferences, RequestPartitionId thePartitionId, RequestDetails theRequestDetails) {
		myHapiTransactionService
				.withRequest(theRequestDetails)
				.withRequestPartitionId(thePartitionId)
				.execute(() -> restoreToPreviousVersions(theReferences, theRequestDetails, thePartitionId));
	}

	private void restoreToPreviousVersions(
			List<Reference> theReferences,
			RequestDetails theRequestDetails,
			@Nullable RequestPartitionId thePinnedPartition) {
		if (theReferences.isEmpty()) {
			ourLog.info("No resource references provided to restore; nothing to do.");
			return;
		}

		// The restore-updates (and undeletes) go into a single cross-partition transaction bundle so that
		// references between the restored resources resolve against the transaction's pre-populated id map,
		// independent of entry order — see the comment on the transactionNested call below.
		BundleBuilder updateBundleBuilder = new BundleBuilder(myFhirContext);

		// The v1 resources created by the operation (e.g. the copies a cross-partition merge writes to the target
		// partition) are deleted separately, after the update bundle — they cannot share the cross-partition
		// update bundle because their ids may be non-partition-decodable (see deleteResources). The ordering
		// matters: the deletes validate their delete conflicts, so the update bundle must have already repointed
		// every referrer away from them by the time they run (satisfying referential-integrity-on-delete).
		List<IIdType> resourcesToDelete = new ArrayList<>();

		for (Reference reference : theReferences) {
			String referenceStr = reference.getReference();
			IIdType referenceId = new IdDt(referenceStr);

			if (!referenceId.hasVersionIdPart()) {
				throw new IllegalArgumentException(
						Msg.code(2730) + "Reference does not have a version: " + referenceStr);
			}
			long referenceVersion = referenceId.getVersionIdPartAsLong();

			IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(referenceId.getResourceType());

			// Pre-read the current resource. When the caller supplied the partition the references live on, the
			// read is pinned to it; otherwise it fans out across all shards so the read finds the resource
			// regardless of which partition it lives in.
			IBaseResource currentResource = null;
			try {
				currentResource = readResource(
						dao, referenceId.toUnqualifiedVersionless(), theRequestDetails, thePinnedPartition);
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
					// Resource was created new by the operation (v1) — delete it to undo. The delete is pinned to
					// the caller-supplied partition when present; otherwise it runs unpinned in the scattered
					// transaction (see deleteResources).
					resourcesToDelete.add(referenceId.toUnqualifiedVersionless());
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
			// Pre-read the previous version (pinned or fanned out, same rationale as the current read).
			IBaseResource previousResource = readResource(dao, previousId, theRequestDetails, thePinnedPartition);
			previousResource.setId(previousResource.getIdElement().toUnqualifiedVersionless());
			// Restore the resource to its previous version's content via the transaction bundle
			updateBundleBuilder.addTransactionUpdateEntry(previousResource);
		}

		// Submit all restores (updates and undeletes) as a single FHIR transaction. Processing them together
		// means references between the restored resources are resolved against the transaction's
		// pre-populated id map, independent of entry order — so a referrer can be restored before its
		// target, and two resources can even reference each other, without tripping
		// referential-integrity-on-write on a not-yet-restored (still tombstoned) target.
		if (!updateBundleBuilder.getBundle().isEmpty()) {
			myDaoRegistry.getSystemDao().transactionNested(theRequestDetails, updateBundleBuilder.getBundle());
		}

		// Now delete the v1 resources created by the operation. By this point the update bundle above has reverted
		// every referrer back to referencing the source originals (not the created resources), so deleting them
		// cannot violate referential-integrity-on-delete.
		deleteResources(resourcesToDelete, thePinnedPartition, theRequestDetails);
	}

	/**
	 * Reads a resource (optionally a specific version) by id. When {@code thePinnedPartition} is supplied the
	 * read is pinned to it; otherwise the read fans out across all shards.
	 */
	private IBaseResource readResource(
			IFhirResourceDao<IBaseResource> theDao,
			IIdType theId,
			RequestDetails theRequestDetails,
			@Nullable RequestPartitionId thePinnedPartition) {
		if (thePinnedPartition != null) {
			return readPinnedToPartition(theDao, theId, thePinnedPartition);
		}
		return readAcrossPartitions(theDao, theId, theRequestDetails);
	}

	/**
	 * Reads a resource by id with partition resolution forced to the given partition. The read goes through a
	 * {@link SystemRequestDetails} carrying an explicit partition because the DAO re-resolves its partition
	 * internally per read — for ids that are not partition-decodable (client-assigned ids; all ids under
	 * MegaScale UUID server-id mode) that resolution yields allPartitions and the read would fan out across
	 * shards even inside a partition-pinned transaction. An explicit partition on a SystemRequestDetails
	 * short-circuits that resolution.
	 */
	private IBaseResource readPinnedToPartition(
			IFhirResourceDao<IBaseResource> theDao, IIdType theId, RequestPartitionId thePartition) {
		SystemRequestDetails pinnedRequestDetails = SystemRequestDetails.forRequestPartitionId(thePartition);
		return theDao.read(theId, pinnedRequestDetails);
	}

	/**
	 * Reads a resource by id, fanning the read out across all shards. allPartitions + REQUIRES_NEW is what
	 * makes MegaScale fan the read out per shard (first success wins, 404s skipped); the default REQUIRED
	 * propagation would reuse the outer single-shard transaction and miss. On non-MegaScale HAPI this degrades
	 * to a normal read. Mirrors the pattern in {@code CrossPartitionReplaceReferencesSvc.findReferencingResourceIds}.
	 */
	private IBaseResource readAcrossPartitions(
			IFhirResourceDao<IBaseResource> theDao, IIdType theId, RequestDetails theRequestDetails) {
		return myHapiTransactionService
				.withRequest(theRequestDetails)
				.withRequestPartitionId(RequestPartitionId.allPartitions())
				.withPropagation(Propagation.REQUIRES_NEW)
				.read(partition -> theDao.read(theId, theRequestDetails));
	}

	/**
	 * Deletes the v1 resources created by the operation (e.g. the copies a cross-partition merge writes to the
	 * target partition), after the update bundle. When the caller supplied the partition the resources live on,
	 * the whole batch is deleted atomically in one transaction pinned to it — either every delete commits or none
	 * do (see {@link MergeResourceHelper#deleteResourcesInPartitionTransaction}); otherwise the deletes
	 * participate in the caller's already-open scattered (allPartitions) transaction (see
	 * {@link #deleteAcrossPartitions}).
	 *
	 * In both cases the deletes deliberately do NOT go through a transaction bundle: a transaction bundle is
	 * repartitioned per entry by the transaction-partitioning interceptor, which re-resolves each entry's
	 * partition from its id and fails for ids that are not partition-decodable (client-assigned ids; all ids under
	 * MegaScale UUID server-id mode) — pinning the bundle to a concrete partition does not prevent that
	 * re-resolution. Calling the DAO directly bypasses the interceptor.
	 */
	private void deleteResources(
			List<IIdType> theResourcesToDelete,
			@Nullable RequestPartitionId thePinnedPartition,
			RequestDetails theRequestDetails) {
		if (theResourcesToDelete.isEmpty()) {
			return;
		}
		if (thePinnedPartition != null) {
			// Same-partition batch: delete the whole list atomically in one transaction pinned to the partition.
			// The returned tombstone ids are not needed here (the undo does not record them).
			MergeResourceHelper.deleteResourcesInPartitionTransaction(
					theResourcesToDelete, thePinnedPartition, myDaoRegistry, myHapiTransactionService);
		} else {
			deleteAcrossPartitions(theResourcesToDelete, theRequestDetails);
		}
	}

	/**
	 * Deletes the given resources without pinning them to a partition; each delete resolves its partition normally
	 * and participates in the scattered (allPartitions) transaction the caller opened. Reached only when
	 * all-partition search is supported (single-database HAPI) — the scattered entry point refuses this mode
	 * otherwise.
	 *
	 * <p>The resources can reference each other (e.g. a copied Observation referencing a copied Encounter), so
	 * delete conflicts are collected across the whole list — with every id pre-marked for deletion so conflicts
	 * among the co-deleted resources are skipped, mirroring how a DELETE transaction bundle validates — and
	 * validated once at the end.
	 */
	private void deleteAcrossPartitions(List<IIdType> theResourcesToDelete, RequestDetails theRequestDetails) {
		DeleteConflictList deleteConflicts = new DeleteConflictList();
		theResourcesToDelete.forEach(deleteConflicts::setResourceIdMarkedForDeletion);
		TransactionDetails transactionDetails = new TransactionDetails();
		for (IIdType id : theResourcesToDelete) {
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(id.getResourceType());
			dao.delete(id, deleteConflicts, theRequestDetails, transactionDetails);
		}
		DeleteConflictUtil.validateDeleteConflictsEmptyOrThrowException(myFhirContext, deleteConflicts);
	}
}
