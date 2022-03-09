package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;

public interface IResourceReindexSvc {

	/**
	 * Indicates whether reindexing all resource types is supported. Implementations are expected to provide a static response (either they support this or they don't).
	 */
	boolean isAllResourceTypeSupported();

	/**
	 * Returns the lastUpdated timestamp for the oldest resource in the storage module
	 *
	 * @param theResourceType The resource type, or <code>null</code> to return the oldest resource type across all resource types. Null will only be supplied if {@link #isAllResourceTypeSupported()} returns <code>true</code>.
	 */
	Date getOldestTimestamp(@Nullable String theResourceType);

	/**
	 * Fetches a page of resource IDs for all resource types. The page size is up to the discretion of the implementation.
	 *
	 * @param theStart The start of the date range, must be inclusive.
	 * @param theEnd   The end of the date range, should be exclusive.
	 * @param theUrl   The search URL, or <code>null</code> to return IDs for all resources across all resource types. Null will only be supplied if {@link #isAllResourceTypeSupported()} returns <code>true</code>.
	 */
	IdChunk fetchResourceIdsPage(Date theStart, Date theEnd, @Nullable String theUrl);

	class IdChunk {

		final List<ResourcePersistentId> myIds;
		final List<String> myResourceTypes;
		final Date myLastDate;

		public IdChunk(List<ResourcePersistentId> theIds, List<String> theResourceTypes, Date theLastDate) {
			myIds = theIds;
			myResourceTypes = theResourceTypes;
			myLastDate = theLastDate;
		}

		public List<String> getResourceTypes() {
			return myResourceTypes;
		}

		public List<ResourcePersistentId> getIds() {
			return myIds;
		}

		public Date getLastDate() {
			return myLastDate;
		}
	}

}
