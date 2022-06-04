package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.List;

public interface IBatchIdChunk {

	Date getLastDate();

	int size();

	@Nonnull
	List<BatchResourceId> getBatchResourceIds();

	String getResourceType(int i);

	List<ResourcePersistentId> getIds();
}
