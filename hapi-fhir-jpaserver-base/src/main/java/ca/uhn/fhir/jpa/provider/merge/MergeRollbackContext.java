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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Created by Claude Opus 4.8 (1M context)
/**
 * Records exactly what each step of a cross-partition {@code $merge} committed, so that a failure can be
 * reverted. It is populated by {@link ResourceMergeService} as the forward
 * steps succeed and consumed by the rollback in {@link CrossPartitionMergeRollbackService}.
 *
 * <p>The whole rollback only runs when partition changes commit in independent transactions
 * (see {@code IHapiTransactionService.isRequiresNewTransactionWhenChangingPartitions()}); otherwise the merge's
 * outer transaction has already rolled everything back.
 *
 * <p>Resources are recorded grouped by the partition each one was committed on; the rollback restores each
 * partition's list as its own partition-pinned transaction (see {@link CrossPartitionMergeRollbackService}).
 * Committed resources whose partition could not be attributed (a rare partial data-bundle failure edge case)
 * are recorded separately and restored with the partition-unpinned fallback.
 */
public class MergeRollbackContext {

	private Throwable myFailureCause;

	/**
	 * Resources to revert, grouped by the partition each one was committed on. Holds the committed data-bundle
	 * copies (CREATEs) and referrer updates, the post-merge target, and the post-merge source when it was kept —
	 * all at their committed post-merge versions. The restorer reverts each (deleting a created resource or
	 * rolling back an update).
	 */
	private final Map<RequestPartitionId, List<IIdType>> myResourcesToRevertByPartition = new LinkedHashMap<>();

	/**
	 * Resources to revert whose partition could not be attributed. The rollback restores these with the
	 * partition-unpinned (allPartitions) fallback.
	 */
	private final List<IIdType> myResourcesToRevertWithUnknownPartition = new ArrayList<>();

	/**
	 * Resources to undelete, grouped by the partition each one lives on. Holds the source-side originals that
	 * were deleted, plus the source itself when it was deleted, each at its tombstone version. The caller records
	 * each one as its delete actually commits.
	 */
	private final Map<RequestPartitionId, List<IIdType>> myResourcesToUndeleteByPartition = new LinkedHashMap<>();

	/** Ids of the Provenances created by this merge attempt (per-partition subs and the main one). */
	private final List<IIdType> myProvenanceIds = new ArrayList<>();

	/**
	 * The partition the merge Provenances are stored on, so the rollback can delete them partition-pinned
	 * (their ids may not be partition-decodable).
	 */
	private RequestPartitionId myProvenancePartition;

	Throwable getFailureCause() {
		return myFailureCause;
	}

	void setFailureCause(Throwable theFailureCause) {
		myFailureCause = theFailureCause;
	}

	void addResourcesToRevert(RequestPartitionId thePartition, List<IIdType> theRefs) {
		myResourcesToRevertByPartition
				.computeIfAbsent(thePartition, k -> new ArrayList<>())
				.addAll(theRefs);
	}

	void addResourceToRevert(RequestPartitionId thePartition, IIdType theRef) {
		addResourcesToRevert(thePartition, List.of(theRef));
	}

	Map<RequestPartitionId, List<IIdType>> getResourcesToRevertByPartition() {
		return myResourcesToRevertByPartition;
	}

	void addResourcesToRevertWithUnknownPartition(List<IIdType> theRefs) {
		myResourcesToRevertWithUnknownPartition.addAll(theRefs);
	}

	List<IIdType> getResourcesToRevertWithUnknownPartition() {
		return myResourcesToRevertWithUnknownPartition;
	}

	void addResourceToUndelete(RequestPartitionId thePartition, IIdType theRef) {
		myResourcesToUndeleteByPartition
				.computeIfAbsent(thePartition, k -> new ArrayList<>())
				.add(theRef);
	}

	Map<RequestPartitionId, List<IIdType>> getResourcesToUndeleteByPartition() {
		return myResourcesToUndeleteByPartition;
	}

	void addProvenanceId(IIdType theProvenanceId) {
		myProvenanceIds.add(theProvenanceId);
	}

	List<IIdType> getProvenanceIds() {
		return myProvenanceIds;
	}

	void setProvenancePartition(RequestPartitionId theProvenancePartition) {
		myProvenancePartition = theProvenancePartition;
	}

	RequestPartitionId getProvenancePartition() {
		return myProvenancePartition;
	}
}
