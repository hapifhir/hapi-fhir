package ca.uhn.fhir.rest.api.server.bulk;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import jakarta.annotation.Nonnull;

/**
 * Bulk export history retrieval helper
 */
public interface IBulkDataExportHistoryHelper {

	/**
	 * Retrieve history for indicated resource IDs
	 *
	 * @param theResourceType         the type of resources to fetch history for
	 * @param theRequestPartitionId   partition ID for the request
	 * @return bundle provider containing historical versions of the resources
	 */
	IBundleProvider fetchHistoryForResourceIds(
		@Nonnull String theResourceType, RequestPartitionId theRequestPartitionId);
}
