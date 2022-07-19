package ca.uhn.fhir.batch2.model;

public class JobInstanceFetchRequest {

	/**
	 * Page index to start from.
	 */
	private int myPageStart;

	/**
	 * Page size (number of elements to return)
	 */
	private int myBatchSize;

	public int getPageStart() {
		return myPageStart;
	}

	public void setPageStart(int thePageStart) {
		myPageStart = thePageStart;
	}

	public int getBatchSize() {
		return myBatchSize;
	}

	public void setBatchSize(int theBatchSize) {
		myBatchSize = theBatchSize;
	}
}
