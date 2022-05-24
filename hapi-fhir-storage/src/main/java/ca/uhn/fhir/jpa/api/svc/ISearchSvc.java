package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;

public interface ISearchSvc {

	IBundleProvider executeQuery(String theResourceType, SearchParameterMap theSearchParameterMap, RequestPartitionId theRequestPartitionId);

}
