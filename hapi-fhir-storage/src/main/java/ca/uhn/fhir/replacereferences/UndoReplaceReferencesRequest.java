package ca.uhn.fhir.replacereferences;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nonnull;

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
	public int resourceLimit;

	public UndoReplaceReferencesRequest(
			@Nonnull IIdType theSourceId,
			@Nonnull IIdType theTargetId,
			RequestPartitionId thePartitionId,
			int theResourceLimit) {
		sourceId = theSourceId;
		targetId = theTargetId;
		partitionId = thePartitionId;
		resourceLimit = theResourceLimit;
	}
}
