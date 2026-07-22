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
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
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

	/**
	 * A v1 "copy" resource to delete during undo, paired with the partition it actually lives in
	 * (captured from its {@code RESOURCE_PARTITION_ID} user data during the pre-read). The
	 * copies were all written to the target partition by the forward merge, but their ids may be
	 * non-partition-decodable (client-assigned ids; all ids under MegaScale UUID server-id mode), so
	 * the captured partition pins the subsequent DELETE to the correct shard rather than relying on
	 * id-based partition resolution.
	 */
	private record CopyToDelete(IIdType id, RequestPartitionId partitionId) {}

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

		// The v1 "copies" are deleted separately, one at a time, after the update bundle — they cannot share the
		// cross-partition update bundle because their ids may be non-partition-decodable (see deleteCopies).
		// The ordering matters: each one-by-one delete validates its own delete conflicts immediately, so the
		// update bundle must have already repointed every referrer away from the copies by the time they run
		// (satisfying referential-integrity-on-delete).
		List<CopyToDelete> copiesToDelete = new ArrayList<>();

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
					// Resource was created new by the operation (v1) — delete it to undo. The DELETE is pinned to
					// the caller-supplied partition when present, otherwise to the partition captured from the
					// pre-read's RESOURCE_PARTITION_ID user data. The caller-supplied partition is preferred
					// because a fanned-out pre-read can resolve through an ESR surrogate row on another shard,
					// which stamps the surrogate's partition rather than the copy's real one.
					RequestPartitionId partitionId = thePinnedPartition != null
							? thePinnedPartition
							: RequestPartitionId.getPartitionFromUserDataIfPresent(currentResource)
									.orElse(RequestPartitionId.allPartitions());
					copiesToDelete.add(new CopyToDelete(referenceId.toUnqualifiedVersionless(), partitionId));
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

		// Now delete the v1 copies. By this point the update bundle above has reverted every referrer back to
		// referencing the source originals (not the copies), so deleting the copies cannot violate
		// referential-integrity-on-delete. Each delete is pinned to the copy's actual partition.
		deleteCopies(copiesToDelete, theRequestDetails);
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
	 * Deletes the v1 copies one at a time, each in its own transaction pinned to the partition the copy actually
	 * lives in (captured from the pre-read).
	 *
	 * The deletes deliberately do NOT go through a transaction bundle: a transaction is repartitioned per entry by
	 * the transaction-partitioning interceptor, which re-resolves each entry's partition from its id and fails for
	 * ids that are not partition-decodable (client-assigned ids; all ids under MegaScale UUID server-id mode) —
	 * pinning the bundle to a concrete partition does not prevent that re-resolution. Calling the DAO directly
	 * bypasses the interceptor, and seeding the resolved-partition cache on the {@link TransactionDetails} makes the
	 * DAO use the captured partition instead of re-resolving the id. Mirrors the forward merge's one-by-one delete.
	 */
	private void deleteCopies(List<CopyToDelete> theCopiesToDelete, RequestDetails theRequestDetails) {
		// The copies can reference each other (e.g. a copied Observation referencing a copied Encounter), so
		// delete conflicts are collected across the whole list — with every id pre-marked for deletion so
		// conflicts among the co-deleted copies are skipped, mirroring how a DELETE transaction bundle
		// validates — and validated once at the end.
		DeleteConflictList deleteConflicts = new DeleteConflictList();
		theCopiesToDelete.forEach(copy -> deleteConflicts.setResourceIdMarkedForDeletion(copy.id()));
		for (CopyToDelete copy : theCopiesToDelete) {
			IIdType id = copy.id();
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(id.getResourceType());
			myHapiTransactionService
					.withRequest(theRequestDetails)
					.withRequestPartitionId(copy.partitionId())
					.execute(() -> {
						TransactionDetails transactionDetails = new TransactionDetails();
						transactionDetails.addResolvedPartition(
								id.getResourceType() + "/" + id.getIdPart(), copy.partitionId());
						return dao.delete(id, deleteConflicts, theRequestDetails, transactionDetails);
					});
		}
		DeleteConflictUtil.validateDeleteConflictsEmptyOrThrowException(myFhirContext, deleteConflicts);
	}
}
