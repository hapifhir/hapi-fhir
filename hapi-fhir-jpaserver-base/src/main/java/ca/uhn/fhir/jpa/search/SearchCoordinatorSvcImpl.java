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
 * #L%family
 */
package ca.uhn.fhir.jpa.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.config.SearchConfig;
import ca.uhn.fhir.jpa.dao.BaseStorageDao;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.NonPersistedSearch;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.search.ResourceNotFoundInIndexException;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
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
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.util.AsyncUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.Serial;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.util.QueryParameterUtils.DEFAULT_SYNC_SIZE;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component("mySearchCoordinatorSvc")
public class SearchCoordinatorSvcImpl implements ISearchCoordinatorSvc<JpaPid> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchCoordinatorSvcImpl.class);
	public static final int SEARCH_EXPIRY_OFFSET_MINUTES = 10;

	private final FhirContext myContext;
	private final JpaStorageSettings myStorageSettings;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;
	private final HapiTransactionService myTxService;
	private final ISearchCacheSvc mySearchCacheSvc;
	private final ISearchResultCacheSvc mySearchResultCacheSvc;
	private final DaoRegistry myDaoRegistry;
	private final SearchBuilderFactory<JpaPid> mySearchBuilderFactory;
	private final ISynchronousSearchSvc mySynchronousSearchSvc;
	private final PersistedJpaBundleProviderFactory myPersistedJpaBundleProviderFactory;
	private final ISearchParamRegistry mySearchParamRegistry;
	private final SearchStrategyFactory mySearchStrategyFactory;
	private final ExceptionService myExceptionSvc;
	private final BeanFactory myBeanFactory;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	private ConcurrentHashMap<String, SearchTask> myIdToSearchTask = new ConcurrentHashMap<>();

	private final Consumer<String> myOnRemoveSearchTask = myIdToSearchTask::remove;

	private final StorageInterceptorHooksFacade myStorageInterceptorHooks;
	private Integer myLoadingThrottleForUnitTests = null;
	private long myMaxMillisToWaitForRemoteResults = DateUtils.MILLIS_PER_MINUTE;
	private boolean myNeverUseLocalSearchForUnitTests;
	private int mySyncSize = DEFAULT_SYNC_SIZE;

	/**
	 * Constructor
	 */
	public SearchCoordinatorSvcImpl(
			FhirContext theContext,
			JpaStorageSettings theStorageSettings,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			HapiTransactionService theTxService,
			ISearchCacheSvc theSearchCacheSvc,
			ISearchResultCacheSvc theSearchResultCacheSvc,
			DaoRegistry theDaoRegistry,
			SearchBuilderFactory<JpaPid> theSearchBuilderFactory,
			ISynchronousSearchSvc theSynchronousSearchSvc,
			PersistedJpaBundleProviderFactory thePersistedJpaBundleProviderFactory,
			ISearchParamRegistry theSearchParamRegistry,
			SearchStrategyFactory theSearchStrategyFactory,
			ExceptionService theExceptionSvc,
			BeanFactory theBeanFactory,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		super();
		myContext = theContext;
		myStorageSettings = theStorageSettings;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
		myTxService = theTxService;
		mySearchCacheSvc = theSearchCacheSvc;
		mySearchResultCacheSvc = theSearchResultCacheSvc;
		myDaoRegistry = theDaoRegistry;
		mySearchBuilderFactory = theSearchBuilderFactory;
		mySynchronousSearchSvc = theSynchronousSearchSvc;
		myPersistedJpaBundleProviderFactory = thePersistedJpaBundleProviderFactory;
		mySearchParamRegistry = theSearchParamRegistry;
		mySearchStrategyFactory = theSearchStrategyFactory;
		myExceptionSvc = theExceptionSvc;
		myBeanFactory = theBeanFactory;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;

		myStorageInterceptorHooks = new StorageInterceptorHooksFacade(myInterceptorBroadcaster);
	}

	@VisibleForTesting
	Set<String> getActiveSearchIds() {
		return myIdToSearchTask.keySet();
	}

	@VisibleForTesting
	public void setIdToSearchTaskMapForUnitTests(ConcurrentHashMap<String, SearchTask> theIdToSearchTaskMap) {
		myIdToSearchTask = theIdToSearchTaskMap;
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
			ourLog.info(
					"Requesting immediate abort of search: {}", next.getSearch().getUuid());
			next.requestImmediateAbort();
			AsyncUtil.awaitLatchAndIgnoreInterrupt(next.getCompletionLatch(), 30, TimeUnit.SECONDS);
		}
	}

	@SuppressWarnings("SameParameterValue")
	@VisibleForTesting
	public void setMaxMillisToWaitForRemoteResultsForUnitTest(long theMaxMillisToWaitForRemoteResults) {
		myMaxMillisToWaitForRemoteResults = theMaxMillisToWaitForRemoteResults;
	}

	/**
	 * This method is called by the HTTP client processing thread in order to
	 * fetch resources.
	 * <p>
	 * This method must not be called from inside a transaction. The rationale is that
	 * the {@link Search} entity is treated as a piece of shared state across client threads
	 * accessing the same thread, so we need to be able to update that table in a transaction
	 * and commit it right away in order for that to work. Examples of needing to do this
	 * include if two different clients request the same search and are both paging at the
	 * same time, but also includes clients that are hacking the paging links to
	 * fetch multiple pages of a search result in parallel. In both cases we need to only
	 * let one of them actually activate the search, or we will have conflicts. The other thread
	 * just needs to wait until the first one actually fetches more results.
	 */
	@Override
	public List<JpaPid> getResources(
			final String theUuid,
			int theFrom,
			int theTo,
			@Nullable RequestDetails theRequestDetails,
			RequestPartitionId theRequestPartitionId) {
		assert !TransactionSynchronizationManager.isActualTransactionActive();

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

			if (!myNeverUseLocalSearchForUnitTests) {
				if (searchTask != null) {
					ourLog.trace("Local search found");
					List<JpaPid> resourcePids = searchTask.getResourcePids(theFrom, theTo);
					ourLog.trace(
							"Local search returned {} pids, wanted {}-{} - Search: {}",
							resourcePids.size(),
							theFrom,
							theTo,
							searchTask.getSearch());

					/*
					 * Generally, if a search task is open, the fastest possible thing is to just return its results. This
					 * will work most of the time, but can fail if the task hit a search threshold and the client is requesting
					 * results beyond that threashold. In that case, we'll keep going below, since that will trigger another
					 * task.
					 */
					if ((searchTask.getSearch().getNumFound()
											- searchTask.getSearch().getNumBlocked())
									>= theTo
							|| resourcePids.size() == (theTo - theFrom)) {
						return resourcePids;
					}
				}
			}

			Callable<Search> searchCallback = () -> mySearchCacheSvc
					.fetchByUuid(theUuid, theRequestPartitionId)
					.orElseThrow(() -> myExceptionSvc.newUnknownSearchException(theUuid));
			search = myTxService
					.withRequest(theRequestDetails)
					.withRequestPartitionId(theRequestPartitionId)
					.execute(searchCallback);
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
				ourLog.error(
						"Search {} of type {} for {}{} timed out after {}ms with status {}.",
						search.getId(),
						search.getSearchType(),
						search.getResourceType(),
						search.getSearchQueryString(),
						sw.getMillis(),
						search.getStatus());
				throw new InternalErrorException(Msg.code(1163) + "Request timed out after " + sw.getMillis() + "ms");
			}

			// If the search was saved in "pass complete mode" it's probably time to
			// start a new pass
			if (search.getStatus() == SearchStatusEnum.PASSCMPLET) {
				ourLog.trace("Going to try to start next search");
				Optional<Search> newSearch =
						mySearchCacheSvc.tryToMarkSearchAsInProgress(search, theRequestPartitionId);
				if (newSearch.isPresent()) {
					ourLog.trace("Launching new search");
					search = newSearch.get();
					String resourceType = search.getResourceType();
					SearchParameterMap params = search.getSearchParameterMap()
							.orElseThrow(() -> new IllegalStateException("No map in PASSCOMPLET search"));
					IFhirResourceDao<?> resourceDao = myDaoRegistry.getResourceDao(resourceType);

					SearchTaskParameters parameters = new SearchTaskParameters(
							search,
							resourceDao,
							params,
							resourceType,
							theRequestDetails,
							theRequestPartitionId,
							myOnRemoveSearchTask,
							mySyncSize);
					parameters.setLoadingThrottleForUnitTests(myLoadingThrottleForUnitTests);
					SearchContinuationTask task =
							(SearchContinuationTask) myBeanFactory.getBean(SearchConfig.CONTINUE_TASK, parameters);
					myIdToSearchTask.put(search.getUuid(), task);
					task.call();
				}
			}

			if (!search.getStatus().isDone()) {
				AsyncUtil.sleep(500);
			}
		}

		ourLog.trace("Finished looping");

		List<JpaPid> pids = fetchResultPids(theUuid, theFrom, theTo, theRequestDetails, search, theRequestPartitionId);

		updateSearchExpiryOrNull(search, theRequestPartitionId);

		ourLog.trace("Fetched {} results", pids.size());

		return pids;
	}

	@Nonnull
	private List<JpaPid> fetchResultPids(
			String theUuid,
			int theFrom,
			int theTo,
			@Nullable RequestDetails theRequestDetails,
			Search theSearch,
			RequestPartitionId theRequestPartitionId) {
		List<JpaPid> pids = mySearchResultCacheSvc.fetchResultPids(
				theSearch, theFrom, theTo, theRequestDetails, theRequestPartitionId);
		if (pids == null) {
			throw myExceptionSvc.newUnknownSearchException(theUuid);
		}
		return pids;
	}

	private void updateSearchExpiryOrNull(Search theSearch, RequestPartitionId theRequestPartitionId) {
		// The created time may be null in some unit tests
		if (theSearch.getCreated() != null) {
			// start tracking last-access-time for this search when it is more than halfway to expire by created time
			// we do this to avoid generating excessive write traffic on busy cached searches.
			long expireAfterMillis = myStorageSettings.getExpireSearchResultsAfterMillis();
			long createdCutoff = theSearch.getCreated().getTime() + expireAfterMillis;
			if (createdCutoff - System.currentTimeMillis() < expireAfterMillis / 2) {
				theSearch.setExpiryOrNull(DateUtils.addMinutes(new Date(), SEARCH_EXPIRY_OFFSET_MINUTES));
				// TODO: A nice future enhancement might be to make this flush be asynchronous
				mySearchCacheSvc.save(theSearch, theRequestPartitionId);
			}
		}
	}

	@Override
	public IBundleProvider registerSearch(
			final IFhirResourceDao<?> theCallingDao,
			final SearchParameterMap theParams,
			String theResourceType,
			CacheControlDirective theCacheControlDirective,
			@Nullable RequestDetails theRequestDetails) {
		final String searchUuid = UUID.randomUUID().toString();

		final String queryString = theParams.toNormalizedQueryString();
		ourLog.debug("Registering new search {}", searchUuid);

		// Invoke any STORAGE_PRESEARCH_PARTITION_SELECTED interceptor hooks
		NonPersistedSearch nonPersistedSearch = new NonPersistedSearch(theResourceType);
		nonPersistedSearch.setUuid(searchUuid);
		myStorageInterceptorHooks.callStoragePresearchPartitionSelected(
				theRequestDetails, theParams, nonPersistedSearch);

		RequestPartitionId requestPartitionId = null;
		if (theRequestDetails instanceof SystemRequestDetails srd) {
			requestPartitionId = srd.getRequestPartitionId();
		}

		// If an explicit request partition wasn't provided, calculate the request
		// partition after invoking STORAGE_PRESEARCH_REGISTERED just in case any interceptors
		// made changes which could affect the calculated partition
		if (requestPartitionId == null) {
			requestPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(
					theRequestDetails, theResourceType, theParams);
		}

		Search search = new Search();
		QueryParameterUtils.populateSearchEntity(
				theParams, theResourceType, searchUuid, queryString, search, requestPartitionId);

		// Invoke any STORAGE_PRESEARCH_REGISTERED interceptor hooks
		myStorageInterceptorHooks.callStoragePresearchRegistered(
				theRequestDetails, theParams, search, requestPartitionId);

		validateSearch(theParams);

		Class<? extends IBaseResource> resourceTypeClass =
				myContext.getResourceDefinition(theResourceType).getImplementingClass();
		final ISearchBuilder<JpaPid> sb = mySearchBuilderFactory.newSearchBuilder(theResourceType, resourceTypeClass);
		sb.setFetchSize(mySyncSize);
		sb.setRequireTotal(theParams.getCount() != null);

		final Integer loadSynchronousUpTo = getLoadSynchronousUpToOrNull(theCacheControlDirective);
		boolean isOffsetQuery = theParams.isOffsetQuery();

		// todo someday - not today.
		//		SearchStrategyFactory.ISearchStrategy searchStrategy = mySearchStrategyFactory.pickStrategy(theResourceType,
		// theParams, theRequestDetails);
		//		return searchStrategy.get();

		if (theParams.isLoadSynchronous() || loadSynchronousUpTo != null || isOffsetQuery) {
			if (mySearchStrategyFactory.isSupportsHSearchDirect(theResourceType, theParams, theRequestDetails)) {
				ourLog.info("Search {} is using direct load strategy", searchUuid);
				SearchStrategyFactory.ISearchStrategy direct = mySearchStrategyFactory.makeDirectStrategy(
						searchUuid, theResourceType, theParams, theRequestDetails);

				try {
					return direct.get();
				} catch (ResourceNotFoundInIndexException theE) {
					// some resources were not found in index, so we will inform this and resort to JPA search
					ourLog.warn(
							"Some resources were not found in index. Make sure all resources were indexed. Resorting to database search.");
				}
			}

			// we need a max to fetch for synchronous searches;
			// otherwise we'll explode memory.
			Integer maxToLoad = getSynchronousMaxResultsToFetch(theParams, loadSynchronousUpTo);
			ourLog.debug("Setting a max fetch value of {} for synchronous search", maxToLoad);
			sb.setMaxResultsToFetch(maxToLoad);

			ourLog.debug("Search {} is loading in synchronous mode", searchUuid);
			return mySynchronousSearchSvc.executeQuery(
					theParams, theRequestDetails, searchUuid, sb, loadSynchronousUpTo, requestPartitionId);
		}

		/*
		 * See if there are any cached searches whose results we can return
		 * instead
		 */
		SearchCacheStatusEnum cacheStatus = SearchCacheStatusEnum.MISS;
		if (theCacheControlDirective != null && theCacheControlDirective.isNoCache()) {
			cacheStatus = SearchCacheStatusEnum.NOT_TRIED;
		}

		if (cacheStatus != SearchCacheStatusEnum.NOT_TRIED) {
			if (theParams.getEverythingMode() == null) {
				if (myStorageSettings.getReuseCachedSearchResultsForMillis() != null) {
					PersistedJpaBundleProvider foundSearchProvider = findCachedQuery(
							theParams, theResourceType, theRequestDetails, queryString, requestPartitionId);
					if (foundSearchProvider != null) {
						foundSearchProvider.setCacheStatus(SearchCacheStatusEnum.HIT);
						return foundSearchProvider;
					}
				}
			}
		}

		PersistedJpaSearchFirstPageBundleProvider retVal = submitSearch(
				theCallingDao, theParams, theResourceType, theRequestDetails, sb, requestPartitionId, search);
		retVal.setCacheStatus(cacheStatus);
		return retVal;
	}

	/**
	 * The max results to return if this is a synchronous search.
	 * <p>
	 * We'll look in this order:
	 * * load synchronous up to (on params)
	 * * param count (+ offset)
	 * * StorageSettings fetch size default max
	 * *
	 */
	private Integer getSynchronousMaxResultsToFetch(SearchParameterMap theParams, Integer theLoadSynchronousUpTo) {
		if (theLoadSynchronousUpTo != null) {
			return theLoadSynchronousUpTo;
		}

		if (theParams.getCount() != null) {
			int valToReturn = theParams.getCount() + 1;
			if (theParams.getOffset() != null) {
				valToReturn += theParams.getOffset();
			}
			return valToReturn;
		}

		if (myStorageSettings.getFetchSizeDefaultMaximum() != null) {
			return myStorageSettings.getFetchSizeDefaultMaximum();
		}

		return myStorageSettings.getInternalSynchronousSearchSize();
	}

	private void validateSearch(SearchParameterMap theParams) {
		/*
		 * Having duplicate identical params in the search (e.g. Patient?gender=male&gender=male) is not
		 * technically wrong, but it's inefficient and can slow query execution down. Checking for it also
		 * adds CPU load itself though, so we only check this in an assert to hopefully catch errors in tests.
		 */
		assert checkNoDuplicateParameters(theParams)
				: "Duplicate parameters found in query: " + theParams.toNormalizedQueryString();

		validateIncludes(theParams.getIncludes(), Constants.PARAM_INCLUDE);
		validateIncludes(theParams.getRevIncludes(), Constants.PARAM_REVINCLUDE);
	}

	/**
	 * This method detects whether we have any duplicate lists of parameters and returns
	 * {@literal true} if none are found. For example, the following query would result
	 * in this method returning {@literal false}:
	 * <code>Patient?name=bart,homer&name=bart,homer</code>
	 * <p>
	 * This is not an optimized test, and it's not technically even prohibited to have
	 * duplicates like these in queries so this method should only be called as a
	 * part of an {@literal assert} statement to catch errors in tests.
	 */
	private boolean checkNoDuplicateParameters(SearchParameterMap theParams) {
		HashSet<List<IQueryParameterType>> lists = new HashSet<>();
		for (List<List<IQueryParameterType>> andList : theParams.values()) {

			lists.clear();
			for (int i = 0; i < andList.size(); i++) {
				List<IQueryParameterType> orListI = andList.get(i);
				if (!orListI.isEmpty() && !lists.add(orListI)) {
					return false;
				}
			}
		}
		return true;
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
				String msg = myContext
						.getLocalizer()
						.getMessageSanitized(SearchCoordinatorSvcImpl.class, "invalidInclude", name, value, "");
				throw new InvalidRequestException(Msg.code(2018) + msg);
			}

			if (!myDaoRegistry.isResourceTypeSupported(paramType)) {
				String resourceTypeMsg = myContext
						.getLocalizer()
						.getMessageSanitized(SearchCoordinatorSvcImpl.class, "invalidResourceType", paramType);
				String msg = myContext
						.getLocalizer()
						.getMessage(
								SearchCoordinatorSvcImpl.class,
								"invalidInclude",
								UrlUtil.sanitizeUrlPart(name),
								UrlUtil.sanitizeUrlPart(value),
								resourceTypeMsg); // last param is pre-sanitized
				throw new InvalidRequestException(Msg.code(2017) + msg);
			}

			if (isNotBlank(paramTargetType) && !myDaoRegistry.isResourceTypeSupported(paramTargetType)) {
				String resourceTypeMsg = myContext
						.getLocalizer()
						.getMessageSanitized(SearchCoordinatorSvcImpl.class, "invalidResourceType", paramTargetType);
				String msg = myContext
						.getLocalizer()
						.getMessage(
								SearchCoordinatorSvcImpl.class,
								"invalidInclude",
								UrlUtil.sanitizeUrlPart(name),
								UrlUtil.sanitizeUrlPart(value),
								resourceTypeMsg); // last param is pre-sanitized
				throw new InvalidRequestException(Msg.code(2016) + msg);
			}

			if (!Constants.INCLUDE_STAR.equals(paramName)
					&& mySearchParamRegistry.getActiveSearchParam(
									paramType, paramName, ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH)
							== null) {
				List<String> validNames = mySearchParamRegistry
						.getActiveSearchParams(paramType, ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH)
						.values()
						.stream()
						.filter(t -> t.getParamType() == RestSearchParameterTypeEnum.REFERENCE)
						.map(t -> UrlUtil.sanitizeUrlPart(t.getName()))
						.sorted()
						.collect(Collectors.toList());
				String searchParamMessage = myContext
						.getLocalizer()
						.getMessage(
								BaseStorageDao.class,
								"invalidSearchParameter",
								UrlUtil.sanitizeUrlPart(paramName),
								UrlUtil.sanitizeUrlPart(paramType),
								validNames);
				String msg = myContext
						.getLocalizer()
						.getMessage(
								SearchCoordinatorSvcImpl.class,
								"invalidInclude",
								UrlUtil.sanitizeUrlPart(name),
								UrlUtil.sanitizeUrlPart(value),
								searchParamMessage); // last param is pre-sanitized
				throw new InvalidRequestException(Msg.code(2015) + msg);
			}
		}
	}

	@Override
	public Optional<Integer> getSearchTotal(
			String theUuid, @Nullable RequestDetails theRequestDetails, RequestPartitionId theRequestPartitionId) {
		SearchTask task = myIdToSearchTask.get(theUuid);
		if (task != null) {
			return Optional.ofNullable(task.awaitInitialSync());
		}

		/*
		 * In case there is no running search, if the total is listed as accurate we know one is coming
		 * so let's wait a bit for it to show up
		 */
		Optional<Search> search = myTxService
				.withRequest(theRequestDetails)
				.execute(() -> mySearchCacheSvc.fetchByUuid(theUuid, theRequestPartitionId));
		if (search.isPresent()) {
			Optional<SearchParameterMap> searchParameterMap = search.get().getSearchParameterMap();
			if (searchParameterMap.isPresent()
					&& searchParameterMap.get().getSearchTotalMode() == SearchTotalModeEnum.ACCURATE) {
				for (int i = 0; i < 10; i++) {
					if (search.isPresent()) {
						QueryParameterUtils.verifySearchHasntFailedOrThrowInternalErrorException(search.get());
						if (search.get().getTotalCount() != null) {
							return Optional.of(search.get().getTotalCount());
						}
					}
					search = mySearchCacheSvc.fetchByUuid(theUuid, theRequestPartitionId);
				}
			}
		}

		return Optional.empty();
	}

	@Nonnull
	private PersistedJpaSearchFirstPageBundleProvider submitSearch(
			IDao theCallingDao,
			SearchParameterMap theParams,
			String theResourceType,
			RequestDetails theRequestDetails,
			ISearchBuilder<JpaPid> theSb,
			RequestPartitionId theRequestPartitionId,
			Search theSearch) {
		StopWatch w = new StopWatch();

		SearchTaskParameters stp = new SearchTaskParameters(
				theSearch,
				theCallingDao,
				theParams,
				theResourceType,
				theRequestDetails,
				theRequestPartitionId,
				myOnRemoveSearchTask,
				mySyncSize);
		stp.setLoadingThrottleForUnitTests(myLoadingThrottleForUnitTests);
		SearchTask task = (SearchTask) myBeanFactory.getBean(SearchConfig.SEARCH_TASK, stp);
		myIdToSearchTask.put(theSearch.getUuid(), task);
		task.call();

		PersistedJpaSearchFirstPageBundleProvider retVal = myPersistedJpaBundleProviderFactory.newInstanceFirstPage(
				theRequestDetails, task, theSb, theRequestPartitionId);

		ourLog.debug("Search initial phase completed in {}ms", w.getMillis());
		return retVal;
	}

	@Nullable
	private PersistedJpaBundleProvider findCachedQuery(
			SearchParameterMap theParams,
			String theResourceType,
			RequestDetails theRequestDetails,
			String theQueryString,
			RequestPartitionId theRequestPartitionId) {
		// May be null
		return myTxService
				.withRequest(theRequestDetails)
				.withRequestPartitionId(theRequestPartitionId)
				.execute(() -> {
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
				});
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

	@Nullable
	private Integer getLoadSynchronousUpToOrNull(CacheControlDirective theCacheControlDirective) {
		final Integer loadSynchronousUpTo;
		if (theCacheControlDirective != null && theCacheControlDirective.isNoStore()) {
			if (theCacheControlDirective.getMaxResults() != null) {
				loadSynchronousUpTo = theCacheControlDirective.getMaxResults();
				if (loadSynchronousUpTo > myStorageSettings.getCacheControlNoStoreMaxResultsUpperLimit()) {
					throw new InvalidRequestException(Msg.code(1165) + Constants.HEADER_CACHE_CONTROL + " header "
							+ Constants.CACHE_CONTROL_MAX_RESULTS + " value must not exceed "
							+ myStorageSettings.getCacheControlNoStoreMaxResultsUpperLimit());
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

		return new PageRequest(pageIndex, pageSize, Sort.unsorted()) {
			@Serial
			private static final long serialVersionUID = 1L;

			@Override
			public long getOffset() {
				return theFromIndex;
			}
		};
	}
}
