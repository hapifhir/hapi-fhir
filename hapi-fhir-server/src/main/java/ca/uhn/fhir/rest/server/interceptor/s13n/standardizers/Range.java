package ca.uhn.fhir.rest.server.interceptor.s13n.standardizers;

import java.util.Objects;

class Range {

	private int myStart;
	private int myEnd;

	public Range(int theStart, int theEnd) {
		this.myStart = theStart;
		this.myEnd = theEnd;
	}

	public boolean isInRange(int theNum) {
		return theNum >= getStart() && theNum <= getEnd();
	}

	public int getStart() {
		return myStart;
	}

	public int getEnd() {
		return myEnd;
	}

	@Override
	public boolean equals(Object theObject) {
		if (this == theObject) {
			return true;
		}

		if (theObject == null || getClass() != theObject.getClass()) {
			return false;
		}

		Range range = (Range) theObject;
		return myStart == range.myStart && myEnd == range.myEnd;
	}

	@Override
	public int hashCode() {
		return Objects.hash(myStart, myEnd);
	}

	@Override
	public String toString() {
		return String.format("[%s, %s]", getStart(), getEnd());
	}
}
