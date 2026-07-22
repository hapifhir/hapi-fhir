package ca.uhn.fhir.jpa.search.exec;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProviderFactory;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.SearchCacheStatusEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CacheAwareSearchSvcImpl implements ICacheAwareSearchSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(CacheAwareSearchSvcImpl.class);

	@Autowired
	private IHapiTransactionService myTxService;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private PersistedJpaBundleProviderFactory myPersistedJpaBundleProviderFactory;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private ISearchCacheSvc mySearchCacheSvc;

	@Override
	public IBundleProvider executeQuery(SearchParameterMap theParams, RequestDetails theRequestDetails, CacheControlDirective theCacheControlDirective, Search theSearchEntity, ISearchBuilder<JpaPid> theSearchBuilder, RequestPartitionId theRequestPartitionId) {
		return new FirstPagePersistedJpaBundleProvider(theParams, theRequestDetails, theCacheControlDirective, theRequestPartitionId, theSearchEntity, theSearchBuilder);
	}


	@Nullable
	private PersistedJpaBundleProvider findCachedQuery(
		SearchParameterMap theParams,
		String theResourceType,
		RequestDetails theRequestDetails,
		String theQueryString,
		RequestPartitionId theRequestPartitionId) {

		HapiTransactionService.requireTransaction();

		IInterceptorBroadcaster compositeBroadcaster =
			CompositeInterceptorBroadcaster.newCompositeBroadcaster(
				myInterceptorBroadcaster, theRequestDetails);

		// Interceptor call: STORAGE_PRECHECK_FOR_CACHED_SEARCH

		HookParams params = new HookParams()
			.add(SearchParameterMap.class, theParams)
			.add(RequestDetails.class, theRequestDetails)
			.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
		boolean canUseCache =
			compositeBroadcaster.callHooks(Pointcut.STORAGE_PRECHECK_FOR_CACHED_SEARCH, params);
		if (!canUseCache) {
			return null;
		}

		// Check for a search matching the given hash
		Search searchToUse = findSearchToUseOrNull(theQueryString, theResourceType, theRequestPartitionId);
		if (searchToUse == null) {
			return null;
		}

		ourLog.debug("Reusing search {} from cache", searchToUse.getUuid());
		// Interceptor call: JPA_PERFTRACE_SEARCH_REUSING_CACHED
		params = new HookParams()
			.add(SearchParameterMap.class, theParams)
			.add(RequestDetails.class, theRequestDetails)
			.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
		compositeBroadcaster.callHooks(Pointcut.JPA_PERFTRACE_SEARCH_REUSING_CACHED, params);

		return myPersistedJpaBundleProviderFactory.newInstance(theRequestDetails, searchToUse.getUuid());
	}

	@Nullable
	private Search findSearchToUseOrNull(
		String theQueryString, String theResourceType, RequestPartitionId theRequestPartitionId) {
		// createdCutoff is in recent past
		final Instant createdCutoff =
			Instant.now().minus(myStorageSettings.getReuseCachedSearchResultsForMillis(), ChronoUnit.MILLIS);

		Optional<Search> candidate = mySearchCacheSvc.findCandidatesForReuse(
			theResourceType, theQueryString, createdCutoff, theRequestPartitionId);
		return candidate.orElse(null);
	}


	public class FirstPagePersistedJpaBundleProvider implements IBundleProvider {

		private final SearchParameterMap myParams;
		private final RequestDetails myRequestDetails;
		private final Search mySearchEntity;
		private final ISearchBuilder<JpaPid> mySearchBuilder;
		private final RequestPartitionId myRequestPartitionId;
		private final CacheControlDirective myCacheControlDirective;
		private boolean mySearchPerformed;
		private PersistedJpaBundleProvider myDelegate;

		public FirstPagePersistedJpaBundleProvider(SearchParameterMap theParams, RequestDetails theRequestDetails, CacheControlDirective theCacheControlDirective, RequestPartitionId theRequestPartitionId, Search theSearchEntity, ISearchBuilder<JpaPid> theSearchBuilder) {
			myParams = theParams;
			myRequestDetails = theRequestDetails;
			myCacheControlDirective = theCacheControlDirective;
			myRequestPartitionId = theRequestPartitionId;
			mySearchEntity = theSearchEntity;
			mySearchBuilder = theSearchBuilder;
		}

		@Override
		public IPrimitiveType<Date> getPublished() {
			ensureSearchPerformed();
			if (myDelegate != null) {
				return myDelegate.getPublished();
			}
			return null;
		}

		@Nullable
		@Override
		public String getUuid() {
			ensureSearchPerformed();
			if (myDelegate != null) {
				return myDelegate.getUuid();
			}
			return "";
		}

		@Override
		public Integer preferredPageSize() {
			if (myDelegate != null) {
				return myDelegate.preferredPageSize();
			}
			return mySearchEntity.getPreferredPageSize();
		}

		@Nullable
		@Override
		public Integer size() {
			ensureSearchPerformed();
			if (myDelegate != null) {
				return myDelegate.size();
			}
			return 0;
		}

		private void ensureSearchPerformed() {
			if (mySearchPerformed) {
				return;
			}

			myTxService
				.withRequest(myRequestDetails)
				.withRequestPartitionId(myRequestPartitionId)
				.execute(() -> {

					/*
					 * See if there are any cached searches whose results we can return
					 * instead
					 */
					SearchCacheStatusEnum cacheStatus;
					if (myCacheControlDirective != null && myCacheControlDirective.isNoCache()) {
						cacheStatus = SearchCacheStatusEnum.NOT_TRIED;
					} else {
						cacheStatus = SearchCacheStatusEnum.MISS;
					}

					if (cacheStatus != SearchCacheStatusEnum.NOT_TRIED) {
						if (myParams.getEverythingMode() == null) {
							if (myStorageSettings.getReuseCachedSearchResultsForMillis() != null) {
								PersistedJpaBundleProvider foundSearchProvider = findCachedQuery(
									myParams, mySearchEntity.getResourceType(), myRequestDetails, mySearchEntity.getSearchQueryString(), myRequestPartitionId);
								if (foundSearchProvider != null) {
									foundSearchProvider.setCacheStatus(SearchCacheStatusEnum.HIT);
									myDelegate = foundSearchProvider;
									return;
								}
							}
						}
					}

					if (myParams.getCount() == null) {
						myParams.setCount(mySearchEntity.getPreferredPageSize());
					}

					SearchRuntimeDetails searchDetails = new SearchRuntimeDetails(myRequestDetails, mySearchEntity.getUuid());
					IResultIterator<JpaPid> query = mySearchBuilder.createQuery(myParams, searchDetails, myRequestDetails, myRequestPartitionId);

					Set<JpaPid> pids = new HashSet<>();


//					PersistedJpaSearchFirstPageBundleProvider retVal = submitSearch(
//						theCallingDao, theParams, theResourceType, theRequestDetails, sb, myRequestPartitionId, search);
//					retVal.setCacheStatus(cacheStatus);
					return;
				});


		}
	}


}
