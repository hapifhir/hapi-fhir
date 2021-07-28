package ca.uhn.fhir.jpa.batch.reader;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.UUID;

public class BatchResourceSearcher {
	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;
	@Autowired
	private DaoRegistry myDaoRegistry;

	public IResultIterator performSearch(ResourceSearch theResourceSearch, Integer theBatchSize) {
		String resourceName = theResourceSearch.getResourceName();
		RequestPartitionId requestPartitionId = theResourceSearch.getRequestPartitionId();

		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceName);
		final ISearchBuilder sb = mySearchBuilderFactory.newSearchBuilder(dao, resourceName, theResourceSearch.getResourceType());
		sb.setFetchSize(theBatchSize);
		SystemRequestDetails requestDetails = buildSystemRequestDetails(requestPartitionId);
		SearchRuntimeDetails searchRuntimeDetails = new SearchRuntimeDetails(requestDetails, UUID.randomUUID().toString());
		IResultIterator resultIter = sb.createQuery(theResourceSearch.getSearchParameterMap(), searchRuntimeDetails, requestDetails, requestPartitionId);
		return resultIter;
	}

	@Nonnull
	private SystemRequestDetails buildSystemRequestDetails(RequestPartitionId theRequestPartitionId) {
		SystemRequestDetails retval = new SystemRequestDetails();
		retval.setRequestPartitionId(theRequestPartitionId);
		return retval;
	}
}
