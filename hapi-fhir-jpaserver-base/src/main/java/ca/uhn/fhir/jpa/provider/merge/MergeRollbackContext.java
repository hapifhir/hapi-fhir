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
 * reverted without probing every resource. It is populated by {@link ResourceMergeService} as the forward
 * steps succeed and consumed by the rollback in {@link CrossPartitionMergeRollbackService}.
 *
 * <p>The whole rollback only runs when partition changes commit in independent transactions
 * (see {@code IHapiTransactionService.isRequiresNewTransactionWhenChangingPartitions()}); otherwise the merge's
 * outer transaction has already rolled everything back.
 */
public class MergeRollbackContext {

	private final boolean myDeleteSource;
	private Throwable myFailureCause;

	/**
	 * Step 1: copies (CREATEs) and referrer updates, at their committed post-merge versions, keyed by the
	 * partition they were committed to. The partition is known at record time (from the copy result, or
	 * derived once per partition group from the partial-failure exception), so the rollback can restore each
	 * group in a transaction pinned to its partition without re-deriving the partition per resource.
	 */
	private final Map<RequestPartitionId, List<IIdType>> myCommittedResourceIdsByPartition = new LinkedHashMap<>();

	/**
	 * Step 1: source-side originals scheduled for deletion, recorded at their anticipated tombstone version (the
	 * version the Step 5 delete will create), keyed by partition. The rollback undeletes them by restoring from
	 * this version, but only if the delete bundle actually committed.
	 */
	private final Map<RequestPartitionId, List<IIdType>> myDeletedOriginalResourceIdsByPartition =
			new LinkedHashMap<>();

	/** Step 2: ids of the per-partition Provenances that were created. */
	private final List<IIdType> myCreatedSubProvenanceIds = new ArrayList<>();

	/**
	 * Step 3: the source's post-merge versioned ref — its post-update version when the source is kept, or the
	 * tombstone version created by its delete when {@code deleteSource}. The rollback restores the source from
	 * this version (reverting the update, or undeleting the tombstone).
	 */
	private IIdType mySourcePostMergeId;

	/**
	 * Step 3: the partition the source resource lives on, recorded so the rollback can pin its restore without a
	 * lookup.
	 */
	private RequestPartitionId mySourcePartition;

	/** Step 3: target resource at its post-update version. */
	private IIdType myTargetPostMergeId;

	/**
	 * Step 3: the partition the target resource lives on, recorded so the rollback can pin its restore without a
	 * lookup.
	 */
	private RequestPartitionId myTargetPartition;

	/** Step 4: id of the main Provenance, if it was created. */
	private IIdType myMainProvenanceId;

	/** Step 5: whether the (single-partition, atomic) delete bundle committed. */
	private boolean myDeletesCommitted;

	MergeRollbackContext(boolean theDeleteSource) {
		myDeleteSource = theDeleteSource;
	}

	boolean isDeleteSource() {
		return myDeleteSource;
	}

	Throwable getFailureCause() {
		return myFailureCause;
	}

	void setFailureCause(Throwable theFailureCause) {
		myFailureCause = theFailureCause;
	}

	void addCommittedResourceIds(RequestPartitionId thePartition, List<IIdType> theRefs) {
		myCommittedResourceIdsByPartition
				.computeIfAbsent(thePartition, k -> new ArrayList<>())
				.addAll(theRefs);
	}

	Map<RequestPartitionId, List<IIdType>> getCommittedResourceIdsByPartition() {
		return myCommittedResourceIdsByPartition;
	}

	void addDeletedOriginalResourceIds(RequestPartitionId thePartition, List<IIdType> theRefs) {
		myDeletedOriginalResourceIdsByPartition
				.computeIfAbsent(thePartition, k -> new ArrayList<>())
				.addAll(theRefs);
	}

	Map<RequestPartitionId, List<IIdType>> getDeletedOriginalResourceIdsByPartition() {
		return myDeletedOriginalResourceIdsByPartition;
	}

	List<IIdType> getCreatedSubProvenanceIds() {
		return myCreatedSubProvenanceIds;
	}

	IIdType getSourcePostMergeId() {
		return mySourcePostMergeId;
	}

	RequestPartitionId getSourcePartition() {
		return mySourcePartition;
	}

	void setSourcePostMergeId(IIdType theSourcePostMergeId, RequestPartitionId thePartition) {
		mySourcePostMergeId = theSourcePostMergeId;
		mySourcePartition = thePartition;
	}

	IIdType getTargetPostMergeId() {
		return myTargetPostMergeId;
	}

	RequestPartitionId getTargetPartition() {
		return myTargetPartition;
	}

	void setTargetPostMergeId(IIdType theTargetPostMergeId, RequestPartitionId thePartition) {
		myTargetPostMergeId = theTargetPostMergeId;
		myTargetPartition = thePartition;
	}

	IIdType getMainProvenanceId() {
		return myMainProvenanceId;
	}

	void setMainProvenanceId(IIdType theMainProvenanceId) {
		myMainProvenanceId = theMainProvenanceId;
	}

	boolean isDeletesCommitted() {
		return myDeletesCommitted;
	}

	void markDeletesCommitted() {
		myDeletesCommitted = true;
	}
}
