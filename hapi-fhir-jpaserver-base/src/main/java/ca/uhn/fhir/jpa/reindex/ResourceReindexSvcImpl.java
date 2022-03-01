package ca.uhn.fhir.jpa.reindex;

import ca.uhn.fhir.jpa.api.svc.IResourceReindexSvc;

import javax.annotation.Nullable;
import java.util.Date;

public class ResourceReindexSvcImpl implements IResourceReindexSvc {
	@Override
	public Date getOldestTimestamp(@Nullable String theResourceType) {
		return null;
	}
}
