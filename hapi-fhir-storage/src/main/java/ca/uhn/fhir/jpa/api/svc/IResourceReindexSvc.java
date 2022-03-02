package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;

public interface IResourceReindexSvc {

	/**
	 * Returns the lastUpdated timestamp for the oldest resource in the storage module
	 *
	 * @param theResourceType The resource type, or <code>null</code> to return the oldest resource type across all resource types
	 */
	Date getOldestTimestamp(@Nullable String theResourceType);

	/**
	 * Fetches a page of resource IDs. The page size is up to the discretion of the implementation.
	 *
	 * @param theStart The start of the date range, must be inclusive.
	 * @param theEnd The end of the date range, should be exclusive.
	 */
	List<ResourcePersistentId> fetchResourceIdsPage(Date theStart, Date theEnd);
}
