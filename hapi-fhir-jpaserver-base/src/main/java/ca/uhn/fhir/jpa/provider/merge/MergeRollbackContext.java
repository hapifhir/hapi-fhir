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
 */
public class MergeRollbackContext {

	private Throwable myFailureCause;

	/**
	 * Resources to revert, keyed by the partition the rollback should target the restore to. Holds the committed
	 * data-bundle copies (CREATEs) and referrer updates, the post-merge target, and the post-merge source when it
	 * was kept — all at their committed post-merge versions. The restorer reverts each (deleting a created
	 * resource or rolling back an update).
	 */
	private final Map<RequestPartitionId, List<IIdType>> myResourcesToRevertByPartition = new LinkedHashMap<>();

	/**
	 * Resources to undelete, keyed by partition. Holds the source-side originals that were deleted, plus the
	 * source itself when it was deleted, each at its tombstone version. The caller only records these once the
	 * delete actually committed. The rollback restores each partition's list as a single FHIR transaction, so
	 * references among the listed resources resolve regardless of order.
	 */
	private final Map<RequestPartitionId, List<IIdType>> myResourcesToUndeleteByPartition = new LinkedHashMap<>();

	/** Ids of the per-partition Provenances that were created. */
	private final List<IIdType> myCreatedSubProvenanceIds = new ArrayList<>();

	/** Id of the main Provenance, if it was created. */
	private IIdType myMainProvenanceId;

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

	void addResourcesToUndelete(RequestPartitionId thePartition, List<IIdType> theRefs) {
		myResourcesToUndeleteByPartition
				.computeIfAbsent(thePartition, k -> new ArrayList<>())
				.addAll(theRefs);
	}

	void addResourceToUndelete(RequestPartitionId thePartition, IIdType theRef) {
		addResourcesToUndelete(thePartition, List.of(theRef));
	}

	Map<RequestPartitionId, List<IIdType>> getResourcesToUndeleteByPartition() {
		return myResourcesToUndeleteByPartition;
	}

	List<IIdType> getCreatedSubProvenanceIds() {
		return myCreatedSubProvenanceIds;
	}

	IIdType getMainProvenanceId() {
		return myMainProvenanceId;
	}

	void setMainProvenanceId(IIdType theMainProvenanceId) {
		myMainProvenanceId = theMainProvenanceId;
	}
}
