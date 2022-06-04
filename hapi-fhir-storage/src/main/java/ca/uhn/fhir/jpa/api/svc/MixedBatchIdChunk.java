package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import io.micrometer.core.lang.NonNull;

import java.util.Date;
import java.util.List;

public class MixedBatchIdChunk extends BaseBatchIdChunk {
	@NonNull
	final List<String> myResourceTypes;

	public MixedBatchIdChunk(List<ResourcePersistentId> theIds, List<String> theResourceTypes, Date theLastDate) {
		super(theIds, theLastDate);
		myResourceTypes = theResourceTypes;
	}

	@Override
	public String getResourceType(int i) {
		return myResourceTypes.get(i);
	}
}
