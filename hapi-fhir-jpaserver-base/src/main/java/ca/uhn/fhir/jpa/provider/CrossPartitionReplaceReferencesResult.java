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
 * Result of copying compartment resources across partitions during a cross-partition merge.
 * Contains the resources written by the data bundle and the original source copies (for deferred
 * deletion), both grouped by partition.
 */
// Created by claude-opus-4-6
public class CrossPartitionReplaceReferencesResult {
	private final Map<RequestPartitionId, List<IIdType>> myCommittedResourcesByPartition;
	private final Map<RequestPartitionId, List<IIdType>> myCopiedResourceOriginalIdsByPartition;

	public CrossPartitionReplaceReferencesResult(
			Map<RequestPartitionId, List<IIdType>> theCommittedResourcesByPartition,
			Map<RequestPartitionId, List<IIdType>> theCopiedResourceOriginalIdsByPartition) {
		myCommittedResourcesByPartition = theCommittedResourcesByPartition;
		myCopiedResourceOriginalIdsByPartition = theCopiedResourceOriginalIdsByPartition;
	}

	/**
	 * Resources written by the data bundle, grouped by the partition they were committed on.
	 * Copies (CREATEs) are grouped under the target partition; updates (PUTs) under each
	 * resource's current partition.
	 */
	public Map<RequestPartitionId, List<IIdType>> getCommittedResourcesByPartition() {
		return myCommittedResourcesByPartition;
	}

	/**
	 * Source-side compartment originals scheduled for deletion, grouped by their source partition.
	 */
	public Map<RequestPartitionId, List<IIdType>> getCopiedResourceOriginalIdsByPartition() {
		return myCopiedResourceOriginalIdsByPartition;
	}
}
