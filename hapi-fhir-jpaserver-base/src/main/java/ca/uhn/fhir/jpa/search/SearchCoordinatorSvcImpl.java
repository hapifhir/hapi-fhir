package ca.uhn.fhir.jpa.search;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import ca.uhn.fhir.jpa.dao.*;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.dao.data.ISearchIncludeDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.method.PageMethodBinding;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import java.util.*;
import java.util.concurrent.*;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

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
	private PlatformTransactionManager myManagedTxManager;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private IPagingProvider myPagingProvider;

	private int mySyncSize = DEFAULT_SYNC_SIZE;

	/**
	 * Constructor
	 */
	public SearchCoordinatorSvcImpl() {
		CustomizableThreadFactory threadFactory = new CustomizableThreadFactory("search_coord_");
		myExecutor = Executors.newCachedThreadPool(threadFactory);
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
	public List<Long> getResources(final String theUuid, int theFrom, int theTo) {
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
				ourLog.info("Client requested unknown paging ID[{}]", theUuid);
				String msg = myContext.getLocalizer().getMessage(PageMethodBinding.class, "unknownSearchId", theUuid);
				throw new ResourceGoneException(msg);
			}

			verifySearchHasntFailedOrThrowInternalErrorException(search);
			if (search.getStatus() == SearchStatusEnum.FINISHED) {
				ourLog.info("Search entity marked as finished");
				break;
			}
			if (search.getNumFound() >= theTo) {
				ourLog.info("Search entity has {} results so far", search.getNumFound());
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
					SearchContinuationTask task = new SearchContinuationTask(search, myDaoRegistry.getResourceDao(resourceType), params, resourceType);
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
				Optional<Search> searchOpt = mySearchDao.findById(theSearch.getId());
				Search search = searchOpt.orElseThrow(IllegalStateException::new);
				if (search.getStatus() != SearchStatusEnum.PASSCMPLET) {
					throw new IllegalStateException("Can't change to LOADING because state is " + search.getStatus());
				}
				theSearch.setStatus(SearchStatusEnum.LOADING);
				Search newSearch = mySearchDao.save(theSearch);
				return Optional.of(newSearch);
			});
		} catch (Exception e) {
			ourLog.warn("Failed to activate search: {}", e.toString());
			return Optional.empty();
		}
	}

	private void populateBundleProvider(PersistedJpaBundleProvider theRetVal) {
		theRetVal.setContext(myContext);
		theRetVal.setEntityManager(myEntityManager);
		theRetVal.setPlatformTransactionManager(myManagedTxManager);
		theRetVal.setSearchDao(mySearchDao);
		theRetVal.setSearchCoordinatorSvc(this);
	}

	@Override
	public IBundleProvider registerSearch(final IDao theCallingDao, final SearchParameterMap theParams, String theResourceType, CacheControlDirective theCacheControlDirective) {
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

			// Execute the query and make sure we return distinct results
			TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
			txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			return txTemplate.execute(t -> {

				// Load the results synchronously
				final List<Long> pids = new ArrayList<>();

				Iterator<Long> resultIter = sb.createQuery(theParams, searchUuid);
				while (resultIter.hasNext()) {
					pids.add(resultIter.next());
					if (loadSynchronousUpTo != null && pids.size() >= loadSynchronousUpTo) {
						break;
					}
					if (theParams.getLoadSynchronousUpTo() != null && pids.size() >= theParams.getLoadSynchronousUpTo()) {
						break;
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
				includedPids.addAll(sb.loadIncludes(theCallingDao, myContext, myEntityManager, pids, theParams.getRevIncludes(), true, theParams.getLastUpdated()));
				includedPids.addAll(sb.loadIncludes(theCallingDao, myContext, myEntityManager, pids, theParams.getIncludes(), false, theParams.getLastUpdated()));

				List<IBaseResource> resources = new ArrayList<>();
				sb.loadResourcesByPid(pids, resources, includedPids, false, myEntityManager, myContext, theCallingDao);
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

					int hashCode = queryString.hashCode();
					Collection<Search> candidates = mySearchDao.find(resourceType, hashCode, createdCutoff);
					for (Search nextCandidateSearch : candidates) {
						if (queryString.equals(nextCandidateSearch.getSearchQueryString())) {
							searchToUse = nextCandidateSearch;
						}
					}

					PersistedJpaBundleProvider retVal = null;
					if (searchToUse != null) {
						ourLog.info("Reusing search {} from cache", searchToUse.getUuid());
						searchToUse.setSearchLastReturned(new Date());
						mySearchDao.updateSearchLastReturned(searchToUse.getId(), new Date());

						retVal = new PersistedJpaBundleProvider(searchToUse.getUuid(), theCallingDao);
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

		SearchTask task = new SearchTask(search, theCallingDao, theParams, theResourceType);
		myIdToSearchTask.put(search.getUuid(), task);
		myExecutor.submit(task);

		PersistedJpaSearchFirstPageBundleProvider retVal = new PersistedJpaSearchFirstPageBundleProvider(search, theCallingDao, task, sb, myManagedTxManager);
		populateBundleProvider(retVal);

		ourLog.info("Search initial phase completed in {}ms", w.getMillis());
		return retVal;

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
	void setMaxMillisToWaitForRemoteResultsForUnitTest(long theMaxMillisToWaitForRemoteResults) {
		myMaxMillisToWaitForRemoteResults = theMaxMillisToWaitForRemoteResults;
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

	public abstract class BaseTask implements Callable<Void> {
		protected Search getSearch() {
			return getSearch;
		}

		private final Search getSearch;
		private  final SearchParameterMap myParams;
		private  final IDao myCallingDao;
		private  final String myResourceType;
		private  final ArrayList<Long> mySyncedPids = new ArrayList<>();

		protected CountDownLatch getInitialCollectionLatch() {
			return myInitialCollectionLatch;
		}

		private final CountDownLatch myInitialCollectionLatch = new CountDownLatch(1);
		private final CountDownLatch myCompletionLatch;
		private final ArrayList<Long> myUnsyncedPids = new ArrayList<>();
		private boolean myAbortRequested;
		private int myCountSaved = 0;
		private boolean myAdditionalPrefetchThresholdsRemaining;
		private List<Long> myPreviouslyAddedResourcePids;
		private Integer myMaxResultsToFetch;
		private int myCountFetchedDuringThisPass;

		/**
		 * Constructor
		 */
		protected BaseTask(Search theSearch, IDao theCallingDao, SearchParameterMap theParams, String theResourceType) {
			getSearch = theSearch;
			myCallingDao = theCallingDao;
			myParams = theParams;
			myResourceType = theResourceType;
			myCompletionLatch = new CountDownLatch(1);
		}

		protected void setPreviouslyAddedResourcePids(List<Long> thePreviouslyAddedResourcePids) {
			myPreviouslyAddedResourcePids = thePreviouslyAddedResourcePids;
			myCountSaved = myPreviouslyAddedResourcePids.size();
		}

		private ISearchBuilder newSearchBuilder() {
			Class<? extends IBaseResource> resourceTypeClass = myContext.getResourceDefinition(myResourceType).getImplementingClass();
			ISearchBuilder sb = myCallingDao.newSearchBuilder();
			sb.setType(resourceTypeClass, myResourceType);

			return sb;
		}

		public List<Long> getResourcePids(int theFromIndex, int theToIndex) {
			ourLog.info("Requesting search PIDs from {}-{}", theFromIndex, theToIndex);

			boolean keepWaiting;
			do {
				synchronized (mySyncedPids) {
					ourLog.trace("Search status is {}", getSearch.getStatus());
					keepWaiting = mySyncedPids.size() < theToIndex && getSearch.getStatus() == SearchStatusEnum.LOADING;
				}
				if (keepWaiting) {
					ourLog.info("Waiting, as we only have {} results", mySyncedPids.size());
					try {
						Thread.sleep(500);
					} catch (InterruptedException theE) {
						// ignore
					}
				}
			} while (keepWaiting);

			ourLog.info("Proceeding, as we have {} results", mySyncedPids.size());

			ArrayList<Long> retVal = new ArrayList<>();
			synchronized (mySyncedPids) {
				verifySearchHasntFailedOrThrowInternalErrorException(getSearch);

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

		protected void saveSearch() {
			TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
			txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			txTemplate.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
					doSaveSearch();
				}

			});
		}

		private void saveUnsynced(final Iterator<Long> theResultIter) {
			TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
			txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			txTemplate.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
					if (getSearch.getId() == null) {
						doSaveSearch();
					}

					List<SearchResult> resultsToSave = Lists.newArrayList();
					for (Long nextPid : myUnsyncedPids) {
						SearchResult nextResult = new SearchResult(getSearch);
						nextResult.setResourcePid(nextPid);
						nextResult.setOrder(myCountSaved++);
						resultsToSave.add(nextResult);
						ourLog.trace("Saving ORDER[{}] Resource {}", nextResult.getOrder(), nextResult.getResourcePid());
					}
					mySearchResultDao.saveAll(resultsToSave);

					synchronized (mySyncedPids) {
						int numSyncedThisPass = myUnsyncedPids.size();
						ourLog.trace("Syncing {} search results", numSyncedThisPass);
						mySyncedPids.addAll(myUnsyncedPids);
						myUnsyncedPids.clear();

						if (theResultIter.hasNext() == false) {
							getSearch.setNumFound(myCountSaved);
							if (myMaxResultsToFetch != null && myCountSaved < myMaxResultsToFetch) {
								getSearch.setStatus(SearchStatusEnum.FINISHED);
								getSearch.setTotalCount(myCountSaved);
							} else if (myAdditionalPrefetchThresholdsRemaining) {
								ourLog.trace("Setting search status to PASSCMPLET");
								getSearch.setStatus(SearchStatusEnum.PASSCMPLET);
								getSearch.setSearchParameterMap(myParams);
							} else {
								getSearch.setStatus(SearchStatusEnum.FINISHED);
								getSearch.setTotalCount(myCountSaved);
							}
						}
					}

					getSearch.setNumFound(myCountSaved);

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

		public boolean isNotAborted() {
			return myAbortRequested == false;
		}

		protected void markComplete() {
			myCompletionLatch.countDown();
		}

		public CountDownLatch getCompletionLatch() {
			return myCompletionLatch;
		}

		/**
		 * Request that the task abort as soon as possible
		 */
		public void requestImmediateAbort() {
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
				txTemplate.execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
						doSearch();
					}
				});

				ourLog.info("Completed search for [{}] and found {} resources in {}ms", getSearch.getSearchQueryString(), mySyncedPids.size(), sw.getMillis());

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

				getSearch.setFailureMessage(failureMessage);
				getSearch.setFailureCode(failureCode);
				getSearch.setStatus(SearchStatusEnum.FAILED);

				saveSearch();

			} finally {

				myIdToSearchTask.remove(getSearch.getUuid());
				myInitialCollectionLatch.countDown();
				markComplete();

			}
			return null;
		}

		private void doSaveSearch() {
			if (getSearch.getId() == null) {
				mySearchDao.save(getSearch);
				for (SearchInclude next : getSearch.getIncludes()) {
					mySearchIncludeDao.save(next);
				}
			} else {
				mySearchDao.save(getSearch);
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
			boolean wantCount = myParams.getSummaryMode().contains(SummaryEnum.COUNT);
			boolean wantOnlyCount = wantCount && myParams.getSummaryMode().size() == 1;
			if (wantCount) {
				ISearchBuilder sb = newSearchBuilder();
				Iterator<Long> countIterator = sb.createCountQuery(myParams, getSearch.getUuid());
				Long count = countIterator.next();

				TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
				txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
				txTemplate.execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
						getSearch.setTotalCount(count.intValue());
						if (wantOnlyCount) {
							getSearch.setStatus(SearchStatusEnum.FINISHED);
						}
						doSaveSearch();
					}
				});
				if (wantOnlyCount) {
					return;
				}
			}

			ISearchBuilder sb = newSearchBuilder();

			/*
			 * Figure out how many results we're actually going to fetch from the
			 * database in this pass. This calculation takes into consideration the
			 * "pre-fetch thresholds" specified in DaoConfig#getPreFetchThresholds()
			 * as well as the value of the _count parameter.
			 */
			int currentlyLoaded = defaultIfNull(getSearch.getNumFound(), 0);
			int minWanted = 0;
			if (myParams.getCount() != null) {
				minWanted = myParams.getCount();
				minWanted = Math.max(minWanted, myPagingProvider.getMaximumPageSize());
				minWanted += currentlyLoaded;
			}

			for (Iterator<Integer> iter = myDaoConfig.getPreFetchThresholds().iterator(); iter.hasNext(); ) {
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
			 * Provide any PID we loaded in previous seasrch passes to the
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
			Iterator<Long> theResultIterator = sb.createQuery(myParams, getSearch.getUuid());

			/*
			 * The following loop actually loads the PIDs of the resources
			 * matching the search off of the disk and into memory. After
			 * every X results, we commit to the HFJ_SEARCH table.
			 */
			int syncSize = mySyncSize;
			while (theResultIterator.hasNext()) {
				myUnsyncedPids.add(theResultIterator.next());
				myCountFetchedDuringThisPass++;

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
					saveUnsynced(theResultIterator);
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

			saveUnsynced(theResultIterator);
		}
	}


	public class SearchContinuationTask extends BaseTask {

		public SearchContinuationTask(Search theSearch, IDao theCallingDao, SearchParameterMap theParams, String theResourceType) {
			super(theSearch, theCallingDao, theParams, theResourceType);
		}

		@Override
		public Void call() {
			try {
				TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
				txTemplate.afterPropertiesSet();
				txTemplate.execute(t -> {
					List<Long> previouslyAddedResourcePids = mySearchResultDao.findWithSearchUuid(getSearch());
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
	public class SearchTask extends BaseTask {

		/**
		 * Constructor
		 */
		public SearchTask(Search theSearch, IDao theCallingDao, SearchParameterMap theParams, String theResourceType) {
			super(theSearch, theCallingDao, theParams, theResourceType);
		}

		/**
		 * This method is called by the server HTTP thread, and
		 * will block until at least one page of results have been
		 * fetched from the DB, and will never block after that.
		 */
		public Integer awaitInitialSync() {
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

		Pageable page = new PageRequest(pageIndex, pageSize) {
			private static final long serialVersionUID = 1L;

			@Override
			public long getOffset() {
				return theFromIndex;
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
