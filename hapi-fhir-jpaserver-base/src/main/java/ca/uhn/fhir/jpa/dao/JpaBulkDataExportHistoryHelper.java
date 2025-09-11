package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProviderFactory;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.bulk.IBulkDataExportHistoryHelper;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class JpaBulkDataExportHistoryHelper implements IBulkDataExportHistoryHelper {

	@Autowired
	private PersistedJpaBundleProviderFactory myBundleProviderFactory;

	@Override
	public IBundleProvider fetchHistoryForResourceIds(
			@Nonnull String theResourceType, RequestPartitionId theRequestPartitionId) {

		Objects.requireNonNull(theResourceType);

		// fixme jm: remove commented
//		RequestDetails requestDetails = SystemRequestDetails.forRequestPartitionId(theRequestPartitionId);
//		requestDetails.setResourceName(theResourceType);
//
//		return myBundleProviderFactory.history(
//			requestDetails, theResourceType, null, null, null, null, theRequestPartitionId);

		// fixme jm: last param?
		return myBundleProviderFactory.historyUnlimited(
			theResourceType, null, null, null, theRequestPartitionId, null);
	}
}
