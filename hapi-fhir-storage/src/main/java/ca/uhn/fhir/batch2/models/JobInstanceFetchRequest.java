package ca.uhn.fhir.batch2.models;

import org.springframework.data.domain.Sort;

public class JobInstanceFetchRequest {

	/**
	 * Page index to start from.
	 */
	private int myPageStart;

	/**
	 * Page size (number of elements to return)
	 */
	private int myBatchSize;

	private Sort mySort;

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

	public Sort getSort() {
		return mySort;
	}

	public void setSort(Sort theSort) {
		mySort = theSort;
	}
}
