/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search.builder.tasks;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.interceptor.JpaPreResourceAccessDetails;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.jpa.util.SearchParameterMapCalculator;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.util.AsyncUtil;
import ca.uhn.fhir.util.StopWatch;
import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Span;
import co.elastic.apm.api.Transaction;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.transaction.annotation.Propagation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

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
	// injected beans
	protected final HapiTransactionService myTxService;
	protected final FhirContext myContext;
	protected final ISearchResultCacheSvc mySearchResultCacheSvc;
	private final SearchParameterMap myParams;
	private final IDao myCallingDao;
	private final String myResourceType;
	private final ArrayList<JpaPid> mySyncedPids = new ArrayList<>();
	private final CountDownLatch myInitialCollectionLatch = new CountDownLatch(1);
	private final CountDownLatch myCompletionLatch;
	private final ArrayList<JpaPid> myUnsyncedPids = new ArrayList<>();
	private final RequestDetails myRequest;
	private final RequestPartitionId myRequestPartitionId;
	private final SearchRuntimeDetails mySearchRuntimeDetails;
	private final Transaction myParentTransaction;
	private final Consumer<String> myOnRemove;
	private final int mySyncSize;
	private final Integer myLoadingThrottleForUnitTests;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;
	private final SearchBuilderFactory<JpaPid> mySearchBuilderFactory;
	private final JpaStorageSettings myStorageSettings;
	private final ISearchCacheSvc mySearchCacheSvc;
	private final IPagingProvider myPagingProvider;
	private Search mySearch;
	private boolean myAbortRequested;
	private int myCountSavedTotal = 0;
	private int myCountSavedThisPass = 0;
	private int myCountBlockedThisPass = 0;
	private boolean myAdditionalPrefetchThresholdsRemaining;
	private List<JpaPid> myPreviouslyAddedResourcePids;
	private Integer myMaxResultsToFetch;

	/**
	 * Constructor
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public SearchTask(
			SearchTaskParameters theCreationParams,
			HapiTransactionService theManagedTxManager,
			FhirContext theContext,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			SearchBuilderFactory theSearchBuilderFactory,
			ISearchResultCacheSvc theSearchResultCacheSvc,
			JpaStorageSettings theStorageSettings,
			ISearchCacheSvc theSearchCacheSvc,
			IPagingProvider thePagingProvider) {
		// beans
		myTxService = theManagedTxManager;
		myContext = theContext;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
		mySearchBuilderFactory = theSearchBuilderFactory;
		mySearchResultCacheSvc = theSearchResultCacheSvc;
		myStorageSettings = theStorageSettings;
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
	}

	protected RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
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
			if (AsyncUtil.awaitLatchAndThrowInternalErrorExceptionOnInterrupt(
					getInitialCollectionLatch(), 250L, TimeUnit.MILLISECONDS)) {
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

	public void setPreviouslyAddedResourcePids(List<JpaPid> thePreviouslyAddedResourcePids) {
		myPreviouslyAddedResourcePids = thePreviouslyAddedResourcePids;
		myCountSavedTotal = myPreviouslyAddedResourcePids.size();
	}

	@SuppressWarnings("rawtypes")
	private ISearchBuilder newSearchBuilder() {
		Class<? extends IBaseResource> resourceTypeClass =
				myContext.getResourceDefinition(myResourceType).getImplementingClass();
		return mySearchBuilderFactory.newSearchBuilder(myCallingDao, myResourceType, resourceTypeClass);
	}

	@Nonnull
	public List<JpaPid> getResourcePids(int theFromIndex, int theToIndex) {
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
				ourLog.info(
						"Waiting as we only have {} results - Search status: {}",
						mySyncedPids.size(),
						mySearch.getStatus());
				AsyncUtil.sleep(500L);
			}
		} while (keepWaiting);

		ourLog.debug("Proceeding, as we have {} results", mySyncedPids.size());

		ArrayList<JpaPid> retVal = new ArrayList<>();
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

		ourLog.trace(
				"Done syncing results - Wanted {}-{} and returning {} of {}",
				theFromIndex,
				theToIndex,
				retVal.size(),
				mySyncedPids.size());

		return retVal;
	}

	public void saveSearch() {
		myTxService
				.withRequest(myRequest)
				.withRequestPartitionId(myRequestPartitionId)
				.withPropagation(Propagation.REQUIRES_NEW)
				.execute(() -> doSaveSearch());
	}

	@SuppressWarnings("rawtypes")
	private void saveUnsynced(final IResultIterator theResultIter) {
		myTxService
				.withRequest(myRequest)
				.withRequestPartitionId(myRequestPartitionId)
				.execute(() -> {
					if (mySearch.getId() == null) {
						doSaveSearch();
					}

					ArrayList<JpaPid> unsyncedPids = myUnsyncedPids;
					int countBlocked = 0;

					// Interceptor call: STORAGE_PREACCESS_RESOURCES
					// This can be used to remove results from the search result details before
					// the user has a chance to know that they were in the results
					if (mySearchRuntimeDetails.getRequestDetails() != null && !unsyncedPids.isEmpty()) {
						JpaPreResourceAccessDetails accessDetails =
								new JpaPreResourceAccessDetails(unsyncedPids, this::newSearchBuilder);
						HookParams params = new HookParams()
								.add(IPreResourceAccessDetails.class, accessDetails)
								.add(RequestDetails.class, mySearchRuntimeDetails.getRequestDetails())
								.addIfMatchesType(
										ServletRequestDetails.class, mySearchRuntimeDetails.getRequestDetails());
						CompositeInterceptorBroadcaster.doCallHooks(
								myInterceptorBroadcaster, myRequest, Pointcut.STORAGE_PREACCESS_RESOURCES, params);

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
					mySearchResultCacheSvc.storeResults(
							mySearch, mySyncedPids, unsyncedPids, myRequest, getRequestPartitionId());

					synchronized (mySyncedPids) {
						int numSyncedThisPass = unsyncedPids.size();
						ourLog.trace(
								"Syncing {} search results - Have more: {}",
								numSyncedThisPass,
								theResultIter.hasNext());
						mySyncedPids.addAll(unsyncedPids);
						unsyncedPids.clear();

						if (!theResultIter.hasNext()) {
							int skippedCount = theResultIter.getSkippedCount();
							ourLog.trace(
									"MaxToFetch[{}] SkippedCount[{}] CountSavedThisPass[{}] CountSavedThisTotal[{}] AdditionalPrefetchRemaining[{}]",
									myMaxResultsToFetch,
									skippedCount,
									myCountSavedThisPass,
									myCountSavedTotal,
									myAdditionalPrefetchThresholdsRemaining);

							if (isFinished(theResultIter)) {
								// finished
								ourLog.trace("Setting search status to FINISHED");
								mySearch.setStatus(SearchStatusEnum.FINISHED);
								mySearch.setTotalCount(myCountSavedTotal - countBlocked);
							} else if (myAdditionalPrefetchThresholdsRemaining) {
								// pass complete
								ourLog.trace("Setting search status to PASSCMPLET");
								mySearch.setStatus(SearchStatusEnum.PASSCMPLET);
								mySearch.setSearchParameterMap(myParams);
							} else {
								// also finished
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

					if (myStorageSettings.getCountSearchResultsUpTo() == null
							|| myStorageSettings.getCountSearchResultsUpTo() <= 0
							|| myStorageSettings.getCountSearchResultsUpTo() <= numSynced) {
						myInitialCollectionLatch.countDown();
					}

					doSaveSearch();

					ourLog.trace("saveUnsynced() - pre-commit");
				});
		ourLog.trace("saveUnsynced() - post-commit");
	}

	@SuppressWarnings("rawtypes")
	private boolean isFinished(final IResultIterator theResultIter) {
		int skippedCount = theResultIter.getSkippedCount();
		int nonSkippedCount = theResultIter.getNonSkippedCount();
		int totalFetched = skippedCount + myCountSavedThisPass + myCountBlockedThisPass;

		if (myMaxResultsToFetch != null && totalFetched < myMaxResultsToFetch) {
			// total fetched < max results to fetch -> we've exhausted the search
			return true;
		} else {
			if (nonSkippedCount == 0) {
				// no skipped resources in this query
				if (myParams.getCount() != null) {
					// count supplied
					// if the count is > what we've fetched -> we've exhausted the query
					return myParams.getCount() > totalFetched;
				} else {
					// legacy - we have no skipped resources - we are done
					return true;
				}
			}
			// skipped resources means we have more to fetch
			return false;
		}
	}

	public boolean isNotAborted() {
		return !myAbortRequested;
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

			myTxService
					.withRequest(myRequest)
					.withRequestPartitionId(myRequestPartitionId)
					.execute(this::doSearch);

			mySearchRuntimeDetails.setSearchStatus(mySearch.getStatus());
			if (mySearch.getStatus() == SearchStatusEnum.FINISHED) {
				HookParams params = new HookParams()
						.add(RequestDetails.class, myRequest)
						.addIfMatchesType(ServletRequestDetails.class, myRequest)
						.add(SearchRuntimeDetails.class, mySearchRuntimeDetails);
				CompositeInterceptorBroadcaster.doCallHooks(
						myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_SEARCH_COMPLETE, params);
			} else {
				HookParams params = new HookParams()
						.add(RequestDetails.class, myRequest)
						.addIfMatchesType(ServletRequestDetails.class, myRequest)
						.add(SearchRuntimeDetails.class, mySearchRuntimeDetails);
				CompositeInterceptorBroadcaster.doCallHooks(
						myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_SEARCH_PASS_COMPLETE, params);
			}

			ourLog.trace(
					"Have completed search for [{}{}] and found {} resources in {}ms - Status is {}",
					mySearch.getResourceType(),
					mySearch.getSearchQueryString(),
					mySyncedPids.size(),
					sw.getMillis(),
					mySearch.getStatus());

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

			if (HapiSystemProperties.isUnitTestCaptureStackEnabled()) {
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
			CompositeInterceptorBroadcaster.doCallHooks(
					myInterceptorBroadcaster, myRequest, Pointcut.JPA_PERFTRACE_SEARCH_FAILED, params);

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
		Search newSearch = mySearchCacheSvc.save(mySearch, myRequestPartitionId);

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
	@SuppressWarnings({"rawtypes", "unchecked"})
	private void doSearch() {
		/*
		 * If the user has explicitly requested a _count, perform a
		 *
		 * SELECT COUNT(*) ....
		 *
		 * before doing anything else.
		 */
		boolean myParamWantOnlyCount = isWantOnlyCount(myParams);
		boolean myParamOrDefaultWantCount = nonNull(myParams.getSearchTotalMode())
				? isWantCount(myParams)
				: SearchParameterMapCalculator.isWantCount(myStorageSettings.getDefaultTotalMode());

		if (myParamWantOnlyCount || myParamOrDefaultWantCount) {
			doCountOnlyQuery(myParamWantOnlyCount);
			if (myParamWantOnlyCount) {
				return;
			}
		}

		ourLog.trace("Done count");
		ISearchBuilder sb = newSearchBuilder();

		/*
		 * Figure out how many results we're actually going to fetch from the
		 * database in this pass. This calculation takes into consideration the
		 * "pre-fetch thresholds" specified in StorageSettings#getSearchPreFetchThresholds()
		 * as well as the value of the _count parameter.
		 */
		int currentlyLoaded = defaultIfNull(mySearch.getNumFound(), 0);
		int minWanted = 0;

		// if no count is provided,
		// we only use the values in SearchPreFetchThresholds
		// but if there is a count...
		if (myParams.getCount() != null) {
			minWanted = Math.min(myParams.getCount(), myPagingProvider.getMaximumPageSize());
			minWanted += currentlyLoaded;
		}

		// iterate through the search thresholds
		for (Iterator<Integer> iter =
						myStorageSettings.getSearchPreFetchThresholds().iterator();
				iter.hasNext(); ) {
			int next = iter.next();
			if (next != -1 && next <= currentlyLoaded) {
				continue;
			}

			if (next == -1) {
				sb.setMaxResultsToFetch(null);
			} else {
				// we want at least 1 more than our requested amount
				// so we know that there are other results
				// (in case we get the exact amount back)
				myMaxResultsToFetch = Math.max(next, minWanted);
				sb.setMaxResultsToFetch(myMaxResultsToFetch + 1);
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
		try (IResultIterator<JpaPid> resultIterator =
				sb.createQuery(myParams, mySearchRuntimeDetails, myRequest, myRequestPartitionId)) {
			// resultIterator is SearchBuilder.QueryIterator
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

				if (myStorageSettings.getCountSearchResultsUpTo() != null
						&& myStorageSettings.getCountSearchResultsUpTo() > 0
						&& myStorageSettings.getCountSearchResultsUpTo() < myUnsyncedPids.size()) {
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

	/**
	 * Does the query but only for the count.
	 * @param theParamWantOnlyCount - if count query is wanted only
	 */
	private void doCountOnlyQuery(boolean theParamWantOnlyCount) {
		ourLog.trace("Performing count");
		@SuppressWarnings("rawtypes")
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

		myTxService
				.withRequest(myRequest)
				.withRequestPartitionId(myRequestPartitionId)
				.execute(() -> {
					mySearch.setTotalCount(count.intValue());
					if (theParamWantOnlyCount) {
						mySearch.setStatus(SearchStatusEnum.FINISHED);
					}
					doSaveSearch();
				});
	}
}
