package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl;
import ca.uhn.fhir.jpa.search.SearchStrategyFactory;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.jpa.search.tasks.SearchContinuationTask;
import ca.uhn.fhir.jpa.search.tasks.SearchTask;
import ca.uhn.fhir.jpa.search.tasks.SearchTaskParameters;
import ca.uhn.fhir.rest.server.IPagingProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SearchConfig {

	@Autowired
	private PlatformTransactionManager myManagedTxManager;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private SearchStrategyFactory mySearchStrategyFactory;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;
	@Autowired
	private ISearchResultCacheSvc mySearchResultCacheSvc;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private ISearchCacheSvc mySearchCacheSvc;
	@Autowired
	private IPagingProvider myPagingProvider;

	@Bean
	public ISearchCoordinatorSvc searchCoordinatorSvc() {
		return new SearchCoordinatorSvcImpl();
	}

	@Bean(name = "searchTask")
	@Scope("prototype")
	public SearchTask createSearchTask(SearchTaskParameters theParams) {
		return new SearchTask(theParams,
			myManagedTxManager,
			myContext,
			mySearchStrategyFactory,
			myInterceptorBroadcaster,
			mySearchBuilderFactory,
			mySearchResultCacheSvc,
			myDaoConfig,
			mySearchCacheSvc,
			myPagingProvider
		);
	}

	@Bean(name = "continueTask")
	@Scope("prototype")
	public SearchContinuationTask createSearchContinuationTask(SearchTaskParameters theParams) {
		return new SearchContinuationTask(theParams,
			myManagedTxManager,
			myContext,
			mySearchStrategyFactory,
			myInterceptorBroadcaster,
			mySearchBuilderFactory,
			mySearchResultCacheSvc,
			myDaoConfig,
			mySearchCacheSvc,
			myPagingProvider
		);
	}
}
