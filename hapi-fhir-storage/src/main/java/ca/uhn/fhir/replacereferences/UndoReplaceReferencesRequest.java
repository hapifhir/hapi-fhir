/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import org.hl7.fhir.instance.model.api.IIdType;

import jakarta.annotation.Nonnull;

/**
 * This class models the parameters for processing a $hapi.fhir.undo-replace-references operation.
 */
public class UndoReplaceReferencesRequest {

	/**
	 * Unqualified source id
	 */
	@Nonnull
	public final IIdType sourceId;

	/**
	 * Unqualified target id
	 */
	@Nonnull
	public final IIdType targetId;

	public final RequestPartitionId partitionId;

	/**
	 * The maximum number of resource that can be processed in a single undo operation.
	 * If the undo operation requires updating more resources than this limit,
	 * the operation will fail.
	 * This is currently not exposed to FHIR clients and is used internally, and set based on jpaStorageSettings.
	 */
	public final int resourceLimit;

	public UndoReplaceReferencesRequest(
			@Nonnull IIdType theSourceId,
			@Nonnull IIdType theTargetId,
			RequestPartitionId thePartitionId,
			int theResourceLimit) {
		sourceId = theSourceId.toUnqualifiedVersionless();
		targetId = theTargetId.toUnqualifiedVersionless();
		partitionId = thePartitionId;
		resourceLimit = theResourceLimit;
	}
}
