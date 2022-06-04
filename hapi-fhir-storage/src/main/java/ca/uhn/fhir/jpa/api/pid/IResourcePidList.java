package ca.uhn.fhir.jpa.api.pid;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.List;

/**
 * List of ResourcePersistentId along with a resource type each id
 */
public interface IResourcePidList {

	Date getLastDate();

	int size();

	@Nonnull
	List<TypedResourcePid> getBatchResourceIds();

	String getResourceType(int i);

	List<ResourcePersistentId> getIds();

	boolean isEmpty();
}
