package ca.uhn.fhir.jpa.search.builder.tasks;

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
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.interceptor.JpaPreResourceAccessDetails;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.search.ExceptionService;
import ca.uhn.fhir.jpa.search.SearchStrategyFactory;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.jpa.util.SearchParameterMapCalculator;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.util.AsyncUtil;
import ca.uhn.fhir.util.StopWatch;
import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Span;
import co.elastic.apm.api.Transaction;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static ca.uhn.fhir.jpa.util.QueryParameterUtils.UNIT_TEST_CAPTURE_STACK;
import static ca.uhn.fhir.jpa.util.SearchParameterMapCalculator.isWantCount;
import static ca.uhn.fhir.jpa.util.SearchParameterMapCalculator.isWantOnlyCount;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

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
public class SearchTask implements Callable<Void> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchTask.class);

	private final SearchParameterMap myParams;
	private final IDao myCallingDao;
	private final String myResourceType;
	private final ArrayList<ResourcePersistentId> mySyncedPids = new ArrayList<>();
	private final CountDownLatch myInitialCollectionLatch = new CountDownLatch(1);
	private final CountDownLatch myCompletionLatch;
	private final ArrayList<ResourcePersistentId> myUnsyncedPids = new ArrayList<>();
	private final RequestDetails myRequest;
	private final RequestPartitionId myRequestPartitionId;
	private final SearchRuntimeDetails mySearchRuntimeDetails;
	private final Transaction myParentTransaction;
	private Search mySearch;
	private boolean myAbortRequested;
	private int myCountSavedTotal = 0;
	private int myCountSavedThisPass = 0;
	private int myCountBlockedThisPass = 0;
	private boolean myAdditionalPrefetchThresholdsRemaining;
	private List<ResourcePersistentId> myPreviouslyAddedResourcePids;
	private Integer myMaxResultsToFetch;

	private final Consumer<String> myOnRemove;

	private final int mySyncSize;
	private final Integer myLoadingThrottleForUnitTests;

	private boolean myCustomIsolationSupported;

	// injected beans
	protected final PlatformTransactionManager myManagedTxManager;
	protected final FhirContext myContext;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;
	private final SearchBuilderFactory mySearchBuilderFactory;
	protected final ISearchResultCacheSvc mySearchResultCacheSvc;
	private final DaoConfig myDaoConfig;
	private final ISearchCacheSvc mySearchCacheSvc;
	private final IPagingProvider myPagingProvider;

	/**
	 * Constructor
	 */
	public SearchTask(
		SearchTaskParameters theCreationParams,
		PlatformTransactionManager theManagedTxManager,
		FhirContext theContext,
		SearchStrategyFactory theSearchStrategyFactory,
		IInterceptorBroadcaster theInterceptorBroadcaster,
		SearchBuilderFactory theSearchBuilderFactory,
		ISearchResultCacheSvc theSearchResultCacheSvc,
		DaoConfig theDaoConfig,
		ISearchCacheSvc theSearchCacheSvc,
		IPagingProvider thePagingProvider
	) {
		// beans
		myManagedTxManager = theManagedTxManager;
		myContext = theContext;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
		mySearchBuilderFactory = theSearchBuilderFactory;
		mySearchResultCacheSvc = theSearchResultCacheSvc;
		myDaoConfig = theDaoConfig;
		mySearchCacheSvc = theSearchCacheSvc;
		myPagingProvider = thePagingProvider;

		// values
		myOnRemove = theCreationParams.OnRemove;
		mySearch = theCreationParams.Search;
		myCallingDao = theCreationParams.CallingDao;
		myParams = theCreationParams.Params;
		myResourceType = theCreationParams.ResourceType;
		myRequest = theCreationParams.Request;
		myCompletionLatch = new CountDownLatch(1);
		mySyncSize = theCreationParams.SyncSize;
		myLoadingThrottleForUnitTests = theCreationParams.getLoadingThrottleForUnitTests();

		mySearchRuntimeDetails = new SearchRuntimeDetails(myRequest, mySearch.getUuid());
		mySearchRuntimeDetails.setQueryString(myParams.toNormalizedQueryString(myCallingDao.getContext()));
		myRequestPartitionId = theCreationParams.RequestPartitionId;
		myParentTransaction = ElasticApm.currentTransaction();

		if (myManagedTxManager instanceof JpaTransactionManager) {
			JpaDialect jpaDialect = ((JpaTransactionManager) myManagedTxManager).getJpaDialect();
			if (jpaDialect instanceof HibernateJpaDialect) {
				myCustomIsolationSupported = true;
			}
		}

		if (!myCustomIsolationSupported) {
			ourLog.warn("JPA dialect does not support transaction isolation! This can have an impact on search performance.");
		}
	}

	/**
	 * This method is called by the server HTTP thread, and
	 * will block until at least one page of results have been
	 * fetched from the DB, and will never block after that.
	 */
	public Integer awaitInitialSync() {
		ourLog.trace("Awaiting initial sync");
		do {
			ourLog.trace("Search {} aborted: {}", getSearch().getUuid(), !isNotAborted());
			if (AsyncUtil.awaitLatchAndThrowInternalErrorExceptionOnInterrupt(getInitialCollectionLatch(), 250L, TimeUnit.MILLISECONDS)) {
				break;
			}
		} while (getSearch().getStatus() == SearchStatusEnum.LOADING);
		ourLog.trace("Initial sync completed");

		return getSearch().getTotalCount();
	}

	public Search getSearch() {
		return mySearch;
	}

	public CountDownLatch getInitialCollectionLatch() {
		return myInitialCollectionLatch;
	}

	public void setPreviouslyAddedResourcePids(List<ResourcePersistentId> thePreviouslyAddedResourcePids) {
		myPreviouslyAddedResourcePids = thePreviouslyAddedResourcePids;
		myCountSavedTotal = myPreviouslyAddedResourcePids.size();
	}

	private ISearchBuilder newSearchBuilder() {
		Class<? extends IBaseResource> resourceTypeClass = myContext.getResourceDefinition(myResourceType).getImplementingClass();
		return mySearchBuilderFactory.newSearchBuilder(myCallingDao, myResourceType, resourceTypeClass);
	}

	@Nonnull
	public List<ResourcePersistentId> getResourcePids(int theFromIndex, int theToIndex) {
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
						case GONE:
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
				AsyncUtil.sleep(500L);
			}
		} while (keepWaiting);

		ourLog.debug("Proceeding, as we have {} results", mySyncedPids.size());

		ArrayList<ResourcePersistentId> retVal = new ArrayList<>();
		synchronized (mySyncedPids) {
			QueryParameterUtils.verifySearchHasntFailedOrThrowInternalErrorException(mySearch);

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

	public void saveSearch() {
		TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theArg0) {
				doSaveSearch();
			}

		});
	}

	private void saveUnsynced(final IResultIterator theResultIter) {
		TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theArg0) {
				if (mySearch.getId() == null) {
					doSaveSearch();
				}

				ArrayList<ResourcePersistentId> unsyncedPids = myUnsyncedPids;
				int countBlocked = 0;

				// Interceptor call: STORAGE_PREACCESS_RESOURCES
				// This can be used to remove results from the search result details before
				// the user has a chance to know that they were in the results
				if (mySearchRuntimeDetails.getRequestDetails() != null && unsyncedPids.isEmpty() == false) {
					JpaPreResourceAccessDetails accessDetails = new JpaPreResourceAccessDetails(unsyncedPids, () -> newSearchBuilder());
					HookParams params = new HookParams()
						.add(IPreResourceAccessDetails.class, accessDetails)
						.add(RequestDetails.class, mySearchRuntimeDetails.getRequestDetails())
						.addIfMatchesType(ServletRequestDetails.class, mySearchRuntimeDetails.getRequestDetails());
					CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.STORAGE_PREACCESS_RESOURCES, params);

					for (int i = unsyncedPids.size() - 1; i >= 0; i--) {
						if (accessDetails.isDontReturnResourceAtIndex(i)) {
							unsyncedPids.remove(i);
							myCountBlockedThisPass++;
							myCountSavedTotal++;
							countBlocked++;
						}
					}
				}

				// Actually store the results in the query cache storage
				myCountSavedTotal += unsyncedPids.size();
				myCountSavedThisPass += unsyncedPids.size();
				mySearchResultCacheSvc.storeResults(mySearch, mySyncedPids, unsyncedPids);

				synchronized (mySyncedPids) {
					int numSyncedThisPass = unsyncedPids.size();
					ourLog.trace("Syncing {} search results - Have more: {}", numSyncedThisPass, theResultIter.hasNext());
					mySyncedPids.addAll(unsyncedPids);
					unsyncedPids.clear();

					if (theResultIter.hasNext() == false) {
						int skippedCount = theResultIter.getSkippedCount();
						int nonSkippedCount = theResultIter.getNonSkippedCount();
						int totalFetched = skippedCount + myCountSavedThisPass + myCountBlockedThisPass;
						ourLog.trace("MaxToFetch[{}] SkippedCount[{}] CountSavedThisPass[{}] CountSavedThisTotal[{}] AdditionalPrefetchRemaining[{}]", myMaxResultsToFetch, skippedCount, myCountSavedThisPass, myCountSavedTotal, myAdditionalPrefetchThresholdsRemaining);

						if (nonSkippedCount == 0 || (myMaxResultsToFetch != null && totalFetched < myMaxResultsToFetch)) {
							ourLog.trace("Setting search status to FINISHED");
							mySearch.setStatus(SearchStatusEnum.FINISHED);
							mySearch.setTotalCount(myCountSavedTotal - countBlocked);
						} else if (myAdditionalPrefetchThresholdsRemaining) {
							ourLog.trace("Setting search status to PASSCMPLET");
							mySearch.setStatus(SearchStatusEnum.PASSCMPLET);
							mySearch.setSearchParameterMap(myParams);
						} else {
							ourLog.trace("Setting search status to FINISHED");
							mySearch.setStatus(SearchStatusEnum.FINISHED);
							mySearch.setTotalCount(myCountSavedTotal - countBlocked);
						}
					}
				}

				mySearch.setNumFound(myCountSavedTotal);
				mySearch.setNumBlocked(mySearch.getNumBlocked() + countBlocked);

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

				ourLog.trace("saveUnsynced() - pre-commit");
			}
		});
		ourLog.trace("saveUnsynced() - post-commit");

	}

	public boolean isNotAborted() {
		return myAbortRequested == false;
	}

	public void markComplete() {
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
		Span span = myParentTransaction.startSpan("db", "query", "search");
		span.setName("FHIR Database Search");
		try {
			// Create an initial search in the DB and give it an ID
			saveSearch();

			TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
			txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

			if (myCustomIsolationSupported) {
				txTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
			}

			txTemplate.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
					doSearch();
				}
			});

			mySearchRuntimeDetails.setSearchStatus(mySearch.getStatus());
			if (mySearch.getStatus() == SearchStatusEnum.FINISHED) {
				HookParams params = new HookParams()
					.add(RequestDetails.class, myRequest)
					.addIfMatchesType(ServletRequestDetails.class, myRequest)
					.add(SearchRuntimeDetails.class, mySearchRuntimeDetails);
				CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_SEARCH_COMPLETE, params);
			} else {
				HookParams params = new HookParams()
					.add(RequestDetails.class, myRequest)
					.addIfMatchesType(ServletRequestDetails.class, myRequest)
					.add(SearchRuntimeDetails.class, mySearchRuntimeDetails);
				CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_SEARCH_PASS_COMPLETE, params);
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

			if (System.getProperty(UNIT_TEST_CAPTURE_STACK) != null) {
				failureMessage += "\nStack\n" + ExceptionUtils.getStackTrace(rootCause);
			}

			mySearch.setFailureMessage(failureMessage);
			mySearch.setFailureCode(failureCode);
			mySearch.setStatus(SearchStatusEnum.FAILED);

			mySearchRuntimeDetails.setSearchStatus(mySearch.getStatus());
			HookParams params = new HookParams()
				.add(RequestDetails.class, myRequest)
				.addIfMatchesType(ServletRequestDetails.class, myRequest)
				.add(SearchRuntimeDetails.class, mySearchRuntimeDetails);
			CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_SEARCH_FAILED, params);

			saveSearch();
			span.captureException(t);
		} finally {
			myOnRemove.accept(mySearch.getUuid());

			myInitialCollectionLatch.countDown();
			markComplete();
			span.end();

		}
		return null;
	}

	private void doSaveSearch() {
		Search newSearch = mySearchCacheSvc.save(mySearch);

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
		boolean myParamWantOnlyCount = isWantOnlyCount(myParams);
		boolean myParamOrDefaultWantCount = nonNull(myParams.getSearchTotalMode()) ? isWantCount(myParams) : SearchParameterMapCalculator.isWantCount(myDaoConfig.getDefaultTotalMode());

		if (myParamWantOnlyCount || myParamOrDefaultWantCount) {
			ourLog.trace("Performing count");
			ISearchBuilder sb = newSearchBuilder();

			/*
			 * createCountQuery
			 * NB: (see createQuery below)
			 * Because FulltextSearchSvcImpl will (internally)
			 * mutate the myParams (searchmap),
			 * (specifically removing the _content and _text filters)
			 * we will have to clone those parameters here so that
			 * the "correct" params are used in createQuery below
			 */
			Long count = sb.createCountQuery(myParams.clone(), mySearch.getUuid(), myRequest, myRequestPartitionId);

			ourLog.trace("Got count {}", count);

			TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
			txTemplate.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theArg0) {
					mySearch.setTotalCount(count.intValue());
					if (myParamWantOnlyCount) {
						mySearch.setStatus(SearchStatusEnum.FINISHED);
					}
					doSaveSearch();
				}
			});
			if (myParamWantOnlyCount) {
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
			minWanted = myParams.getCount() + 1; // Always fetch one past this page, so we know if there is a next page.
			minWanted = Math.min(minWanted, myPagingProvider.getMaximumPageSize());
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
		 * createQuery
		 * Construct the SQL query we'll be sending to the database
		 *
		 * NB: (See createCountQuery above)
		 * We will pass the original myParams here (not a copy)
		 * because we actually _want_ the mutation of the myParams to happen.
		 * Specifically because SearchBuilder itself will _expect_
		 * not to have these parameters when dumping back
		 * to our DB.
		 *
		 * This is an odd implementation behaviour, but the change
		 * for this will require a lot more handling at higher levels
		 */
		try (IResultIterator resultIterator = sb.createQuery(myParams, mySearchRuntimeDetails, myRequest, myRequestPartitionId)) {
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
					AsyncUtil.sleep(myLoadingThrottleForUnitTests);
				}

			}

			// If no abort was requested, bail out
			Validate.isTrue(isNotAborted(), "Abort has been requested");

			saveUnsynced(resultIterator);

		} catch (IOException e) {
			ourLog.error("IO failure during database access", e);
			throw new InternalErrorException(Msg.code(1166) + e);
		}
	}
}
