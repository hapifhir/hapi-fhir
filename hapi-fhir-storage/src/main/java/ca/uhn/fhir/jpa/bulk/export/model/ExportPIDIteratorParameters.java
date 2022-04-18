package ca.uhn.fhir.jpa.bulk.export.model;

import java.util.Date;
import java.util.List;

public class ExportPIDIteratorParameters {
	private String myResourceType;

	private Date myStartDate;

	// have towork thsi out
	private List<String> myFilters;

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
}
