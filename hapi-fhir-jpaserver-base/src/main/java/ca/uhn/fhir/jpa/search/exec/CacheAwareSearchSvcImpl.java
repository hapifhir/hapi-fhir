package ca.uhn.fhir.jpa.search.exec;

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
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.jpa.partition.BaseRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProviderFactory;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchResultCacheSvc;
import ca.uhn.fhir.jpa.search.cache.SearchCacheStatusEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.ServerInterceptorUtil;
import ca.uhn.fhir.rest.server.method.ResponsePage;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
	private PersistedJpaBundleProviderFactory myPersistedJpaBundleProviderFactory;

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
	public IBundleProvider executeQuery(SearchParameterMap theParams, RequestDetails theRequestDetails, CacheControlDirective theCacheControlDirective, Search theSearchEntity, ISearchBuilder<JpaPid> theSearchBuilder, RequestPartitionId theRequestPartitionId) {
		return new JpaBundleProvider(theParams, theRequestDetails, theCacheControlDirective, theRequestPartitionId, theSearchEntity, theSearchBuilder);
	}

	@Override
	public IBundleProvider continueQuery(RequestDetails theRequestDetails, String theId) {
		return new JpaBundleProvider(theRequestDetails, theId);
	}

	@Nullable
	private PersistedJpaBundleProvider findCachedQuery(
		SearchParameterMap theParams,
		String theResourceType,
		RequestDetails theRequestDetails,
		String theQueryString,
		RequestPartitionId theRequestPartitionId) {

		HapiTransactionService.requireTransaction();

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

		private SearchParameterMap myParams;
		private RequestDetails myRequestDetails;
		private RequestPartitionId myRequestPartitionId;
		private CacheControlDirective myCacheControlDirective;
		private IInterceptorBroadcaster myCompositeBroadcaster;
		private String mySearchUuid;
		private Search mySearchEntity;
		private boolean mySearchPerformed;
		private PersistedJpaBundleProvider myDelegate;
		private Set<JpaPid> myFoundPidsUnfiltered;
		private List<IBaseResource> myFoundResources;

		public JpaBundleProvider(SearchParameterMap theParams, RequestDetails theRequestDetails, CacheControlDirective theCacheControlDirective, RequestPartitionId theRequestPartitionId, Search theSearchEntity, ISearchBuilder<JpaPid> theSearchBuilder) {
			myParams = theParams;
			myRequestDetails = theRequestDetails;
			myCacheControlDirective = theCacheControlDirective;
			myRequestPartitionId = theRequestPartitionId;
			mySearchEntity = theSearchEntity;
			mySearchUuid = theSearchEntity.getUuid();
			myCompositeBroadcaster = CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, myRequestDetails);
		}

		public JpaBundleProvider(RequestDetails theRequestDetails, String theSearchUuid) {
			myRequestDetails = theRequestDetails;
			mySearchUuid = theSearchUuid;
		}

		@SuppressWarnings("unchecked")
		@Override
		public IPrimitiveType<Date> getPublished() {
			if (myDelegate != null) {
				return myDelegate.getPublished();
			}
			IPrimitiveType<Date> retVal = (IPrimitiveType<Date>) myFhirContext.getElementDefinition("instant").newInstance();
			retVal.setValue(mySearchEntity.getCreated());
			return retVal;
		}

		@Nullable
		@Override
		public String getUuid() {
			if (myDelegate != null) {
				return myDelegate.getUuid();
			}
			return mySearchEntity.getUuid();
		}

		@Override
		public Integer preferredPageSize() {
			if (myDelegate != null) {
				return myDelegate.preferredPageSize();
			}
			return mySearchEntity.getPreferredPageSize();
		}

		@Nullable
		@Override
		public Integer size() {
			if (myDelegate != null) {
				return myDelegate.size();
			}
			if (mySearchEntity != null) {
				return mySearchEntity.getTotalCount();
			}
			return null;
		}


		@Override
		public List<IBaseResource> getResources(int theFromIndex, int theToIndex, @Nonnull ResponsePage.ResponsePageBuilder theResponsePageBuilder) {
			return ensureSearchPerformed(theFromIndex, theToIndex, theResponsePageBuilder);
		}

		protected List<IBaseResource> toResourceList(
			ISearchBuilder theSearchBuilder,
			List<JpaPid> thePids,
			@Nullable List<IBaseResource> theResources,
			ResponsePage.ResponsePageBuilder theResponsePageBuilder) {
			List<JpaPid> includedPidList = new ArrayList<>();
			if (mySearchEntity.getSearchType() == SearchTypeEnum.SEARCH) {
				Integer maxIncludes = myStorageSettings.getMaximumIncludesToLoadPerPage();

				// Save original search result PIDs — non-iterate `_include` must apply only to initial results, not to
				// `_revinclude` results
				Set<JpaPid> originalPids = new HashSet<>(thePids);

				// Load non-iterate `_revinclude`
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

				// Load non-iterate `_include` (use originalPids so `_include` only applies to the
				// initial search results, not to revincluded resources — per FHIR spec, without `:iterate`)
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

				// Load `_revinclude:iterate`
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

				// Load `_include:iterate`
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

			// Fetch the resource bodies

			List<IBaseResource> resources;
			if (theResources != null) {
				// The match results were previously fetched so we only need to fetch the included resources
				resources = theResources;
				if (!includedPidList.isEmpty()) {
					List<IBaseResource> includeResources = new ArrayList<>(includedPidList.size());
					theSearchBuilder.loadResourcesByPid(includedPidList, includedPidList, includeResources, false, myRequestDetails);
					resources.addAll(includeResources);
				}
			} else {
				// We need to fetch all rsources
				resources = new ArrayList<>(thePids.size());
				theSearchBuilder.loadResourcesByPid(thePids, includedPidList, resources, false, myRequestDetails);
			}

			// we will send the resource list to our interceptors
			// this can (potentially) change the results being returned.
			int precount = resources.size();
			resources = ServerInterceptorUtil.fireStoragePreshowResource(resources, myRequestDetails, myInterceptorBroadcaster);
			// we only care about omitted results from this page
			theResponsePageBuilder.setOmittedResourceCount(precount - resources.size());
			theResponsePageBuilder.setResources(resources);
			theResponsePageBuilder.setIncludedResourceCount(includedPidList.size());

			return resources;
		}

		private List<IBaseResource> ensureSearchPerformed(int theFromIndex, int theToIndex, ResponsePage.ResponsePageBuilder theResponsePageBuilder) {
			return myTxService
				.withRequest(myRequestDetails)
				.withRequestPartitionId(myRequestPartitionId)
				.execute(() -> {

					List<JpaPid> allPids = new ArrayList<>();
					Set<JpaPid> allPidsSet = new HashSet<>();

					if (mySearchEntity == null) {

						ReadPartitionIdRequestDetails details = ReadPartitionIdRequestDetails.forSearchUuid(mySearchUuid);
						myRequestPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequest(myRequestDetails, details);


						Optional<Search> searchEntityOpt = mySearchCacheSvc.fetchByUuid(mySearchUuid, myRequestPartitionId);
						// FIXME: throw better exception
						mySearchEntity = searchEntityOpt.orElseThrow();

						List<JpaPid> existingSearchPids = mySearchResultCacheSvc.fetchResultPids(mySearchEntity, theFromIndex, theToIndex, myRequestDetails, myRequestPartitionId);
						allPids.addAll(existingSearchPids);
						allPidsSet.addAll(existingSearchPids);

					} else {

						/*
						 * See if there are any cached searches whose results we can return
						 * instead
						 */
						SearchCacheStatusEnum cacheStatus;
						if (myCacheControlDirective != null && myCacheControlDirective.isNoCache()) {
							cacheStatus = SearchCacheStatusEnum.NOT_TRIED;
						} else {
							cacheStatus = SearchCacheStatusEnum.MISS;
						}

						if (cacheStatus != SearchCacheStatusEnum.NOT_TRIED) {
							if (myParams.getEverythingMode() == null) {
								if (myStorageSettings.getReuseCachedSearchResultsForMillis() != null) {
									PersistedJpaBundleProvider foundSearchProvider = findCachedQuery(
										myParams, mySearchEntity.getResourceType(), myRequestDetails, mySearchEntity.getSearchQueryString(), myRequestPartitionId);
									if (foundSearchProvider != null) {
										foundSearchProvider.setCacheStatus(SearchCacheStatusEnum.HIT);
										myDelegate = foundSearchProvider;
										return myDelegate.getResources(theFromIndex, theToIndex, theResponsePageBuilder);
									}
								}
							}
						}

					}

					Class<? extends IBaseResource> resourceType = myFhirContext.getResourceDefinition(mySearchEntity.getResourceType()).getImplementingClass();
					ISearchBuilder<JpaPid> searchBuilder = mySearchBuilderFactory.newSearchBuilder(mySearchEntity.getResourceType(), resourceType);

					int countFoundThisPass = 0;
					int countBlockedThisPass = 0;
					List<JpaPid> newUnsyncedPids = null;
					List<IBaseResource> newUnsyncedResources = null;
					if (theToIndex > mySearchEntity.getNumFound()) {
						SearchRuntimeDetails searchDetails = new SearchRuntimeDetails(myRequestDetails, mySearchEntity.getUuid());
						IResultIterator<JpaPid> query = searchBuilder.createQuery(myParams, searchDetails, myRequestDetails, myRequestPartitionId);

						List<JpaPid> newPids = new ArrayList<>();

						while (query.hasNext()) {
							JpaPid next = query.next();
							if (allPidsSet.add(next)) {
								newPids.add(next);
							}

							if (myParams.getCount() != null && newPids.size() >= myParams.getCount()) {
								break;
							}
						}

						List<IBaseResource> newResources = searchBuilder.loadResourcesByPid(newPids, myRequestDetails);
						countFoundThisPass += newPids.size();

						// Interceptor call: STORAGE_PREACCESS_RESOURCES
						// This can be used to remove results from the search result details before
						// the user has a chance to know that they were in the results
						if (myRequestDetails != null && !newPids.isEmpty()) {
							JpaPreResourceAccessDetails accessDetails =
								new JpaPreResourceAccessDetails(allPids, newResources);
							HookParams params = new HookParams()
								.add(IPreResourceAccessDetails.class, accessDetails)
								.add(RequestDetails.class, myRequestDetails)
								.addIfMatchesType(
									ServletRequestDetails.class, myRequestDetails);
							myCompositeBroadcaster.callHooks(Pointcut.STORAGE_PREACCESS_RESOURCES, params);

							for (int i = newPids.size() - 1; i >= 0; i--) {
								if (accessDetails.isDontReturnResourceAtIndex(i)) {
									newPids.remove(i);
									newResources.remove(i);
									countBlockedThisPass++;
								}
							}
						}

						if (newUnsyncedPids == null) {
							newUnsyncedPids = new ArrayList<>();
						}
						if (newUnsyncedResources == null) {
							newUnsyncedResources = new ArrayList<>();
						}
						newUnsyncedPids.addAll(newPids);
						newUnsyncedResources.addAll(newResources);
					}

					mySearchEntity.setNumFound(mySearchEntity.getNumFound() + countFoundThisPass);
					mySearchEntity.setNumBlocked(mySearchEntity.getNumBlocked() + countBlockedThisPass);
					mySearchEntity.setStatus(SearchStatusEnum.PASSCMPLET);
					mySearchEntity.setSearchParameterMap(myParams);

					mySearchCacheSvc.save(mySearchEntity, myRequestPartitionId);
					mySearchResultCacheSvc.storeResults(
						mySearchEntity, allPids, newUnsyncedPids, myRequestDetails, myRequestPartitionId);

					// Fetch actual wanted resources
					List<IBaseResource> fetchedResources = null;
					if (newUnsyncedPids != null) {
						if (theFromIndex == allPids.size()) {
							int toIndex = theToIndex - theFromIndex;
							toIndex = Math.min(toIndex, newUnsyncedResources.size());
							fetchedResources = new ArrayList<>(newUnsyncedResources.subList(0, toIndex));
						}

						allPids.addAll(newUnsyncedPids);
					}

					int toIndex = Math.min(allPids.size(), theToIndex);
					int fromIndex = Math.max(0, Math.min(theFromIndex, allPids.size() - 1));
					List<JpaPid> pidsToFetch = allPids.subList(fromIndex, toIndex);

					mySearchPerformed = true;
					return toResourceList(searchBuilder, pidsToFetch, fetchedResources, theResponsePageBuilder);
				});


		}
	}


}
