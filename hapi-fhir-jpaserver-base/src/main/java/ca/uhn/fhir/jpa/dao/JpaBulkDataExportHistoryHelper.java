package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProviderFactory;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.bulk.IBulkDataExportHistoryHelper;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

public class JpaBulkDataExportHistoryHelper implements IBulkDataExportHistoryHelper {

	@Autowired
	private PersistedJpaBundleProviderFactory myBundleProviderFactory;

	@Override
	public IBundleProvider fetchHistoryForResourceIds(
			@Nonnull String theResourceType, List<String> theIdList, RequestPartitionId theRequestPartitionId) {

		Objects.requireNonNull(theResourceType);

		return myBundleProviderFactory.historyFromResourceIds(theResourceType, theIdList, theRequestPartitionId);
	}
}
