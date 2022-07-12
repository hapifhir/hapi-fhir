package ca.uhn.fhir.jpa.batch.models;

public class Batch2JobStartResponse {

	/**
	 * The job id
	 */
	private String myJobId;

	/**
	 * True if an existing job is being used instead
	 * (to prevent multiples of the exact same job being
	 * requested).
	 */
	private boolean myUsesCachedResult;

	public String getJobId() {
		return myJobId;
	}

	public void setJobId(String theJobId) {
		myJobId = theJobId;
	}

	public boolean isUsesCachedResult() {
		return myUsesCachedResult;
	}

	public void setUsesCachedResult(boolean theUseCachedResult) {
		myUsesCachedResult = theUseCachedResult;
	}
}
