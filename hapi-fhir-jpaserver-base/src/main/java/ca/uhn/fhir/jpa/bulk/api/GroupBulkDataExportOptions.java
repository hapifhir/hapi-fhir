package ca.uhn.fhir.jpa.bulk.api;

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Date;
import java.util.Set;

public class GroupBulkDataExportOptions extends BulkDataExportOptions {
	private final IIdType myGroupId;
	private final boolean myMdm;

	public GroupBulkDataExportOptions(String theOutputFormat, Set<String> theResourceTypes, Date theSince, Set<String> theFilters, IIdType theGroupId, boolean theMdm) {
		super(theOutputFormat, theResourceTypes, theSince, theFilters);
		myGroupId = theGroupId;
		myMdm = theMdm;
	}

	public IIdType getGroupId() {
		return myGroupId;
	}

	public boolean isMdm() {
		return myMdm;
	}
}
