package ca.uhn.fhir.jpa.search.exec;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.ExceptionService;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CacheAwareSearchSvcImpl implements ICacheAwareSearchSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(CacheAwareSearchSvcImpl.class);

	@Autowired
	private ExceptionService myExceptionSvc;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IHapiTransactionService myTxService;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private ISearchCacheSvc mySearchCacheSvc;

	@Autowired
	private ISearchResultCacheSvc mySearchResultCacheSvc;

	@Autowired
	private EntityManager myEntityManager;

	@Autowired
	private SearchBuilderFactory<JpaPid> mySearchBuilderFactory;

	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Autowired
	private IPagingProvider myPagingProvider;

	/**
	 * Constructor
	 */
	public CacheAwareSearchSvcImpl() {
		super();
	}

	/**
	 * Unit test constructor
	 */
	public CacheAwareSearchSvcImpl(
			FhirContext theFhirContext,
			IHapiTransactionService theTxService,
			JpaStorageSettings theStorageSettings,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			ISearchCacheSvc theSearchCacheSvc,
			ISearchResultCacheSvc theSearchResultCacheSvc,
			EntityManager theEntityManager,
			SearchBuilderFactory<JpaPid> theSearchBuilderFactory,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myFhirContext = theFhirContext;
		myTxService = theTxService;
		myStorageSettings = theStorageSettings;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
		mySearchCacheSvc = theSearchCacheSvc;
		mySearchResultCacheSvc = theSearchResultCacheSvc;
		myEntityManager = theEntityManager;
		mySearchBuilderFactory = theSearchBuilderFactory;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
	}

	@Override
	public IBundleProvider createNewSearch(
			SearchParameterMap theParams,
			RequestDetails theRequestDetails,
			CacheControlDirective theCacheControlDirective,
			Search theSearchEntity,
			ISearchBuilder<JpaPid> theSearchBuilder,
			RequestPartitionId theRequestPartitionId) {
		return new JpaBundleProviderFirstPage(
				myFhirContext,
				theParams,
				theRequestDetails,
				theCacheControlDirective,
				theRequestPartitionId,
				theSearchEntity,
				myInterceptorBroadcaster,
				myPagingProvider,
				myStorageSettings,
				myEntityManager,
				myTxService,
				myRequestPartitionHelperSvc,
				mySearchCacheSvc,
				mySearchResultCacheSvc,
				myExceptionSvc,
				mySearchBuilderFactory);
	}

	@Override
	public IBundleProvider continueExistingSearch(String theId, RequestDetails theRequestDetails) {
		return new JpaBundleProviderSubsequentPage(
				myFhirContext,
				theRequestDetails,
				theId,
				myInterceptorBroadcaster,
				myPagingProvider,
				myStorageSettings,
				myEntityManager,
				myTxService,
				myRequestPartitionHelperSvc,
				mySearchCacheSvc,
				mySearchResultCacheSvc,
				myExceptionSvc,
				mySearchBuilderFactory);
	}
}
