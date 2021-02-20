package ca.uhn.fhir.jpa.bulk.api;

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Date;
import java.util.Set;

public class GroupBulkDataExportOptions extends BulkDataExportOptions {
	private final IIdType myGroupId;

	public GroupBulkDataExportOptions(String theOutputFormat, Set<String> theResourceTypes, Date theSince, Set<String> theFilters, IIdType theGroupId) {
		super(theOutputFormat, theResourceTypes, theSince, theFilters);
		myGroupId = theGroupId;
	}

	public IIdType getGroupId() {
		return myGroupId;
	}
}
