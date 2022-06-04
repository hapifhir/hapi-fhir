package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import io.micrometer.core.lang.NonNull;

import java.util.Date;
import java.util.List;

public class HomogeneousBatchIdChunk extends BaseBatchIdChunk {
	@NonNull
	// FIXME KHS rename
	final String myOnlyResourceType;

	HomogeneousBatchIdChunk(List<ResourcePersistentId> theIds, String theResourceType, Date theLastDate) {
		super(theIds, theLastDate);
		myOnlyResourceType = theResourceType;
	}

	@Override
	public String getResourceType(int i) {
			return myOnlyResourceType;
	}
}
