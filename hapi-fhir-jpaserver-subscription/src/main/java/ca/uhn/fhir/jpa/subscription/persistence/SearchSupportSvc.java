package ca.uhn.fhir.jpa.subscription.persistence;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.api.svc.ISearchSvc;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SearchSupportSvc {
	@Autowired
	private ISearchSvc mySearchService;

	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;

	@Autowired
	private ISearchCoordinatorSvc<? extends IResourcePersistentId<?>> mySearchCoordinatorSvc;


	public IBundleProvider search(IFhirResourceDao<?> callingDao, SearchParameterMap params, String resourceType, RequestPartitionId thePartitionId) {
		return mySearchCoordinatorSvc.registerSearch(
			callingDao,
			params,
			resourceType,
			new CacheControlDirective(),
			null,
			thePartitionId);
	}

	public List<? extends IResourcePersistentId<?>> getResources(String theCurrentSearchUuid, int fromIndex, int toIndex, RequestPartitionId requestPartitionId) {
		return mySearchCoordinatorSvc.getResources(
			theCurrentSearchUuid, fromIndex, toIndex, null, requestPartitionId);
	}

	public ISearchBuilder getSearchBuilder(IFhirResourceDao<?> resourceDao, String resourceType, Class<? extends IBaseResource> theResourceClass) {
		return mySearchBuilderFactory.newSearchBuilder(
			resourceDao, resourceType, theResourceClass);
	}


	public IBundleProvider search(SearchParameterMap params, String theResourceName, RequestPartitionId thePartitionId) {
		return mySearchService.executeQuery(theResourceName, params, thePartitionId);
	}
}
