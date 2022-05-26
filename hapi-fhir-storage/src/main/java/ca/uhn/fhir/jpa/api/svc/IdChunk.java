package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import java.util.Date;
import java.util.List;

public class IdChunk {

	final List<ResourcePersistentId> myIds;
	final List<String> myResourceTypes;
	final Date myLastDate;

	public IdChunk(List<ResourcePersistentId> theIds, List<String> theResourceTypes, Date theLastDate) {
		myIds = theIds;
		myResourceTypes = theResourceTypes;
		myLastDate = theLastDate;
	}

	public List<String> getResourceTypes() {
		return myResourceTypes;
	}

	public List<ResourcePersistentId> getIds() {
		return myIds;
	}

	public Date getLastDate() {
		return myLastDate;
	}
}
