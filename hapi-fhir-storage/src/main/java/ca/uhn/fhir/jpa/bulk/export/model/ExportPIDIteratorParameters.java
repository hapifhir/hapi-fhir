package ca.uhn.fhir.jpa.bulk.export.model;

import java.util.Date;
import java.util.List;

public class ExportPIDIteratorParameters {
	/**
	 * Resource type
	 */
	private String myResourceType;

	/**
	 * The earliest date from which to retrieve records
	 */
	private Date myStartDate;

	/**
	 * List of filters to be applied to the search.
	 * Eg:
	 * Patient/123?group=1
	 * "group=1" would be the filter
	 */
	private List<String> myFilters;

	/**
	 * The ID of the BatchJob.
	 * (Batch jobs are stored in Persistence, to keep track
	 * of results/status).
	 */
	private String myJobId;

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public Date getStartDate() {
		return myStartDate;
	}

	public void setStartDate(Date theStartDate) {
		myStartDate = theStartDate;
	}

	public List<String> getFilters() {
		return myFilters;
	}

	public void setFilters(List<String> theFilters) {
		myFilters = theFilters;
	}

	public String getJobId() {
		return myJobId;
	}

	public void setJobId(String theJobId) {
		myJobId = theJobId;
	}
}
