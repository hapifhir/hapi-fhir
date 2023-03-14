package ca.uhn.fhir.mdm.api;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.envers.RevisionType;

import java.util.Objects;

// TODO:  find a better module for this:
public class EnversRevision {
	private final RevisionType myRevisionType;
	private final long myRevisionNumber;
	// TODO:  what is this timestamp based on?
	private final long myRevisionTimestamp;

	public EnversRevision(RevisionType theRevisionType, long theRevisionNumber, long theRevisionTimestamp) {
		myRevisionType = theRevisionType;
		myRevisionNumber = theRevisionNumber;
		myRevisionTimestamp = theRevisionTimestamp;
	}

	public RevisionType getRevisionType() {
		return myRevisionType;
	}

	public long getRevisionNumber() {
		return myRevisionNumber;
	}

	public long getRevisionTimestamp() {
		return myRevisionTimestamp;
	}

	@Override
	public boolean equals(Object theO) {

		if (this == theO) return true;
		if (theO == null || getClass() != theO.getClass()) return false;
		final EnversRevision that = (EnversRevision) theO;
		return myRevisionNumber == that.myRevisionNumber && myRevisionTimestamp == that.myRevisionTimestamp && myRevisionType == that.myRevisionType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(myRevisionType, myRevisionNumber, myRevisionTimestamp);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("myRevisionType", myRevisionType)
			.append("myRevisionNumber", myRevisionNumber)
			.append("myRevisionTimestamp", myRevisionTimestamp)
			.toString();
	}
}
