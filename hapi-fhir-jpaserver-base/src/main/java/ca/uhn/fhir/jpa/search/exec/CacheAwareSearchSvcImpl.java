package ca.uhn.fhir.jpa.search.exec;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.jpa.interceptor.JpaPreResourceAccessDetails;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.jpa.search.cache.SearchCacheStatusEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
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

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CacheAwareSearchSvcImpl implements ICacheAwareSearchSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(CacheAwareSearchSvcImpl.class);

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

	@Override
	public IBundleProvider executeQuery(
		SearchParameterMap theParams,
		RequestDetails theRequestDetails,
		CacheControlDirective theCacheControlDirective,
		Search theSearchEntity,
		ISearchBuilder<JpaPid> theSearchBuilder,
		RequestPartitionId theRequestPartitionId) {
		return new JpaBundleProvider(
			theParams, theRequestDetails, theCacheControlDirective, theRequestPartitionId, theSearchEntity);
	}

	@Override
	public IBundleProvider continueQuery(RequestDetails theRequestDetails, String theId) {
		return new JpaBundleProvider(theRequestDetails, theId);
	}

	private Optional<Search> findCachedQuery(
		SearchParameterMap theParams,
		String theResourceType,
		RequestDetails theRequestDetails,
		String theQueryString,
		RequestPartitionId theRequestPartitionId) {

		HapiTransactionService.requireTransaction();

		IInterceptorBroadcaster compositeBroadcaster =
			CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, theRequestDetails);

		// Interceptor call: STORAGE_PRECHECK_FOR_CACHED_SEARCH

		HookParams params = new HookParams()
			.add(SearchParameterMap.class, theParams)
			.add(RequestDetails.class, theRequestDetails)
			.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
		boolean canUseCache = compositeBroadcaster.callHooks(Pointcut.STORAGE_PRECHECK_FOR_CACHED_SEARCH, params);
		if (!canUseCache) {
			return Optional.empty();
		}

		// Check for a search matching the given hash
		Search searchToUse = findSearchToUseOrNull(theQueryString, theResourceType, theRequestPartitionId);
		if (searchToUse == null) {
			return Optional.empty();
		}

		ourLog.debug("Reusing search {} from cache", searchToUse.getUuid());
		// Interceptor call: JPA_PERFTRACE_SEARCH_REUSING_CACHED
		params = new HookParams()
			.add(SearchParameterMap.class, theParams)
			.add(RequestDetails.class, theRequestDetails)
			.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
		compositeBroadcaster.callHooks(Pointcut.JPA_PERFTRACE_SEARCH_REUSING_CACHED, params);

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

	public class JpaBundleProvider implements IBundleProvider {
		private final Map<JpaPid, IBaseResource> myFetchedResources = new HashMap<>();

		private SearchParameterMap myParams;
		private RequestDetails myRequestDetails;
		private RequestPartitionId myRequestPartitionId;
		private CacheControlDirective myCacheControlDirective;
		private IInterceptorBroadcaster myCompositeBroadcaster;
		private String mySearchUuid;
		private Search mySearchEntity;
		private List<JpaPid> myCachedPidsFromMatches;
		private List<JpaPid> myCachedPidsFromMatchesAndIncludes;
		private Integer myCachedPidsFromMatchesStartingIndex;
		private Integer myCachedPidsFromMatchesEndingIndex;
		private Integer myCachedPidsFromMatchesAndIncludesStartingIndex;
		private Integer myCachedPidsFromMatchesAndIncludesEndingIndex;

		public JpaBundleProvider(
			SearchParameterMap theParams,
			RequestDetails theRequestDetails,
			CacheControlDirective theCacheControlDirective,
			RequestPartitionId theRequestPartitionId,
			Search theSearchEntity) {
			this(theRequestDetails, theSearchEntity.getUuid());
			myParams = theParams;
			myCacheControlDirective = theCacheControlDirective;
			myRequestPartitionId = theRequestPartitionId;
			mySearchEntity = theSearchEntity;
		}

		public JpaBundleProvider(RequestDetails theRequestDetails, String theSearchUuid) {
			myRequestDetails = theRequestDetails;
			mySearchUuid = theSearchUuid;
			myCompositeBroadcaster =
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, myRequestDetails);
		}

		@SuppressWarnings("unchecked")
		@Override
		public IPrimitiveType<Date> getPublished() {
			ensureSearchPerformed();
			IPrimitiveType<Date> retVal = (IPrimitiveType<Date>)
				myFhirContext.getElementDefinition("instant").newInstance();
			retVal.setValue(mySearchEntity.getCreated());
			return retVal;
		}

		@Nullable
		@Override
		public String getUuid() {
			ensureSearchPerformed();
			if (mySearchUuid == null) {
				return mySearchUuid;
			}
			return mySearchEntity.getUuid();
		}

		@Override
		public Integer preferredPageSize() {
			return mySearchEntity.getPreferredPageSize();
		}

		@Nullable
		@Override
		public Integer size() {
			if (myParams != null && myParams.getSearchTotalMode() == SearchTotalModeEnum.ACCURATE) {
				ensureSearchPerformed();
			}
			if (mySearchEntity != null) {
				return mySearchEntity.getTotalCount();
			}
			return null;
		}

		@Override
		public List<IBaseResource> getResources(
			int theFromIndex, int theToIndex, @Nonnull ResponsePage.ResponsePageBuilder theResponsePageBuilder) {
			ensureSearchPerformed(theFromIndex, theToIndex);

			/// These should always be true unless we have a logic bug, since
			/// {@link #ensureSearchPerformed(int, int, ResponsePage.ResponsePageBuilder)}
			/// should reset them
			Validate.isTrue(
				theFromIndex == myCachedPidsFromMatchesAndIncludesStartingIndex,
				"Expected %d but got %d",
				myCachedPidsFromMatchesStartingIndex,
				theFromIndex);
			Validate.isTrue(
				theToIndex == myCachedPidsFromMatchesAndIncludesEndingIndex,
				"Expected %d but got %d",
				myCachedPidsFromMatchesEndingIndex,
				theToIndex);

			List<IBaseResource> retVal = new ArrayList<>();
			for (JpaPid nextPid : myCachedPidsFromMatchesAndIncludes) {
				retVal.add(myFetchedResources.get(nextPid));
			}

			// we will send the resource list to our interceptors
			// this can (potentially) change the results being returned.
			int precount = retVal.size();
			retVal = ServerInterceptorUtil.fireStoragePreshowResource(
				retVal, myRequestDetails, myInterceptorBroadcaster);

			// we only care about omitted results from this page
			// FIXME: what are these used for?
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
				Integer maxIncludes = myStorageSettings.getMaximumIncludesToLoadPerPage();

				// Save original search result PIDs — non-iterate `_include` must apply only to initial results, not to
				// `_revinclude` results
				Set<JpaPid> originalPids = new HashSet<>(thePids);

				// Load non-iterate `_revinclude`
				{
					Set<JpaPid> nonIterateRevIncludedPids = theSearchBuilder.loadIncludes(
						myFhirContext,
						myEntityManager,
						thePids,
						mySearchEntity.toRevIncludesList(false),
						true,
						mySearchEntity.getLastUpdated(),
						mySearchEntity.getUuid(),
						myRequestDetails,
						maxIncludes);
					if (maxIncludes != null) {
						maxIncludes -= nonIterateRevIncludedPids.size();
					}
					thePids.addAll(nonIterateRevIncludedPids);
					includedPidList.addAll(nonIterateRevIncludedPids);
				}

				// Load non-iterate `_include` (use originalPids so `_include` only applies to the
				// initial search results, not to revincluded resources — per FHIR spec, without `:iterate`)
				{
					Set<JpaPid> nonIterateIncludedPids = theSearchBuilder.loadIncludes(
						myFhirContext,
						myEntityManager,
						originalPids,
						mySearchEntity.toIncludesList(false),
						false,
						mySearchEntity.getLastUpdated(),
						mySearchEntity.getUuid(),
						myRequestDetails,
						maxIncludes);
					if (maxIncludes != null) {
						maxIncludes -= nonIterateIncludedPids.size();
					}
					thePids.addAll(nonIterateIncludedPids);
					includedPidList.addAll(nonIterateIncludedPids);
				}

				// Load `_revinclude:iterate`
				{
					Set<JpaPid> iterateRevIncludedPids = theSearchBuilder.loadIncludes(
						myFhirContext,
						myEntityManager,
						thePids,
						mySearchEntity.toRevIncludesList(true),
						true,
						mySearchEntity.getLastUpdated(),
						mySearchEntity.getUuid(),
						myRequestDetails,
						maxIncludes);
					if (maxIncludes != null) {
						maxIncludes -= iterateRevIncludedPids.size();
					}
					thePids.addAll(iterateRevIncludedPids);
					includedPidList.addAll(iterateRevIncludedPids);
				}

				// Load `_include:iterate`
				{
					Set<JpaPid> iterateIncludedPids = theSearchBuilder.loadIncludes(
						myFhirContext,
						myEntityManager,
						thePids,
						mySearchEntity.toIncludesList(true),
						false,
						mySearchEntity.getLastUpdated(),
						mySearchEntity.getUuid(),
						myRequestDetails,
						maxIncludes);
					thePids.addAll(iterateIncludedPids);
					includedPidList.addAll(iterateIncludedPids);
				}
			}

			// Fetch the resource bodies

			Collection<JpaPid> pidsToFetch;
			if (!myFetchedResources.isEmpty()) {
				pidsToFetch = new HashSet<>(thePids);
				pidsToFetch.removeAll(myFetchedResources.keySet());
			} else {
				pidsToFetch = thePids;
			}

			if (!pidsToFetch.isEmpty()) {
				List<IBaseResource> includeResources = new ArrayList<>(pidsToFetch.size());
				theSearchBuilder.loadResourcesByPid(
					pidsToFetch, includedPidList, includeResources, false, myRequestDetails);
				for (IBaseResource next : includeResources) {
					JpaPid pid = extractFetchedResourcePid(next);
					myFetchedResources.put(pid, next);
				}
			}

			myCachedPidsFromMatchesAndIncludes = thePids;
			myCachedPidsFromMatchesAndIncludesStartingIndex = theFromIndex;
			myCachedPidsFromMatchesAndIncludesEndingIndex = theToIndex;
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
				Integer firstPrefetchThreshold =
					myStorageSettings.getSearchPreFetchThresholds().get(0);
				ensureSearchPerformed(0, firstPrefetchThreshold);
			}
		}

		private void ensureSearchPerformed(int theFromIndex, int theToIndex) {

			if (myCachedPidsFromMatchesAndIncludes != null) {
				if (myCachedPidsFromMatchesAndIncludesStartingIndex == theFromIndex) {
					if (myCachedPidsFromMatchesAndIncludesEndingIndex == theToIndex) {
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
						myCachedPidsFromMatchesAndIncludes = myCachedPidsFromMatches.subList(rangeStart, rangeEnd);
						myCachedPidsFromMatchesAndIncludesStartingIndex = theFromIndex;
						myCachedPidsFromMatchesAndIncludesEndingIndex = theToIndex;
						return;
					}
				}
			}

			for (int i = 0; ; i++) {
				try {
					myTxService
						.withRequest(myRequestDetails)
						.withRequestPartitionId(myRequestPartitionId)
						.execute(() -> ensureSearchPerformedInsideTransaction(theFromIndex, theToIndex));
					return;
				} catch (Exception e) {
					if (i == 5) {
						throw e;
					}
					ourLog.warn("Constraint error while writing search results to query cache");
					new SleepUtil().sleepAtLeast(500, false);
					resetState();
				}
			}
		}

		private void resetState() {
			if (mySearchEntity != null && mySearchEntity.getId() != null) {
				mySearchUuid = mySearchEntity.getUuid();
				mySearchEntity = null;
				myFetchedResources.clear();
			}
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

				ReadPartitionIdRequestDetails details =
					ReadPartitionIdRequestDetails.forSearchUuid(mySearchUuid);
				myRequestPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequest(
					myRequestDetails, details);

				Optional<Search> searchEntityOpt =
					mySearchCacheSvc.fetchByUuid(mySearchUuid, myRequestPartitionId);
				// FIXME: throw better exception
				mySearchEntity = searchEntityOpt.orElseThrow();
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
									// FIXME: where does this go?
									cacheStatus = SearchCacheStatusEnum.HIT;
									mySearchEntity = cachedQueryOpt.get();
									initialSearch = false;
									// FIXME: add better exception
									myParams = mySearchEntity
										.getSearchParameterMap()
										.orElseThrow();
								}
							}
						}
					}
				}
			}

			if (mySearchEntity.getNumFound() >= theToIndex) {
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
			SearchRuntimeDetails searchDetails =
				new SearchRuntimeDetails(myRequestDetails, mySearchEntity.getUuid());

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

				Integer numToFetch = calculateNextSearchThreshold(numWanted, searchBuilder);
				searchBuilder.setMaxResultsToFetch(numToFetch);

				List<JpaPid> newPidsThisPass = new ArrayList<>();
				try (IResultIterator<JpaPid> query = searchBuilder.createQuery(
					myParams, searchDetails, myRequestDetails, myRequestPartitionId)) {

					while (query.hasNext()) {
						JpaPid next = query.next();
						newPidsThisPass.add(next);

						if (numToSkip == 0) {
							pidsToReturn.add(next);
						} else {
							numToSkip--;
						}

						if (numToFetch != null && newPidsThisPass.size() == numToFetch) {
							break;
						}
					}

					if (!newPidsThisPass.isEmpty()) {
						addedResultsThisPass = true;
					}
					if (numToFetch != null && newPidsThisPass.size() == numToFetch) {
						haveMoreResults = true;
					}
				} catch (IOException e) {
					// FIXME: add code
					throw new InternalErrorException(Msg.code(1) + e, e);
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
					for (IBaseResource newResource : newResources) {
						JpaPid pid = extractFetchedResourcePid(newResource);
						myFetchedResources.put(pid, newResource);
					}
				}

				mySearchEntity.setNumFound(mySearchEntity.getNumFound() + countFoundThisPass);
				mySearchEntity.setNumBlocked(mySearchEntity.getNumBlocked() + countBlockedThisPass);

				newPids.addAll(newPidsThisPass);

				if (!blockedResults || mySearchEntity.getNumFound() >= theToIndex) {
					break;
				}
			}

			if (initialSearch || addedResultsThisPass) {
				if (haveMoreResults) {
					mySearchEntity.setStatus(SearchStatusEnum.PASSCMPLET);
					/*
					 * If we finished the first page of results and we still don't know
					 * the total count, but the client requested the total cound, we will
					 * perform an explicit count query.
					 */
					if (myParams.getSearchTotalMode() == SearchTotalModeEnum.ACCURATE
						&& mySearchEntity.getTotalCount() == null) {
						Long countQuery = newSearchBuilder()
							.createCountQuery(
								myParams,
								mySearchEntity.getUuid(),
								myRequestDetails,
								myRequestPartitionId);
						if (countQuery != null) {
							mySearchEntity.setTotalCount(Math.toIntExact(countQuery));
						}
					}
				} else {
					mySearchEntity.setStatus(SearchStatusEnum.FINISHED);
					mySearchEntity.setTotalCount(mySearchEntity.getNumFound());
				}
				mySearchEntity.setSearchParameterMap(myParams);

				mySearchCacheSvc.save(mySearchEntity, myRequestPartitionId);
				mySearchResultCacheSvc.storeResults(
					mySearchEntity,
					previouslyFoundPids,
					newPids,
					myRequestDetails,
					myRequestPartitionId);
			}

			pidsToReturn =
				pidsToReturn.subList(0, Math.min(pidsToReturn.size(), theToIndex - theFromIndex));

			fetchResourcesAndIncludes(searchBuilder, pidsToReturn, theFromIndex, theToIndex);
		}

		@Nullable
		private Integer calculateNextSearchThreshold(int numWanted, ISearchBuilder<JpaPid> searchBuilder) {
			@Nullable Integer numToFetch = null;
			
			// For subsequent pages, we'll use the predetermined search thresholds
			for (int nextThreshold : myStorageSettings.getSearchPreFetchThresholds()) {

				/*
				 * If we're past the last prefetch threshold then
				 * we're potentially fetching unlimited amounts of data.
				 * We'll move responsibility for deduplication to the database in this case
				 * so that we don't run the risk of blowing out the memory
				 * in the app server
				 */
				if (nextThreshold == -1) {
					searchBuilder.setDeduplicateInDatabase(true);
				} else {
					if ((numWanted + mySearchEntity.getNumFound()) < nextThreshold) {
						numToFetch = nextThreshold + 1;
						break;
					}
				}
			}

			return numToFetch;
		}

		private ISearchBuilder<JpaPid> newSearchBuilder() {
			Class<? extends IBaseResource> resourceType = myFhirContext
				.getResourceDefinition(mySearchEntity.getResourceType())
				.getImplementingClass();
			return mySearchBuilderFactory.newSearchBuilder(mySearchEntity.getResourceType(), resourceType);
		}

		private static JpaPid extractFetchedResourcePid(IBaseResource next) {
			return (JpaPid) next.getUserData(IDao.RESOURCE_PID_KEY);
		}
	}
}
