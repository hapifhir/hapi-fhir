package ca.uhn.fhir.jpa.bulk.api;

import java.util.Date;
import java.util.Set;

public class BulkDataExportOptions {
	private final String myOutputFormat;
	private final Set<String> myResourceTypes;
	private final Date mySince;
	private final Set<String> myFilters;

	public BulkDataExportOptions(String theOutputFormat, Set<String> theResourceTypes, Date theSince, Set<String> theFilters) {
		myOutputFormat = theOutputFormat;
		myResourceTypes = theResourceTypes;
		mySince = theSince;
		myFilters = theFilters;
	}

	public String getOutputFormat() {
		return myOutputFormat;
	}

	public Set<String> getResourceTypes() {
		return myResourceTypes;
	}

	public Date getSince() {
		return mySince;
	}

	public Set<String> getFilters() {
		return myFilters;
	}
}
