package ca.uhn.fhir.batch2.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
