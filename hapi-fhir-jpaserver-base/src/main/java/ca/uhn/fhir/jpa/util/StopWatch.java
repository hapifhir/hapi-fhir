package ca.uhn.fhir.jpa.util;


public class StopWatch {

	private long myStarted = System.currentTimeMillis();
	
	public long getMillisAndRestart() {
		long now = System.currentTimeMillis();
		long retVal = now - myStarted;
		myStarted = now;
		return retVal;
	}
	
}
