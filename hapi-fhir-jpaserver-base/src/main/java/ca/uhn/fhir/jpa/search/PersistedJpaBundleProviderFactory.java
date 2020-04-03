package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.jpa.config.BaseConfig;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class PersistedJpaBundleProviderFactory {

	@Autowired
	private ApplicationContext myApplicationContext;

	public PersistedJpaBundleProvider newInstance(RequestDetails theRequest, String theUuid) {
		Object retVal = myApplicationContext.getBean(BaseConfig.PERSISTED_JPA_BUNDLE_PROVIDER, theRequest, theUuid);
		return (PersistedJpaBundleProvider) retVal;
	}

	public PersistedJpaSearchFirstPageBundleProvider newInstanceFirstPage(RequestDetails theRequestDetails, Search theSearch, SearchCoordinatorSvcImpl.SearchTask theTask, ISearchBuilder theSearchBuilder) {
		return (PersistedJpaSearchFirstPageBundleProvider) myApplicationContext.getBean(BaseConfig.PERSISTED_JPA_SEARCH_FIRST_PAGE_BUNDLE_PROVIDER, theRequestDetails, theSearch, theTask, theSearchBuilder);
	}
}
