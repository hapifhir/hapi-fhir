package ca.uhn.fhir.jpa.search.reindex;

import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.partition.BaseRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.partition.RequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

public class ReindexDryRunServiceImpl implements IReindexDryRunService {

	@Autowired
	private SearchParamExtractorService mySearchParamExtractorService;
	@Autowired
	private BaseRequestPartitionHelperSvc myPartitionHelperSvc;
	@Autowired
	private I

	@Override
	public IBaseParameters reindexDryRun(RequestDetails theRequestDetails, IIdType theResourceId) {

		ReadPartitionIdRequestDetails details = ReadPartitionIdRequestDetails.forRead(theResourceId);
		RequestPartitionId partitionId = myPartitionHelperSvc.determineReadPartitionForRequest(theRequestDetails, details);

		ResourceIndexedSearchParams paramsToPopulate = new ResourceIndexedSearchParams();
		mySearchParamExtractorService.extractFromResource(partitionId, theRequestDetails, paramsToPopulate, entity, resource, transactionDetails, false);

		return null;
	}

}
