package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProviderFactory;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.bulk.IBulkDataExportHistoryHelper;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class JpaBulkDataExportHistoryHelper implements IBulkDataExportHistoryHelper {

	@Autowired
	private PersistedJpaBundleProviderFactory myBundleProviderFactory;

	@Override
	public IBundleProvider fetchHistoryForResourceIds(
			@Nonnull String theResourceType,
			@Nonnull List<String> theIdList,
			RequestPartitionId theRequestPartitionId,
			@Nullable Date theRangeStartInclusive,
			@Nonnull Date theRangeEndInclusive) {

		return myBundleProviderFactory.historyFromResourceIds(
				theResourceType, theIdList, theRequestPartitionId, theRangeStartInclusive, theRangeEndInclusive);
	}
}
