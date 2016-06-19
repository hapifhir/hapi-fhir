package ca.uhn.fhir.jpa.util;

public class Counter {

	private long myCount;
	
	public long getThenAdd() {
		return myCount++;
	}
	
}
