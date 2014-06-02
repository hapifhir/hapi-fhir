package ca.uhn.fhir.jpa.dao;

public class DaoConfig {

	private int myHardSearchLimit = 1000;
	private int myHardTagListLimit = 1000;

	public int getHardSearchLimit() {
		return myHardSearchLimit;
	}

	public int getHardTagListLimit() {
		return myHardTagListLimit;
	}

	public void setHardSearchLimit(int theHardSearchLimit) {
		myHardSearchLimit = theHardSearchLimit;
	}

	public void setHardTagListLimit(int theHardTagListLimit) {
		myHardTagListLimit = theHardTagListLimit;
	}

}
