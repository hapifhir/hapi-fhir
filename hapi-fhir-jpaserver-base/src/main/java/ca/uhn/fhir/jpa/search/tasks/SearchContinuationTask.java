package ca.uhn.fhir.jpa.search.tasks;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.search.SearchStrategyFactory;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.method.PageMethodBinding;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.util.List;

public class SearchContinuationTask extends SearchTask {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchContinuationTask.class);

	public SearchContinuationTask(SearchTaskParameters theCreationParams, PlatformTransactionManager theManagedTxManager, FhirContext theContext, SearchStrategyFactory theSearchStrategyFactory, IInterceptorBroadcaster theInterceptorBroadcaster, SearchBuilderFactory theSearchBuilderFactory, ISearchResultCacheSvc theSearchResultCacheSvc, DaoConfig theDaoConfig, ISearchCacheSvc theSearchCacheSvc, IPagingProvider thePagingProvider) {
		super(theCreationParams, theManagedTxManager, theContext, theSearchStrategyFactory, theInterceptorBroadcaster, theSearchBuilderFactory, theSearchResultCacheSvc, theDaoConfig, theSearchCacheSvc, thePagingProvider);
	}


	@Override
	public Void call() {
		try {
			TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
			txTemplate.afterPropertiesSet();
			txTemplate.execute(t -> {
				List<ResourcePersistentId> previouslyAddedResourcePids = mySearchResultCacheSvc.fetchAllResultPids(getSearch());
				if (previouslyAddedResourcePids == null) {
					throw newResourceGoneException(getSearch().getUuid());
				}

				ourLog.trace("Have {} previously added IDs in search: {}", previouslyAddedResourcePids.size(), getSearch().getUuid());
				setPreviouslyAddedResourcePids(previouslyAddedResourcePids);
				return null;
			});
		} catch (Throwable e) {
			ourLog.error("Failure processing search", e);
			getSearch().setFailureMessage(e.getMessage());
			getSearch().setStatus(SearchStatusEnum.FAILED);
			if (e instanceof BaseServerResponseException) {
				getSearch().setFailureCode(((BaseServerResponseException) e).getStatusCode());
			}

			saveSearch();
			return null;
		}

		return super.call();
	}

	// Copy pasta - find a better way
	@Nonnull
	private ResourceGoneException newResourceGoneException(String theUuid) {
		ourLog.trace("Client requested unknown paging ID[{}]", theUuid);
		String msg = myContext.getLocalizer().getMessage(PageMethodBinding.class, "unknownSearchId", theUuid);
		return new ResourceGoneException(msg);
	}
}
