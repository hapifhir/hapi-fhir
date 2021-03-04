package ca.uhn.fhir.jpa.bulk.api;

import java.util.Date;
import java.util.Set;

public class BulkDataExportOptions {
	private final String myOutputFormat;
	private final Set<String> myResourceTypes;
	private final Date mySince;
	private final Set<String> myFilters;
	private boolean mySystemLevel;

	public BulkDataExportOptions(String theOutputFormat, Set<String> theResourceTypes, Date theSince, Set<String> theFilters, boolean theSystemLevel) {
		myOutputFormat = theOutputFormat;
		myResourceTypes = theResourceTypes;
		mySince = theSince;
		myFilters = theFilters;
		mySystemLevel = theSystemLevel;
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

	public boolean isSystemLevel() {
		return mySystemLevel;
	}
}
