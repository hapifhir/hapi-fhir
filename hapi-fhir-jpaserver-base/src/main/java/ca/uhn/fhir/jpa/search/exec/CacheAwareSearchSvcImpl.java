package ca.uhn.fhir.jpa.search.exec;

import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
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
import ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.interceptor.ServerInterceptorUtil;
import ca.uhn.fhir.rest.server.method.ResponsePage;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.util.SleepUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.UnexpectedRollbackException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CacheAwareSearchSvcImpl implements ICacheAwareSearchSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(CacheAwareSearchSvcImpl.class);

	@Autowired
	private ExceptionService myExceptionSvc;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IHapiTransactionService myTxService;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private ISearchCacheSvc mySearchCacheSvc;

	@Autowired
	private ISearchResultCacheSvc mySearchResultCacheSvc;

	@Autowired
	private EntityManager myEntityManager;

	@Autowired
	private SearchBuilderFactory<JpaPid> mySearchBuilderFactory;

	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Autowired
	private IPagingProvider myPagingProvider;

	/**
	 * Constructor
	 */
	public CacheAwareSearchSvcImpl() {
		super();
	}

	/**
	 * Unit test constructor
	 */
	public CacheAwareSearchSvcImpl(
			FhirContext theFhirContext,
			IHapiTransactionService theTxService,
			JpaStorageSettings theStorageSettings,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			ISearchCacheSvc theSearchCacheSvc,
			ISearchResultCacheSvc theSearchResultCacheSvc,
			EntityManager theEntityManager,
			SearchBuilderFactory<JpaPid> theSearchBuilderFactory,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myFhirContext = theFhirContext;
		myTxService = theTxService;
		myStorageSettings = theStorageSettings;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
		mySearchCacheSvc = theSearchCacheSvc;
		mySearchResultCacheSvc = theSearchResultCacheSvc;
		myEntityManager = theEntityManager;
		mySearchBuilderFactory = theSearchBuilderFactory;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
	}

	@Override
	public IBundleProvider createNewSearch(
			SearchParameterMap theParams,
			RequestDetails theRequestDetails,
			CacheControlDirective theCacheControlDirective,
			Search theSearchEntity,
			ISearchBuilder<JpaPid> theSearchBuilder,
			RequestPartitionId theRequestPartitionId) {
		return new JpaBundleProvider(
				myFhirContext,
				theParams,
				theRequestDetails,
				theCacheControlDirective,
				theRequestPartitionId,
				theSearchEntity,
				myInterceptorBroadcaster,
				myPagingProvider,
				myStorageSettings,
				myEntityManager,
				myTxService,
				myRequestPartitionHelperSvc,
				mySearchCacheSvc,
				mySearchResultCacheSvc,
				myExceptionSvc,
				mySearchBuilderFactory);
	}

	@Override
	public IBundleProvider continueExistingSearch(String theId, RequestDetails theRequestDetails) {
		return new JpaBundleProvider(
				myFhirContext,
				theRequestDetails,
				theId,
				myInterceptorBroadcaster,
				myPagingProvider,
				myStorageSettings,
				myEntityManager,
				myTxService,
				myRequestPartitionHelperSvc,
				mySearchCacheSvc,
				mySearchResultCacheSvc,
				myExceptionSvc,
				mySearchBuilderFactory);
	}

	public static class JpaBundleProvider implements IBundleProvider {
		private final Map<JpaPid, IBaseResource> myFetchedResources = new HashMap<>();
		private final RequestDetails myRequestDetails;
		private final IInterceptorBroadcaster myCompositeBroadcaster;
		private final FhirContext myFhirContext;
		private final IPagingProvider myPagingProvider;
		private final JpaStorageSettings myStorageSettings;
		private final EntityManager myEntityManager;
		private final IHapiTransactionService myTxService;
		private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
		private final ISearchCacheSvc mySearchCacheSvc;
		private final ISearchResultCacheSvc mySearchResultCacheSvc;
		private final ExceptionService myExceptionService;
		private final SearchBuilderFactory<JpaPid> mySearchBuilderFactory;
		private final BaseRuntimeElementDefinition<IPrimitiveType<Date>> myInstantDefinition;
		private SearchParameterMap myParams;
		private RequestPartitionId myRequestPartitionId;
		private CacheControlDirective myCacheControlDirective;
		private String mySearchUuid;
		private Search mySearchEntity;
		private List<JpaPid> myCachedPidsFromMatches;
		private List<JpaPid> myCachedPidsFromMatchesAndIncludes;
		private Integer myCachedPidsFromMatchesStartingIndex;
		private Integer myCachedPidsFromMatchesEndingIndex;
		private Integer myCachedPidsFromMatchesAndIncludesStartingIndex;
		private Integer myCachedPidsFromMatchesAndIncludesEndingIndex;
		private SearchCacheStatus myCacheStatus;

		/**
		 * Constructor for a new (first page) search
		 */
		public JpaBundleProvider(
				FhirContext theFhirContext,
				SearchParameterMap theParams,
				RequestDetails theRequestDetails,
				CacheControlDirective theCacheControlDirective,
				RequestPartitionId theRequestPartitionId,
				Search theSearchEntity,
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
					theSearchEntity.getUuid(),
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
			myCacheControlDirective = theCacheControlDirective;
			myRequestPartitionId = theRequestPartitionId;
			mySearchEntity = theSearchEntity;
		}

		/**
		 * Constructor for a pre-existing (i.e. subsequent page) search by UUID
		 */
		@SuppressWarnings("unchecked")
		public JpaBundleProvider(
				FhirContext theFhirContext,
				RequestDetails theRequestDetails,
				String theSearchUuid,
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
			mySearchUuid = theSearchUuid;
			myStorageSettings = theStorageSettings;
			myEntityManager = theEntityManager;
			myTxService = theTxService;
			myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
			mySearchCacheSvc = theSearchCacheSvc;
			mySearchResultCacheSvc = theSearchResultCacheSvc;
			myExceptionService = theExceptionService;
			mySearchBuilderFactory = theSearchBuilderFactory;
			myCompositeBroadcaster = CompositeInterceptorBroadcaster.newCompositeBroadcaster(
					theInterceptorBroadcaster, myRequestDetails);
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
			if (mySearchEntity == null) {
				return mySearchUuid;
			}
			return mySearchEntity.getUuid();
		}

		@Override
		public Integer preferredPageSize() {
			if (mySearchEntity != null) {
				return mySearchEntity.getPreferredPageSize();
			}
			return null;
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
			for (JpaPid nextPid : myCachedPidsFromMatchesAndIncludes) {
				retVal.add(myFetchedResources.get(nextPid));
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

			return retVal;
		}

		@Nonnull
		@Override
		public List<IBaseResource> getAllResources() {
			List<IBaseResource> resources = getResources(0, 10000);
			Validate.isTrue(
					resources.size() < 10000,
					"Can not call getAllResources on a collection of more than 10000 resources");
			return resources;
		}

		protected void fetchResourcesAndIncludes(
				ISearchBuilder<JpaPid> theSearchBuilder, List<JpaPid> thePids, int theFromIndex, int theToIndex) {

			myCachedPidsFromMatches = List.copyOf(thePids);
			myCachedPidsFromMatchesStartingIndex = theFromIndex;
			myCachedPidsFromMatchesEndingIndex = theToIndex;

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

			myCachedPidsFromMatchesAndIncludes = thePids;
			myCachedPidsFromMatchesAndIncludesStartingIndex = theFromIndex;
			myCachedPidsFromMatchesAndIncludesEndingIndex = theToIndex;
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
		private void ensureSearchPerformed() {
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

		private void ensureSearchPerformed(int theFromIndex, int theToIndex) {

			if (myCachedPidsFromMatchesAndIncludes != null) {
				if (myCachedPidsFromMatchesAndIncludesStartingIndex == theFromIndex) {
					if (myCachedPidsFromMatchesAndIncludesEndingIndex == theToIndex) {
						return;
					}
					if (mySearchEntity != null
							&& mySearchEntity.getStatus() == SearchStatusEnum.FINISHED
							&& mySearchEntity.getTotalCount() != null
							&& mySearchEntity.getTotalCount() <= theToIndex
							&& theToIndex < myCachedPidsFromMatchesAndIncludesEndingIndex) {
						return;
					}
				}
			}

			if (myCachedPidsFromMatches != null) {
				/*
				 * If we don't have any _include or _revinclude parameters, and we've already
				 * fetched a collection of matched resources for a window equivalent to or
				 * exceeding the window we want, we can just return that window.
				 */
				if (myParams.getIncludes().isEmpty()
						&& myParams.getRevIncludes().isEmpty()) {
					if (myCachedPidsFromMatchesStartingIndex <= theFromIndex
							&& myCachedPidsFromMatchesEndingIndex >= theToIndex) {
						int rangeStart = theFromIndex - myCachedPidsFromMatchesStartingIndex;
						int rangeEnd = (theToIndex - theFromIndex) + rangeStart;
						rangeEnd = Math.min(rangeEnd, myCachedPidsFromMatches.size());
						myCachedPidsFromMatchesAndIncludes = myCachedPidsFromMatches.subList(rangeStart, rangeEnd);
						myCachedPidsFromMatchesAndIncludesStartingIndex = theFromIndex;
						myCachedPidsFromMatchesAndIncludesEndingIndex = theToIndex;
						return;
					}
				}
			}

			if (myParams != null && mySearchEntity != null && myParams.getSummaryMode() == SummaryEnum.COUNT) {
				if (mySearchEntity.getTotalCount() != null) {
					myCachedPidsFromMatchesAndIncludes = List.of();
					myCachedPidsFromMatchesAndIncludesStartingIndex = theFromIndex;
					myCachedPidsFromMatchesAndIncludesEndingIndex = theToIndex;
					return;
				}
			}

			for (int i = 0; ; i++) {
				try {
					myTxService
							.withRequest(myRequestDetails)
							.withRequestPartitionId(myRequestPartitionId)
							.execute(() -> ensureSearchPerformedInsideTransaction(theFromIndex, theToIndex));
					break;
				} catch (ResourceGoneException e) {
					ourLog.info("Attempting to access search with unknown UUID: {}", mySearchUuid);
					throw e;
				} catch (UnexpectedRollbackException e) {
					validateSearchEntityNotFailed();
					throw e;
				} catch (ResourceVersionConflictException e) {
					// FIXME: is this the right exception?
					if (i == 5) {
						throw e;
					}
					ourLog.warn("Constraint error while writing search results to query cache: {}", e.toString());
					new SleepUtil().sleepAtLeast(500, false);
					resetState();
				}
			}

			validateSearchEntityNotFailed();
		}

		private void resetState() {
			if (mySearchEntity != null && mySearchEntity.getId() != null) {
				mySearchUuid = mySearchEntity.getUuid();
				mySearchEntity = null;
				myFetchedResources.clear();
			}
		}

		@Nullable
		@Override
		public SearchCacheStatus getCacheStatus() {
			ensureSearchPerformed();
			return myCacheStatus;
		}

		private void ensureSearchPerformedInsideTransaction(int theFromIndex, int theToIndex) {
			List<JpaPid> pidsToReturn = new ArrayList<>();
			boolean haveMoreResults = false;
			boolean addedResultsThisPass = false;
			boolean initialSearch = false;

			SearchCacheStatusEnum cacheStatus;
			if (myCacheControlDirective != null && myCacheControlDirective.isNoCache()) {
				cacheStatus = SearchCacheStatusEnum.NOT_TRIED;
			} else {
				cacheStatus = SearchCacheStatusEnum.MISS;
			}

			if (mySearchEntity == null) {

				ReadPartitionIdRequestDetails details = ReadPartitionIdRequestDetails.forSearchUuid(mySearchUuid);
				myRequestPartitionId =
						myRequestPartitionHelperSvc.determineReadPartitionForRequest(myRequestDetails, details);

				// FIXME: make debug
				ourLog.info("Fetching cached search with UUID: {}", mySearchUuid);

				Optional<Search> searchEntityOpt = mySearchCacheSvc.fetchByUuid(mySearchUuid, myRequestPartitionId);
				mySearchEntity =
						searchEntityOpt.orElseThrow(() -> myExceptionService.newUnknownSearchException(mySearchUuid));
				// FIXME: throw better exception
				myParams = mySearchEntity.getSearchParameterMap().orElseThrow();

			} else {

				// If the search entity doesn't have an ID, it's just a candidate
				// created to back a fresh search. We can first check whether there
				// are any cached seaerches we can reuse instead
				if (mySearchEntity.getId() == null) {
					initialSearch = true;
					if (cacheStatus != SearchCacheStatusEnum.NOT_TRIED) {
						if (myParams.getEverythingMode() == null) {
							if (myStorageSettings.getReuseCachedSearchResultsForMillis() != null) {
								Optional<Search> cachedQueryOpt;
								cachedQueryOpt = findCachedQuery(
										myParams,
										mySearchEntity.getResourceType(),
										myRequestDetails,
										mySearchEntity.getSearchQueryString(),
										myRequestPartitionId);
								if (cachedQueryOpt.isPresent()) {
									mySearchEntity = cachedQueryOpt.get();
									mySearchUuid = mySearchEntity.getUuid();

									myCacheStatus = new IBundleProvider.SearchCacheStatus();
									myCacheStatus.setStatus(SearchCacheStatusEnum.HIT);
									myCacheStatus.setCacheEntryTimestamp(mySearchEntity.getCreated());

									ourLog.debug(
											"Query cache HIT - Replacing search {} with search {}",
											mySearchUuid,
											mySearchEntity.getUuid());

									initialSearch = false;
									// FIXME: add better exception
									myParams = mySearchEntity
											.getSearchParameterMap()
											.orElseThrow();
								}
							}
						}
					}
				} else {

					// FIXME: better exception
					mySearchEntity = mySearchCacheSvc
							.fetchByUuid(mySearchEntity.getUuid(), myRequestPartitionId)
							.orElseThrow();
				}
			}

			/// If we have a `_count=summary` query, just calculate the count and return
			if (myParams.getSummaryMode() == SummaryEnum.COUNT) {
				if (mySearchEntity.getTotalCount() == null) {
					Long countQuery = newSearchBuilder()
							.createCountQuery(
									myParams, mySearchEntity.getUuid(), myRequestDetails, myRequestPartitionId);
					mySearchEntity.setSearchParameterMap(myParams);
					mySearchEntity.setTotalCount(Math.toIntExact(countQuery));
					mySearchEntity.setStatus(SearchStatusEnum.FINISHED);
					mySearchCacheSvc.save(mySearchEntity, myRequestPartitionId);
				}
				myCachedPidsFromMatchesAndIncludes = List.of();
				myCachedPidsFromMatchesAndIncludesStartingIndex = theFromIndex;
				myCachedPidsFromMatchesAndIncludesEndingIndex = theToIndex;
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

			int countFoundThisPass = 0;
			int countBlockedThisPass = 0;
			List<JpaPid> previouslyFoundPids = List.of();
			SearchRuntimeDetails searchDetails = new SearchRuntimeDetails(myRequestDetails, mySearchEntity.getUuid());

			if (mySearchEntity.getNumFound() > 0) {
				previouslyFoundPids = mySearchResultCacheSvc.fetchAllResultPids(
						mySearchEntity, myRequestDetails, myRequestPartitionId);
				Validate.notNull(previouslyFoundPids, "previouslyFoundPids should not be null");
				searchBuilder.setPreviouslyAddedResourcePids(previouslyFoundPids);

				if (previouslyFoundPids.size() > theFromIndex) {
					int to = Math.min(theToIndex, previouslyFoundPids.size());
					pidsToReturn.addAll(previouslyFoundPids.subList(theFromIndex, to));
				}
			}

			int numWanted = theToIndex - mySearchEntity.getNumFound();

			// FIXME: set these?
			/*
			searchBuilder.setFetchSize(0);
			searchBuilder.setRequireTotal(true);
			*/

			int numToSkip = 0;
			if (theFromIndex > mySearchEntity.getNumFound()) {
				numToSkip = theFromIndex - mySearchEntity.getNumFound();
			}

			List<JpaPid> newPids = new ArrayList<>();
			while (true) {

				SearchThreshold searchThreshold = calculateNextSearchThreshold(numWanted, searchBuilder);
				searchBuilder.setMaxResultsToFetch(searchThreshold.threshold());

				List<JpaPid> newPidsThisPass = new ArrayList<>();
				try (IResultIterator<JpaPid> query =
						searchBuilder.createQuery(myParams, searchDetails, myRequestDetails, myRequestPartitionId)) {

					while (query.hasNext()) {
						JpaPid next = query.next();
						newPidsThisPass.add(next);

						if (numToSkip == 0) {
							pidsToReturn.add(next);
						} else {
							numToSkip--;
						}

						if (searchThreshold.threshold() != null
								&& newPidsThisPass.size() == searchThreshold.threshold()) {
							break;
						}
					}

					if (!newPidsThisPass.isEmpty()) {
						addedResultsThisPass = true;
					}

					int pidsCountThisPass = newPidsThisPass.size() + query.getSkippedCount();
					if (searchThreshold.threshold() != null && pidsCountThisPass >= searchThreshold.threshold()) {
						haveMoreResults = true;
					}
				} catch (Exception e) {
					SearchCoordinatorSvcImpl.markSearchAsFailedWithExceptionDetails(mySearchEntity, e);
					mySearchCacheSvc.save(mySearchEntity, myRequestPartitionId);
					return;
				}


				countFoundThisPass += newPidsThisPass.size();

				// Interceptor call: STORAGE_PREACCESS_RESOURCES
				// This can be used to remove results from the search result details before
				// the user has a chance to know that they were in the results
				boolean blockedResults = false;
				if (myCompositeBroadcaster.hasHooks(Pointcut.STORAGE_PREACCESS_RESOURCES)
						&& !newPidsThisPass.isEmpty()) {
					Set<JpaPid> blockedPids = new HashSet<>();

					List<IBaseResource> newResources =
							searchBuilder.loadResourcesByPid(newPidsThisPass, myRequestDetails);
					JpaPreResourceAccessDetails accessDetails =
							new JpaPreResourceAccessDetails(newPidsThisPass, newResources);
					HookParams params = new HookParams()
							.add(IPreResourceAccessDetails.class, accessDetails)
							.add(RequestDetails.class, myRequestDetails)
							.addIfMatchesType(ServletRequestDetails.class, myRequestDetails);
					myCompositeBroadcaster.callHooks(Pointcut.STORAGE_PREACCESS_RESOURCES, params);

					for (int i = newPidsThisPass.size() - 1; i >= 0; i--) {
						if (accessDetails.isDontReturnResourceAtIndex(i)) {
							blockedResults = true;
							JpaPid blockedPid = newPidsThisPass.remove(i);
							newResources.remove(i);
							blockedPids.add(blockedPid);
							countFoundThisPass--;
							countBlockedThisPass++;
						}
					}

					if (!blockedPids.isEmpty()) {
						pidsToReturn.removeIf(blockedPids::contains);
					}

					for (int i = 0; i < newPidsThisPass.size(); i++) {
						JpaPid pid = newPidsThisPass.get(i);
						IBaseResource resource = newResources.get(i);
						myFetchedResources.put(pid, resource);
					}
				}

				mySearchEntity.setNumFound(mySearchEntity.getNumFound() + countFoundThisPass);
				mySearchEntity.setNumBlocked(mySearchEntity.getNumBlocked() + countBlockedThisPass);

				newPids.addAll(newPidsThisPass);

				// If our last prefetch threshold specifies a specific maximum count, force the search
				// to be over when we hit that count.
				if (searchThreshold.threshold() != null
						&& searchThreshold.isLastThreshold()
						&& mySearchEntity.getNumFound() >= searchThreshold.threshold()) {
					haveMoreResults = false;
					break;
				}

				if (!blockedResults || mySearchEntity.getNumFound() >= theToIndex) {
					break;
				}
			}

			if (initialSearch || addedResultsThisPass) {
				if (haveMoreResults) {
					mySearchEntity.setStatus(SearchStatusEnum.PASSCMPLET);
					searchDetails.setSearchStatus(SearchStatusEnum.PASSCMPLET);
					/*
					 * If we finished the first page of results and we still don't know
					 * the total count, but the client requested the total cound, we will
					 * perform an explicit count query.
					 */
					if (myParams.getSearchTotalMode() == SearchTotalModeEnum.ACCURATE
							&& mySearchEntity.getTotalCount() == null) {
						Long countQuery = newSearchBuilder()
								.createCountQuery(
										myParams, mySearchEntity.getUuid(), myRequestDetails, myRequestPartitionId);
						if (countQuery != null) {
							mySearchEntity.setTotalCount(Math.toIntExact(countQuery));
						}
					}

					// Interceptor: JPA_PERFTRACE_SEARCH_PASS_COMPLETE
					myCompositeBroadcaster.ifHasCallHooks(
							Pointcut.JPA_PERFTRACE_SEARCH_PASS_COMPLETE, () -> new HookParams()
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

				mySearchCacheSvc.save(mySearchEntity, myRequestPartitionId);
				mySearchResultCacheSvc.storeResults(
						mySearchEntity, previouslyFoundPids, newPids, myRequestDetails, myRequestPartitionId);
			}

			pidsToReturn = pidsToReturn.subList(0, Math.min(pidsToReturn.size(), theToIndex - theFromIndex));

			fetchResourcesAndIncludes(searchBuilder, pidsToReturn, theFromIndex, theToIndex);
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
				boolean canUseCache =
						myCompositeBroadcaster.callHooks(Pointcut.STORAGE_PRECHECK_FOR_CACHED_SEARCH, params);
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

		private record SearchThreshold(@Nullable Integer threshold, boolean isLastThreshold) {}
	}
}
