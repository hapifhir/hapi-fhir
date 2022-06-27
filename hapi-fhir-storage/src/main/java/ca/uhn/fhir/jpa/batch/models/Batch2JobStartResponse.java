package ca.uhn.fhir.jpa.batch.models;

public class Batch2JobStartResponse {

	private String myJobId;


	public String getJobId() {
		return myJobId;
	}

	public void setJobId(String theJobId) {
		myJobId = theJobId;
	}
}
