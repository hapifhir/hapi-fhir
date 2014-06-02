package ca.uhn.fhir.jpa.dao;

import java.util.Date;

class HistoryTuple implements Comparable<HistoryTuple> {

	private Object myId;
	private Class<?> myTable;
	private Date myUpdated;

	public HistoryTuple(Class<?> theTable, Date theUpdated, Object theId) {
		super();
		myTable = theTable;
		myUpdated = theUpdated;
		myId = theId;
	}

	@Override
	public int compareTo(HistoryTuple theO) {
		return myUpdated.compareTo(theO.myUpdated);
	}

	public Object getId() {
		return myId;
	}

	public Class<?> getTable() {
		return myTable;
	}

	public Date getUpdated() {
		return myUpdated;
	}

	public void setId(Object theId) {
		myId = theId;
	}

	public void setTable(Class<?> theTable) {
		myTable = theTable;
	}

	public void setUpdated(Date theUpdated) {
		myUpdated = theUpdated;
	}

}
