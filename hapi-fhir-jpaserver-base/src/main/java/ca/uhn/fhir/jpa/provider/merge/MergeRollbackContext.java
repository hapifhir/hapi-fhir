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

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.List;

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
 * <p>Resources are recorded as flat lists spanning all partitions; the rollback restores each list as a single
 * cross-partition FHIR transaction, which splits and dependency-orders the writes per partition (see
 * {@link CrossPartitionMergeRollbackService}).
 */
public class MergeRollbackContext {

	private Throwable myFailureCause;

	/**
	 * Resources to revert. Holds the committed data-bundle copies (CREATEs) and referrer updates, the post-merge
	 * target, and the post-merge source when it was kept — all at their committed post-merge versions. The
	 * restorer reverts each (deleting a created resource or rolling back an update).
	 */
	private final List<IIdType> myResourcesToRevert = new ArrayList<>();

	/**
	 * Resources to undelete. Holds the source-side originals that were deleted, plus the source itself when it was
	 * deleted, each at its tombstone version. The caller only records these once the delete actually committed.
	 */
	private final List<IIdType> myResourcesToUndelete = new ArrayList<>();

	/** Id of the Provenance, if it was created. */
	private IIdType myProvenanceId;

	Throwable getFailureCause() {
		return myFailureCause;
	}

	void setFailureCause(Throwable theFailureCause) {
		myFailureCause = theFailureCause;
	}

	void addResourcesToRevert(List<IIdType> theRefs) {
		myResourcesToRevert.addAll(theRefs);
	}

	void addResourceToRevert(IIdType theRef) {
		myResourcesToRevert.add(theRef);
	}

	List<IIdType> getResourcesToRevert() {
		return myResourcesToRevert;
	}

	void addResourcesToUndelete(List<IIdType> theRefs) {
		myResourcesToUndelete.addAll(theRefs);
	}

	void addResourceToUndelete(IIdType theRef) {
		myResourcesToUndelete.add(theRef);
	}

	List<IIdType> getResourcesToUndelete() {
		return myResourcesToUndelete;
	}

	IIdType getProvenanceId() {
		return myProvenanceId;
	}

	void setProvenanceId(IIdType theProvenanceId) {
		myProvenanceId = theProvenanceId;
	}
}
