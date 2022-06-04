package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import io.micrometer.core.lang.NonNull;

import java.util.Date;
import java.util.List;

/**
 * A resource pid list where all pids have the same resource type
 */
public class HomogeneousResourcePidList extends BaseResourcePidList {
	@NonNull
	final String myResourceType;

	public HomogeneousResourcePidList(List<ResourcePersistentId> theIds, String theResourceType, Date theLastDate) {
		super(theIds, theLastDate);
		myResourceType = theResourceType;
	}

	@Override
	public String getResourceType(int i) {
			return myResourceType;
	}
}
