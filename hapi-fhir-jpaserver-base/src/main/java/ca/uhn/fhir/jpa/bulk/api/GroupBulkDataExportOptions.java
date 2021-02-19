package ca.uhn.fhir.jpa.bulk.api;

import java.util.Date;
import java.util.Set;

public class GroupBulkDataExportOptions extends BulkDataExportOptions {
	private final String myGroupId;

	public GroupBulkDataExportOptions(String theOutputFormat, Set<String> theResourceTypes, Date theSince, Set<String> theFilters, String theGroupId) {
		super(theOutputFormat, theResourceTypes, theSince, theFilters);
		myGroupId = theGroupId;
	}

	public String getGroupId() {
		return myGroupId;
	}
}
