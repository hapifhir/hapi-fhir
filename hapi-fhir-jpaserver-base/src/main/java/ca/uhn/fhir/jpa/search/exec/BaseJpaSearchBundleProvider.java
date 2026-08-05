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
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.interceptor.ServerInterceptorUtil;
import ca.uhn.fhir.rest.server.method.ResponsePage;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.util.IntCounter;
import ca.uhn.fhir.util.SleepUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.jetbrains.annotations.NotNull;
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
import java.util.Set;

/**
 * This class handles search results for <b>FHIR Search</b> operations leveraging the {@link ISearchCacheSvc search cache}. It has 2 concrete subclasses:
 * <ul>
 *     <li>{@link JpaSearchBundleProviderFirstPage}</li> is used for the initial search
 *     <li>{@link JpaSearchBundleProviderSubsequentPage}</li> is used for the following page loads from the query cache
 * </ul>
 *
 * @see ISynchronousSearchSvc The Synchronous Search Service is used instead of this class for searches which don't use the search cache
 * @see ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider The search result for <b>FHIR History</b> operations.
 * @since 8.14.0
 */
public abstract class BaseJpaSearchBundleProvider implements IBundleProvider {

	public static final SearchCacheStatus CACHE_STATUS_BYPASS = SearchCacheStatus.builder()
			.withCacheName("HapiFhirQueryCache")
			.setStatus(SearchCacheStatus.SearchCacheStatusEnum.FWD_BYPASS)
			.build();
	private static final int SEARCH_EXPIRY_OFFSET_MINUTES = 10;
	private static final Logger ourLog = LoggerFactory.getLogger(BaseJpaSearchBundleProvider.class);

	/**
	 * Adjust this to raise the level of the debuga logs if you are
	 * troubleshooting something.
	 */
	// FIXME: lower level
	private static final Level DEBUG_LOG_LEVEL = Level.INFO;
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

	private Search mySearchEntity;
	private CachedPids myCachedPidsFromMatches;
	private CachedPids myCachedPidsFromMatchesAndIncludes;

	/**
	 * Constructor
	 */
	protected BaseJpaSearchBundleProvider(
			FhirContext theFhirContext,
			SearchParameterMap theParams,
			RequestDetails theRequestDetails,
			RequestPartitionId theRequestPartitionId,
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
		this(
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
				theSearchBuilderFactory);
		myParams = theParams;
		myRequestPartitionId = theRequestPartitionId;
	}

	/**
	 * Constructor
	 */
	@SuppressWarnings("unchecked")
	protected BaseJpaSearchBundleProvider(
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
			SearchBuilderFactory<JpaPid> theSearchBuilderFactory) {
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
	}

	@Override
	public IPrimitiveType<Date> getPublished() {
		ensureSearchPerformed();
		IPrimitiveType<Date> retVal = myInstantDefinition.newInstance();
		retVal.setValue(mySearchEntity.getCreated());
		return retVal;
	}

	@Nullable
	@Override
	public String getUuid() {
		ensureSearchPerformed();
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
			ensureSearchPerformed(from, to);
			return mySearchEntity.getTotalCount();
		}
		return null;
	}

	@Override
	public List<IBaseResource> getResources(
			int theFromIndex, int theToIndex, @Nonnull ResponsePage.ResponsePageBuilder theResponsePageBuilder) {
		ensureSearchPerformed(theFromIndex, theToIndex);

		List<IBaseResource> retVal = new ArrayList<>();
		for (JpaPid nextPid : myCachedPidsFromMatchesAndIncludes.pids()) {
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
		theResponsePageBuilder.setOmittedResourceCount(precount - retVal.size());
		theResponsePageBuilder.setResources(retVal);
		theResponsePageBuilder.setIncludedResourceCount(retVal.size());
		theResponsePageBuilder.setTotalRequestedResourcesFetched(mySearchEntity.getNumFound());
		theResponsePageBuilder.setHasNextPage(
				theToIndex < mySearchEntity.getNumFound() || mySearchEntity.getStatus() == SearchStatusEnum.PASSCMPLET);

		ourLog.atLevel(DEBUG_LOG_LEVEL)
			.setMessage("Returning {} results for range {}-{}")
			.addArgument(retVal.size())
			.addArgument(theFromIndex)
			.addArgument(theToIndex)
			.log();

		return retVal;
	}

	/**
	 * This is really only intended for unit tests, the regular search paths don't call this methos
	 */
	@Nonnull
	@Override
	public List<IBaseResource> getAllResources() {
		List<IBaseResource> resources = getResources(0, 10000);
		Validate.isTrue(
				resources.size() < 10000, "Can not call getAllResources on a collection of more than 10000 resources");
		return resources;
	}

	protected void fetchResourcesAndIncludes(
			ISearchBuilder<JpaPid> theSearchBuilder, List<JpaPid> thePids, int theFromIndex, int theToIndex) {

		List<JpaPid> cachedPidsFromMatches = List.copyOf(thePids);

		List<JpaPid> includedPidList = new ArrayList<>();
		if (mySearchEntity.getSearchType() == SearchTypeEnum.SEARCH) {
			Integer remainingIncludesUntilMax = myStorageSettings.getMaximumIncludesToLoadPerPage();

			// Save original search result PIDs — non-iterate `_include` must apply only to initial results, not to
			// `_revinclude` results
			Set<JpaPid> originalPids = new HashSet<>(thePids);

			// Load non-iterate `_revinclude`
			{
				Collection<Include> includes = mySearchEntity.toRevIncludesList(false);
				remainingIncludesUntilMax = fetchRevIncludes(
						theSearchBuilder, thePids, includedPidList, remainingIncludesUntilMax, includes);
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
				thePids.addAll(nonIterateIncludedPids);
				includedPidList.addAll(nonIterateIncludedPids);
			}

			// Load `_revinclude:iterate`
			{
				Collection<Include> includes = mySearchEntity.toRevIncludesList(true);
				remainingIncludesUntilMax = fetchRevIncludes(
						theSearchBuilder, thePids, includedPidList, remainingIncludesUntilMax, includes);
			}

			// Load `_include:iterate`
			{
				Collection<Include> includes = mySearchEntity.toIncludesList(true);
				SearchBuilderLoadIncludesParameters<JpaPid> parameters =
						createLoadIncludeParameters(thePids, includes, false, remainingIncludesUntilMax);
				Set<JpaPid> iterateIncludedPids = theSearchBuilder.loadIncludes(parameters);
				thePids.addAll(iterateIncludedPids);
				includedPidList.addAll(iterateIncludedPids);
			}
		}

		// Fetch the resource bodies

		List<JpaPid> pidsToFetch;
		if (!myFetchedResources.isEmpty()) {
			pidsToFetch = thePids.stream()
					.filter(p -> !myFetchedResources.containsKey(p))
					.toList();
		} else {
			pidsToFetch = thePids;
		}

		if (!pidsToFetch.isEmpty()) {
			List<IBaseResource> includeResources = new ArrayList<>(pidsToFetch.size());
			theSearchBuilder.loadResourcesByPid(
					pidsToFetch, includedPidList, includeResources, false, myRequestDetails);

			int limit = Math.min(pidsToFetch.size(), includeResources.size());
			for (int i = 0; i < limit; i++) {
				JpaPid pid = pidsToFetch.get(i);
				IBaseResource resource = includeResources.get(i);
				myFetchedResources.put(pid, resource);
			}
		}

		myCachedPidsFromMatches = new CachedPids(theFromIndex, theToIndex, cachedPidsFromMatches);
		myCachedPidsFromMatchesAndIncludes = new CachedPids(theFromIndex, theToIndex, thePids);
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

	/**
	 * Initializes the search, assuming that the first search threshold is the
	 * desired number of resources if the search hasn't already been initialized.
	 * This should only happen if a consumer of the {@link IBundleProvider} calls
	 * a method other than {@link #getResources(int, int, ResponsePage.ResponsePageBuilder)}
	 * first, which won't happen during normal search scenarios.
	 */
	protected void ensureSearchPerformed() {
		if (myCachedPidsFromMatches == null) {
			int to;
			if (myParams != null && myParams.getCount() != null) {
				to = myParams.getCount();
			} else {
				to = myStorageSettings.getSearchPreFetchThresholds().get(0);
			}
			ensureSearchPerformed(0, to);
		}
		validateSearchEntityNotFailed();
	}

	/**
	 * Prepare to return resources for the search result range from {@literal theFromIndex} to
	 * {@literal theToIndex}.
	 *
	 * @param theFromIndex The start of the search range (inclusive)
	 * @param theToIndex   The end of the search range (exclusive)
	 */
	private void ensureSearchPerformed(int theFromIndex, int theToIndex) {

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
					return;
				}

				/*
				 * If we've finished the search, and the currently fetched range has the same
				 * start as the one we want, and the end exceeds the end of the fetched results,
				 * then the fetched range is fine to return.
				 */
				if (mySearchEntity != null
						&& mySearchEntity.getStatus() == SearchStatusEnum.FINISHED
						&& mySearchEntity.getTotalCount() != null
						&& mySearchEntity.getTotalCount() <= theToIndex
						&& theToIndex <= myCachedPidsFromMatchesAndIncludes.toIndex()) {
					return;
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
					myCachedPidsFromMatchesAndIncludes = new CachedPids(theFromIndex, theToIndex, rangePids);
					return;
				}
			}
		}

		/*
		 * If we only want to fetch the count, and we already know it, there is nothing else to do.
		 */
		if (myParams != null && mySearchEntity != null && SearchParameterMapCalculator.isWantOnlyCount(myParams)) {
			if (mySearchEntity.getTotalCount() != null) {
				myCachedPidsFromMatchesAndIncludes = new CachedPids(theFromIndex, theToIndex, List.of());
				return;
			}
		}

		/*
		 * Ok, so we need to actually open a database transaction and perform the search. We do this in a loop to
		 * defend against the case where multiple client threads are all fetching new pages of search results
		 * concurrently (since they will all try to write new pages of results to the query cache, and only
		 * one will succeed while the others fail with a constraint error.
		 */
		for (int i = 0; ; i++) {
			try {
				myTxService
						.withRequest(myRequestDetails)
						.withRequestPartitionId(myRequestPartitionId)
						.execute(() -> ensureSearchPerformedInsideTransaction(theFromIndex, theToIndex));
				break;
			} catch (ResourceVersionConflictException e) {
				// We failed to write to the query cache. We'll retry the search a few times since presumably
				// some other thread is also performing the search. If the error just keeps happening, we must
				// have a deeper problem so we should bail.
				if (i == 5) {
					throw e;
				}
				ourLog.warn("Constraint error while writing search results to query cache: {}", e.toString());
				new SleepUtil().sleepAtLeast(500, false);
			} catch (ResourceGoneException e) {
				ourLog.info("Aborted search: {}", e.getMessage());
				throw e;
			} catch (UnexpectedRollbackException e) {
				validateSearchEntityNotFailed();
				throw e;
			}
		}

		validateSearchEntityNotFailed();
	}

	/**
	 *
	 * @param theFromIndex The start of the search range (inclusive)
	 * @param theToIndex   The end of the search range (exclusive)
	 */
	private void ensureSearchPerformedInsideTransaction(int theFromIndex, int theToIndex) {
		ourLog.atLevel(DEBUG_LOG_LEVEL)
			.setMessage("About to ensure search performed for results {}-{}")
			.addArgument(theFromIndex)
			.addArgument(theToIndex)
			.log();

		final List<JpaPid> pidsToReturn = new ArrayList<>();

		mySearchEntity = provideSearchEntity();
		if (myParams == null) {
			myParams = extractSearchParameterMapFromSearchEntity();
		}

		/// If we have a `_count=summary` query, just calculate the count and return
		if (SearchParameterMapCalculator.isWantOnlyCount(myParams)) {
			if (mySearchEntity.getTotalCount() == null) {
				Long countQuery = newSearchBuilder()
						.createCountQuery(myParams, mySearchEntity.getUuid(), myRequestDetails, myRequestPartitionId);
				mySearchEntity.setSearchParameterMap(myParams);
				mySearchEntity.setTotalCount(Math.toIntExact(countQuery));
				mySearchEntity.setStatus(SearchStatusEnum.FINISHED);
				mySearchCacheSvc.save(mySearchEntity, myRequestPartitionId);
			}
			myCachedPidsFromMatchesAndIncludes = new CachedPids(theFromIndex, theToIndex, List.of());
			return;
		}

		/*
		 * If the previously found search is either finished, or has already found enough results to
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
			SearchThreshold searchThreshold = calculateNextSearchThreshold(numWanted + lastSkipCount, searchBuilder);
			searchBuilder.setMaxResultsToFetch(searchThreshold.threshold());
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
					myRequestDetails,
					newPidsThisPass,
					numToSkip,
					pidsToReturn,
					searchThreshold,
					searchBuilder,
					myCompositeBroadcaster,
					myFetchedResources);
			SearchProgressTracker outcome;
			try {
				outcome = searchBuilder.performSearchForPids(
						consumer, myParams, searchDetails, myRequestDetails, myRequestPartitionId);
				consumer.firePreAccessHooksOnNewPids();
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

			// FIXME: remove
//			if (consumer.getBlockedCount() == 0 || mySearchEntity.getNumFound() >= theToIndex) {
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
		while (pidsToReturn.size() > numberToReturn) {
			pidsToReturn.remove(pidsToReturn.size() - 1);
		}

		fetchResourcesAndIncludes(searchBuilder, pidsToReturn, theFromIndex, theToIndex);
	}

	@Nullable
	@Override
	public SearchCacheStatus getCacheStatus() {
		ensureSearchPerformed();
		return myCacheStatus;
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

	protected abstract Search provideSearchEntity();

	@Nonnull
	private SearchParameterMap extractSearchParameterMapFromSearchEntity() {
		return mySearchEntity
				.getSearchParameterMap()
				.orElseThrow(() -> new IllegalStateException(
						Msg.code(3021) + "Search entity eas stored without a SearchParameterMap"));
	}

	private void validateSearchEntityNotFailed() {
		if (mySearchEntity != null) {
			QueryParameterUtils.verifySearchHasntFailedOrThrowInternalErrorException(mySearchEntity);
		}
	}

	@Nonnull
	private SearchThreshold calculateNextSearchThreshold(int theNumWanted, ISearchBuilder<JpaPid> searchBuilder) {
		List<Integer> thresholds = myStorageSettings.getSearchPreFetchThresholds();
		int firstThreshold = thresholds.get(0);
		int lastThreshold = thresholds.get(thresholds.size() - 1);

		/*
		 * If we're searching for the first page of results:
		 *
		 * - If the requested count is greater than the first threshold, just search
		 *   for exactly the requested count instead of advancing to the next threshold.
		 * - If the requested count is greater than the last threshold, just search
		 *   for exactly the last threshold, and don't exceed it.
		 */
		boolean firstSearch = mySearchEntity.getNumFound() == 0 && mySearchEntity.getNumBlocked() == 0;
		if (firstSearch) {
			if (theNumWanted > firstThreshold) {
				if (lastThreshold > 0 && theNumWanted > lastThreshold) {
					return new SearchThreshold(lastThreshold, true);
				}
				return new SearchThreshold(theNumWanted + 1, false);
			}
		}

		// For subsequent pages, we'll use the predetermined search thresholds
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
					int numToFetch = (nextThreshold - mySearchEntity.getNumFound()) + 1;
					boolean isLastThreshold = !iterator.hasNext();
					return new SearchThreshold(numToFetch, isLastThreshold);
				}
			}
		}

		searchBuilder.setDeduplicateInDatabase(true);
		return new SearchThreshold(null, true);
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

	private record SearchThreshold(@Nullable Integer threshold, boolean isLastThreshold) {
		@Override
		public @NotNull String toString() {
			if (isLastThreshold()) {
				return "LastThreshold[" + threshold() + "]";
			}
			return "Threshold[" + threshold() + "]";
		}
	}

	private static class PidConsumer implements ISearchResultConsumer<JpaPid> {
		private final List<JpaPid> myNewPidsThisPass;
		private final IntCounter myNumToSkip;
		private final List<JpaPid> myPidsToReturn;
		private final SearchThreshold mySearchThreshold;
		private final ISearchBuilder<JpaPid> mySearchBuilder;
		private final IInterceptorBroadcaster myCompositeBroadcaster;
		private final RequestDetails myRequestDetails;
		private final Map<JpaPid, IBaseResource> myFetchedResources;

		private int myCountFoundThisPass;
		private int myCountBlockedThisPass;

		public PidConsumer(
				RequestDetails theRequestDetails,
				List<JpaPid> theNewPidsThisPass,
				IntCounter theNumToSkip,
				List<JpaPid> thePidsToReturn,
				SearchThreshold theSearchThreshold,
				ISearchBuilder<JpaPid> theSearchBuilder,
				IInterceptorBroadcaster theCompositeBroadcaster,
				Map<JpaPid, IBaseResource> theFetchedResources) {
			myRequestDetails = theRequestDetails;
			myNewPidsThisPass = theNewPidsThisPass;
			myNumToSkip = theNumToSkip;
			myPidsToReturn = thePidsToReturn;
			mySearchThreshold = theSearchThreshold;
			mySearchBuilder = theSearchBuilder;
			myCompositeBroadcaster = theCompositeBroadcaster;
			myFetchedResources = theFetchedResources;
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

		public void firePreAccessHooksOnNewPids() {

			// Interceptor call: STORAGE_PREACCESS_RESOURCES
			// This can be used to remove results from the search result details before
			// the user has a chance to know that they were in the results
			if (myCompositeBroadcaster.hasHooks(Pointcut.STORAGE_PREACCESS_RESOURCES) && !myNewPidsThisPass.isEmpty()) {
				Set<JpaPid> blockedPids = new HashSet<>();

				List<IBaseResource> newResources =
						mySearchBuilder.loadResourcesByPid(myNewPidsThisPass, myRequestDetails);
				JpaPreResourceAccessDetails accessDetails =
						new JpaPreResourceAccessDetails(myNewPidsThisPass, newResources);
				HookParams params = new HookParams()
						.add(IPreResourceAccessDetails.class, accessDetails)
						.add(RequestDetails.class, myRequestDetails)
						.addIfMatchesType(ServletRequestDetails.class, myRequestDetails);
				myCompositeBroadcaster.callHooks(Pointcut.STORAGE_PREACCESS_RESOURCES, params);

				for (int i = myNewPidsThisPass.size() - 1; i >= 0; i--) {
					if (accessDetails.isDontReturnResourceAtIndex(i)) {
						JpaPid blockedPid = myNewPidsThisPass.remove(i);
						newResources.remove(i);
						blockedPids.add(blockedPid);
						myCountFoundThisPass--;
						myCountBlockedThisPass++;
					}
				}

				if (!blockedPids.isEmpty()) {
					myPidsToReturn.removeIf(blockedPids::contains);
				}

				for (int i = 0; i < myNewPidsThisPass.size(); i++) {
					JpaPid pid = myNewPidsThisPass.get(i);
					IBaseResource resource = newResources.get(i);
					myFetchedResources.put(pid, resource);
				}
			}
		}

		public int getBlockedCount() {
			return myCountBlockedThisPass;
		}

		public int getFoundCount() {
			return myCountFoundThisPass;
		}
	}

	private record CachedPids(int fromIndex, int toIndex, List<JpaPid> pids) {
		public int size() {
			return pids.size();
		}
	}
}
