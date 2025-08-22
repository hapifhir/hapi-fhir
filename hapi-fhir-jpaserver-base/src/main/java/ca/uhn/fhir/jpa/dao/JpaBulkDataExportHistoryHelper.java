package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProviderFactory;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.IBulkDataExportHistoryHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ca.uhn.fhir.rest.api.Constants.PARAM_ID;

public class JpaBulkDataExportHistoryHelper implements IBulkDataExportHistoryHelper {

	@Autowired
	private PersistedJpaBundleProviderFactory myBundleProviderFactory;

	@Override
	public IBundleProvider fetchHistoryForResourceIds(
			String theResourceType, List<String> theResourceIds, RequestPartitionId theRequestPartitionId) {

		RequestDetails requestDetails = SystemRequestDetails.forRequestPartitionId(theRequestPartitionId);
		requestDetails.setResourceName(theResourceType);
		requestDetails.addParameter(PARAM_ID, theResourceIds.toArray(new String[0]));

		return myBundleProviderFactory.history(
				requestDetails, theResourceType, null, null, null, null, theRequestPartitionId);
	}
}
