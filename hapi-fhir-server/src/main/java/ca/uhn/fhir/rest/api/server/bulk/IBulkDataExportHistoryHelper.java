package ca.uhn.fhir.rest.api.server.bulk;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.IBundleProvider;

import java.util.List;

// fixme jm: doc
public interface IBulkDataExportHistoryHelper {

	IBundleProvider fetchHistoryForResourceIds(
			String theResourceType, List<String> theIdList, RequestPartitionId theRequestPartitionId);
}
