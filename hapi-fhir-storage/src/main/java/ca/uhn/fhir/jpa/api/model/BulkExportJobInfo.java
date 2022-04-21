package ca.uhn.fhir.jpa.api.model;

import java.util.Collection;

public class BulkExportJobInfo {
	private String myJobId;

	private Collection<String> myResourceTypes;

	public String getJobId() {
		return myJobId;
	}

	public void setJobId(String theJobId) {
		myJobId = theJobId;
	}

	public Collection<String> getResourceTypes() {
		return myResourceTypes;
	}

	public void setResourceTypes(Collection<String> theResourceTypes) {
		myResourceTypes = theResourceTypes;
	}
}
