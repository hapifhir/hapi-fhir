package ca.uhn.fhir.jpa.search;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.*;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.dao.data.ISearchIncludeDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchInclude;
import ca.uhn.fhir.jpa.entity.SearchResult;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.jpa.interceptor.JpaPreResourceAccessDetails;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.method.PageMethodBinding;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.ICachedSearchDetails;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Component("mySearchCoordinatorSvc")
public class SearchCoordinatorSvcImpl implements ISearchCoordinatorSvc {
	public static final int DEFAULT_SYNC_SIZE = 250;

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchCoordinatorSvcImpl.class);
	private final ConcurrentHashMap<String, BaseTask> myIdToSearchTask = new ConcurrentHashMap<>();
	@Autowired
	private FhirContext myContext;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private EntityManager myEntityManager;
	private ExecutorService myExecutor;
	private Integer myLoadingThrottleForUnitTests = null;
	private long myMaxMillisToWaitForRemoteResults = DateUtils.MILLIS_PER_MINUTE;
	private boolean myNeverUseLocalSearchForUnitTests;
	@Autowired
	private ISearchDao mySearchDao;
	@Autowired
	private ISearchIncludeDao mySearchIncludeDao;
	@Autowired
	private ISearchResultDao mySearchResultDao;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private PlatformTransactionManager myManagedTxManager;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private IPagingProvider myPagingProvider;

	private int mySyncSize = DEFAULT_SYNC_SIZE;
	/**
	 * Set in {@link #start()}
	 */
	private boolean myCustomIsolationSupported;

	/**
	 * Constructor
	 */
	public SearchCoordinatorSvcImpl() {
		CustomizableThreadFactory threadFactory = new CustomizableThreadFactory("search_coord_");
		myExecutor = Executors.newCachedThreadPool(threadFactory);
	}

	@PostConstruct
	public void start() {
		if (myManagedTxManager instanceof JpaTransactionManager) {
			JpaDialect jpaDialect = ((JpaTransactionManager) myManagedTxManager).getJpaDialect();
			if (jpaDialect instanceof HibernateJpaDialect) {
				myCustomIsolationSupported = true;
			}
		}
		if (myCustomIsolationSupported == false) {
			ourLog.warn("JPA dialect does not support transaction isolation! This can have an impact on search performance.");
		}
	}

	@Override
	public void cancelAllActiveSearches() {
		for (BaseTask next : myIdToSearchTask.values()) {
			next.requestImmediateAbort();
			try {
				next.getCompletionLatch().await(30, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				ourLog.warn("Failed to wait for completion", e);
			}
		}
	}

	/**
	 * This method is called by the HTTP client processing thread in order to
	 * fetch resources.
	 */
	@Override
	@Transactional(propagation = Propagation.NEVER)
	public List<Long> getResources(final String theUuid, int theFrom, int theTo, @Nullable RequestDetails theRequestDetails) {
		TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

		Search search;
		StopWatch sw = new StopWatch();
		while (true) {

			if (myNeverUseLocalSearchForUnitTests == false) {
				BaseTask task = myIdToSearchTask.get(theUuid);
				if (task != null) {
					ourLog.trace("Local search found");
					List<Long> resourcePids = task.getResourcePids(theFrom, theTo);
					if (resourcePids != null) {
						return resourcePids;
					}
				}
			}

			search = txTemplate.execute(t -> mySearchDao.findByUuid(theUuid));

			if (search == null) {
				ourLog.debug("Client requested unknown paging ID[{}]", theUuid);
				String msg = myContext.getLocalizer().getMessage(PageMethodBinding.class, "unknownSearchId", theUuid);
				throw new ResourceGoneException(msg);
			}

			verifySearchHasntFailedOrThrowInternalErrorException(search);
			if (search.getStatus() == SearchStatusEnum.FINISHED) {
				ourLog.debug("Search entity marked as finished with {} results", search.getNumFound());
				break;
			}
			if (search.getNumFound() >= theTo) {
				ourLog.debug("Search entity has {} results so far", search.getNumFound());
				break;
			}

			if (sw.getMillis() > myMaxMillisToWaitForRemoteResults) {
				ourLog.error("Search {} of type {} for {}{} timed out after {}ms", search.getId(), search.getSearchType(), search.getResourceType(), search.getSearchQueryString(), sw.getMillis());
				throw new InternalErrorException("Request timed out after " + sw.getMillis() + "ms");
			}

			// If the search was saved in "pass complete mode" it's probably time to
			// start a new pass
			if (search.getStatus() == SearchStatusEnum.PASSCMPLET) {
				Optional<Search> newSearch = tryToMarkSearchAsInProgress(search);
				if (newSearch.isPresent()) {
					search = newSearch.get();
					String resourceType = search.getResourceType();
					SearchParameterMap params = search.getSearchParameterMap();
					IFhirResourceDao<?> resourceDao = myDaoRegistry.getResourceDao(resourceType);
					SearchContinuationTask task = new SearchContinuationTask(search, resourceDao, params, resourceType, theRequestDetails);
					myIdToSearchTask.put(search.getUuid(), task);
					myExecutor.submit(task);
				}
			}

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// ignore
			}
		}

		final Pageable page = toPage(theFrom, theTo);
		if (page == null) {
			return Collections.emptyList();
		}

		final Search foundSearch = search;

		ourLog.trace("Loading stored search");
		List<Long> retVal = txTemplate.execute(theStatus -> {
			final List<Long> resultPids = new ArrayList<>();
			Page<Long> searchResultPids = mySearchResultDao.findWithSearchUuid(foundSearch, page);
			for (Long next : searchResultPids) {
				resultPids.add(next);
			}
			return resultPids;
		});
		return retVal;
	}

	private Optional<Search> tryToMarkSearchAsInProgress(Search theSearch) {
		ourLog.trace("Going to try to change search status from {} to {}", theSearch.getStatus(), SearchStatusEnum.LOADING);
		try {
			TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
			txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			txTemplate.afterPropertiesSet();
			return txTemplate.execute(t -> {
				Search search = mySearchDao.findById(theSearch.getId()).orElse(theSearch);

				if (search.getStatus() != SearchStatusEnum.PASSCMPLET) {
					throw new IllegalStateException("Can't change to LOADING because state is " + theSearch.getStatus());
				}
				search.setStatus(SearchStatusEnum.LOADING);
				Search newSearch = mySearchDao.save(search);
				return Optional.of(newSearch);
			});
		} catch (Exception e) {
			ourLog.warn("Failed to activate search: {}", e.toString());
			ourLog.trace("Failed to activate search", e);
			return Optional.empty();
		}
	}

	private void populateBundleProvider(PersistedJpaBundleProvider theRetVal) {
		theRetVal.setContext(myContext);
		theRetVal.setEntityManager(myEntityManager);
		theRetVal.setPlatformTransactionManager(myManagedTxManager);
		theRetVal.setSearchDao(mySearchDao);
		theRetVal.setSearchCoordinatorSvc(this);
		theRetVal.setInterceptorBroadcaster(myInterceptorBroadcaster);
	}

	@Override
	public IBundleProvider registerSearch(final IDao theCallingDao, final SearchParameterMap theParams, String theResourceType, CacheControlDirective theCacheControlDirective, RequestDetails theRequestDetails) {
		StopWatch w = new StopWatch();
		final String searchUuid = UUID.randomUUID().toString();

		ourLog.debug("Registering new search {}", searchUuid);

		Class<? extends IBaseResource> resourceTypeClass = myContext.getResourceDefinition(theResourceType).getImplementingClass();
		final ISearchBuilder sb = theCallingDao.newSearchBuilder();
		sb.setType(resourceTypeClass, theResourceType);
		sb.setFetchSize(mySyncSize);

		final Integer loadSynchronousUpTo;
		if (theCacheControlDirective != null && theCacheControlDirective.isNoStore()) {
			if (theCacheControlDirective.getMaxResults() != null) {
				loadSynchronousUpTo = theCacheControlDirective.getMaxResults();
				if (loadSynchronousUpTo > myDaoConfig.getCacheControlNoStoreMaxResultsUpperLimit()) {
					throw new InvalidRequestException(Constants.HEADER_CACHE_CONTROL + " header " + Constants.CACHE_CONTROL_MAX_RESULTS + " value must not exceed " + myDaoConfig.getCacheControlNoStoreMaxResultsUpperLimit());
				}
			} else {
				loadSynchronousUpTo = 100;
			}
		} else {
			loadSynchronousUpTo = null;
		}

		if (theParams.isLoadSynchronous() || loadSynchronousUpTo != null) {

			ourLog.debug("Search {} is loading in synchronous mode", searchUuid);
			SearchRuntimeDetails searchRuntimeDetails = new SearchRuntimeDetails(theRequestDetails, searchUuid);
			searchRuntimeDetails.setLoadSynchronous(true);

			// Execute the query and make sure we return distinct results
			TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
			txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			return txTemplate.execute(t -> {

				// Load the results synchronously
				final List<Long> pids = new ArrayList<>();

				try (IResultIterator resultIter = sb.createQuery(theParams, searchRuntimeDetails, theRequestDetails)) {
					while (resultIter.hasNext()) {
						pids.add(resultIter.next());
						if (loadSynchronousUpTo != null && pids.size() >= loadSynchronousUpTo) {
							break;
						}
						if (theParams.getLoadSynchronousUpTo() != null && pids.size() >= theParams.getLoadSynchronousUpTo()) {
							break;
						}
					}
				} catch (IOException e) {
					ourLog.error("IO failure during database access", e);
					throw new InternalErrorException(e);
				}

				JpaPreResourceAccessDetails accessDetails = new JpaPreResourceAccessDetails(pids, () -> sb);
				HookParams params = new HookParams()
					.add(IPreResourceAccessDetails.class, accessDetails)
					.add(RequestDetails.class, theRequestDetails)
					.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
				JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequestDetails, Pointcut.STORAGE_PREACCESS_RESOURCES, params);

				for (int i = pids.size() - 1; i >= 0; i--) {
					if (accessDetails.isDontReturnResourceAtIndex(i)) {
						pids.remove(i);
					}
				}

				/*
				 * For synchronous queries, we load all the includes right away
				 * since we're returning a static bundle with all the results
				 * pre-loaded. This is ok because syncronous requests are not
				 * expected to be paged
				 *
				 * On the other hand for async queries we load includes/revincludes
				 * individually for pages as we return them to clients
				 */
				final Set<Long> includedPids = new HashSet<>();
				includedPids.addAll(sb.loadIncludes(myContext, myEntityManager, pids, theParams.getRevIncludes(), true, theParams.getLastUpdated(), "(synchronous)", theRequestDetails));
				includedPids.addAll(sb.loadIncludes(myContext, myEntityManager, pids, theParams.getIncludes(), false, theParams.getLastUpdated(), "(synchronous)", theRequestDetails));
				List<Long> includedPidsList = new ArrayList<>(includedPids);

				List<IBaseResource> resources = new ArrayList<>();
				sb.loadResourcesByPid(pids, includedPidsList, resources, false, theRequestDetails);
				return new SimpleBundleProvider(resources);
			});
		}

		/*
		 * See if there are any cached searches whose results we can return
		 * instead
		 */
		boolean useCache = true;
		if (theCacheControlDirective != null && theCacheControlDirective.isNoCache() == true) {
			useCache = false;
		}
		final String queryString = theParams.toNormalizedQueryString(myContext);
		if (theParams.getEverythingMode() == null) {
			if (myDaoConfig.getReuseCachedSearchResultsForMillis() != null && useCache) {

				final Date createdCutoff = new Date(System.currentTimeMillis() - myDaoConfig.getReuseCachedSearchResultsForMillis());
				final String resourceType = theResourceType;

				TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
				PersistedJpaBundleProvider foundSearchProvider = txTemplate.execute(t -> {
					Search searchToUse = null;

					// Interceptor call: STORAGE_PRECHECK_FOR_CACHED_SEARCH
					HookParams params = new HookParams()
						.add(SearchParameterMap.class, theParams)
						.add(RequestDetails.class, theRequestDetails)
						.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
					Object outcome = JpaInterceptorBroadcaster.doCallHooksAndReturnObject(myInterceptorBroadcaster, theRequestDetails, Pointcut.STORAGE_PRECHECK_FOR_CACHED_SEARCH, params);
					if (Boolean.FALSE.equals(outcome)) {
						return null;
					}

					// Check for a search matching the given hash
					int hashCode = queryString.hashCode();
					Collection<Search> candidates = mySearchDao.find(resourceType, hashCode, createdCutoff);
					for (Search nextCandidateSearch : candidates) {
						if (queryString.equals(nextCandidateSearch.getSearchQueryString())) {
							searchToUse = nextCandidateSearch;
							break;
						}
					}

					PersistedJpaBundleProvider retVal = null;
					if (searchToUse != null) {
						ourLog.debug("Reusing search {} from cache", searchToUse.getUuid());

						// Interceptor call: JPA_PERFTRACE_SEARCH_REUSING_CACHED
						params = new HookParams()
							.add(SearchParameterMap.class, theParams)
							.add(RequestDetails.class, theRequestDetails)
							.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
						JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequestDetails, Pointcut.JPA_PERFTRACE_SEARCH_REUSING_CACHED, params);

						searchToUse.setSearchLastReturned(new Date());
						mySearchDao.updateSearchLastReturned(searchToUse.getId(), new Date());

						retVal = new PersistedJpaBundleProvider(theRequestDetails, searchToUse.getUuid(), theCallingDao);
						retVal.setCacheHit(true);

						populateBundleProvider(retVal);
					}

					return retVal;
				});

				if (foundSearchProvider != null) {
					return foundSearchProvider;
				}

			}
		}

		Search search = new Search();
		populateSearchEntity(theParams, theResourceType, searchUuid, queryString, search);

		// Interceptor call: STORAGE_PRESEARCH_REGISTERED
		HookParams params = new HookParams()
			.add(ICachedSearchDetails.class, search)
			.add(RequestDetails.class, theRequestDetails)
			.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
		JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequestDetails, Pointcut.STORAGE_PRESEARCH_REGISTERED, params);

		SearchTask task = new SearchTask(search, theCallingDao, theParams, theResourceType, theRequestDetails);
		myIdToSearchTask.put(search.getUuid(), task);
		myExecutor.submit(task);

		PersistedJpaSearchFirstPageBundleProvider retVal = new PersistedJpaSearchFirstPageBundleProvider(search, theCallingDao, task, sb, myManagedTxManager, theRequestDetails);
		populateBundleProvider(retVal);

		ourLog.debug("Search initial phase completed in {}ms", w.getMillis());
		return retVal;

	}

	private void callInterceptorStoragePreAccessResources(IInterceptorBroadcaster theInterceptorBroadcaster, RequestDetails theRequestDetails, ISearchBuilder theSb, List<Long> thePids) {
		if (thePids.isEmpty() == false) {
			JpaPreResourceAccessDetails accessDetails = new JpaPreResourceAccessDetails(thePids, () -> theSb);
			HookParams params = new HookParams()
				.add(IPreResourceAccessDetails.class, accessDetails)
				.add(RequestDetails.class, theRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
			JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequestDetails, Pointcut.STORAGE_PREACCESS_RESOURCES, params);
			for (int i = thePids.size() - 1; i >= 0; i--) {
				if (accessDetails.isDontReturnResourceAtIndex(i)) {
					thePids.remove(i);
				}
			}
		}
	}

	@VisibleForTesting
	void setContextForUnitTest(FhirContext theCtx) {
		myContext = theCtx;
	}

	@VisibleForTesting
	void setDaoConfigForUnitTest(DaoConfig theDaoConfig) {
		myDaoConfig = theDaoConfig;
	}

	@VisibleForTesting
	void setEntityManagerForUnitTest(EntityManager theEntityManager) {
		myEntityManager = theEntityManager;
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
	void setSearchDaoForUnitTest(ISearchDao theSearchDao) {
		mySearchDao = theSearchDao;
	}

	@VisibleForTesting
	void setSearchDaoIncludeForUnitTest(ISearchIncludeDao theSearchIncludeDao) {
		mySearchIncludeDao = theSearchIncludeDao;
	}

	@VisibleForTesting
	void setSearchDaoResultForUnitTest(ISearchResultDao theSearchResultDao) {
		mySearchResultDao = theSearchResultDao;
	}

	@VisibleForTesting
	public void setSyncSizeForUnitTests(int theSyncSize) {
		mySyncSize = theSyncSize;
	}

	@VisibleForTesting
	void setTransactionManagerForUnitTest(PlatformTransactionManager theTxManager) {
		myManagedTxManager = theTxManager;
	}

	@VisibleForTesting
	void setDaoRegistryForUnitTest(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
	}

	@VisibleForTesting
	void setInterceptorBroadcasterForUnitTest(IInterceptorBroadcaster theInterceptorBroadcaster) {
		myInterceptorBroadcaster = theInterceptorBroadcaster;
	}

	public abstract class BaseTask implements Callable<Void> {
		private final SearchParameterMap myParams;
		private final IDao myCallingDao;
		private final String myResourceType;
		private final ArrayList<Long> mySyncedPids = new ArrayList<>();
		private final CountDownLatch myInitialCollectionLatch = new CountDownLatch(1);
		private final CountDownLatch myCompletionLatch;
		private final ArrayList<Long> myUnsyncedPids = new ArrayList<>();
		private final RequestDetails myRequest;
		private Search mySearch;
		private boolean myAbortRequested;
		private int myCountSavedTotal = 0;
		private int myCountSavedThisPass = 0;
		private int myCountBlockedThisPass = 0;
		private boolean myAdditionalPrefetchThresholdsRemaining;
		private List<Long> myPreviouslyAddedResourcePids;
		private Integer myMaxResultsToFetch;
		private SearchRuntimeDetails mySearchRuntimeDetails;

		/**
		 * Constructor
		 */
		protected BaseTask(Search theSearch, IDao theCallingDao, SearchParameterMap theParams, String theResourceType, RequestDetails theRequest) {
			mySearch = theSearch;
			myCallingDao = theCallingDao;
			myParams = theParams;
			myResourceType = theResourceType;
			myCompletionLatch = new CountDownLatch(1);
			mySearchRuntimeDetails = new SearchRuntimeDetails(theRequest, mySearch.getUuid());
			mySearchRuntimeDetails.setQueryString(theParams.toNormalizedQueryString(theCallingDao.getContext()));
			myRequest = theRequest;
		}

		protected Search getSearch() {
			return mySearch;
		}

		CountDownLatch getInitialCollectionLatch() {
			return myInitialCollectionLatch;
		}

		void setPreviouslyAddedResourcePids(List<Long> thePreviouslyAddedResourcePids) {
			myPreviouslyAddedResourcePids = thePreviouslyAddedResourcePids;
			myCountSavedTotal = myPreviouslyAddedResourcePids.size();
		}

		private ISearchBuilder newSearchBuilder() {
			Class<? extends IBaseResource> resourceTypeClass = myContext.getResourceDefinition(myResourceType).getImplementingClass();
			ISearchBuilder sb = myCallingDao.newSearchBuilder();
			sb.setType(resourceTypeClass, myResourceType);

			return sb;
		}

		public List<Long> getResourcePids(int theFromIndex, int theToIndex) {
			ourLog.debug("Requesting search PIDs from {}-{}", theFromIndex, theToIndex);

			boolean keepWaiting;
			do {
				synchronized (mySyncedPids) {
					ourLog.trace("Search status is {}", mySearch.getStatus());
					boolean haveEnoughResults = mySyncedPids.size() >= theToIndex;
					if (!haveEnoughResults) {
						switch (mySearch.getStatus()) {
							case LOADING:
								keepWaiting = true;
								break;
							case PASSCMPLET:
								/*
								 * If we get here, it means that the user requested resources that crossed the
								 * current pre-fetch boundary. For example, if the prefetch threshold is 50 and the
								 * user has requested resources 0-60, then they would get 0-50 back but the search
								 * coordinator would then stop searching.SearchCoordinatorSvcImplTest
								 */
								keepWaiting = false;
								break;
							case FAILED:
							case FINISHED:
							default:
								keepWaiting = false;
								break;
						}
					} else {
						keepWaiting = false;
					}
				}

				if (keepWaiting) {
					ourLog.info("Waiting as we only have {} results - Search status: {}", mySyncedPids.size(), mySearch.getStatus());
					try {
						Thread.sleep(500);
					} catch (InterruptedException theE) {
						// ignore
					}
				}
			} while (keepWaiting);

			ourLog.debug("Proceeding, as we have {} results", mySyncedPids.size());

			ArrayList<Long> retVal = new ArrayList<>();
			synchronized (mySyncedPids) {
				verifySearchHasntFailedOrThrowInternalErrorException(mySearch);

				int toIndex = theToIndex;
				if (mySyncedPids.size() < toIndex) {
					toIndex = mySyncedPids.size();
				}
				for (int i = theFromIndex; i < toIndex; i++) {
					retVal.add(mySyncedPids.get(i));
				}
			}

			ourLog.trace("Done syncing results - Wanted {}-{} and returning {} of {}", theFromIndex, theToIndex, retVal.size(), mySyncedPids.size());

			return retVal;
		}

		void saveSearch() {
			TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
			txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			txTemplate.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(@NotNull TransactionStatus theArg0) {
					doSaveSearch();
				}

			});
		}

		private void saveUnsynced(final IResultIterator theResultIter) {
			TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
			txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			txTemplate.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(@NotNull TransactionStatus theArg0) {
					if (mySearch.getId() == null) {
						doSaveSearch();
					}

					ArrayList<Long> unsyncedPids = myUnsyncedPids;

					// Interceptor call: STORAGE_PREACCESS_RESOURCES
					// This can be used to remove results from the search result details before
					// the user has a chance to know that they were in the results
					if (mySearchRuntimeDetails.getRequestDetails() != null && unsyncedPids.isEmpty() == false) {
						JpaPreResourceAccessDetails accessDetails = new JpaPreResourceAccessDetails(unsyncedPids, () -> newSearchBuilder());
						HookParams params = new HookParams()
							.add(IPreResourceAccessDetails.class, accessDetails)
							.add(RequestDetails.class, mySearchRuntimeDetails.getRequestDetails())
							.addIfMatchesType(ServletRequestDetails.class, mySearchRuntimeDetails.getRequestDetails());
						JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.STORAGE_PREACCESS_RESOURCES, params);

						for (int i = unsyncedPids.size() - 1; i >= 0; i--) {
							if (accessDetails.isDontReturnResourceAtIndex(i)) {
								unsyncedPids.remove(i);
								myCountBlockedThisPass++;
								myCountSavedTotal++;
							}
						}
					}

					List<SearchResult> resultsToSave = Lists.newArrayList();
					for (Long nextPid : unsyncedPids) {
						SearchResult nextResult = new SearchResult(mySearch);
						nextResult.setResourcePid(nextPid);
						nextResult.setOrder(myCountSavedTotal);
						resultsToSave.add(nextResult);
						int order = nextResult.getOrder();
						ourLog.trace("Saving ORDER[{}] Resource {}", order, nextResult.getResourcePid());

						myCountSavedTotal++;
						myCountSavedThisPass++;
					}

					mySearchResultDao.saveAll(resultsToSave);

					synchronized (mySyncedPids) {
						int numSyncedThisPass = unsyncedPids.size();
						ourLog.trace("Syncing {} search results - Have more: {}", numSyncedThisPass, theResultIter.hasNext());
						mySyncedPids.addAll(unsyncedPids);
						unsyncedPids.clear();

						if (theResultIter.hasNext() == false) {
							mySearch.setNumFound(myCountSavedTotal);
							int skippedCount = theResultIter.getSkippedCount();
							int totalFetched = skippedCount + myCountSavedThisPass + myCountBlockedThisPass;
							ourLog.trace("MaxToFetch[{}] SkippedCount[{}] CountSavedThisPass[{}] CountSavedThisTotal[{}] AdditionalPrefetchRemaining[{}]", myMaxResultsToFetch, skippedCount, myCountSavedThisPass, myCountSavedTotal, myAdditionalPrefetchThresholdsRemaining);

							if (myMaxResultsToFetch != null && totalFetched < myMaxResultsToFetch) {
								ourLog.trace("Setting search status to FINISHED");
								mySearch.setStatus(SearchStatusEnum.FINISHED);
								mySearch.setTotalCount(myCountSavedTotal);
							} else if (myAdditionalPrefetchThresholdsRemaining) {
								ourLog.trace("Setting search status to PASSCMPLET");
								mySearch.setStatus(SearchStatusEnum.PASSCMPLET);
								mySearch.setSearchParameterMap(myParams);
							} else {
								ourLog.trace("Setting search status to FINISHED");
								mySearch.setStatus(SearchStatusEnum.FINISHED);
								mySearch.setTotalCount(myCountSavedTotal);
							}
						}
					}

					mySearch.setNumFound(myCountSavedTotal);

					int numSynced;
					synchronized (mySyncedPids) {
						numSynced = mySyncedPids.size();
					}

					if (myDaoConfig.getCountSearchResultsUpTo() == null ||
						myDaoConfig.getCountSearchResultsUpTo() <= 0 ||
						myDaoConfig.getCountSearchResultsUpTo() <= numSynced) {
						myInitialCollectionLatch.countDown();
					}

					doSaveSearch();

				}
			});

		}

		boolean isNotAborted() {
			return myAbortRequested == false;
		}

		void markComplete() {
			myCompletionLatch.countDown();
		}

		CountDownLatch getCompletionLatch() {
			return myCompletionLatch;
		}

		/**
		 * Request that the task abort as soon as possible
		 */
		void requestImmediateAbort() {
			myAbortRequested = true;
		}

		/**
		 * This is the method which actually performs the search.
		 * It is called automatically by the thread pool.
		 */
		@Override
		public Void call() {
			StopWatch sw = new StopWatch();

			try {
				// Create an initial search in the DB and give it an ID
				saveSearch();

				TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
				txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

				if (myCustomIsolationSupported) {
					txTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
				}

				txTemplate.execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
						doSearch();
					}
				});

				mySearchRuntimeDetails.setSearchStatus(mySearch.getStatus());
				if (mySearch.getStatus() == SearchStatusEnum.FINISHED) {
					HookParams params = new HookParams()
						.add(RequestDetails.class, myRequest)
						.addIfMatchesType(ServletRequestDetails.class, myRequest)
						.add(SearchRuntimeDetails.class, mySearchRuntimeDetails);
					JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_SEARCH_COMPLETE, params);
				} else {
					HookParams params = new HookParams()
						.add(RequestDetails.class, myRequest)
						.addIfMatchesType(ServletRequestDetails.class, myRequest)
						.add(SearchRuntimeDetails.class, mySearchRuntimeDetails);
					JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_SEARCH_PASS_COMPLETE, params);
				}

				ourLog.trace("Have completed search for [{}{}] and found {} resources in {}ms - Status is {}", mySearch.getResourceType(), mySearch.getSearchQueryString(), mySyncedPids.size(), sw.getMillis(), mySearch.getStatus());

			} catch (Throwable t) {

				/*
				 * Don't print a stack trace for client errors (i.e. requests that
				 * aren't valid because the client screwed up).. that's just noise
				 * in the logs and who needs that.
				 */
				boolean logged = false;
				if (t instanceof BaseServerResponseException) {
					BaseServerResponseException exception = (BaseServerResponseException) t;
					if (exception.getStatusCode() >= 400 && exception.getStatusCode() < 500) {
						logged = true;
						ourLog.warn("Failed during search due to invalid request: {}", t.toString());
					}
				}

				if (!logged) {
					ourLog.error("Failed during search loading after {}ms", sw.getMillis(), t);
				}
				myUnsyncedPids.clear();

				Throwable rootCause = ExceptionUtils.getRootCause(t);
				rootCause = defaultIfNull(rootCause, t);

				String failureMessage = rootCause.getMessage();

				int failureCode = InternalErrorException.STATUS_CODE;
				if (t instanceof BaseServerResponseException) {
					failureCode = ((BaseServerResponseException) t).getStatusCode();
				}

				mySearch.setFailureMessage(failureMessage);
				mySearch.setFailureCode(failureCode);
				mySearch.setStatus(SearchStatusEnum.FAILED);

				mySearchRuntimeDetails.setSearchStatus(mySearch.getStatus());
				HookParams params = new HookParams()
					.add(RequestDetails.class, myRequest)
					.addIfMatchesType(ServletRequestDetails.class, myRequest)
					.add(SearchRuntimeDetails.class, mySearchRuntimeDetails);
				JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_SEARCH_FAILED, params);

				saveSearch();

			} finally {

				myIdToSearchTask.remove(mySearch.getUuid());
				myInitialCollectionLatch.countDown();
				markComplete();

			}
			return null;
		}

		private void doSaveSearch() {

			Search newSearch;
			if (mySearch.getId() == null) {
				newSearch = mySearchDao.save(mySearch);
				for (SearchInclude next : mySearch.getIncludes()) {
					mySearchIncludeDao.save(next);
				}
			} else {
				newSearch = mySearchDao.save(mySearch);
			}

			// mySearchDao.save is not supposed to return null, but in unit tests
			// it can if the mock search dao isn't set up to handle that
			if (newSearch != null) {
				mySearch = newSearch;
			}
		}

		/**
		 * This method actually creates the database query to perform the
		 * search, and starts it.
		 */
		private void doSearch() {

			/*
			 * If the user has explicitly requested a _count, perform a
			 *
			 * SELECT COUNT(*) ....
			 *
			 * before doing anything else.
			 */
			boolean wantOnlyCount = SummaryEnum.COUNT.equals(myParams.getSummaryMode());
			boolean wantCount =
				wantOnlyCount ||
					SearchTotalModeEnum.ACCURATE.equals(myParams.getSearchTotalMode()) ||
					(myParams.getSearchTotalMode() == null && SearchTotalModeEnum.ACCURATE.equals(myDaoConfig.getDefaultTotalMode()));
			if (wantCount) {
				ourLog.trace("Performing count");
				ISearchBuilder sb = newSearchBuilder();
				Iterator<Long> countIterator = sb.createCountQuery(myParams, mySearch.getUuid(), myRequest);
				Long count = countIterator.next();
				ourLog.trace("Got count {}", count);

				TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
				txTemplate.execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
						mySearch.setTotalCount(count.intValue());
						if (wantOnlyCount) {
							mySearch.setStatus(SearchStatusEnum.FINISHED);
						}
						doSaveSearch();
						mySearchDao.flush();
					}
				});
				if (wantOnlyCount) {
					return;
				}
			}

			ourLog.trace("Done count");
			ISearchBuilder sb = newSearchBuilder();

			/*
			 * Figure out how many results we're actually going to fetch from the
			 * database in this pass. This calculation takes into consideration the
			 * "pre-fetch thresholds" specified in DaoConfig#getSearchPreFetchThresholds()
			 * as well as the value of the _count parameter.
			 */
			int currentlyLoaded = defaultIfNull(mySearch.getNumFound(), 0);
			int minWanted = 0;
			if (myParams.getCount() != null) {
				minWanted = myParams.getCount();
				minWanted = Math.max(minWanted, myPagingProvider.getMaximumPageSize());
				minWanted += currentlyLoaded;
			}

			for (Iterator<Integer> iter = myDaoConfig.getSearchPreFetchThresholds().iterator(); iter.hasNext(); ) {
				int next = iter.next();
				if (next != -1 && next <= currentlyLoaded) {
					continue;
				}

				if (next == -1) {
					sb.setMaxResultsToFetch(null);
				} else {
					myMaxResultsToFetch = Math.max(next, minWanted);
					sb.setMaxResultsToFetch(myMaxResultsToFetch);
				}

				if (iter.hasNext()) {
					myAdditionalPrefetchThresholdsRemaining = true;
				}

				// If we get here's we've found an appropriate threshold
				break;
			}

			/*
			 * Provide any PID we loaded in previous search passes to the
			 * SearchBuilder so that we don't get duplicates coming from running
			 * the same query again.
			 *
			 * We could possibly accomplish this in a different way by using sorted
			 * results in our SQL query and specifying an offset. I don't actually
			 * know if that would be faster or not. At some point should test this
			 * idea.
			 */
			if (myPreviouslyAddedResourcePids != null) {
				sb.setPreviouslyAddedResourcePids(myPreviouslyAddedResourcePids);
				mySyncedPids.addAll(myPreviouslyAddedResourcePids);
			}

			/*
			 * Construct the SQL query we'll be sending to the database
			 */
			try (IResultIterator resultIterator = sb.createQuery(myParams, mySearchRuntimeDetails, myRequest)) {
				assert (resultIterator != null);

				/*
				 * The following loop actually loads the PIDs of the resources
				 * matching the search off of the disk and into memory. After
				 * every X results, we commit to the HFJ_SEARCH table.
				 */
				int syncSize = mySyncSize;
				while (resultIterator.hasNext()) {
					myUnsyncedPids.add(resultIterator.next());

					boolean shouldSync = myUnsyncedPids.size() >= syncSize;

					if (myDaoConfig.getCountSearchResultsUpTo() != null &&
						myDaoConfig.getCountSearchResultsUpTo() > 0 &&
						myDaoConfig.getCountSearchResultsUpTo() < myUnsyncedPids.size()) {
						shouldSync = false;
					}

					if (myUnsyncedPids.size() > 50000) {
						shouldSync = true;
					}

					// If no abort was requested, bail out
					Validate.isTrue(isNotAborted(), "Abort has been requested");

					if (shouldSync) {
						saveUnsynced(resultIterator);
					}

					if (myLoadingThrottleForUnitTests != null) {
						try {
							Thread.sleep(myLoadingThrottleForUnitTests);
						} catch (InterruptedException e) {
							// ignore
						}
					}

				}

				// If no abort was requested, bail out
				Validate.isTrue(isNotAborted(), "Abort has been requested");

				saveUnsynced(resultIterator);

			} catch (IOException e) {
				ourLog.error("IO failure during database access", e);
				throw new InternalErrorException(e);
			}
		}
	}


	public class SearchContinuationTask extends BaseTask {

		public SearchContinuationTask(Search theSearch, IDao theCallingDao, SearchParameterMap theParams, String theResourceType, RequestDetails theRequest) {
			super(theSearch, theCallingDao, theParams, theResourceType, theRequest);
		}

		@Override
		public Void call() {
			try {
				TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
				txTemplate.afterPropertiesSet();
				txTemplate.execute(t -> {
					List<Long> previouslyAddedResourcePids = mySearchResultDao.findWithSearchUuidOrderIndependent(getSearch());
					ourLog.debug("Have {} previously added IDs in search: {}", previouslyAddedResourcePids.size(), getSearch().getUuid());
					setPreviouslyAddedResourcePids(previouslyAddedResourcePids);
					return null;
				});
			} catch (Throwable e) {
				ourLog.error("Failure processing search", e);
				getSearch().setFailureMessage(e.toString());
				getSearch().setStatus(SearchStatusEnum.FAILED);

				saveSearch();
				return null;
			}

			return super.call();
		}

		@Override
		public List<Long> getResourcePids(int theFromIndex, int theToIndex) {
			return super.getResourcePids(theFromIndex, theToIndex);
		}
	}

	/**
	 * A search task is a Callable task that runs in
	 * a thread pool to handle an individual search. One instance
	 * is created for any requested search and runs from the
	 * beginning to the end of the search.
	 * <p>
	 * Understand:
	 * This class executes in its own thread separate from the
	 * web server client thread that made the request. We do that
	 * so that we can return to the client as soon as possible,
	 * but keep the search going in the background (and have
	 * the next page of results ready to go when the client asks).
	 */
	class SearchTask extends BaseTask {

		/**
		 * Constructor
		 */
		SearchTask(Search theSearch, IDao theCallingDao, SearchParameterMap theParams, String theResourceType, RequestDetails theRequestDetails) {
			super(theSearch, theCallingDao, theParams, theResourceType, theRequestDetails);
		}

		/**
		 * This method is called by the server HTTP thread, and
		 * will block until at least one page of results have been
		 * fetched from the DB, and will never block after that.
		 */
		Integer awaitInitialSync() {
			ourLog.trace("Awaiting initial sync");
			do {
				try {
					if (getInitialCollectionLatch().await(250, TimeUnit.MILLISECONDS)) {
						break;
					}
				} catch (InterruptedException e) {
					// Shouldn't happen
					throw new InternalErrorException(e);
				}
			} while (getSearch().getStatus() == SearchStatusEnum.LOADING);
			ourLog.trace("Initial sync completed");

			return getSearch().getTotalCount();
		}

	}

	public static void populateSearchEntity(SearchParameterMap theParams, String theResourceType, String theSearchUuid, String theQueryString, Search theSearch) {
		theSearch.setDeleted(false);
		theSearch.setUuid(theSearchUuid);
		theSearch.setCreated(new Date());
		theSearch.setSearchLastReturned(new Date());
		theSearch.setTotalCount(null);
		theSearch.setNumFound(0);
		theSearch.setPreferredPageSize(theParams.getCount());
		theSearch.setSearchType(theParams.getEverythingMode() != null ? SearchTypeEnum.EVERYTHING : SearchTypeEnum.SEARCH);
		theSearch.setLastUpdated(theParams.getLastUpdated());
		theSearch.setResourceType(theResourceType);
		theSearch.setStatus(SearchStatusEnum.LOADING);

		theSearch.setSearchQueryString(theQueryString);
		theSearch.setSearchQueryStringHash(theQueryString.hashCode());

		for (Include next : theParams.getIncludes()) {
			theSearch.addInclude(new SearchInclude(theSearch, next.getValue(), false, next.isRecurse()));
		}
		for (Include next : theParams.getRevIncludes()) {
			theSearch.addInclude(new SearchInclude(theSearch, next.getValue(), true, next.isRecurse()));
		}
	}

	/**
	 * Creates a {@link Pageable} using a start and end index
	 */
	@SuppressWarnings("WeakerAccess")
	public static @Nullable
	Pageable toPage(final int theFromIndex, int theToIndex) {
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
		};

		return page;
	}

	static void verifySearchHasntFailedOrThrowInternalErrorException(Search theSearch) {
		if (theSearch.getStatus() == SearchStatusEnum.FAILED) {
			Integer status = theSearch.getFailureCode();
			status = defaultIfNull(status, 500);

			String message = theSearch.getFailureMessage();
			throw BaseServerResponseException.newInstance(status, message);
		}
	}

}
