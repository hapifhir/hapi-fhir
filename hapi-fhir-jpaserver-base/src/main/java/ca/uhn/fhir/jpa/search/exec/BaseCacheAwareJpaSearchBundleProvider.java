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

import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.ISearchResultConsumer;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.SearchProgressTracker;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.jpa.interceptor.JpaPreResourceAccessDetails;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.search.SearchBuilderLoadIncludesParameters;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.ExceptionService;
import ca.uhn.fhir.jpa.search.PersistedJpaHistoryBundleProvider;
import ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.jpa.util.SearchParameterMapCalculator;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SearchCacheStatus;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.interceptor.ServerInterceptorUtil;
import ca.uhn.fhir.rest.server.method.ResponsePage;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.util.IntCounter;
import ca.uhn.fhir.util.TaskChunker;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.transaction.UnexpectedRollbackException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * This class handles search results for <b>FHIR Search</b> operations leveraging the {@link ISearchCacheSvc search cache}. It has 2 concrete subclasses:
 * <ul>
 *     <li>{@link CacheAwareJpaSearchBundleProviderFirstPage}</li> is used for the initial search
 *     <li>{@link CacheAwareJpaSearchBundleProviderSubsequentPage}</li> is used for the following page loads from the query cache
 * </ul>
 *
 * @see IStatelessJpaSearchSvc The Synchronous Search Service is used instead of this class for searches which don't use the search cache
 * @see PersistedJpaHistoryBundleProvider The search result for <b>FHIR History</b> operations.
 * @since 8.14.0
 */
public abstract class BaseCacheAwareJpaSearchBundleProvider implements IBundleProvider {

	public static final SearchCacheStatus CACHE_STATUS_BYPASS = SearchCacheStatus.builder()
			.withCacheName("HapiFhirQueryCache")
			.setStatus(SearchCacheStatus.SearchCacheStatusEnum.FWD_BYPASS)
			.build();
	private static final int SEARCH_EXPIRY_OFFSET_MINUTES = 10;
	private static final Logger ourLog = LoggerFactory.getLogger(BaseCacheAwareJpaSearchBundleProvider.class);

	/**
	 * Adjust this to raise the level of the debug logs if you are
	 * troubleshooting something.
	 */
	private static final Level DEBUG_LOG_LEVEL = Level.DEBUG;

	protected final RequestDetails myRequestDetails;
	protected final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	protected final ISearchCacheSvc mySearchCacheSvc;
	protected final ExceptionService myExceptionService;
	protected final JpaStorageSettings myStorageSettings;
	protected final IInterceptorBroadcaster myCompositeBroadcaster;
	private final Map<JpaPid, IBaseResource> myFetchedResources = new HashMap<>();
	private final FhirContext myFhirContext;
	private final IPagingProvider myPagingProvider;
	private final EntityManager myEntityManager;
	private final IHapiTransactionService myTxService;
	private final ISearchResultCacheSvc mySearchResultCacheSvc;
	private final SearchBuilderFactory<JpaPid> mySearchBuilderFactory;
	private final BaseRuntimeElementDefinition<IPrimitiveType<Date>> myInstantDefinition;

	protected SearchParameterMap myParams;
	protected RequestPartitionId myRequestPartitionId;
	protected SearchCacheStatus myCacheStatus;

	/**
	 * If the search has been
	 * {@link #initializeSearchInsideTransaction(int, int) initialized},
	 * this is the search entity associated with the search.
	 */
	private Search mySearchEntity;

	/**
	 * If the search has been
	 * {@link #initializeSearchInsideTransaction(int, int) initialized},
	 * this is the search match results (i.e. any resource PIDs that were found
	 * for the given search parameters, <b>not including</b> any <code>_include</code>
	 * or <code>_revinclude</code> results.
	 */
	private CachedPids myCachedPidsFromMatches;

	/**
	 * If the search has been
	 * {@link #initializeSearchInsideTransaction(int, int) initialized},
	 * this is the search match results (i.e. any resource PIDs that were found
	 * for the given search parameters, <b>including</b> any <code>_include</code>
	 * or <code>_revinclude</code> results.
	 */
	private CachedPids myCachedPidsFromMatchesAndIncludes;

	@Nullable
	private Integer myFetchedResourceLocalCacheMaximumSize;

	/**
	 * Constructor
	 */
	@SuppressWarnings("unchecked")
	protected BaseCacheAwareJpaSearchBundleProvider(
			FhirContext theFhirContext,
			RequestDetails theRequestDetails,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			IPagingProvider thePagingProvider,
			JpaStorageSettings theStorageSettings,
			EntityManager theEntityManager,
			IHapiTransactionService theTxService,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc,
			ISearchCacheSvc theSearchCacheSvc,
			ISearchResultCacheSvc theSearchResultCacheSvc,
			ExceptionService theExceptionService,
			SearchBuilderFactory<JpaPid> theSearchBuilderFactory,
			SearchParameterMap theParams,
			RequestPartitionId theRequestPartitionId) {
		myPagingProvider = thePagingProvider;
		myFhirContext = theFhirContext;
		myRequestDetails = theRequestDetails;
		myStorageSettings = theStorageSettings;
		myEntityManager = theEntityManager;
		myTxService = theTxService;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
		mySearchCacheSvc = theSearchCacheSvc;
		mySearchResultCacheSvc = theSearchResultCacheSvc;
		myExceptionService = theExceptionService;
		mySearchBuilderFactory = theSearchBuilderFactory;
		myCompositeBroadcaster =
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(theInterceptorBroadcaster, myRequestDetails);
		myInstantDefinition =
				(BaseRuntimeElementDefinition<IPrimitiveType<Date>>) myFhirContext.getElementDefinition("instant");
		myParams = theParams;
		setRequestPartitionId(theRequestPartitionId);
	}

	@Override
	public IPrimitiveType<Date> getPublished() {
		initializeSearchIfNecessary();
		IPrimitiveType<Date> retVal = myInstantDefinition.newInstance();
		retVal.setValue(mySearchEntity.getCreated());
		return retVal;
	}

	@Nullable
	@Override
	public String getUuid() {
		initializeSearchIfNecessary();
		return mySearchEntity.getUuid();
	}

	@Override
	public Integer preferredPageSize() {
		if (hasLoadedSearchEntity()) {
			return mySearchEntity.getPreferredPageSize();
		}
		return null;
	}

	@Override
	public boolean isShouldFetchResourcesBeforeOtherProperties() {
		return true;
	}

	@Nullable
	@Override
	public Integer size() {
		if (mySearchEntity != null && mySearchEntity.getId() != null) {
			return mySearchEntity.getTotalCount();
		}
		if (myParams != null && myPagingProvider != null) {
			int from = 0;
			if (myParams.getOffset() != null) {
				from = myParams.getOffset();
			}

			int to = myPagingProvider.getDefaultPageSize();
			if (myParams.getCount() != null) {
				to = myParams.getCount();
			}
			initializeSearchIfNecessary(from, to);
			return mySearchEntity.getTotalCount();
		}
		return null;
	}

	@Nullable
	@Override
	public SearchCacheStatus getCacheStatus() {
		initializeSearchIfNecessary();
		return myCacheStatus;
	}

	/**
	 * Fetch a range of search results
	 *
	 * @param theFromIndex           The low index (inclusive) to return
	 * @param theToIndex             The high index (exclusive) to return
	 * @param theResponsePageBuilder The ResponsePageBuilder. The builder will add values needed for the response page.
	 */
	@Override
	public List<IBaseResource> getResources(
			int theFromIndex, int theToIndex, @Nonnull ResponsePage.ResponsePageBuilder theResponsePageBuilder) {
		CachedPids searchResults = initializeSearchIfNecessary(theFromIndex, theToIndex);

		List<IBaseResource> retVal = new ArrayList<>();
		for (JpaPid nextPid : searchResults.pids()) {
			IBaseResource resource = myFetchedResources.get(nextPid);
			if (resource != null) {
				retVal.add(resource);
			}
		}

		// we will send the resource list to our interceptors
		// this can (potentially) change the results being returned.
		int precount = retVal.size();
		retVal = ServerInterceptorUtil.fireStoragePreshowResourcesToCompositeBroadcaster(
				retVal, myRequestDetails, myCompositeBroadcaster);

		// we only care about omitted results from this page
		theResponsePageBuilder.setPageSize(precount);
		theResponsePageBuilder.setOmittedResourceCount(precount - retVal.size());
		theResponsePageBuilder.setResources(retVal);
		theResponsePageBuilder.setIncludedResourceCount(searchResults.includedResourceCount());
		theResponsePageBuilder.setTotalRequestedResourcesFetched(mySearchEntity.getNumFound());

		// TODO: JA2 the "hasNextPage" property should be removed entirely
		//		theResponsePageBuilder.setHasNextPage(
		//				theToIndex < mySearchEntity.getNumFound() || mySearchEntity.getStatus() == SearchStatusEnum.PASSCMPLET);

		ourLog.atLevel(DEBUG_LOG_LEVEL)
				.setMessage("Returning {} results for range {}-{}")
				.addArgument(retVal.size())
				.addArgument(theFromIndex)
				.addArgument(theToIndex)
				.log();

		return retVal;
	}

	/**
	 * This is really only intended for unit tests, the regular search paths don't call this method.
	 */
	@Nonnull
	@Override
	public List<IBaseResource> getAllResources() {
		List<IBaseResource> resources = getResources(0, 10000);
		Validate.isTrue(resources.size() < 10000, "Can not call getAllResources on a collection >= 10000 resources");
		resources.removeIf(Objects::isNull);
		return resources;
	}

	/**
	 * Initializes the search, assuming that the first search threshold is the
	 * desired number of resources if the search hasn't already been initialized.
	 * <p>
	 * This should only happen if a consumer of the {@link IBundleProvider} calls
	 * a method other than {@link #getResources(int, int, ResponsePage.ResponsePageBuilder)}
	 * first, which won't happen during normal search scenarios.
	 * In other words, this method should not do any work in any normal scenatios
	 * where it will be called (but older unit tests may cause it to be called first).
	 * </p>
	 */
	protected void initializeSearchIfNecessary() {
		if (myCachedPidsFromMatches == null) {
			ourLog.warn(
					"Initializing search without an explicit range specified. This is inefficient and should be avoided.");
			int to;
			if (myParams != null && myParams.getCount() != null) {
				to = myParams.getCount();
			} else {
				to = myStorageSettings.getSearchPreFetchThresholds().get(0);
			}
			initializeSearchIfNecessary(0, to);
		}
		validateSearchEntityNotFailed();
	}

	/**
	 * Prepare to return resources for the search result range from {@literal theFromIndex} to
	 * {@literal theToIndex}.
	 *
	 * @param theFromIndex The start of the search range (inclusive)
	 * @param theToIndex   The end of the search range (exclusive)
	 * @return Returns the PIDs associated with the given range of search results.
	 */
	@Nonnull
	private CachedPids initializeSearchIfNecessary(int theFromIndex, int theToIndex) {

		/*
		 * We're not yet in a database transaction here, so we'll first make a few
		 * attempts to avoid opening one.
		 */

		if (myCachedPidsFromMatchesAndIncludes != null) {
			if (myCachedPidsFromMatchesAndIncludes.fromIndex() == theFromIndex) {
				/*
				 * If we've already fetched the exact range we want, we are done
				 */
				if (myCachedPidsFromMatchesAndIncludes.toIndex() == theToIndex) {
					return myCachedPidsFromMatchesAndIncludes;
				}

				/*
				 * We can reuse the previously fetched matches if the range being
				 * requested now is the same as the one we already fetched. We can
				 * also reuse if the end of the requested range (theToIndex) is
				 * >= the total number of matches for the search, and <= the
				 * upper end of the range (since this means that no matter how
				 * high theToIndex is, we already have all the matches we need)
				 */
				if (mySearchEntity != null
						&& mySearchEntity.getStatus() == SearchStatusEnum.FINISHED
						&& mySearchEntity.getTotalCount() != null
						&& theFromIndex == myCachedPidsFromMatchesAndIncludes.fromIndex()
						&& theToIndex <= myCachedPidsFromMatchesAndIncludes.toIndex()
						&& theToIndex >= mySearchEntity.getTotalCount()) {
					return myCachedPidsFromMatchesAndIncludes;
				}
			}
		}

		/*
		 * If we don't have any _include or _revinclude parameters, and we've already
		 * fetched a collection of matched resources for a range that is equivalent or
		 * exceeding the range we actually want, we can just use a subsection of the
		 * already fetched resources
		 */
		if (myCachedPidsFromMatches != null) {
			if (myParams.getIncludes().isEmpty() && myParams.getRevIncludes().isEmpty()) {
				if (myCachedPidsFromMatches.fromIndex() <= theFromIndex
						&& myCachedPidsFromMatches.toIndex() >= theToIndex) {
					int rangeStart = theFromIndex - myCachedPidsFromMatches.fromIndex();
					int rangeEnd = (theToIndex - theFromIndex) + rangeStart;
					rangeEnd = Math.min(rangeEnd, myCachedPidsFromMatches.size());
					List<JpaPid> rangePids = myCachedPidsFromMatches.pids().subList(rangeStart, rangeEnd);
					myCachedPidsFromMatchesAndIncludes = new CachedPids(theFromIndex, theToIndex, rangePids, 0);
					return myCachedPidsFromMatchesAndIncludes;
				}
			}
		}

		/*
		 * If we only want to fetch the count, and we already know it, there is nothing else to do.
		 */
		if (myParams != null && mySearchEntity != null && SearchParameterMapCalculator.isWantOnlyCount(myParams)) {
			if (mySearchEntity.getTotalCount() != null) {
				myCachedPidsFromMatchesAndIncludes = new CachedPids(theFromIndex, theToIndex, List.of(), 0);
				return myCachedPidsFromMatchesAndIncludes;
			}
		}

		/*
		 * Ok, so we need to actually open a database transaction and perform the search.
		 * We will automatically retry if we get a constraint error, since presumably
		 * some other thread is also performing the search. If the error just keeps happening,
		 * we must have a deeper problem, so we should bail.
		 */
		try {
			myTxService
					.withRequest(myRequestDetails)
					.withRequestPartitionId(myRequestPartitionId)
					.withMaxRetries(3)
					.execute(() -> initializeSearchInsideTransaction(theFromIndex, theToIndex));
		} catch (ResourceGoneException e) {
			ourLog.info("Aborted search: {}", e.getMessage());
			throw e;
		} catch (UnexpectedRollbackException e) {
			validateSearchEntityNotFailed();
			throw e;
		}

		validateSearchEntityNotFailed();
		return myCachedPidsFromMatchesAndIncludes;
	}

	/**
	 * This method performs the "in database transaction" portion of performing
	 * a search, meaning fetching result PIDs and hydrating the responses.
	 *
	 * @param theFromIndex The start of the search range (inclusive)
	 * @param theToIndex   The end of the search range (exclusive)
	 */
	private void initializeSearchInsideTransaction(int theFromIndex, int theToIndex) {
		HapiTransactionService.requireTransaction();

		ourLog.atLevel(DEBUG_LOG_LEVEL)
				.setMessage("About to initialize search results {}-{}")
				.addArgument(theFromIndex)
				.addArgument(theToIndex)
				.log();

		final List<JpaPid> pidsToReturn = new ArrayList<>();

		mySearchEntity = provideSearchEntity();
		if (myParams == null) {
			myParams = extractSearchParameterMapFromSearchEntity();
		}

		// If we have a _count=summary query, calculate the count and return
		if (SearchParameterMapCalculator.isWantOnlyCount(myParams)) {
			if (mySearchEntity.getTotalCount() == null) {
				Long countQuery = newSearchBuilder()
						.createCountQuery(myParams, mySearchEntity.getUuid(), myRequestDetails, myRequestPartitionId);
				mySearchEntity.setSearchParameterMap(myParams);
				if (countQuery != null) {
					mySearchEntity.setTotalCount(Math.toIntExact(countQuery));
				}
				mySearchEntity.setStatus(SearchStatusEnum.FINISHED);
				mySearchCacheSvc.save(mySearchEntity, myRequestPartitionId);
			}
			myCachedPidsFromMatchesAndIncludes = new CachedPids(theFromIndex, theToIndex, List.of(), 0);
			return;
		}

		/*
		 * If the previously found search is either finished or has already found enough results to
		 * satisfy the currently wanted count, then we can just return previously fetched results.
		 */
		if (mySearchEntity.getStatus() == SearchStatusEnum.FINISHED || mySearchEntity.getNumFound() >= theToIndex) {
			List<JpaPid> existingSearchPids = mySearchResultCacheSvc.fetchResultPids(
					mySearchEntity, theFromIndex, theToIndex, myRequestDetails, myRequestPartitionId);
			ISearchBuilder<JpaPid> searchBuilder = newSearchBuilder();
			fetchResourcesAndIncludes(searchBuilder, existingSearchPids, theFromIndex, theToIndex);
			return;
		}

		ISearchBuilder<JpaPid> searchBuilder = newSearchBuilder();

		List<JpaPid> previouslyFoundPids = List.of();
		SearchRuntimeDetails searchDetails = new SearchRuntimeDetails(myRequestDetails, mySearchEntity.getUuid());

		if (mySearchEntity.getNumFound() > 0) {
			previouslyFoundPids =
					mySearchResultCacheSvc.fetchAllResultPids(mySearchEntity, myRequestDetails, myRequestPartitionId);
			Validate.notNull(previouslyFoundPids, "previouslyFoundPids should not be null");
			searchBuilder.setPreviouslyAddedResourcePids(previouslyFoundPids);

			if (previouslyFoundPids.size() > theFromIndex) {
				int to = Math.min(theToIndex, previouslyFoundPids.size());
				pidsToReturn.addAll(previouslyFoundPids.subList(theFromIndex, to));
			}
		}

		trimLocalFetchedResourceCache(pidsToReturn);

		int numWanted = theToIndex - mySearchEntity.getNumFound();

		final IntCounter numToSkip = new IntCounter(0);
		if (theFromIndex > mySearchEntity.getNumFound()) {
			numToSkip.set(theFromIndex - mySearchEntity.getNumFound());
		}

		List<JpaPid> foundPidsToStore = new ArrayList<>();
		boolean haveMoreResults = true;
		int lastSkipCount = 0;
		while (haveMoreResults) {
			/*
			 * To ensure that every individual page load doesn't need to turn around and perform the
			 * search again, we pre-fetch a set of sensible thresholds. So for example, if the client
			 * wants the first 10 results, we might fetch the first 30
			 */
			SearchThreshold searchThreshold = calculateNextSearchThreshold(numWanted + lastSkipCount);
			searchBuilder.setMaxResultsToFetch(searchThreshold.threshold());
			searchBuilder.setDeduplicateInDatabase(searchThreshold.deduplicateInDatabase());
			ourLog.atLevel(DEBUG_LOG_LEVEL)
					.setMessage("About to perform search with threshold {} for results {}-{}")
					.addArgument(searchThreshold)
					.addArgument(theFromIndex)
					.addArgument(theToIndex)
					.log();

			/*
			 * Actually perform the search
			 */
			List<JpaPid> newPidsThisPass = new ArrayList<>();
			PidConsumer consumer = new PidConsumer(
					numWanted, newPidsThisPass, numToSkip, pidsToReturn, searchThreshold, searchBuilder);
			SearchProgressTracker outcome;
			try {
				outcome = searchBuilder.performSearchForPids(
						consumer, myParams, searchDetails, myRequestDetails, myRequestPartitionId);
			} catch (Exception e) {
				SearchCoordinatorSvcImpl.markSearchAsFailedWithExceptionDetails(mySearchEntity, e);
				mySearchCacheSvc.save(mySearchEntity, myRequestPartitionId);
				return;
			}

			lastSkipCount = outcome.getSkippedCount() + consumer.getBlockedCount();
			int pidsCountThisPass = newPidsThisPass.size() + lastSkipCount;
			if (searchThreshold.threshold() == null || pidsCountThisPass < searchThreshold.threshold()) {
				haveMoreResults = false;
			}

			mySearchEntity.setNumFound(mySearchEntity.getNumFound() + consumer.getFoundCount());
			mySearchEntity.setNumBlocked(mySearchEntity.getNumBlocked() + consumer.getBlockedCount());

			foundPidsToStore.addAll(newPidsThisPass);

			// If our last prefetch threshold specifies a specific maximum count, force the search
			// to be over when we hit that count.
			if (searchThreshold.threshold() != null
					&& searchThreshold.isLastThreshold()
					&& mySearchEntity.getNumFound() >= searchThreshold.threshold()) {
				haveMoreResults = false;
				break;
			}

			if (mySearchEntity.getNumFound() >= theToIndex) {
				break;
			}
		}

		if (haveMoreResults) {
			mySearchEntity.setStatus(SearchStatusEnum.PASSCMPLET);
			searchDetails.setSearchStatus(SearchStatusEnum.PASSCMPLET);

			/*
			 * If we finished the first page of results, and we still don't know
			 * the total count, but the client requested the total count (or the
			 * server is configured to always return it), we will perform an
			 * explicit count query.
			 */
			if (mySearchEntity.getTotalCount() == null) {
				if (SearchParameterMapCalculator.isWantCount(myParams, myStorageSettings)) {
					Long countQuery = newSearchBuilder()
							.createCountQuery(
									myParams, mySearchEntity.getUuid(), myRequestDetails, myRequestPartitionId);
					if (countQuery != null) {
						mySearchEntity.setTotalCount(Math.toIntExact(countQuery));
					}
				}
			}

			// Interceptor: JPA_PERFTRACE_SEARCH_PASS_COMPLETE
			myCompositeBroadcaster.ifHasCallHooks(Pointcut.JPA_PERFTRACE_SEARCH_PASS_COMPLETE, () -> new HookParams()
					.add(RequestDetails.class, myRequestDetails)
					.addIfMatchesType(ServletRequestDetails.class, myRequestDetails)
					.add(SearchRuntimeDetails.class, searchDetails));

		} else {
			mySearchEntity.setStatus(SearchStatusEnum.FINISHED);
			searchDetails.setSearchStatus(SearchStatusEnum.FINISHED);
			mySearchEntity.setTotalCount(mySearchEntity.getNumFound());

			// Interceptor: JPA_PERFTRACE_SEARCH_COMPLETE
			myCompositeBroadcaster.ifHasCallHooks(Pointcut.JPA_PERFTRACE_SEARCH_COMPLETE, () -> new HookParams()
					.add(RequestDetails.class, myRequestDetails)
					.addIfMatchesType(ServletRequestDetails.class, myRequestDetails)
					.add(SearchRuntimeDetails.class, searchDetails));
		}

		mySearchEntity.setSearchParameterMap(myParams);
		updateSearchExpiryIfNecessary();

		ourLog.atLevel(DEBUG_LOG_LEVEL)
				.setMessage("Now have {} results and {} blocked in query cache")
				.addArgument(mySearchEntity.getNumFound())
				.addArgument(mySearchEntity.getNumBlocked())
				.log();

		mySearchCacheSvc.save(mySearchEntity, myRequestPartitionId);
		mySearchResultCacheSvc.storeResults(
				mySearchEntity, previouslyFoundPids, foundPidsToStore, myRequestDetails, myRequestPartitionId);

		int numberToReturn = theToIndex - theFromIndex;
		if (pidsToReturn.size() > numberToReturn) {
			pidsToReturn.subList(numberToReturn, pidsToReturn.size()).clear();
		}

		fetchResourcesAndIncludes(searchBuilder, pidsToReturn, theFromIndex, theToIndex);
	}

	private void trimLocalFetchedResourceCache(List<JpaPid> thePidsToRetain) {
		if (!myFetchedResources.isEmpty()) {
			myFetchedResources.entrySet().removeIf(entry -> !thePidsToRetain.contains(entry.getKey()));
		}
	}

	/**
	 * Using the {@link JpaStorageSettings#getSearchPreFetchThresholds() pre-fetch thresholds},
	 * determines the next search threshold to use.
	 *
	 * @param theNumWanted The minimum number of results we want to fetch.
	 */
	@Nonnull
	private SearchThreshold calculateNextSearchThreshold(int theNumWanted) {
		Integer threshold;
		boolean isLastThreshold;
		boolean deduplicateInDatabase;

		List<Integer> thresholds = myStorageSettings.getSearchPreFetchThresholds();
		int firstThreshold = thresholds.get(0);
		int lastThreshold = thresholds.get(thresholds.size() - 1);

		/*
		 * If we're searching for the first page of results:
		 *
		 * - If the requested count is greater than the first threshold, just search
		 *   for exactly one more than the requested count instead of advancing to
		 *   the next threshold. We do this because we assume that many searches will
		 *   request exactly the number they want to consume, and will never fetch
		 *   subsequent pages, so this way we avoid fetching 500 results when the
		 *   client just wants one page of 20. We add one so that we know whether
		 *   a subsequent page exists though.
		 * - If the requested count is greater than the last threshold, just search
		 *   for exactly the last threshold, and don't exceed it.
		 */
		boolean firstSearch = mySearchEntity.getNumFound() == 0 && mySearchEntity.getNumBlocked() == 0;
		if (firstSearch && theNumWanted > firstThreshold) {
			if (lastThreshold > 0 && theNumWanted > lastThreshold) {
				threshold = lastThreshold;
				isLastThreshold = true;
				deduplicateInDatabase = false;
			} else {
				threshold = theNumWanted + 1;
				isLastThreshold = false;
				deduplicateInDatabase = false;
			}
		} else {

			/*
			 * For subsequent pages, we'll use the predetermined search thresholds
			 */

			threshold = null;
			isLastThreshold = true;
			deduplicateInDatabase = true;

			for (Iterator<Integer> iterator = thresholds.iterator(); iterator.hasNext(); ) {
				int nextThreshold = iterator.next();

				/*
				 * If we're past the last prefetch threshold then
				 * we're potentially fetching unlimited amounts of data.
				 * We'll move responsibility for deduplication to the database in this case
				 * so that we don't run the risk of blowing out the memory
				 * in the app server
				 */
				if (nextThreshold == -1) {
					break;
				} else {
					if ((theNumWanted + mySearchEntity.getNumFound()) <= nextThreshold) {
						threshold = (nextThreshold - mySearchEntity.getNumFound()) + 1;
						isLastThreshold = !iterator.hasNext();
						deduplicateInDatabase = false;
						break;
					}
				}
			}
		}

		return new SearchThreshold(threshold, isLastThreshold, deduplicateInDatabase);
	}

	/**
	 * Given a set of search result PIDs, query the database for the associated
	 * <code>_include</code> and <code>_revinclude</code> PIDs, and fetch the
	 * resources associated with those PIDs.
	 * This method populates {@link #myCachedPidsFromMatches} and
	 * {@link #myCachedPidsFromMatchesAndIncludes}.
	 */
	protected void fetchResourcesAndIncludes(
			ISearchBuilder<JpaPid> theSearchBuilder, List<JpaPid> theMatchPids, int theFromIndex, int theToIndex) {

		trimLocalFetchedResourceCache(theMatchPids);
		List<JpaPid> cachedPidsFromMatches = List.copyOf(theMatchPids);

		List<JpaPid> includedPidList = new ArrayList<>();
		if (mySearchEntity.getSearchType() == SearchTypeEnum.SEARCH) {
			Integer remainingIncludesUntilMax = myStorageSettings.getMaximumIncludesToLoadPerPage();

			// Save original search result PIDs — non-iterate `_include` must apply only to initial results, not to
			// `_revinclude` results
			Set<JpaPid> originalPids = new HashSet<>(theMatchPids);

			// Load non-iterate `_revinclude`
			{
				Collection<Include> includes = mySearchEntity.toRevIncludesList(false);
				remainingIncludesUntilMax = fetchRevIncludes(
						theSearchBuilder, theMatchPids, includedPidList, remainingIncludesUntilMax, includes);
			}

			// Load non-iterate `_include` (use originalPids so `_include` only applies to the
			// initial search results, not to revincluded resources — per FHIR spec, without `:iterate`)
			{
				Collection<Include> includes = mySearchEntity.toIncludesList(false);
				SearchBuilderLoadIncludesParameters<JpaPid> parameters =
						createLoadIncludeParameters(originalPids, includes, false, remainingIncludesUntilMax);
				Set<JpaPid> nonIterateIncludedPids = theSearchBuilder.loadIncludes(parameters);
				if (remainingIncludesUntilMax != null) {
					remainingIncludesUntilMax -= nonIterateIncludedPids.size();
				}
				theMatchPids.addAll(nonIterateIncludedPids);
				includedPidList.addAll(nonIterateIncludedPids);
			}

			// Load `_revinclude:iterate`
			{
				Collection<Include> includes = mySearchEntity.toRevIncludesList(true);
				remainingIncludesUntilMax = fetchRevIncludes(
						theSearchBuilder, theMatchPids, includedPidList, remainingIncludesUntilMax, includes);
			}

			// Load `_include:iterate`
			{
				Collection<Include> includes = mySearchEntity.toIncludesList(true);
				SearchBuilderLoadIncludesParameters<JpaPid> parameters =
						createLoadIncludeParameters(theMatchPids, includes, false, remainingIncludesUntilMax);
				Set<JpaPid> iterateIncludedPids = theSearchBuilder.loadIncludes(parameters);
				theMatchPids.addAll(iterateIncludedPids);
				includedPidList.addAll(iterateIncludedPids);
			}
		}

		// Fetch the resource bodies

		List<JpaPid> pidsToFetch;
		if (!myFetchedResources.isEmpty()) {
			pidsToFetch = theMatchPids.stream()
					.filter(p -> !myFetchedResources.containsKey(p))
					.toList();
		} else {
			pidsToFetch = theMatchPids;
		}

		if (!pidsToFetch.isEmpty()) {
			List<IBaseResource> includeResources = new ArrayList<>(pidsToFetch.size());
			theSearchBuilder.loadResourcesByPid(
					pidsToFetch, includedPidList, includeResources, false, myRequestDetails);

			int limit = Math.min(pidsToFetch.size(), includeResources.size());
			for (int i = 0; i < limit; i++) {
				JpaPid pid = pidsToFetch.get(i);
				IBaseResource resource = includeResources.get(i);
				addResourceToLocalCache(pid, resource);
			}
		}

		myCachedPidsFromMatches = new CachedPids(theFromIndex, theToIndex, cachedPidsFromMatches, 0);
		myCachedPidsFromMatchesAndIncludes =
				new CachedPids(theFromIndex, theToIndex, theMatchPids, includedPidList.size());
	}

	private void addResourceToLocalCache(JpaPid thePid, IBaseResource theResource) {
		if (myFetchedResourceLocalCacheMaximumSize != null) {
			Validate.isTrue(
					myFetchedResources.size() < myFetchedResourceLocalCacheMaximumSize,
					"Local fetched resource cache can't exceed size: %s",
					myFetchedResourceLocalCacheMaximumSize);
		}
		myFetchedResources.put(thePid, theResource);
	}

	private Integer fetchRevIncludes(
			ISearchBuilder<JpaPid> theSearchBuilder,
			List<JpaPid> thePids,
			List<JpaPid> theIncludedPidList,
			Integer theMaxIncludes,
			Collection<Include> theIncludes) {
		SearchBuilderLoadIncludesParameters<JpaPid> parameters =
				createLoadIncludeParameters(thePids, theIncludes, true, theMaxIncludes);
		Set<JpaPid> nonIterateRevIncludedPids = theSearchBuilder.loadIncludes(parameters);
		if (theMaxIncludes != null) {
			theMaxIncludes -= nonIterateRevIncludedPids.size();
		}
		thePids.addAll(nonIterateRevIncludedPids);
		theIncludedPidList.addAll(nonIterateRevIncludedPids);
		return theMaxIncludes;
	}

	@Nonnull
	private SearchBuilderLoadIncludesParameters<JpaPid> createLoadIncludeParameters(
			Collection<JpaPid> thePids,
			Collection<Include> theIncludesToLoad,
			boolean theReverse,
			Integer theMaxIncludes) {
		SearchBuilderLoadIncludesParameters<JpaPid> parameters = new SearchBuilderLoadIncludesParameters<>();
		parameters.setFhirContext(myFhirContext);
		parameters.setEntityManager(myEntityManager);
		parameters.setMatches(thePids);
		parameters.setIncludeFilters(theIncludesToLoad);
		parameters.setReverseMode(theReverse);
		parameters.setLastUpdated(mySearchEntity.getLastUpdated());
		parameters.setSearchIdOrDescription(mySearchEntity.getUuid());
		parameters.setRequestDetails(myRequestDetails);
		parameters.setMaxCount(theMaxIncludes);
		return parameters;
	}

	private void updateSearchExpiryIfNecessary() {
		// The created time may be null in some unit tests
		if (mySearchEntity.getCreated() != null) {
			// start tracking last-access-time for this search when it is more than halfway to expire by created time
			// we do this to avoid generating excessive write traffic on busy cached searches.
			long expireAfterMillis = myStorageSettings.getExpireSearchResultsAfterMillis();
			long createdCutoff = mySearchEntity.getCreated().getTime() + expireAfterMillis;
			if (createdCutoff - System.currentTimeMillis() < expireAfterMillis / 2) {
				mySearchEntity.setExpiryOrNull(DateUtils.addMinutes(new Date(), SEARCH_EXPIRY_OFFSET_MINUTES));
			}
		}
	}

	/**
	 * Subclasses should return a search entity that will be used to satisfy the search request.
	 * This method will be called once per database transaction, from within an existing
	 * transaction.
	 */
	protected abstract Search provideSearchEntity();

	/**
	 * Extract the {@link SearchParameterMap} from the search entity, and throw
	 * an exception if it is not present (which should never happen and would be a bug).
	 */
	@Nonnull
	private SearchParameterMap extractSearchParameterMapFromSearchEntity() {
		return mySearchEntity
				.getSearchParameterMap()
				.orElseThrow(() -> new IllegalStateException(
						Msg.code(3021) + "Search entity eas stored without a SearchParameterMap"));
	}

	/**
	 * Validate that the search entity has not failed and throw an exception if it has.
	 * The specific exception will depend on the {@link Search#getFailureCode() failure code}
	 * in the search entity.
	 */
	private void validateSearchEntityNotFailed() {
		if (mySearchEntity != null) {
			QueryParameterUtils.verifySearchHasntFailedOrThrowInternalErrorException(mySearchEntity);
		}
	}

	private ISearchBuilder<JpaPid> newSearchBuilder() {
		Class<? extends IBaseResource> resourceType = myFhirContext
				.getResourceDefinition(mySearchEntity.getResourceType())
				.getImplementingClass();
		return mySearchBuilderFactory.newSearchBuilder(mySearchEntity.getResourceType(), resourceType);
	}

	protected boolean hasLoadedSearchEntity() {
		return mySearchEntity != null;
	}

	protected Search provideLoadedSearchEntity() {
		return mySearchEntity;
	}

	public void setRequestPartitionId(RequestPartitionId theRequestPartitionId) {
		myRequestPartitionId = theRequestPartitionId;
	}

	@VisibleForTesting
	int getFetchedResourceCacheSize() {
		return myFetchedResources.size();
	}

	@VisibleForTesting
	void setFetchedResourceLocalCacheMaximumSize(int theFetchedResourceLocalCacheMaximumSize) {
		myFetchedResourceLocalCacheMaximumSize = theFetchedResourceLocalCacheMaximumSize;
	}

	private record SearchThreshold(
			@Nullable Integer threshold, boolean isLastThreshold, boolean deduplicateInDatabase) {
		@Nonnull
		@Override
		public String toString() {
			if (isLastThreshold()) {
				return "LastThreshold[" + threshold() + "]";
			}
			return "Threshold[" + threshold() + "]";
		}
	}

	private class PidConsumer implements ISearchResultConsumer<JpaPid> {
		private final List<JpaPid> myNewPidsThisPass;
		private final IntCounter myNumToSkip;
		private final List<JpaPid> myPidsToReturn;
		private final SearchThreshold mySearchThreshold;
		private final ISearchBuilder<JpaPid> mySearchBuilder;
		private final IntCounter myNumToRetainInLocalCache;

		private int myCountFoundThisPass;
		private int myCountBlockedThisPass;

		public PidConsumer(
				int theNumWantedThisPass,
				List<JpaPid> theNewPidsThisPass,
				IntCounter theNumToSkip,
				List<JpaPid> thePidsToReturn,
				SearchThreshold theSearchThreshold,
				ISearchBuilder<JpaPid> theSearchBuilder) {
			myNumToRetainInLocalCache = new IntCounter(theNumWantedThisPass);
			myNewPidsThisPass = theNewPidsThisPass;
			myNumToSkip = theNumToSkip;
			myPidsToReturn = thePidsToReturn;
			mySearchThreshold = theSearchThreshold;
			mySearchBuilder = theSearchBuilder;
		}

		@Nonnull
		@Override
		public Outcome consume(SearchProgressTracker theProgressTracker, JpaPid theResult) {
			myNewPidsThisPass.add(theResult);
			myCountFoundThisPass++;

			if (myNumToSkip.get() == 0) {
				myPidsToReturn.add(theResult);
			} else {
				myNumToSkip.decrement(1);
			}

			if (mySearchThreshold.threshold() != null && myNewPidsThisPass.size() == mySearchThreshold.threshold()) {
				return ISearchResultConsumer.STOP;
			}

			return ISearchResultConsumer.CONTINUE;
		}

		@Override
		public void consumptionComplete() {

			// Interceptor call: STORAGE_PREACCESS_RESOURCES

			// This can be used to remove results from the search result details before
			// the user has a chance to know that they were in the results. We work in
			// small batches to avoid loading too many resources into memory at once.
			if (havePreAccessHooks() && !myNewPidsThisPass.isEmpty()) {
				TaskChunker.chunk(myNewPidsThisPass, 100, chunk -> {
					Set<JpaPid> blockedPids = new HashSet<>();

					ArrayList<IBaseResource> newResources = new ArrayList<>();
					mySearchBuilder.loadResourcesByPid(chunk, List.of(), newResources, false, myRequestDetails);
					JpaPreResourceAccessDetails accessDetails = new JpaPreResourceAccessDetails(chunk, newResources);
					HookParams params = new HookParams()
							.add(IPreResourceAccessDetails.class, accessDetails)
							.add(RequestDetails.class, myRequestDetails)
							.addIfMatchesType(ServletRequestDetails.class, myRequestDetails);
					myCompositeBroadcaster.callHooks(Pointcut.STORAGE_PREACCESS_RESOURCES, params);

					for (int i = chunk.size() - 1; i >= 0; i--) {
						if (accessDetails.isDontReturnResourceAtIndex(i)) {
							JpaPid blockedPid = chunk.remove(i);
							newResources.remove(i);
							blockedPids.add(blockedPid);
							myCountFoundThisPass--;
							myCountBlockedThisPass++;
						}
					}

					if (!blockedPids.isEmpty()) {
						myPidsToReturn.removeIf(blockedPids::contains);
					}

					for (int i = 0; i < chunk.size() && myNumToRetainInLocalCache.get() > 0; i++) {
						JpaPid pid = chunk.get(i);
						IBaseResource resource = newResources.get(i);
						addResourceToLocalCache(pid, resource);
						myNumToRetainInLocalCache.decrement();
					}
				});
			}
		}

		public int getBlockedCount() {
			return myCountBlockedThisPass;
		}

		public int getFoundCount() {
			return myCountFoundThisPass;
		}
	}

	private boolean havePreAccessHooks() {
		return myCompositeBroadcaster.hasHooks(Pointcut.STORAGE_PREACCESS_RESOURCES);
	}

	/**
	 * @param fromIndex The from index (includes) corresponding to these PIDs
	 * @param toIndex The to index (exclusive) corresponding to these PIDs
	 * @param pids The PIDs themselves
	 * @param includedResourceCount How many of the PIDs are present because they were pulled into the page by {@literal _include} or {@literal _revinclude} parameters
	 */
	private record CachedPids(int fromIndex, int toIndex, List<JpaPid> pids, int includedResourceCount) {
		public int size() {
			return pids.size();
		}
	}
}
