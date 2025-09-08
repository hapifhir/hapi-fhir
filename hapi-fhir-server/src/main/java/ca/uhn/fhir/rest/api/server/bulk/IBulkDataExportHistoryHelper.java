package ca.uhn.fhir.rest.api.server.bulk;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * Bulk export history retrieval helper
 */
public interface IBulkDataExportHistoryHelper {

	IBundleProvider fetchHistoryForResourceIds(
		@Nonnull String theResourceType, List<String> theIdList, RequestPartitionId theRequestPartitionId);
}
