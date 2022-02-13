package ca.uhn.fhir.jpa.model.search;

import java.util.Date;

class DateSearchIndexData {
	private final Date myLowerBoundDate;
	private final int myLowerBoundOrdinal;
	private final Date myUpperBoundDate;
	private final int myUpperBoundOrdinal;

	DateSearchIndexData(Date theLowerBoundDate, int theLowerBoundOrdinal, Date theUpperBoundDate, int theUpperBoundOrdinal) {
		myLowerBoundDate = theLowerBoundDate;
		myLowerBoundOrdinal = theLowerBoundOrdinal;
		myUpperBoundDate = theUpperBoundDate;
		myUpperBoundOrdinal = theUpperBoundOrdinal;
	}

	public Date getLowerBoundDate() {
		return myLowerBoundDate;
	}

	public int getLowerBoundOrdinal() {
		return myLowerBoundOrdinal;
	}

	public Date getUpperBoundDate() {
		return myUpperBoundDate;
	}

	public int getUpperBoundOrdinal() {
		return myUpperBoundOrdinal;
	}
}
