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
		return theRequestPartitionId.isDefaultPartition(getDefaultPartitionId());
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
