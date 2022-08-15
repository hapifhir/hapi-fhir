package ca.uhn.fhir.jpa.search;

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
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.interceptor.JpaPreResourceAccessDetails;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.ServerInterceptorUtil;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static ca.uhn.fhir.jpa.util.SearchParameterMapCalculator.isWantCount;
import static ca.uhn.fhir.jpa.util.SearchParameterMapCalculator.isWantOnlyCount;
import static java.util.Objects.nonNull;

public class SynchronousSearchSvcImpl implements ISynchronousSearchSvc {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SynchronousSearchSvcImpl.class);

	private FhirContext myContext;

	@Autowired
	private DaoConfig myDaoConfig;

	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private PlatformTransactionManager myManagedTxManager;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private EntityManager myEntityManager;

	private int mySyncSize = 250;

	public IBundleProvider executeQuery(SearchParameterMap theParams, RequestDetails theRequestDetails, String theSearchUuid, ISearchBuilder theSb, Integer theLoadSynchronousUpTo, RequestPartitionId theRequestPartitionId) {
		SearchRuntimeDetails searchRuntimeDetails = new SearchRuntimeDetails(theRequestDetails, theSearchUuid);
		searchRuntimeDetails.setLoadSynchronous(true);

		boolean theParamWantOnlyCount = isWantOnlyCount(theParams);
		boolean theParamOrConfigWantCount = nonNull(theParams.getSearchTotalMode()) ? isWantCount(theParams) : isWantCount(myDaoConfig.getDefaultTotalMode());
		boolean wantCount = theParamWantOnlyCount || theParamOrConfigWantCount;

		// Execute the query and make sure we return distinct results
		TransactionTemplate txTemplate = new TransactionTemplate(myManagedTxManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		txTemplate.setReadOnly(theParams.isLoadSynchronous() || theParams.isOffsetQuery());
		return txTemplate.execute(t -> {

			// Load the results synchronously
			final List<ResourcePersistentId> pids = new ArrayList<>();

			Long count = 0L;
			if (wantCount) {

				ourLog.trace("Performing count");
				// TODO FulltextSearchSvcImpl will remove necessary parameters from the "theParams", this will cause actual query after count to
				//  return wrong response. This is some dirty fix to avoid that issue. Params should not be mutated?
				//  Maybe instead of removing them we could skip them in db query builder if full text search was used?
				List<List<IQueryParameterType>> contentAndTerms = theParams.get(Constants.PARAM_CONTENT);
				List<List<IQueryParameterType>> textAndTerms = theParams.get(Constants.PARAM_TEXT);

				count = theSb.createCountQuery(theParams, theSearchUuid, theRequestDetails, theRequestPartitionId);

				if (contentAndTerms != null) theParams.put(Constants.PARAM_CONTENT, contentAndTerms);
				if (textAndTerms != null) theParams.put(Constants.PARAM_TEXT, textAndTerms);

				ourLog.trace("Got count {}", count);
			}

			if (theParamWantOnlyCount) {
				SimpleBundleProvider bundleProvider = new SimpleBundleProvider();
				bundleProvider.setSize(count.intValue());
				return bundleProvider;
			}

			try (IResultIterator resultIter = theSb.createQuery(theParams, searchRuntimeDetails, theRequestDetails, theRequestPartitionId)) {
				while (resultIter.hasNext()) {
					pids.add(resultIter.next());
					if (theLoadSynchronousUpTo != null && pids.size() >= theLoadSynchronousUpTo) {
						break;
					}
					if (theParams.getLoadSynchronousUpTo() != null && pids.size() >= theParams.getLoadSynchronousUpTo()) {
						break;
					}
				}
			} catch (IOException e) {
				ourLog.error("IO failure during database access", e);
				throw new InternalErrorException(Msg.code(1164) + e);
			}

			JpaPreResourceAccessDetails accessDetails = new JpaPreResourceAccessDetails(pids, () -> theSb);
			HookParams params = new HookParams()
				.add(IPreResourceAccessDetails.class, accessDetails)
				.add(RequestDetails.class, theRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
			CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequestDetails, Pointcut.STORAGE_PREACCESS_RESOURCES, params);

			for (int i = pids.size() - 1; i >= 0; i--) {
				if (accessDetails.isDontReturnResourceAtIndex(i)) {
					pids.remove(i);
				}
			}

			/*
			 * For synchronous queries, we load all the includes right away
			 * since we're returning a static bundle with all the results
			 * pre-loaded. This is ok because synchronous requests are not
			 * expected to be paged
			 *
			 * On the other hand for async queries we load includes/revincludes
			 * individually for pages as we return them to clients
			 */

			// _includes
			Integer maxIncludes = myDaoConfig.getMaximumIncludesToLoadPerPage();
			final Set<ResourcePersistentId> includedPids = theSb.loadIncludes(myContext, myEntityManager, pids, theParams.getRevIncludes(), true, theParams.getLastUpdated(), "(synchronous)", theRequestDetails, maxIncludes);
			if (maxIncludes != null) {
				maxIncludes -= includedPids.size();
			}
			pids.addAll(includedPids);
			List<ResourcePersistentId> includedPidsList = new ArrayList<>(includedPids);

			// _revincludes
			if (theParams.getEverythingMode() == null && (maxIncludes == null || maxIncludes > 0)) {
				Set<ResourcePersistentId> revIncludedPids = theSb.loadIncludes(myContext, myEntityManager, pids, theParams.getIncludes(), false, theParams.getLastUpdated(), "(synchronous)", theRequestDetails, maxIncludes);
				includedPids.addAll(revIncludedPids);
				pids.addAll(revIncludedPids);
				includedPidsList.addAll(revIncludedPids);
			}

			List<IBaseResource> resources = new ArrayList<>();
			theSb.loadResourcesByPid(pids, includedPidsList, resources, false, theRequestDetails);
			// Hook: STORAGE_PRESHOW_RESOURCES
			resources = ServerInterceptorUtil.fireStoragePreshowResource(resources, theRequestDetails, myInterceptorBroadcaster);

			SimpleBundleProvider bundleProvider = new SimpleBundleProvider(resources);
			if (theParams.isOffsetQuery()) {
				bundleProvider.setCurrentPageOffset(theParams.getOffset());
				bundleProvider.setCurrentPageSize(theParams.getCount());
			}

			if (wantCount) {
				bundleProvider.setSize(count.intValue());
			} else {
				Integer queryCount = getQueryCount(theLoadSynchronousUpTo, theParams);
				if (queryCount == null || queryCount > resources.size()) {
					// No limit, last page or everything was fetched within the limit
					bundleProvider.setSize(getTotalCount(queryCount, theParams.getOffset(), resources.size()));
				} else {
					bundleProvider.setSize(null);
				}
			}

			bundleProvider.setPreferredPageSize(theParams.getCount());

			return bundleProvider;
		});
	}

	@Override
	public IBundleProvider executeQuery(String theResourceType, SearchParameterMap theSearchParameterMap, RequestPartitionId theRequestPartitionId) {
		final String searchUuid = UUID.randomUUID().toString();

		IFhirResourceDao<?> callingDao = myDaoRegistry.getResourceDao(theResourceType);

		Class<? extends IBaseResource> resourceTypeClass = myContext.getResourceDefinition(theResourceType).getImplementingClass();
		final ISearchBuilder sb = mySearchBuilderFactory.newSearchBuilder(callingDao, theResourceType, resourceTypeClass);
		sb.setFetchSize(mySyncSize);
		return executeQuery(theSearchParameterMap, null, searchUuid, sb, theSearchParameterMap.getLoadSynchronousUpTo(), theRequestPartitionId);
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
		} else if (myDaoConfig.getFetchSizeDefaultMaximum() != null) {
			return myDaoConfig.getFetchSizeDefaultMaximum();
		}
		return null;
	}
}
