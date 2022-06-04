package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class EmptyBatchIdChunk implements IBatchIdChunk {
	@Override
	public Date getLastDate() {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}

	@Nonnull
	@Override
	public List<BatchResourceId> getBatchResourceIds() {
		return Collections.emptyList();
	}

	@Override
	public String getResourceType(int i) {
		throw new ArrayIndexOutOfBoundsException();
	}

	@Override
	public List<ResourcePersistentId> getIds() {
		return Collections.emptyList();
	}
}
