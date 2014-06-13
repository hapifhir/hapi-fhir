package ca.uhn.fhir.jpa.dao;

import java.util.Date;

class HistoryTuple implements Comparable<HistoryTuple> {

	private Long myId;
	private boolean myIsHistory;
	private Date myUpdated;

	public HistoryTuple(boolean theIsHistory, Date theUpdated, Long theId) {
		super();
		myIsHistory = theIsHistory;
		myUpdated = theUpdated;
		myId = theId;
	}

	@Override
	public int compareTo(HistoryTuple theO) {
		return myUpdated.compareTo(theO.myUpdated);
	}

	public Long getId() {
		return myId;
	}

	public boolean isHistory() {
		return myIsHistory;
	}

	public Date getUpdated() {
		return myUpdated;
	}

	public void setId(Long theId) {
		myId = theId;
	}

	public void setIsHistory(boolean theIsHistory) {
		myIsHistory = theIsHistory;
	}

	public void setUpdated(Date theUpdated) {
		myUpdated = theUpdated;
	}

}
