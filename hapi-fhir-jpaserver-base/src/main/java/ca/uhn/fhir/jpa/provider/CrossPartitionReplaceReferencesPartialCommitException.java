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
import ca.uhn.fhir.jpa.dao.PartitionedTransactionPartialFailureException;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;
import java.util.Map;

// Created by Claude Fable 5
/**
 * Thrown when the cross-partition copy/replace-references data bundle commits some per-partition sub-bundles
 * before a later one fails. On top of the committed response entries the parent carries, this attributes the
 * committed resource ids to the partition each one was written on (derived from the request-side classification,
 * which the generic parent exception cannot know), so the merge rollback can restore them partition-pinned.
 * Ids whose partition could not be attributed are reported separately.
 */
public class CrossPartitionReplaceReferencesPartialCommitException
		extends PartitionedTransactionPartialFailureException {

	private final Map<RequestPartitionId, List<IIdType>> myCommittedIdsByPartition;
	private final List<IIdType> myCommittedIdsWithUnknownPartition;

	public CrossPartitionReplaceReferencesPartialCommitException(
			PartitionedTransactionPartialFailureException theWrappedFailure,
			Map<RequestPartitionId, List<IIdType>> theCommittedIdsByPartition,
			List<IIdType> theCommittedIdsWithUnknownPartition) {
		super(
				theWrappedFailure.getMessage(),
				theWrappedFailure.getCommittedResponseEntriesPerSubBundle(),
				theWrappedFailure.getCause());
		myCommittedIdsByPartition = theCommittedIdsByPartition;
		myCommittedIdsWithUnknownPartition = theCommittedIdsWithUnknownPartition;
	}

	/**
	 * The versioned ids of the resources whose writes committed before the failure, grouped by the partition
	 * they were written on.
	 */
	public Map<RequestPartitionId, List<IIdType>> getCommittedIdsByPartition() {
		return myCommittedIdsByPartition;
	}

	/**
	 * The versioned ids of committed resources whose partition could not be attributed (e.g. copies created
	 * when the classification produced more than one destination partition).
	 */
	public List<IIdType> getCommittedIdsWithUnknownPartition() {
		return myCommittedIdsWithUnknownPartition;
	}
}
