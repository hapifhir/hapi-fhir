/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.provider.merge;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.dao.PartitionedTransactionPartialFailureException;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.delete.DeleteConflictUtil;
import ca.uhn.fhir.replacereferences.PreviousResourceVersionRestorer;
import ca.uhn.fhir.replacereferences.ReplaceReferencesProvenanceSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ca.uhn.fhir.merge.MergeResourceHelper.addErrorToOperationOutcome;
import static ca.uhn.fhir.rest.api.Constants.STATUS_HTTP_500_INTERNAL_ERROR;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

// Created by Claude Opus 4.8 (1M context)
/**
 * Reverts a cross-partition {@code $merge} that failed partway through. This is an internal
 * failure-handling mechanism for the forward merge, driven entirely by what the forward steps recorded as committed
 * (see {@link MergeRollbackContext}) — it does not read or validate persisted Provenances. It is distinct from the
 * user-initiated {@code $hapi.fhir.undo-merge} operation in {@link ResourceUndoMergeService}, which reverts a
 * <em>successful</em> merge from its persisted Provenances; the two only share the version-restore primitive.
 *
 * <p>Every step here is best-effort: a step that fails does not abort the rollback but is swallowed, logged, and (for
 * resources left in their merged state) reported in the outcome so they can be reverted manually, so the remaining
 * steps still run.
 */
public class CrossPartitionMergeRollbackService {

	private static final Logger ourLog = LoggerFactory.getLogger(CrossPartitionMergeRollbackService.class);

	private static final String ISSUE_TYPE_EXCEPTION = "exception";

	private static final String MSG_FULLY_ROLLED_BACK =
			"Cross-partition merge failed and was fully rolled back; no resources remain in a merged state. Merge failure cause: %s";

	private final PreviousResourceVersionRestorer myResourceVersionRestorer;
	private final DaoRegistry myDaoRegistry;
	private final FhirContext myFhirContext;
	private final IHapiTransactionService myHapiTransactionService;

	public CrossPartitionMergeRollbackService(
			DaoRegistry theDaoRegistry,
			PreviousResourceVersionRestorer theResourceVersionRestorer,
			IHapiTransactionService theHapiTransactionService) {
		myDaoRegistry = theDaoRegistry;
		myResourceVersionRestorer = theResourceVersionRestorer;
		myFhirContext = theDaoRegistry.getFhirContext();
		myHapiTransactionService = theHapiTransactionService;
	}

	/**
	 * Reverts a cross-partition {@code $merge} that failed partway through, driven entirely by
	 * what the forward steps recorded as committed (see {@link MergeRollbackContext}). This applies when the committed
	 * steps are durable because partition changes commit in their own transactions; otherwise the outer transaction
	 * has already rolled everything back and the caller reports that directly without invoking this service.
	 *
	 * <p>The work runs partition-pinned and in referential-integrity order: first the undeletes (tombstoned
	 * originals, and the source when it was deleted) so the resources the reverts repoint referrers at exist; then
	 * the reverts partition by partition, with partitions holding merge-created copies (v1 resources, reverted by
	 * deleting them) restored last so every referrer elsewhere is repointed away from a copy before it is deleted;
	 * then any committed resources whose partition is unknown via the partition-unpinned fallback; and finally the
	 * Provenances created by this attempt are deleted. Undeletes and reverts are best-effort — anything that cannot
	 * be reverted is reported so it can be reverted manually; an un-deletable Provenance is left orphaned but not
	 * reported (no resource is in a merged state).
	 *
	 * @param theContext what the forward steps recorded as committed
	 * @param theRequestDetails the request details
	 * @param theOutcome the outcome to populate with the rollback result
	 */
	public void rollbackPartialCrossPartitionMerge(
			MergeRollbackContext theContext,
			RequestDetails theRequestDetails,
			OperationOutcomeWithStatusCode theOutcome) {

		IBaseOperationOutcome opOutcome = theOutcome.getOperationOutcome();

		// Collects the ids of any resources that could not be reverted, for the partial-failure report.
		List<IIdType> notReverted = new ArrayList<>();

		// Undeletes first (tombstoned originals, and the source when it was deleted) so that the resources they
		// reference exist before the reverts below repoint referrers at them.
		theContext
				.getResourcesToUndeleteByPartition()
				.forEach((partition, ids) -> restoreResourcesPinned(partition, ids, theRequestDetails, notReverted));

		// Then revert the committed copies, referrer updates, target, and the kept source, partition by partition.
		// Partitions holding merge-created copies (v1 ids, reverted by deleting them) go last, so referrers in other
		// partitions are repointed away from the copies before the copies are deleted.
		orderPartitionsWithCopiesLast(theContext.getResourcesToRevertByPartition())
				.forEach((partition, ids) -> restoreResourcesPinned(partition, ids, theRequestDetails, notReverted));

		// Committed resources whose partition could not be attributed: fall back to the partition-unpinned restore.
		restoreResourcesUnpinned(theContext.getResourcesToRevertWithUnknownPartition(), theRequestDetails, notReverted);

		// Delete the Provenances created by this attempt (per-partition subs and the main "merge succeeded" signal).
		theContext
				.getProvenanceIds()
				.forEach(provenanceId ->
						deleteProvenance(provenanceId, theContext.getProvenancePartition(), theRequestDetails));

		String msg;
		int statusCode;
		if (notReverted.isEmpty()) {
			// Everything reverted, so the merge failed cleanly — report the triggering exception's own status,
			// just as the outer-transaction (nothing-committed) path does.
			statusCode = resolveHttpStatusCode(theContext.getFailureCause());
			msg = format(MSG_FULLY_ROLLED_BACK, describeFailureCause(theContext.getFailureCause()));
		} else {
			// The rollback itself could not revert everything, so resources are left in their merged state — a
			// server-side inconsistency regardless of what triggered the merge failure, hence always 500.
			statusCode = STATUS_HTTP_500_INTERNAL_ERROR;
			// Report each resource by its versioned id — this is the post-merge version the resource is left
			// at, which points an operator straight at the exact state that needs manual reverting.
			String notRevertedIds = notReverted.stream()
					.map(id -> id.toUnqualified().getValue())
					.collect(joining(", "));
			msg = format(
					"Cross-partition merge failed and was partially rolled back. The following resources could not be "
							+ "reverted and remain in their merged state, and must be reverted manually: %s. Merge failure cause: %s",
					notRevertedIds, describeFailureCause(theContext.getFailureCause()));
		}
		theOutcome.setHttpStatusCode(statusCode);
		addErrorToOperationOutcome(myFhirContext, opOutcome, msg, ISSUE_TYPE_EXCEPTION);
	}

	/**
	 * Returns the given per-partition ids reordered so that partitions containing any version-1 id come last.
	 * A v1 id is a resource the merge created (a copy), which the restorer reverts by deleting it — that delete
	 * must not run until the reverts of every other partition have repointed referrers away from it.
	 */
	private static Map<RequestPartitionId, List<IIdType>> orderPartitionsWithCopiesLast(
			Map<RequestPartitionId, List<IIdType>> theIdsByPartition) {
		Map<RequestPartitionId, List<IIdType>> result = new LinkedHashMap<>();
		theIdsByPartition.forEach((partition, ids) -> {
			if (!containsVersionOneId(ids)) {
				result.put(partition, ids);
			}
		});
		theIdsByPartition.forEach((partition, ids) -> {
			if (containsVersionOneId(ids)) {
				result.put(partition, ids);
			}
		});
		return result;
	}

	private static boolean containsVersionOneId(List<IIdType> theIds) {
		return theIds.stream().anyMatch(id -> id.hasVersionIdPart() && id.getVersionIdPartAsLong() == 1L);
	}

	/**
	 * Renders the failure that triggered the rollback for the outcome, as {@code <exception type>: <message>} (or just
	 * the type when the exception carried no message), so the reported cause names both what failed and why.
	 */
	private static String describeFailureCause(@Nullable Throwable theFailureCause) {
		if (theFailureCause == null) {
			return "unknown";
		}
		String type = theFailureCause.getClass().getSimpleName();
		String message = theFailureCause.getMessage();
		return message != null ? type + ": " + message : type;
	}

	/**
	 * Maps the merge failure to the HTTP status to report: the exception's own status when it carries one
	 * (a {@link BaseServerResponseException}), otherwise 500.
	 */
	private static int resolveHttpStatusCode(@Nullable Throwable theFailureCause) {
		if (theFailureCause instanceof BaseServerResponseException serverException) {
			return serverException.getStatusCode();
		}
		return STATUS_HTTP_500_INTERNAL_ERROR;
	}

	/**
	 * Restores the given recorded ids in one transaction pinned to the partition they were committed on — including
	 * the pre-reads, which must not fan out across shards (a fanned-out read can resolve through a MegaScale ESR
	 * surrogate row and fail, e.g. with Gone for a tombstoned original). The restorer reverts an update, deletes a
	 * created resource, or undeletes a tombstone as appropriate; the ids are already at the version to restore
	 * from. A single-partition restore is atomic, so on failure every id in the list is recorded for the
	 * manual-reconciliation report and the rollback moves on to the next partition.
	 */
	private void restoreResourcesPinned(
			RequestPartitionId thePartition,
			List<IIdType> theIds,
			RequestDetails theRequestDetails,
			List<IIdType> theNotReverted) {
		if (theIds.isEmpty()) {
			return;
		}
		List<Reference> refs = theIds.stream().map(Reference::new).toList();
		try {
			myResourceVersionRestorer.restoreToPreviousVersionsInTrx(refs, thePartition, theRequestDetails);
		} catch (Exception e) {
			ourLog.error(
					"Merge rollback restore failed for partition {}; its {} resource(s) were not reverted",
					thePartition,
					theIds.size(),
					e);
			theNotReverted.addAll(theIds);
		}
	}

	/**
	 * Restores recorded ids whose partition is unknown, as a single partition-unpinned (allPartitions) transaction.
	 * This is the fallback for the rare partial data-bundle failure whose committed ids could not be attributed to
	 * a partition; unlike the pinned path its pre-reads fan out across shards and can hit ESR surrogate rows, so it
	 * is best-effort. On failure every id is recorded for the manual-reconciliation report.
	 */
	private void restoreResourcesUnpinned(
			List<IIdType> theIds, RequestDetails theRequestDetails, List<IIdType> theNotReverted) {
		if (theIds.isEmpty()) {
			return;
		}
		List<Reference> refs = theIds.stream().map(Reference::new).toList();
		try {
			myResourceVersionRestorer.restoreToPreviousVersionsInTrx(refs, theRequestDetails);
		} catch (PartitionedTransactionPartialFailureException thePartialFailure) {
			// The unpinned restore is split per partition and the sub-bundles commit independently — some committed
			// before a later one failed. The committed sub-bundles name exactly the resources that were reverted;
			// everything else remains in its merged state and is reported for manual reconciliation.
			Set<String> revertedVersionlessIds = extractCommittedVersionlessIds(thePartialFailure);
			ourLog.error(
					"Merge rollback fallback restore partially failed; {} of {} resource(s) were reverted before a later failure",
					revertedVersionlessIds.size(),
					theIds.size(),
					thePartialFailure);
			for (IIdType id : theIds) {
				if (!revertedVersionlessIds.contains(
						id.toUnqualifiedVersionless().getValue())) {
					theNotReverted.add(id);
				}
			}
		} catch (Exception e) {
			ourLog.error(
					"Merge rollback fallback restore failed; its {} resource(s) were not reverted", theIds.size(), e);
			theNotReverted.addAll(theIds);
		}
	}

	/**
	 * Collects the versionless ids of the resources the committed sub-bundles of a partitioned-transaction partial
	 * failure actually changed — i.e. the resources whose restore committed before a later sub-bundle failed.
	 */
	private static Set<String> extractCommittedVersionlessIds(
			PartitionedTransactionPartialFailureException theFailure) {
		Set<String> committed = new HashSet<>();
		for (List<IBase> subBundleEntries : theFailure.getCommittedResponseEntriesPerSubBundle()) {
			for (IBase entry : subBundleEntries) {
				ReplaceReferencesProvenanceSvc.extractChangedResourceId((Bundle.BundleEntryComponent) entry)
						.ifPresent(id ->
								committed.add(id.toUnqualifiedVersionless().getValue()));
			}
		}
		return committed;
	}

	/**
	 * Deletes a Provenance created by the merge attempt, pinned to the partition the merge recorded the
	 * Provenances as stored on — their ids may not be partition-decodable (client-assigned ids; all ids under
	 * MegaScale UUID server-id mode), so id-based partition resolution cannot be relied on. A failure is swallowed
	 * and logged — an orphaned Provenance is left behind but no resource remains in a merged state, so it is not
	 * reported as needing a manual revert.
	 */
	private void deleteProvenance(
			@Nullable IIdType theProvenanceId,
			@Nullable RequestPartitionId thePartition,
			RequestDetails theRequestDetails) {
		if (theProvenanceId == null) {
			return;
		}
		IIdType versionlessId = theProvenanceId.toUnqualifiedVersionless();
		try {
			IFhirResourceDao<IBaseResource> provenanceDao = myDaoRegistry.getResourceDao("Provenance");
			if (thePartition == null) {
				provenanceDao.delete(versionlessId, theRequestDetails);
				return;
			}
			myHapiTransactionService
					.withRequest(theRequestDetails)
					.withRequestPartitionId(thePartition)
					.execute(() -> {
						// Seed the resolved-partition cache (keyed by "Type/id") so the 4-arg delete uses the
						// pinned partition instead of re-resolving a non-decodable id.
						TransactionDetails transactionDetails = new TransactionDetails();
						transactionDetails.addResolvedPartition(
								versionlessId.getResourceType() + "/" + versionlessId.getIdPart(), thePartition);
						DeleteConflictList deleteConflicts = new DeleteConflictList();
						DaoMethodOutcome outcome = provenanceDao.delete(
								versionlessId, deleteConflicts, theRequestDetails, transactionDetails);
						DeleteConflictUtil.validateDeleteConflictsEmptyOrThrowException(myFhirContext, deleteConflicts);
						return outcome;
					});
		} catch (Exception e) {
			ourLog.error("Merge rollback could not delete Provenance {}", theProvenanceId.getValue(), e);
		}
	}
}
