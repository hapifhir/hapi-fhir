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
package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;
import java.util.Map;

/**
 * Metadata produced when preparing the copy/update portion of a single-transaction cross-partition merge bundle.
 * Unlike the old flow, no transaction is executed while preparing — the caller appends the remaining entries
 * (source/target PUTs, the Provenance POST, the DELETEs) to the same bundle, executes one transaction, and then
 * correlates the response using the data carried here.
 */
// Created by claude-opus-4-8[1m]
public class CrossPartitionReplaceReferencesPrepareResult {

	private final int myCopyEntryCount;
	private final List<String> myCopyPlaceholdersInEntryOrder;
	private final List<RequestPartitionId> myCopyAndUpdateEntryPartitions;
	private final List<IIdType> myUpdatedReferrerCurrentVersionedIds;
	private final Map<RequestPartitionId, List<IIdType>> myCopiedResourceOriginalIdsByPartition;

	public CrossPartitionReplaceReferencesPrepareResult(
			int theCopyEntryCount,
			List<String> theCopyPlaceholdersInEntryOrder,
			List<RequestPartitionId> theCopyAndUpdateEntryPartitions,
			List<IIdType> theUpdatedReferrerCurrentVersionedIds,
			Map<RequestPartitionId, List<IIdType>> theCopiedResourceOriginalIdsByPartition) {
		myCopyEntryCount = theCopyEntryCount;
		myCopyPlaceholdersInEntryOrder = theCopyPlaceholdersInEntryOrder;
		myCopyAndUpdateEntryPartitions = theCopyAndUpdateEntryPartitions;
		myUpdatedReferrerCurrentVersionedIds = theUpdatedReferrerCurrentVersionedIds;
		myCopiedResourceOriginalIdsByPartition = theCopiedResourceOriginalIdsByPartition;
	}

	/**
	 * Number of POST (copy) entries the preparer added at the front of the bundle. The next
	 * {@code copyAndUpdateEntryPartitions.size() - copyEntryCount} entries are the PUT (update) entries.
	 */
	public int getCopyEntryCount() {
		return myCopyEntryCount;
	}

	/**
	 * The {@code urn:uuid} placeholder fullUrl assigned to each copy entry, in bundle-entry order. The Provenance
	 * references copies by these placeholders so the transaction processor resolves them to the assigned ids.
	 */
	public List<String> getCopyPlaceholdersInEntryOrder() {
		return myCopyPlaceholdersInEntryOrder;
	}

	/**
	 * The partition each copy/update entry was written to, aligned 1:1 with the first
	 * {@code copyAndUpdateEntryPartitions.size()} bundle entries (copies first, then updates). Used to build the
	 * committed-resources-by-partition map from the transaction response.
	 */
	public List<RequestPartitionId> getCopyAndUpdateEntryPartitions() {
		return myCopyAndUpdateEntryPartitions;
	}

	/**
	 * The current (pre-merge) versioned ids of the reference-update (PUT) referrers, in update-entry order. The
	 * single merge transaction is the only writer, so each referrer's post-merge version is its current version + 1;
	 * the caller uses these to build the Provenance's changed-resource references at their post-merge versions.
	 */
	public List<IIdType> getUpdatedReferrerCurrentVersionedIds() {
		return myUpdatedReferrerCurrentVersionedIds;
	}

	/**
	 * Source-side compartment originals scheduled for deletion, grouped by their source partition.
	 */
	public Map<RequestPartitionId, List<IIdType>> getCopiedResourceOriginalIdsByPartition() {
		return myCopiedResourceOriginalIdsByPartition;
	}
}
