/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.interceptor.model;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface IDefaultPartitionSettings {
	/**
	 * This method returns the default partition ID. Implementers of this interface should overwrite this method to provide
	 * a default partition ID that is different than the default value of null.
	 *
	 * @return the default partition ID
	 */
	@Nullable
	default Integer getDefaultPartitionId() {
		return null;
	}

	/**
	 * Test whether <code>theRequestPartitionId</code> is only targeting the default partition where the ID of the default
	 * partition is provided by {@link #getDefaultPartitionId()}.
	 *
	 * @param theRequestPartitionId to perform the evaluation upon.
	 * @return true if the <code>theRequestPartitionId</code> is for the default partition only.
	 */
	default boolean isDefaultPartition(@Nonnull RequestPartitionId theRequestPartitionId) {
		return theRequestPartitionId.isPartition(getDefaultPartitionId());
	}

	/**
	 * Test whether <code>theRequestPartitionId</code> has one of its targeted partitions matching the default partition
	 * where the ID of the default partition is provided by {@link #getDefaultPartitionId()}.
	 *
	 * @param theRequestPartitionId to perform the evaluation upon.
	 * @return true if the <code>theRequestPartitionId</code> is targeting the default partition.
	 */
	default boolean hasDefaultPartitionId(@Nonnull RequestPartitionId theRequestPartitionId) {
		return theRequestPartitionId.hasDefaultPartitionId(getDefaultPartitionId());
	}

	/**
	 * Returns a RequestPartitionId instance for the default partition.
	 * This is a convenience method that creates a RequestPartitionId using the default partition ID
	 * from {@link #getDefaultPartitionId()}.
	 *
	 * @return a RequestPartitionId for the default partition
	 */
	default RequestPartitionId getDefaultRequestPartitionId() {
		return RequestPartitionId.fromPartitionId(getDefaultPartitionId());
	}
}
