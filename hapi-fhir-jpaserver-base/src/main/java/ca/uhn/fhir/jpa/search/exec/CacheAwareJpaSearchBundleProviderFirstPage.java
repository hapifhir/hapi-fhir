/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.search.exec;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.ExceptionService;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SearchCacheStatus;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Search result provider for the first page of a cache-aware search.
 *
 * @see CacheAwareSearchSvcImpl
 * @since 8.14.0
 */
public class CacheAwareJpaSearchBundleProviderFirstPage extends BaseCacheAwareJpaSearchBundleProvider {
	private static final Logger ourLog = LoggerFactory.getLogger(CacheAwareJpaSearchBundleProviderFirstPage.class);

	private final CacheControlDirective myCacheControlDirective;
	private Search myCandidateSearchEntity;

	/**
	 * Constructor
	 */
	public CacheAwareJpaSearchBundleProviderFirstPage(
			FhirContext theFhirContext,
			SearchParameterMap theParams,
			RequestDetails theRequestDetails,
			CacheControlDirective theCacheControlDirective,
			RequestPartitionId theRequestPartitionId,
			Search theCandidateSearchEntity,
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
				theInterceptorBroadcaster,
				thePagingProvider,
				theStorageSettings,
				theEntityManager,
				theTxService,
				theRequestPartitionHelperSvc,
				theSearchCacheSvc,
				theSearchResultCacheSvc,
				theExceptionService,
				theSearchBuilderFactory,
				theParams,
				theRequestPartitionId);

		myCacheControlDirective = theCacheControlDirective;
		myCandidateSearchEntity = theCandidateSearchEntity;
	}

	@Override
	protected Search provideSearchEntity() {
		Search retVal;

		/*
		 * When we create this IBundleProvider, an unsaved candidate Search entity is created with all the details
		 * about the search. If we're configured to do so, we check the query cache for any stored searches we can
		 * reuse before we actually commit to using the candidate.
		 */
		if (myCandidateSearchEntity != null) {
			retVal = myCandidateSearchEntity;
			myCandidateSearchEntity = null;

			if (!myCacheControlDirective.isNoCache()) {
				if (myParams.getEverythingMode() == null) {
					if (myStorageSettings.getReuseCachedSearchResultsForMillis() != null) {
						Optional<Search> cachedQueryOpt;
						cachedQueryOpt = findCachedQuery(
								myParams,
								retVal.getResourceType(),
								myRequestDetails,
								myParams.toNormalizedQueryString(),
								myRequestPartitionId);
						if (cachedQueryOpt.isPresent()) {
							retVal = cachedQueryOpt.get();

							myCacheStatus = SearchCacheStatus.builder()
									.withCacheName("HapiQueryCache")
									.setStatus(SearchCacheStatus.SearchCacheStatusEnum.HIT)
									.setCacheEntryTimestamp(retVal.getCreated())
									.build();

							ourLog.atDebug()
									.setMessage("Query cache HIT - Search[{}} is satisfied by with search UUID: {}")
									.addArgument(() -> myParams.toNormalizedQueryString())
									.addArgument(retVal.getUuid())
									.log();

						} else {
							myCacheStatus = SearchCacheStatus.builder()
									.withCacheName("HapiQueryCache")
									.setStatus(SearchCacheStatus.SearchCacheStatusEnum.FWD_MISS)
									.build();
						}
					}
				}
			} else {
				myCacheStatus = CACHE_STATUS_BYPASS;
			}

		} else {

			/// If we get here, we're in a second transaction on the same IBundleProvidr instance. This
			/// generally means that someone has kept it around and are making subsequent calls to
			/// {@link #getResources(int, int, ResponsePage.ResponsePageBuilder)}. So we reload the
			/// entity so we have a fresh copy attached to the session for when we go to commit it.

			retVal = mySearchCacheSvc
					.fetchByUuid(provideLoadedSearchEntity().getUuid(), myRequestPartitionId)
					.orElseThrow(() -> myExceptionService.newUnknownSearchException(
							provideLoadedSearchEntity().getUuid()));
		}

		return retVal;
	}

	private Optional<Search> findCachedQuery(
			SearchParameterMap theParams,
			String theResourceType,
			RequestDetails theRequestDetails,
			String theQueryString,
			RequestPartitionId theRequestPartitionId) {

		HapiTransactionService.requireTransaction();

		// Interceptor call: STORAGE_PRECHECK_FOR_CACHED_SEARCH
		if (myCompositeBroadcaster.hasHooks(Pointcut.STORAGE_PRECHECK_FOR_CACHED_SEARCH)) {
			HookParams params = new HookParams()
					.add(SearchParameterMap.class, theParams)
					.add(RequestDetails.class, theRequestDetails)
					.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
			boolean canUseCache = myCompositeBroadcaster.callHooks(Pointcut.STORAGE_PRECHECK_FOR_CACHED_SEARCH, params);
			if (!canUseCache) {
				return Optional.empty();
			}
		}

		// Check for a search matching the given hash
		Search searchToUse = findSearchToUseOrNull(theQueryString, theResourceType, theRequestPartitionId);
		if (searchToUse == null) {
			return Optional.empty();
		}

		ourLog.debug("Reusing search {} from cache", searchToUse.getUuid());

		// Interceptor call: JPA_PERFTRACE_SEARCH_REUSING_CACHED
		if (myCompositeBroadcaster.hasHooks(Pointcut.JPA_PERFTRACE_SEARCH_REUSING_CACHED)) {
			HookParams params = new HookParams()
					.add(SearchParameterMap.class, theParams)
					.add(RequestDetails.class, theRequestDetails)
					.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
			myCompositeBroadcaster.callHooks(Pointcut.JPA_PERFTRACE_SEARCH_REUSING_CACHED, params);
		}

		return Optional.of(searchToUse);
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

	@Override
	public Integer preferredPageSize() {
		if (myCandidateSearchEntity != null) {
			return myCandidateSearchEntity.getPreferredPageSize();
		}
		return super.preferredPageSize();
	}
}
