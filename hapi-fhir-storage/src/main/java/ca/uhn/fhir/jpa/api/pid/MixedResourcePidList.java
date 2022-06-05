package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import io.micrometer.core.lang.NonNull;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * A resource pid list where the pids can have different resource types
 */
public class MixedResourcePidList extends BaseResourcePidList {
	@NonNull
	final List<String> myResourceTypes;

	public MixedResourcePidList(Collection<ResourcePersistentId> theIds, List<String> theResourceTypes, Date theLastDate) {
		super(theIds, theLastDate);
		myResourceTypes = theResourceTypes;
	}

	@Override
	public String getResourceType(int i) {
		return myResourceTypes.get(i);
	}
}
