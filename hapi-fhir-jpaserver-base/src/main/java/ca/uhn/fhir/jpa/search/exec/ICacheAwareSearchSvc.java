package ca.uhn.fhir.jpa.search.exec;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;

public interface ICacheAwareSearchSvc {

	// FIXME: rename to executeNewQuery
	IBundleProvider createNewSearch(
			SearchParameterMap theParams,
			RequestDetails theRequestDetails,
			CacheControlDirective theCacheControlDirective,
			Search theSearchEntity,
			ISearchBuilder<JpaPid> theSearchBuilder,
			RequestPartitionId theRequestPartitionId);

	// FIXME: rename to continueExistingQuery
	IBundleProvider continueExistingSearch(String theId, RequestDetails theRequestDetails);
}
