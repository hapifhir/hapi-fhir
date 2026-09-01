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
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.interceptor.JpaPreResourceAccessDetails;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.SearchParameterMapCalculator;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.interceptor.ServerInterceptorUtil;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

	private int mySyncSize = 250;

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public IBundleProvider createNewSearch(
			SearchParameterMap theParams,
			RequestDetails theRequestDetails,
			String theSearchUuid,
			ISearchBuilder theSb,
			Integer theLoadSynchronousUpTo,
			RequestPartitionId theRequestPartitionId) {
		SearchRuntimeDetails searchRuntimeDetails = new SearchRuntimeDetails(theRequestDetails, theSearchUuid);
		searchRuntimeDetails.setLoadSynchronous(true);

		boolean theParamWantOnlyCount = isWantOnlyCount(theParams);
		boolean theParamOrConfigWantCount = SearchParameterMapCalculator.isWantCount(theParams, myStorageSettings);
		boolean wantCount = theParamWantOnlyCount || theParamOrConfigWantCount;

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

					// if we have a count, we'll want to request
					// additional resources
					SearchParameterMap clonedParams = theParams.clone();
					Integer requestedCount = clonedParams.getCount();
					boolean hasACount = requestedCount != null;
					if (hasACount) {
						clonedParams.setCount(requestedCount + 1);
					}

					// Perform the actual search
					// Load the results synchronously
					final List<JpaPid> consumedPids = new ArrayList<>();
					ISearchResultConsumer<JpaPid> searchResultConsumer = (progress, pid) -> {
						consumedPids.add(pid);
						if (theLoadSynchronousUpTo != null && consumedPids.size() >= theLoadSynchronousUpTo) {
							return ISearchResultConsumer.STOP;
						}
						if (theParams.getLoadSynchronousUpTo() != null
								&& consumedPids.size() >= theParams.getLoadSynchronousUpTo()) {
							return ISearchResultConsumer.STOP;
						}
						return ISearchResultConsumer.CONTINUE;
					};
					theSb.performSearchForPids(
							searchResultConsumer,
							clonedParams,
							searchRuntimeDetails,
							theRequestDetails,
							theRequestPartitionId);
					List<JpaPid> pids = consumedPids;

					// truncate the list we retrieved - if needed
					int receivedResourceCount = -1;
					if (hasACount) {
						// we want the accurate received resource count
						receivedResourceCount = pids.size();
						int resourcesToReturn = Math.min(theParams.getCount(), pids.size());
						pids = pids.subList(0, resourcesToReturn);
					}

					IInterceptorBroadcaster compositeBroadcaster =
							CompositeInterceptorBroadcaster.newCompositeBroadcaster(
									myInterceptorBroadcaster, theRequestDetails);

					List<IBaseResource> loadedResources = null;
					if (compositeBroadcaster.hasHooks(Pointcut.STORAGE_PREACCESS_RESOURCES)) {

						loadedResources = new ArrayList<>();
						theSb.loadResourcesByPid(pids, Collections.emptySet(), loadedResources, false, null);
						JpaPreResourceAccessDetails accessDetails =
								new JpaPreResourceAccessDetails(pids, loadedResources);

						HookParams params = new HookParams()
								.add(IPreResourceAccessDetails.class, accessDetails)
								.add(RequestDetails.class, theRequestDetails)
								.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
						compositeBroadcaster.callHooks(Pointcut.STORAGE_PREACCESS_RESOURCES, params);

						Validate.isTrue(pids.size() == loadedResources.size(), "PID collection size %s doesn't match expected resource collection size of %s", pids.size(), loadedResources.size());
						for (int i = pids.size() - 1; i >= 0; i--) {
							if (accessDetails.isDontReturnResourceAtIndex(i)) {
								pids.remove(i);
								loadedResources.remove(i);
							}
						}
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
							Set<JpaPid> revIncludedPids = theSb.loadIncludes(
									myContext,
									myEntityManager,
									originalPids,
									nonIterateRevIncludes,
									true,
									theParams.getLastUpdated(),
									"(synchronous)",
									theRequestDetails,
									maxIncludes);
							if (maxIncludes != null) {
								maxIncludes -= revIncludedPids.size();
							}
							pids.addAll(revIncludedPids);
							allIncludedPidsList.addAll(revIncludedPids);
						}

						// Phase 2: non-iterate `_include` on original search result PIDs
						// (use originalPids so `_include` only applies to the initial search results,
						// not to revincluded resources — per FHIR spec, without `:iterate`)
						if (theParams.getEverythingMode() == null
								&& !nonIterateIncludes.isEmpty()
								&& (maxIncludes == null || maxIncludes > 0)) {
							Set<JpaPid> forwardIncludedPids = theSb.loadIncludes(
									myContext,
									myEntityManager,
									originalPids,
									nonIterateIncludes,
									false,
									theParams.getLastUpdated(),
									"(synchronous)",
									theRequestDetails,
									maxIncludes);
							if (maxIncludes != null) {
								maxIncludes -= forwardIncludedPids.size();
							}
							pids.addAll(forwardIncludedPids);
							allIncludedPidsList.addAll(forwardIncludedPids);
						}

						// Phase 3: `_revinclude:iterate` on expanded PIDs (including non-iterate revinclude results)
						if (!iterateRevIncludes.isEmpty() && (maxIncludes == null || maxIncludes > 0)) {
							Set<JpaPid> iterateRevIncludedPids = theSb.loadIncludes(
									myContext,
									myEntityManager,
									pids,
									iterateRevIncludes,
									true,
									theParams.getLastUpdated(),
									"(synchronous)",
									theRequestDetails,
									maxIncludes);
							if (maxIncludes != null) {
								maxIncludes -= iterateRevIncludedPids.size();
							}
							pids.addAll(iterateRevIncludedPids);
							allIncludedPidsList.addAll(iterateRevIncludedPids);
						}

						// Phase 4: `_include:iterate` on all expanded PIDs (including revinclude results)
						if (theParams.getEverythingMode() == null
								&& !iterateIncludes.isEmpty()
								&& (maxIncludes == null || maxIncludes > 0)) {
							Set<JpaPid> iterateForwardIncludedPids = theSb.loadIncludes(
									myContext,
									myEntityManager,
									pids,
									iterateIncludes,
									false,
									theParams.getLastUpdated(),
									"(synchronous)",
									theRequestDetails,
									maxIncludes);
							pids.addAll(iterateForwardIncludedPids);
							allIncludedPidsList.addAll(iterateForwardIncludedPids);
						}
					}

					if (loadedResources == null) {
						loadedResources = new ArrayList<>();
						theSb.loadResourcesByPid(pids, allIncludedPidsList, loadedResources, false, theRequestDetails);
					} else if (!allIncludedPidsList.isEmpty()) {
						List<IBaseResource> includeResources = new ArrayList<>();
						theSb.loadResourcesByPid(
								allIncludedPidsList, allIncludedPidsList, includeResources, false, theRequestDetails);
						loadedResources.addAll(includeResources);
					}

					// Hook: STORAGE_PRESHOW_RESOURCES
					List<IBaseResource> resources = ServerInterceptorUtil.fireStoragePreshowResource(
							loadedResources, theRequestDetails, myInterceptorBroadcaster);

					SimpleBundleProvider bundleProvider = new SimpleBundleProvider(resources);

					if (hasACount && theSb.requiresTotal()) {
						bundleProvider.setTotalResourcesRequestedReturned(receivedResourceCount);
					}

					bundleProvider.setCurrentPageOffset(theParams.getOffset() != null ? theParams.getOffset() : 0);
					bundleProvider.setCurrentPageSize(theParams.getCount());

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
}
