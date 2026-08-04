package ca.uhn.fhir.jpa.search.exec;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.ExceptionService;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class JpaSearchBundleProviderSubsequentPage extends BaseJpaSearchBundleProvider {
	private static final Logger ourLog = LoggerFactory.getLogger(JpaSearchBundleProviderSubsequentPage.class);

	/**
	 * Constructor
	 */
	public JpaSearchBundleProviderSubsequentPage(
			FhirContext theFhirContext,
			RequestDetails theRequestDetails,
			String theSearchUuid,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			IPagingProvider thePagingProvider,
			JpaStorageSettings theStorageSettings,
			EntityManager theEntityManager,
			IHapiTransactionService theTxService,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc,
			ISearchCacheSvc theSearchCacheSvc,
			ISearchResultCacheSvc theSearchResultCacheSvc,
			ExceptionService theExceptionService,
			SearchBuilderFactory<JpaPid> theSearchBuilderFactory) {
		super(
				theFhirContext,
				theRequestDetails,
				theSearchUuid,
				theInterceptorBroadcaster,
				thePagingProvider,
				theStorageSettings,
				theEntityManager,
				theTxService,
				theRequestPartitionHelperSvc,
				theSearchCacheSvc,
				theSearchResultCacheSvc,
				theExceptionService,
				theSearchBuilderFactory);
	}

	@Override
	protected Search provideSearchEntity() {
		ReadPartitionIdRequestDetails details = ReadPartitionIdRequestDetails.forSearchUuid(mySearchUuid);
		myRequestPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequest(myRequestDetails, details);

		ourLog.debug("Fetching cached search with UUID: {}", mySearchUuid);

		Optional<Search> searchEntityOpt = mySearchCacheSvc.fetchByUuid(mySearchUuid, myRequestPartitionId);
		return searchEntityOpt.orElseThrow(() -> myExceptionService.newUnknownSearchException(mySearchUuid));
	}
}
