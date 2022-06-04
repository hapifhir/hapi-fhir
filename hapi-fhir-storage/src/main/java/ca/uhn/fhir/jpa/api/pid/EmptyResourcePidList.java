package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * An empty resource pid list
 */
public class EmptyResourcePidList implements IResourcePidList {
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
	public List<TypedResourcePid> getTypedResourcePids() {
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

	@Override
	public boolean isEmpty() {
		return true;
	}
}
