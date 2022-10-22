package ca.uhn.fhir.jpa.search;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
 * #L%family
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.config.SearchConfig;
import ca.uhn.fhir.jpa.dao.BaseStorageDao;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.search.ResourceNotFoundInIndexException;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.builder.StorageInterceptorHooksFacade;
import ca.uhn.fhir.jpa.search.builder.tasks.SearchContinuationTask;
import ca.uhn.fhir.jpa.search.builder.tasks.SearchTask;
import ca.uhn.fhir.jpa.search.builder.tasks.SearchTaskParameters;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.jpa.search.cache.SearchCacheStatusEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.AsyncUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.util.QueryParameterUtils.DEFAULT_SYNC_SIZE;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component("mySearchCoordinatorSvc")
public class SearchCoordinatorSvcImpl implements ISearchCoordinatorSvc {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchCoordinatorSvcImpl.class);

	private final FhirContext myContext;
	private final DaoConfig myDaoConfig;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;
	private final PlatformTransactionManager myManagedTxManager;
	private final ISearchCacheSvc mySearchCacheSvc;
	private final ISearchResultCacheSvc mySearchResultCacheSvc;
	private final DaoRegistry myDaoRegistry;
	private final SearchBuilderFactory mySearchBuilderFactory;
	private final ISynchronousSearchSvc mySynchronousSearchSvc;
	private final PersistedJpaBundleProviderFactory myPersistedJpaBundleProviderFactory;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperService;
	private final ISearchParamRegistry mySearchParamRegistry;
	private final SearchStrategyFactory mySearchStrategyFactory;
	private final ExceptionService myExceptionSvc;
	private final BeanFactory myBeanFactory;

	private Integer myLoadingThrottleForUnitTests = null;
	private long myMaxMillisToWaitForRemoteResults = DateUtils.MILLIS_PER_MINUTE;
	private boolean myNeverUseLocalSearchForUnitTests;

	private int mySyncSize = DEFAULT_SYNC_SIZE;

	private final ConcurrentHashMap<String, SearchTask> myIdToSearchTask = new ConcurrentHashMap<>();

	private final Consumer<String> myOnRemoveSearchTask = (theId) -> {
		myIdToSearchTask.remove(theId);
	};

	private final StorageInterceptorHooksFacade myStorageInterceptorHooks;

	/**
	 * Constructor
	 */
	public SearchCoordinatorSvcImpl(
		FhirContext theContext,
		DaoConfig theDaoConfig,
		IInterceptorBroadcaster theInterceptorBroadcaster,
		PlatformTransactionManager theManagedTxManager,
		ISearchCacheSvc theSearchCacheSvc,
		ISearchResultCacheSvc theSearchResultCacheSvc,
		DaoRegistry theDaoRegistry,
		SearchBuilderFactory theSearchBuilderFactory,
		ISynchronousSearchSvc theSynchronousSearchSvc,
		PersistedJpaBundleProviderFactory thePersistedJpaBundleProviderFactory,
		IRequestPartitionHelperSvc theRequestPartitionHelperService,
		ISearchParamRegistry theSearchParamRegistry,
		SearchStrategyFactory theSearchStrategyFactory,
		ExceptionService theExceptionSvc,
		BeanFactory theBeanFactory
	) {
		super();
		myContext = theContext;
		myDaoConfig = theDaoConfig;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
		myManagedTxManager = theManagedTxManager;
		mySearchCacheSvc = theSearchCacheSvc;
		mySearchResultCacheSvc = theSearchResultCacheSvc;
		myDaoRegistry = theDaoRegistry;
		mySearchBuilderFactory = theSearchBuilderFactory;
		mySynchronousSearchSvc = theSynchronousSearchSvc;
		myPersistedJpaBundleProviderFactory = thePersistedJpaBundleProviderFactory;
		myRequestPartitionHelperService = theRequestPartitionHelperService;
		mySearchParamRegistry = theSearchParamRegistry;
		mySearchStrategyFactory = theSearchStrategyFactory;
		myExceptionSvc = theExceptionSvc;
		myBeanFactory = theBeanFactory;

		myStorageInterceptorHooks = new StorageInterceptorHooksFacade(myInterceptorBroadcaster);
	}

	@VisibleForTesting
	Set<String> getActiveSearchIds() {
		return myIdToSearchTask.keySet();
	}

	@VisibleForTesting
	public void setLoadingThrottleForUnitTests(Integer theLoadingThrottleForUnitTests) {
		myLoadingThrottleForUnitTests = theLoadingThrottleForUnitTests;
	}

	@VisibleForTesting
	public void setNeverUseLocalSearchForUnitTests(boolean theNeverUseLocalSearchForUnitTests) {
		myNeverUseLocalSearchForUnitTests = theNeverUseLocalSearchForUnitTests;
	}

	@VisibleForTesting
	public void setSyncSizeForUnitTests(int theSyncSize) {
		mySyncSize = theSyncSize;
	}

	@Override
	public void cancelAllActiveSearches() {
		for (SearchTask next : myIdToSearchTask.values()) {
			ourLog.info("Requesting immediate abort of search: {}", next.getSearch().getUuid());
			next.requestImmediateAbort();
			AsyncUtil.awaitLatchAndIgnoreInterrupt(next.getCompletionLatch(), 30, TimeUnit.SECONDS);
		}
	}

	@SuppressWarnings("SameParameterValue")
	@VisibleForTesting
	void setMaxMillisToWaitForRemoteResultsForUnitTest(long theMaxMillisToWaitForRemoteResults) {
		myMaxMillisToWaitForRemoteResults = theMaxMillisToWaitForRemoteResults;
	}

	/**
	 * This method is called by the HTTP client processing thread in order to
	 * fetch resources.
	 */
	@Override
	@Transactional(propagation = Propagation.NEVER)
	public List<ResourcePersistentId> getResources(final String theUuid, int theFrom, int theTo, @Nullable RequestDetails theRequestDetails) {
		TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

		// If we're actively searching right now, don't try to do anything until at least one batch has been
		// persisted in the DB
		SearchTask searchTask = myIdToSearchTask.get(theUuid);
		if (searchTask != null) {
			searchTask.awaitInitialSync();
		}

		ourLog.trace("About to start looking for resources {}-{}", theFrom, theTo);

		Search search;
		StopWatch sw = new StopWatch();
		while (true) {

			if (myNeverUseLocalSearchForUnitTests == false) {
				if (searchTask != null) {
					ourLog.trace("Local search found");
					List<ResourcePersistentId> resourcePids = searchTask.getResourcePids(theFrom, theTo);
					ourLog.trace("Local search returned {} pids, wanted {}-{} - Search: {}", resourcePids.size(), theFrom, theTo, searchTask.getSearch());

					/*
					 * Generally, if a search task is open, the fastest possible thing is to just return its results. This
					 * will work most of the time, but can fail if the task hit a search threshold and the client is requesting
					 * results beyond that threashold. In that case, we'll keep going below, since that will trigger another
					 * task.
					 */
					if ((searchTask.getSearch().getNumFound() - searchTask.getSearch().getNumBlocked()) >= theTo || resourcePids.size() == (theTo - theFrom)) {
						return resourcePids;
					}
				}
			}

			search = mySearchCacheSvc
				.fetchByUuid(theUuid)
				.orElseThrow(() -> myExceptionSvc.newUnknownSearchException(theUuid));

			QueryParameterUtils.verifySearchHasntFailedOrThrowInternalErrorException(search);
			if (search.getStatus() == SearchStatusEnum.FINISHED) {
				ourLog.trace("Search entity marked as finished with {} results", search.getNumFound());
				break;
			}
			if ((search.getNumFound() - search.getNumBlocked()) >= theTo) {
				ourLog.trace("Search entity has {} results so far", search.getNumFound());
				break;
			}

			if (sw.getMillis() > myMaxMillisToWaitForRemoteResults) {
				ourLog.error("Search {} of type {} for {}{} timed out after {}ms", search.getId(), search.getSearchType(), search.getResourceType(), search.getSearchQueryString(), sw.getMillis());
				throw new InternalErrorException(Msg.code(1163) + "Request timed out after " + sw.getMillis() + "ms");
			}

			// If the search was saved in "pass complete mode" it's probably time to
			// start a new pass
			if (search.getStatus() == SearchStatusEnum.PASSCMPLET) {
				ourLog.trace("Going to try to start next search");
				Optional<Search> newSearch = mySearchCacheSvc.tryToMarkSearchAsInProgress(search);
				if (newSearch.isPresent()) {
					ourLog.trace("Launching new search");
					search = newSearch.get();
					String resourceType = search.getResourceType();
					SearchParameterMap params = search.getSearchParameterMap().orElseThrow(() -> new IllegalStateException("No map in PASSCOMPLET search"));
					IFhirResourceDao<?> resourceDao = myDaoRegistry.getResourceDao(resourceType);
					RequestPartitionId requestPartitionId = myRequestPartitionHelperService.determineReadPartitionForRequestForSearchType(theRequestDetails, resourceType, params, null);

					SearchTaskParameters parameters = new SearchTaskParameters(
						search,
						resourceDao,
						params,
						resourceType,
						theRequestDetails,
						requestPartitionId,
						myOnRemoveSearchTask,
						mySyncSize
					);
					parameters.setLoadingThrottleForUnitTests(myLoadingThrottleForUnitTests);
					SearchContinuationTask task = (SearchContinuationTask) myBeanFactory.getBean(SearchConfig.CONTINUE_TASK,
						parameters);
					myIdToSearchTask.put(search.getUuid(), task);
					task.call();
				}
			}

			AsyncUtil.sleep(500);
		}

		ourLog.trace("Finished looping");

		List<ResourcePersistentId> pids = mySearchResultCacheSvc.fetchResultPids(search, theFrom, theTo);
		if (pids == null) {
			throw myExceptionSvc.newUnknownSearchException(theUuid);
		}

		ourLog.trace("Fetched {} results", pids.size());

		return pids;
	}

	@Override
	public IBundleProvider registerSearch(final IFhirResourceDao<?> theCallingDao, final SearchParameterMap theParams, String theResourceType, CacheControlDirective theCacheControlDirective, RequestDetails theRequestDetails, RequestPartitionId theRequestPartitionId) {
		final String searchUuid = UUID.randomUUID().toString();

		final String queryString = theParams.toNormalizedQueryString(myContext);
		ourLog.debug("Registering new search {}", searchUuid);

		Search search = new Search();
		QueryParameterUtils.populateSearchEntity(theParams, theResourceType, searchUuid, queryString, search, theRequestPartitionId);

		myStorageInterceptorHooks.callStoragePresearchRegistered(theRequestDetails, theParams, search);

		validateSearch(theParams);

		Class<? extends IBaseResource> resourceTypeClass = myContext.getResourceDefinition(theResourceType).getImplementingClass();
		final ISearchBuilder sb = mySearchBuilderFactory.newSearchBuilder(theCallingDao, theResourceType, resourceTypeClass);
		sb.setFetchSize(mySyncSize);

		final Integer loadSynchronousUpTo = getLoadSynchronousUpToOrNull(theCacheControlDirective);
		boolean isOffsetQuery = theParams.isOffsetQuery();

		// todo someday - not today.
//		SearchStrategyFactory.ISearchStrategy searchStrategy = mySearchStrategyFactory.pickStrategy(theResourceType, theParams, theRequestDetails);
//		return searchStrategy.get();

		if (theParams.isLoadSynchronous() || loadSynchronousUpTo != null || isOffsetQuery) {
			if (mySearchStrategyFactory.isSupportsHSearchDirect(theResourceType, theParams, theRequestDetails)) {
				ourLog.info("Search {} is using direct load strategy", searchUuid);
				SearchStrategyFactory.ISearchStrategy direct =  mySearchStrategyFactory.makeDirectStrategy(searchUuid, theResourceType, theParams, theRequestDetails);

				try {
					return direct.get();

				} catch (ResourceNotFoundInIndexException theE) {
					// some resources were not found in index, so we will inform this and resort to JPA search
					ourLog.warn("Some resources were not found in index. Make sure all resources were indexed. Resorting to database search.");
				}
			}

			ourLog.debug("Search {} is loading in synchronous mode", searchUuid);
			return mySynchronousSearchSvc.executeQuery(theParams, theRequestDetails, searchUuid, sb, loadSynchronousUpTo, theRequestPartitionId);
		}

		/*
		 * See if there are any cached searches whose results we can return
		 * instead
		 */
		SearchCacheStatusEnum cacheStatus = SearchCacheStatusEnum.MISS;
		if (theCacheControlDirective != null && theCacheControlDirective.isNoCache() == true) {
			cacheStatus = SearchCacheStatusEnum.NOT_TRIED;
		}

		if (cacheStatus != SearchCacheStatusEnum.NOT_TRIED) {
			if (theParams.getEverythingMode() == null) {
				if (myDaoConfig.getReuseCachedSearchResultsForMillis() != null) {
					PersistedJpaBundleProvider foundSearchProvider = findCachedQuery(theParams, theResourceType, theRequestDetails, queryString, theRequestPartitionId);
					if (foundSearchProvider != null) {
						foundSearchProvider.setCacheStatus(SearchCacheStatusEnum.HIT);
						return foundSearchProvider;
					}
				}
			}
		}

		PersistedJpaSearchFirstPageBundleProvider retVal = submitSearch(theCallingDao, theParams, theResourceType, theRequestDetails, searchUuid, sb, queryString, theRequestPartitionId, search);
		retVal.setCacheStatus(cacheStatus);
		return retVal;
	}

	private void validateSearch(SearchParameterMap theParams) {
		validateIncludes(theParams.getIncludes(), Constants.PARAM_INCLUDE);
		validateIncludes(theParams.getRevIncludes(), Constants.PARAM_REVINCLUDE);
	}

	private void validateIncludes(Set<Include> includes, String name) {
		for (Include next : includes) {
			String value = next.getValue();
			if (value.equals(Constants.INCLUDE_STAR) || isBlank(value)) {
				continue;
			}

			String paramType = next.getParamType();
			String paramName = next.getParamName();
			String paramTargetType = next.getParamTargetType();

			if (isBlank(paramType) || isBlank(paramName)) {
				String msg = myContext.getLocalizer().getMessageSanitized(SearchCoordinatorSvcImpl.class, "invalidInclude", name, value, "");
				throw new InvalidRequestException(Msg.code(2018) + msg);
			}

			if (!myDaoRegistry.isResourceTypeSupported(paramType)) {
				String resourceTypeMsg = myContext.getLocalizer().getMessageSanitized(SearchCoordinatorSvcImpl.class, "invalidResourceType", paramType);
				String msg = myContext.getLocalizer().getMessage(SearchCoordinatorSvcImpl.class, "invalidInclude", UrlUtil.sanitizeUrlPart(name), UrlUtil.sanitizeUrlPart(value), resourceTypeMsg); // last param is pre-sanitized
				throw new InvalidRequestException(Msg.code(2017) + msg);
			}

			if (isNotBlank(paramTargetType) && !myDaoRegistry.isResourceTypeSupported(paramTargetType)) {
				String resourceTypeMsg = myContext.getLocalizer().getMessageSanitized(SearchCoordinatorSvcImpl.class, "invalidResourceType", paramTargetType);
				String msg = myContext.getLocalizer().getMessage(SearchCoordinatorSvcImpl.class, "invalidInclude", UrlUtil.sanitizeUrlPart(name), UrlUtil.sanitizeUrlPart(value), resourceTypeMsg); // last param is pre-sanitized
				throw new InvalidRequestException(Msg.code(2016) + msg);
			}

			if (!Constants.INCLUDE_STAR.equals(paramName) && mySearchParamRegistry.getActiveSearchParam(paramType, paramName) == null) {
				List<String> validNames = mySearchParamRegistry
					.getActiveSearchParams(paramType)
					.values()
					.stream()
					.filter(t -> t.getParamType() == RestSearchParameterTypeEnum.REFERENCE)
					.map(t -> UrlUtil.sanitizeUrlPart(t.getName()))
					.sorted()
					.collect(Collectors.toList());
				String searchParamMessage = myContext.getLocalizer().getMessage(BaseStorageDao.class, "invalidSearchParameter", UrlUtil.sanitizeUrlPart(paramName), UrlUtil.sanitizeUrlPart(paramType), validNames);
				String msg = myContext.getLocalizer().getMessage(SearchCoordinatorSvcImpl.class, "invalidInclude", UrlUtil.sanitizeUrlPart(name), UrlUtil.sanitizeUrlPart(value), searchParamMessage); // last param is pre-sanitized
				throw new InvalidRequestException(Msg.code(2015) + msg);
			}

		}
	}

	@Override
	public Optional<Integer> getSearchTotal(String theUuid) {
		SearchTask task = myIdToSearchTask.get(theUuid);
		if (task != null) {
			return Optional.ofNullable(task.awaitInitialSync());
		}

		/*
		 * In case there is no running search, if the total is listed as accurate we know one is coming
		 * so let's wait a bit for it to show up
		 */
		TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		Optional<Search> search = mySearchCacheSvc.fetchByUuid(theUuid);
		if (search.isPresent()) {
			Optional<SearchParameterMap> searchParameterMap = search.get().getSearchParameterMap();
			if (searchParameterMap.isPresent() && searchParameterMap.get().getSearchTotalMode() == SearchTotalModeEnum.ACCURATE) {
				for (int i = 0; i < 10; i++) {
					if (search.isPresent()) {
						QueryParameterUtils.verifySearchHasntFailedOrThrowInternalErrorException(search.get());
						if (search.get().getTotalCount() != null) {
							return Optional.of(search.get().getTotalCount());
						}
					}
					search = mySearchCacheSvc.fetchByUuid(theUuid);
				}
			}
		}

		return Optional.empty();
	}

	@Nonnull
	private PersistedJpaSearchFirstPageBundleProvider submitSearch(IDao theCallingDao, SearchParameterMap theParams, String theResourceType, RequestDetails theRequestDetails, String theSearchUuid, ISearchBuilder theSb, String theQueryString, RequestPartitionId theRequestPartitionId, Search theSearch) {
		StopWatch w = new StopWatch();

		SearchTaskParameters stp = new SearchTaskParameters(
			theSearch,
			theCallingDao,
			theParams,
			theResourceType,
			theRequestDetails,
			theRequestPartitionId,
			myOnRemoveSearchTask,
			mySyncSize
		);
		stp.setLoadingThrottleForUnitTests(myLoadingThrottleForUnitTests);
		SearchTask task = (SearchTask) myBeanFactory.getBean(SearchConfig.SEARCH_TASK, stp);
		myIdToSearchTask.put(theSearch.getUuid(), task);
		task.call();

		PersistedJpaSearchFirstPageBundleProvider retVal = myPersistedJpaBundleProviderFactory.newInstanceFirstPage(theRequestDetails, theSearch, task, theSb);

		ourLog.debug("Search initial phase completed in {}ms", w.getMillis());
		return retVal;
	}

	@Nullable
	private PersistedJpaBundleProvider findCachedQuery(SearchParameterMap theParams, String theResourceType, RequestDetails theRequestDetails, String theQueryString, RequestPartitionId theRequestPartitionId) {
		TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);

		// May be null
		return txTemplate.execute(t -> {

			// Interceptor call: STORAGE_PRECHECK_FOR_CACHED_SEARCH
			HookParams params = new HookParams()
				.add(SearchParameterMap.class, theParams)
				.add(RequestDetails.class, theRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
			Object outcome = CompositeInterceptorBroadcaster.doCallHooksAndReturnObject(myInterceptorBroadcaster, theRequestDetails, Pointcut.STORAGE_PRECHECK_FOR_CACHED_SEARCH, params);
			if (Boolean.FALSE.equals(outcome)) {
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
			CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequestDetails, Pointcut.JPA_PERFTRACE_SEARCH_REUSING_CACHED, params);

			return myPersistedJpaBundleProviderFactory.newInstance(theRequestDetails, searchToUse.getUuid());
		});
	}

	@Nullable
	private Search findSearchToUseOrNull(String theQueryString, String theResourceType, RequestPartitionId theRequestPartitionId) {
		// createdCutoff is in recent past
		final Instant createdCutoff = Instant.now().minus(myDaoConfig.getReuseCachedSearchResultsForMillis(), ChronoUnit.MILLIS);

		Optional<Search> candidate = mySearchCacheSvc.findCandidatesForReuse(theResourceType, theQueryString, createdCutoff, theRequestPartitionId);
		return candidate.orElse(null);
	}

	@Nullable
	private Integer getLoadSynchronousUpToOrNull(CacheControlDirective theCacheControlDirective) {
		final Integer loadSynchronousUpTo;
		if (theCacheControlDirective != null && theCacheControlDirective.isNoStore()) {
			if (theCacheControlDirective.getMaxResults() != null) {
				loadSynchronousUpTo = theCacheControlDirective.getMaxResults();
				if (loadSynchronousUpTo > myDaoConfig.getCacheControlNoStoreMaxResultsUpperLimit()) {
					throw new InvalidRequestException(Msg.code(1165) + Constants.HEADER_CACHE_CONTROL + " header " + Constants.CACHE_CONTROL_MAX_RESULTS + " value must not exceed " + myDaoConfig.getCacheControlNoStoreMaxResultsUpperLimit());
				}
			} else {
				loadSynchronousUpTo = 100;
			}
		} else {
			loadSynchronousUpTo = null;
		}
		return loadSynchronousUpTo;
	}


	/**
	 * Creates a {@link Pageable} using a start and end index
	 */
	@SuppressWarnings("WeakerAccess")
	@Nullable
	public static Pageable toPage(final int theFromIndex, int theToIndex) {
		int pageSize = theToIndex - theFromIndex;
		if (pageSize < 1) {
			return null;
		}

		int pageIndex = theFromIndex / pageSize;

		Pageable page = new AbstractPageRequest(pageIndex, pageSize) {
			private static final long serialVersionUID = 1L;

			@Override
			public long getOffset() {
				return theFromIndex;
			}

			@Override
			public Sort getSort() {
				return Sort.unsorted();
			}

			@Override
			public Pageable next() {
				return null;
			}

			@Override
			public Pageable previous() {
				return null;
			}

			@Override
			public Pageable first() {
				return null;
			}

			@Override
			public Pageable withPage(int theI) {
				return null;
			}
		};

		return page;
	}

}
