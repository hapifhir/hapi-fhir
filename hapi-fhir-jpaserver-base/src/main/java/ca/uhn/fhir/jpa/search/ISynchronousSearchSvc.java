package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.ISearchSvc;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;

public interface ISynchronousSearchSvc extends ISearchSvc {

	IBundleProvider executeQuery(SearchParameterMap theParams, RequestDetails theRequestDetails, String theSearchUuid, ISearchBuilder theSb, Integer theLoadSynchronousUpTo, RequestPartitionId theRequestPartitionId);

}
