package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProviderFactory;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.IBulkDataExportHistoryHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

// fixme jm: doc
public class JpaBulkDataExportHistoryHelper implements IBulkDataExportHistoryHelper {

	@Autowired
	private PersistedJpaBundleProviderFactory myBundleProviderFactory;

	@Override
	public IBundleProvider fetchHistoryForResourceIds(
			String theResourceType, List<String> theResourceIds, RequestPartitionId theRequestPartitionId) {

		return myBundleProviderFactory.history(
				new SystemRequestDetails(), theResourceType, null, null, null, null, theRequestPartitionId);
	}
}
