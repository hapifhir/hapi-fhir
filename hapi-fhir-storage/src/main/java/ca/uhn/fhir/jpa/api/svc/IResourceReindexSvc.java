package ca.uhn.fhir.jpa.api.svc;

import javax.annotation.Nullable;
import java.util.Date;

public interface IResourceReindexSvc {

	/**
	 * Returns the lastUpdated timestamp for the oldest resource in the storage module
	 *
	 * @param theResourceType The resource type, or <code>null</code> to return the oldest resource type across all resource types
	 */
	Date getOldestTimestamp(@Nullable String theResourceType);

}
