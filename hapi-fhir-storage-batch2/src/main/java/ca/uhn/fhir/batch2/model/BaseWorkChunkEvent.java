package ca.uhn.fhir.batch2.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Payloads for WorkChunk state transitions.
 * Some events in the state-machine update the chunk metadata (e.g. error message).
 * This class provides a base-class for those event objects.
 * @see hapi-fhir-docs/src/main/resources/ca/uhn/hapi/fhir/docs/server_jpa_batch/batch2_states.md
 */
public abstract class BaseWorkChunkEvent {
	protected final String myChunkId;

	protected BaseWorkChunkEvent(String theChunkId) {
		myChunkId = theChunkId;
	}

	public String getChunkId() {
		return myChunkId;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (theO == null || getClass() != theO.getClass()) return false;

		BaseWorkChunkEvent that = (BaseWorkChunkEvent) theO;

		return new EqualsBuilder().append(myChunkId, that.myChunkId).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(myChunkId).toHashCode();
	}
}
