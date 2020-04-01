package ca.uhn.fhir.jpa.search.lastn;

//import ca.uhn.fhir.rest.api.server.IBundleProvider;

import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.util.List;

public interface IElasticsearchSvc {


//    IBundleProvider lastN(javax.servlet.http.HttpServletRequest theServletRequest, ca.uhn.fhir.rest.api.server.RequestDetails theRequestDetails);

//    IBundleProvider uniqueCodes(javax.servlet.http.HttpServletRequest theServletRequest, ca.uhn.fhir.rest.api.server.RequestDetails theRequestDetails);

	List<ResourcePersistentId> executeLastN(SearchParameterMap theSearchParameterMap, RequestDetails theRequestDetails, IdHelperService theIdHelperService);
}
