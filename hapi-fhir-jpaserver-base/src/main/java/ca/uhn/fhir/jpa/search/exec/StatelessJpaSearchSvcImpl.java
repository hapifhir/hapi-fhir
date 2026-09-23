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

import ca.uhn.fhir.context.FhirContext;
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
import ca.uhn.fhir.jpa.interceptor.JpaPreResourceAccessDetails;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.search.SearchBuilderLoadIncludesParameters;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.SearchParameterMapCalculator;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.interceptor.ServerInterceptorUtil;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.util.IntCounter;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.util.SearchParameterMapCalculator.isWantOnlyCount;

public class StatelessJpaSearchSvcImpl implements IStatelessJpaSearchSvc {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(StatelessJpaSearchSvcImpl.class);

	private FhirContext myContext;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	protected SearchBuilderFactory<JpaPid> mySearchBuilderFactory;

	@Autowired
	private HapiTransactionService myTxService;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private EntityManager myEntityManager;

	@Autowired
	private IPagingProvider myPagingProvider;

	private final int mySyncSize = 250;

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public IBundleProvider createNewSearch(
			SearchParameterMap theParams,
			RequestDetails theRequestDetails,
			String theSearchUuid,
			ISearchBuilder<JpaPid> theSb,
			Integer theLoadSynchronousUpTo,
			RequestPartitionId theRequestPartitionId) {
		SearchRuntimeDetails searchRuntimeDetails = new SearchRuntimeDetails(theRequestDetails, theSearchUuid);
		searchRuntimeDetails.setLoadSynchronous(true);

		boolean theParamWantOnlyCount = isWantOnlyCount(theParams);
		boolean theParamOrConfigWantCount = SearchParameterMapCalculator.isWantCount(theParams, myStorageSettings);
		boolean wantCount = theParamWantOnlyCount || theParamOrConfigWantCount;

		IInterceptorBroadcaster compositeBroadcaster =
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, theRequestDetails);
		boolean havePreAccessHooks = compositeBroadcaster.hasHooks(Pointcut.STORAGE_PREACCESS_RESOURCES);

		// Execute the query and make sure we return distinct results
		return myTxService
				.withRequest(theRequestDetails)
				.withRequestPartitionId(theRequestPartitionId)
				.readOnly()
				.execute(() -> {
					Long count = 0L;
					if (wantCount) {

						ourLog.trace("Performing count");
						// TODO FulltextSearchSvcImpl will remove necessary parameters from the "theParams", this will
						// cause actual query after count to
						//  return wrong response. This is some dirty fix to avoid that issue. Params should not be
						// mutated?
						//  Maybe instead of removing them we could skip them in db query builder if full text search
						// was used?
						List<List<IQueryParameterType>> contentAndTerms = theParams.get(Constants.PARAM_CONTENT);
						List<List<IQueryParameterType>> textAndTerms = theParams.get(Constants.PARAM_TEXT);

						count = theSb.createCountQuery(
								theParams, theSearchUuid, theRequestDetails, theRequestPartitionId);

						if (contentAndTerms != null) theParams.put(Constants.PARAM_CONTENT, contentAndTerms);
						if (textAndTerms != null) theParams.put(Constants.PARAM_TEXT, textAndTerms);

						ourLog.trace("Got count {}", count);
					}

					if (theParamWantOnlyCount) {
						SimpleBundleProvider bundleProvider = new SimpleBundleProvider();
						bundleProvider.setSize(count.intValue());
						return bundleProvider;
					}

					boolean hasACount = theParams.getCount() != null;
					List<IBaseResource> loadedResources = new ArrayList<>();
					IntCounter receivedResourceCount = new IntCounter(-1);

					/*
					 * If we have any STORAGE_PREACCESS_RESOURCES, and we're doing an offset
					 * search, we need to search right from offset 0 because we don't know
					 * which resources might have been filtered out from the first page, and
					 * we need to ensure that we return full sized pages. So we temporarily
					 * zero out the offset and add it to the count so that we search from the
					 * start, and then filter out the eventual list of PIDs and resources.
					 */
					Integer originalCount = null;
					Integer originalOffset = null;
					if (havePreAccessHooks
							&& theParams.getCount() != null
							&& theParams.getOffset() != null
							&& theParams.getOffset() > 0) {
						originalCount = theParams.getCount();
						originalOffset = theParams.getOffset();
						theParams.setOffset(0);
						theParams.setCount(originalCount + originalOffset);
					}

					// Perform the search
					List<JpaPid> pids = new ArrayList<>();
					{
						Integer requestedCount = theParams.getCount();
						while (true) {
							PerformedSearchResult searchResult = performSearch(
									theParams,
									theRequestDetails,
									theSb,
									theLoadSynchronousUpTo,
									theRequestPartitionId,
									requestedCount,
									searchRuntimeDetails,
									receivedResourceCount,
									loadedResources);
							pids.addAll(searchResult.receivedPids());

							// If we have any STORAGE_PREACCESS_RESOURCES hooks, then we
							// might receive less than the desired number since the consent service
							// can filter some out. If this happens, and we know that there
							// are more potential resources, try again with a higher
							// maximum count
							if (requestedCount != null
									&& havePreAccessHooks
									&& searchResult.receivedAsManyResourcesAsRequested()
									&& searchResult.receivedPids().size() < theParams.getCount()) {
								requestedCount = requestedCount + mySyncSize;
								theSb.setMaxResultsToFetch(requestedCount);
							} else {
								break;
							}
						}
					}

					if (originalCount != null) {
						pids = pids.subList(originalOffset, Math.min(pids.size(), originalOffset + originalCount));
						loadedResources = loadedResources.subList(
								originalOffset, Math.min(loadedResources.size(), originalOffset + originalCount));
					}

					if (theParams.getCount() != null) {
						pids = pids.subList(0, Math.min(pids.size(), theParams.getCount()));
						loadedResources =
								loadedResources.subList(0, Math.min(loadedResources.size(), theParams.getCount()));
					}

					/*
					 * For stateless queries, we load all the includes right away
					 * since we're returning a static bundle with all the results
					 * pre-loaded. This is ok because stateless requests are not
					 * expected to be paged
					 *
					 * On the other hand for cache-aware queries we load includes/revincludes
					 * individually for pages as we return them to clients
					 */

					List<JpaPid> allIncludedPidsList = List.of();
					Map<JpaPid, IBaseResource> fetchedIncludedResources = new HashMap<>();
					if (theParams.hasIncludes() || theParams.hasRevIncludes()) {
						// Save original PIDs before any include/revinclude expansion
						Set<JpaPid> originalPids = new HashSet<>(pids);

						Integer maxIncludes = myStorageSettings.getMaximumIncludesToLoadPerPage();
						allIncludedPidsList = new ArrayList<>();

						// Separate non-iterate and iterate includes/revincludes
						Set<Include> nonIterateRevIncludes = theParams.getRevIncludes().stream()
								.filter(i -> !i.isRecurse())
								.collect(Collectors.toSet());
						Set<Include> iterateRevIncludes = theParams.getRevIncludes().stream()
								.filter(Include::isRecurse)
								.collect(Collectors.toSet());
						Set<Include> nonIterateIncludes = theParams.getIncludes().stream()
								.filter(i -> !i.isRecurse())
								.collect(Collectors.toSet());
						Set<Include> iterateIncludes = theParams.getIncludes().stream()
								.filter(Include::isRecurse)
								.collect(Collectors.toSet());

						// Phase 1: non-iterate `_revinclude` on original search result PIDs
						if (!nonIterateRevIncludes.isEmpty()) {
							SearchBuilderLoadIncludesParameters<JpaPid> p = new SearchBuilderLoadIncludesParameters<>();
							p.setFhirContext(myContext);
							p.setEntityManager(myEntityManager);
							p.setMatches(originalPids);
							p.setIncludeFilters(nonIterateRevIncludes);
							p.setReverseMode(true);
							p.setLastUpdated(theParams.getLastUpdated());
							p.setSearchIdOrDescription("(synchronous)");
							p.setRequestDetails(theRequestDetails);
							p.setMaxCount(maxIncludes);
							ISearchBuilder.FetchedIncludes<JpaPid> revIncludedPids = theSb.loadIncludes(p);
							if (maxIncludes != null) {
								maxIncludes -= revIncludedPids.pids().size();
							}
							pids.addAll(revIncludedPids.pids());
							allIncludedPidsList.addAll(revIncludedPids.pids());
							if (revIncludedPids.resourcesIfFetched().isPresent()) {
								fetchedIncludedResources.putAll(
										revIncludedPids.resourcesIfFetched().get());
							}
						}

						// Phase 2: non-iterate `_include` on original search result PIDs
						// (use originalPids so `_include` only applies to the initial search results,
						// not to revincluded resources — per FHIR spec, without `:iterate`)
						if (theParams.getEverythingMode() == null
								&& !nonIterateIncludes.isEmpty()
								&& (maxIncludes == null || maxIncludes > 0)) {
							SearchBuilderLoadIncludesParameters<JpaPid> p = new SearchBuilderLoadIncludesParameters<>();
							p.setFhirContext(myContext);
							p.setEntityManager(myEntityManager);
							p.setMatches(originalPids);
							p.setIncludeFilters(nonIterateIncludes);
							p.setReverseMode(false);
							p.setLastUpdated(theParams.getLastUpdated());
							p.setSearchIdOrDescription("(synchronous)");
							p.setRequestDetails(theRequestDetails);
							p.setMaxCount(maxIncludes);
							ISearchBuilder.FetchedIncludes<JpaPid> forwardIncludedPids = theSb.loadIncludes(p);
							if (maxIncludes != null) {
								maxIncludes -= forwardIncludedPids.pids().size();
							}
							pids.addAll(forwardIncludedPids.pids());
							allIncludedPidsList.addAll(forwardIncludedPids.pids());
							if (forwardIncludedPids.resourcesIfFetched().isPresent()) {
								fetchedIncludedResources.putAll(
										forwardIncludedPids.resourcesIfFetched().get());
							}
						}

						// Phase 3: `_revinclude:iterate` on expanded PIDs (including non-iterate revinclude results)
						if (!iterateRevIncludes.isEmpty() && (maxIncludes == null || maxIncludes > 0)) {
							SearchBuilderLoadIncludesParameters<JpaPid> p = new SearchBuilderLoadIncludesParameters<>();
							p.setFhirContext(myContext);
							p.setEntityManager(myEntityManager);
							p.setMatches(pids);
							p.setIncludeFilters(iterateRevIncludes);
							p.setReverseMode(true);
							p.setLastUpdated(theParams.getLastUpdated());
							p.setSearchIdOrDescription("(synchronous)");
							p.setRequestDetails(theRequestDetails);
							p.setMaxCount(maxIncludes);
							ISearchBuilder.FetchedIncludes<JpaPid> iterateRevIncludedPids = theSb.loadIncludes(p);
							if (maxIncludes != null) {
								maxIncludes -= iterateRevIncludedPids.pids().size();
							}
							pids.addAll(iterateRevIncludedPids.pids());
							allIncludedPidsList.addAll(iterateRevIncludedPids.pids());
							if (iterateRevIncludedPids.resourcesIfFetched().isPresent()) {
								fetchedIncludedResources.putAll(iterateRevIncludedPids
										.resourcesIfFetched()
										.get());
							}
						}

						// Phase 4: `_include:iterate` on all expanded PIDs (including revinclude results)
						if (theParams.getEverythingMode() == null
								&& !iterateIncludes.isEmpty()
								&& (maxIncludes == null || maxIncludes > 0)) {
							SearchBuilderLoadIncludesParameters<JpaPid> p = new SearchBuilderLoadIncludesParameters<>();
							p.setFhirContext(myContext);
							p.setEntityManager(myEntityManager);
							p.setMatches(pids);
							p.setIncludeFilters(iterateIncludes);
							p.setReverseMode(false);
							p.setLastUpdated(theParams.getLastUpdated());
							p.setSearchIdOrDescription("(synchronous)");
							p.setRequestDetails(theRequestDetails);
							p.setMaxCount(maxIncludes);
							ISearchBuilder.FetchedIncludes<JpaPid> iterateForwardIncludedPids = theSb.loadIncludes(p);
							pids.addAll(iterateForwardIncludedPids.pids());
							allIncludedPidsList.addAll(iterateForwardIncludedPids.pids());
							if (iterateForwardIncludedPids.resourcesIfFetched().isPresent()) {
								fetchedIncludedResources.putAll(iterateForwardIncludedPids
										.resourcesIfFetched()
										.get());
							}
						}
					}

					if (loadedResources.isEmpty()) {
						theSb.loadResourcesByPid(pids, allIncludedPidsList, loadedResources, false, theRequestDetails);
					} else if (!allIncludedPidsList.isEmpty()) {
						List<IBaseResource> includeResources = new ArrayList<>();
						for (Iterator<JpaPid> iter = allIncludedPidsList.iterator(); iter.hasNext(); ) {
							JpaPid nextPid = iter.next();
							if (fetchedIncludedResources.containsKey(nextPid)) {
								includeResources.add(fetchedIncludedResources.get(nextPid));
								iter.remove();
							}
						}
						if (!allIncludedPidsList.isEmpty()) {
							theSb.loadResourcesByPid(
									allIncludedPidsList,
									allIncludedPidsList,
									includeResources,
									false,
									theRequestDetails);
						}
						loadedResources.addAll(includeResources);
					}

					// Hook: STORAGE_PRESHOW_RESOURCES
					List<IBaseResource> resources = ServerInterceptorUtil.fireStoragePreshowResource(
							loadedResources, theRequestDetails, myInterceptorBroadcaster);

					SimpleBundleProvider bundleProvider = new SimpleBundleProvider(resources);

					if (hasACount && theSb.requiresTotal()) {
						bundleProvider.setTotalResourcesRequestedReturned(receivedResourceCount.get());
					}

					int offset = 0;
					if (theParams.getOffset() != null) {
						offset = theParams.getOffset();
					}
					bundleProvider.setCurrentPageOffset(offset);

					int pageSize = DatabaseBackedPagingProvider.DEFAULT_DEFAULT_PAGE_SIZE;
					if (theParams.getCount() != null) {
						pageSize = theParams.getCount();
					} else if (myPagingProvider != null) {
						pageSize = myPagingProvider.getDefaultPageSize();
					}
					bundleProvider.setCurrentPageSize(pageSize);

					if (wantCount) {
						bundleProvider.setSize(count.intValue());
					} else {
						Integer queryCount = getQueryCount(theLoadSynchronousUpTo, theParams);
						if (queryCount == null || queryCount > resources.size()) {
							// No limit, last page or everything was fetched within the limit
							// NB: total should *not* include included resources
							bundleProvider.setSize(getTotalCount(
									queryCount, theParams.getOffset(), resources.size() - allIncludedPidsList.size()));
						} else {
							bundleProvider.setSize(null);
						}
					}

					bundleProvider.setPreferredPageSize(pids.size() - allIncludedPidsList.size());

					return bundleProvider;
				});
	}

	@Nonnull
	private PerformedSearchResult performSearch(
			SearchParameterMap theParams,
			RequestDetails theRequestDetails,
			ISearchBuilder<JpaPid> theSb,
			Integer theLoadSynchronousUpTo,
			RequestPartitionId theRequestPartitionId,
			Integer theRequestedCount,
			SearchRuntimeDetails theSearchRuntimeDetails,
			IntCounter theReceivedResourceCountToPopulate,
			List<IBaseResource> theLoadedResourcesToOptionallyPopulate) {
		SearchParameterMap clonedParams = theParams.clone();
		boolean hasACount = theRequestedCount != null;
		if (hasACount) {
			clonedParams.setCount(theRequestedCount + 1);
		}

		// Perform the actual search
		// Load the results synchronously
		final List<JpaPid> consumedPids = new ArrayList<>();
		ISearchResultConsumer<JpaPid> searchResultConsumer =
				new StatelessSearchResultConsumer(consumedPids, theLoadSynchronousUpTo, theParams);
		theSb.performSearchForPids(
				searchResultConsumer, clonedParams, theSearchRuntimeDetails, theRequestDetails, theRequestPartitionId);

		boolean receivedAsManyResourcesAsRequested = true;
		if (hasACount) {
			receivedAsManyResourcesAsRequested = consumedPids.size() > theRequestedCount;
		}

		IInterceptorBroadcaster compositeBroadcaster =
				CompositeInterceptorBroadcaster.newCompositeBroadcaster(myInterceptorBroadcaster, theRequestDetails);

		if (compositeBroadcaster.hasHooks(Pointcut.STORAGE_PREACCESS_RESOURCES)) {

			List<IBaseResource> loadedResources = new ArrayList<>();
			theSb.loadResourcesByPid(consumedPids, Collections.emptySet(), loadedResources, false, null);
			JpaPreResourceAccessDetails accessDetails = new JpaPreResourceAccessDetails(consumedPids, loadedResources);

			HookParams params = new HookParams()
					.add(IPreResourceAccessDetails.class, accessDetails)
					.add(RequestDetails.class, theRequestDetails)
					.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
			compositeBroadcaster.callHooks(Pointcut.STORAGE_PREACCESS_RESOURCES, params);

			Validate.isTrue(
					consumedPids.size() == loadedResources.size(),
					"PID collection size %s doesn't match expected resource collection size of %s",
					consumedPids.size(),
					loadedResources.size());
			for (int i = consumedPids.size() - 1; i >= 0; i--) {
				if (accessDetails.isDontReturnResourceAtIndex(i)) {
					consumedPids.remove(i);
					loadedResources.remove(i);
				}
			}

			theLoadedResourcesToOptionallyPopulate.addAll(loadedResources);
		}

		// truncate the list we retrieved - if needed
		if (hasACount) {
			// we want the accurate received resource count
			if (theReceivedResourceCountToPopulate.get() == -1) {
				theReceivedResourceCountToPopulate.set(consumedPids.size());
			} else {
				theReceivedResourceCountToPopulate.increment(consumedPids.size());
			}
			int resourcesToReturn = Math.min(theRequestedCount, consumedPids.size());
			consumedPids.subList(resourcesToReturn, consumedPids.size()).clear();
		}

		return new PerformedSearchResult(consumedPids, receivedAsManyResourcesAsRequested);
	}

	@Override
	public IBundleProvider executeQuery(
			String theResourceType,
			SearchParameterMap theSearchParameterMap,
			RequestPartitionId theRequestPartitionId) {
		final String searchUuid = UUID.randomUUID().toString();

		Class<? extends IBaseResource> resourceTypeClass =
				myContext.getResourceDefinition(theResourceType).getImplementingClass();
		final ISearchBuilder<JpaPid> sb = mySearchBuilderFactory.newSearchBuilder(theResourceType, resourceTypeClass);
		sb.setFetchSize(mySyncSize);
		return createNewSearch(
				theSearchParameterMap,
				null,
				searchUuid,
				sb,
				theSearchParameterMap.getLoadSynchronousUpTo(),
				theRequestPartitionId);
	}

	@Autowired
	public void setContext(FhirContext theContext) {
		myContext = theContext;
	}

	private int getTotalCount(Integer queryCount, Integer offset, int queryResultCount) {
		if (queryCount != null) {
			if (offset != null) {
				return offset + queryResultCount;
			} else {
				return queryResultCount;
			}
		} else {
			return queryResultCount;
		}
	}

	private Integer getQueryCount(Integer theLoadSynchronousUpTo, SearchParameterMap theParams) {
		if (theLoadSynchronousUpTo != null) {
			return theLoadSynchronousUpTo;
		} else if (theParams.getCount() != null) {
			return theParams.getCount();
		} else if (myStorageSettings.getFetchSizeDefaultMaximum() != null) {
			return myStorageSettings.getFetchSizeDefaultMaximum();
		}
		return null;
	}

	private record PerformedSearchResult(List<JpaPid> receivedPids, boolean receivedAsManyResourcesAsRequested) {}

	@SuppressWarnings("ClassCanBeRecord")
	private static class StatelessSearchResultConsumer implements ISearchResultConsumer<JpaPid> {
		private final List<JpaPid> myConsumedPids;
		private final Integer myLoadSynchronousUpTo;
		private final SearchParameterMap myParams;

		public StatelessSearchResultConsumer(
				List<JpaPid> theConsumedPids, Integer theLoadSynchronousUpTo, SearchParameterMap theParams) {
			myConsumedPids = theConsumedPids;
			myLoadSynchronousUpTo = theLoadSynchronousUpTo;
			myParams = theParams;
		}

		@Nonnull
		@Override
		public Outcome consume(SearchProgressTracker theProgressTracker, JpaPid theResult) {
			myConsumedPids.add(theResult);
			if (myLoadSynchronousUpTo != null && myConsumedPids.size() >= myLoadSynchronousUpTo) {
				return ISearchResultConsumer.STOP;
			}
			if (myParams.getLoadSynchronousUpTo() != null
					&& myConsumedPids.size() >= myParams.getLoadSynchronousUpTo()) {
				return ISearchResultConsumer.STOP;
			}
			return ISearchResultConsumer.CONTINUE;
		}
	}
}
