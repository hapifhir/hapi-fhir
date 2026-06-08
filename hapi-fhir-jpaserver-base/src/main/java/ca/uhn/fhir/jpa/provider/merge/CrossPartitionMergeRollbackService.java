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
import ca.uhn.fhir.replacereferences.PreviousResourceVersionRestorer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

	public CrossPartitionMergeRollbackService(
			DaoRegistry theDaoRegistry, PreviousResourceVersionRestorer theResourceVersionRestorer) {
		myDaoRegistry = theDaoRegistry;
		myResourceVersionRestorer = theResourceVersionRestorer;
		myFhirContext = theDaoRegistry.getFhirContext();
	}

	/**
	 * Reports, without doing any work, that the failed merge needs no rollback because its partition changes joined the
	 * outer transaction, which has already rolled everything back. The caller decides this applies (when partition
	 * changes do not commit in their own transactions); see {@link #rollbackPartialCrossPartitionMerge} for the case
	 * where the committed steps are durable and must actually be reverted.
	 *
	 * @param theContext what the forward steps recorded as committed (only its failure cause is used here)
	 * @param theOutcome the outcome to populate with the rollback result
	 */
	public void reportFullyRolledBackByOuterTransaction(
			MergeRollbackContext theContext, OperationOutcomeWithStatusCode theOutcome) {
		theOutcome.setHttpStatusCode(STATUS_HTTP_500_INTERNAL_ERROR);
		addErrorToOperationOutcome(
				myFhirContext,
				theOutcome.getOperationOutcome(),
				format(MSG_FULLY_ROLLED_BACK, describeFailureCause(theContext.getFailureCause())),
				ISSUE_TYPE_EXCEPTION);
	}

	/**
	 * Reverts a cross-partition {@code $merge} that failed partway through, driven entirely by
	 * what the forward steps recorded as committed (see {@link MergeRollbackContext}). This applies when the committed
	 * steps are durable because partition changes commit in their own transactions; otherwise the outer transaction
	 * has already rolled everything back and {@link #reportFullyRolledBackByOuterTransaction} is used instead.
	 *
	 * <p>The committed steps are reverted in referential-integrity order: source resource first (so referrers and
	 * undeleted originals can point to it), then the per-partition data, then the target resource; the main and
	 * per-partition Provenances are deleted. Reverts are best-effort — anything that cannot be reverted is reported so
	 * it can be reverted manually.
	 *
	 * @param theContext what the forward steps recorded as committed
	 * @param theRequestDetails the request details
	 * @param theOutcome the outcome to populate with the rollback result
	 */
	public void rollbackPartialCrossPartitionMerge(
			MergeRollbackContext theContext,
			RequestDetails theRequestDetails,
			OperationOutcomeWithStatusCode theOutcome) {

		theOutcome.setHttpStatusCode(STATUS_HTTP_500_INTERNAL_ERROR);
		IBaseOperationOutcome opOutcome = theOutcome.getOperationOutcome();

		// Partition changes commit independently — revert the recorded committed set, best-effort.
		List<IIdType> notReverted = new ArrayList<>();

		// Source resource first so referrers and undeleted originals can point to it.
		revertSourceIfCommitted(theContext, theRequestDetails, notReverted);

		// Revert the committed copies and updated referrers (grouped per partition during the merge), plus the
		// deleted originals (recorded at their tombstone version by the forward steps).
		Map<RequestPartitionId, List<Reference>> dataByPartition = new LinkedHashMap<>();
		addAsReferences(dataByPartition, theContext.getCommittedResourceIdsByPartition());
		if (theContext.isDeletesCommitted()) {
			addAsReferences(dataByPartition, theContext.getDeletedOriginalResourceIdsByPartition());
		}
		for (Map.Entry<RequestPartitionId, List<Reference>> entry : dataByPartition.entrySet()) {
			try {
				myResourceVersionRestorer.restoreToPreviousVersionsInTrx(
						entry.getValue(), entry.getKey(), theRequestDetails);
			} catch (Exception e) {
				ourLog.error(
						"Merge rollback could not revert partition {}; its resources remain in their merged state",
						entry.getKey(),
						e);
				entry.getValue().forEach(ref -> notReverted.add(ref.getReferenceElement()));
			}
		}

		// Target resource last.
		revertSingle(
				theContext.getTargetPostMergeId(), theContext.getTargetPartition(), theRequestDetails, notReverted);

		// Delete the Provenances created by this attempt (main = the "merge succeeded" signal, plus per-partition).
		deleteProvenance(theContext.getMainProvenanceId(), theRequestDetails);
		for (IIdType subProvenanceId : theContext.getCreatedSubProvenanceIds()) {
			deleteProvenance(subProvenanceId, theRequestDetails);
		}

		String msg;
		if (notReverted.isEmpty()) {
			msg = format(MSG_FULLY_ROLLED_BACK, describeFailureCause(theContext.getFailureCause()));
		} else {
			// Report each resource by versionless identity — the version we failed to restore from would be
			// misleading, since the resource is left in its merged state, not at that version.
			String notRevertedIds = notReverted.stream()
					.map(id -> id.toUnqualifiedVersionless().getValue())
					.collect(joining(", "));
			msg = format(
					"Cross-partition merge failed and was partially rolled back. The following resources could not be "
							+ "reverted and remain in their merged state, and must be reverted manually: %s. Merge failure cause: %s",
					notRevertedIds, describeFailureCause(theContext.getFailureCause()));
		}
		addErrorToOperationOutcome(myFhirContext, opOutcome, msg, ISSUE_TYPE_EXCEPTION);
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
	 * Reverts the source resource from its post-merge versioned reference back to the version before the merge. When the
	 * source resource was kept that post-merge reference is its post-update version (restore reverts the update); when it
	 * was deleted it is its tombstone version (restore undeletes it), but only once the delete actually committed — if it
	 * never committed there is nothing to do.
	 */
	private void revertSourceIfCommitted(
			MergeRollbackContext theContext, RequestDetails theRequestDetails, List<IIdType> theNotReverted) {
		if (theContext.isDeleteSource() && !theContext.isDeletesCommitted()) {
			return;
		}
		revertSingle(
				theContext.getSourcePostMergeId(), theContext.getSourcePartition(), theRequestDetails, theNotReverted);
	}

	/**
	 * Restores a single recorded-committed reference, pinned to the partition it was recorded on. The restorer reverts
	 * an update, deletes a created resource, or undeletes a tombstone as appropriate. A failure is recorded for the
	 * report.
	 */
	private void revertSingle(
			@Nullable IIdType thePostMergeId,
			RequestPartitionId thePartition,
			RequestDetails theRequestDetails,
			List<IIdType> theNotReverted) {
		if (thePostMergeId == null) {
			return;
		}
		Reference ref = new Reference(thePostMergeId);
		try {
			myResourceVersionRestorer.restoreToPreviousVersionsInTrx(List.of(ref), thePartition, theRequestDetails);
		} catch (Exception e) {
			ourLog.error(
					"Merge rollback could not revert {}; it remains in its merged state", thePostMergeId.getValue(), e);
			theNotReverted.add(thePostMergeId);
		}
	}

	/**
	 * Deletes a Provenance created by the merge attempt. A failure is swallowed and logged — an orphaned Provenance
	 * is left behind but no resource remains in a merged state, so it is not reported as needing a manual revert.
	 */
	private void deleteProvenance(@Nullable IIdType theProvenanceId, RequestDetails theRequestDetails) {
		if (theProvenanceId == null) {
			return;
		}
		try {
			myDaoRegistry
					.getResourceDao("Provenance")
					.delete(theProvenanceId.toUnqualifiedVersionless(), theRequestDetails);
		} catch (Exception e) {
			ourLog.error("Merge rollback could not delete Provenance {}", theProvenanceId.getValue(), e);
		}
	}

	/**
	 * Appends each partition's ids to {@code theTarget} as {@link Reference}s, creating the partition's bucket if
	 * absent. The ids are already at the version to restore from, so no transformation is applied.
	 */
	private static void addAsReferences(
			Map<RequestPartitionId, List<Reference>> theTarget,
			Map<RequestPartitionId, List<IIdType>> theIdsByPartition) {
		theIdsByPartition.forEach((partition, ids) -> theTarget
				.computeIfAbsent(partition, k -> new ArrayList<>())
				.addAll(ids.stream().map(Reference::new).toList()));
	}
}
