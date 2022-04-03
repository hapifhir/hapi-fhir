package ca.uhn.fhir.jpa.search;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.dao.HistoryBuilder;
import ca.uhn.fhir.jpa.dao.HistoryBuilderFactory;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.jpa.model.entity.BaseHasResource;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.partition.RequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.search.cache.SearchCacheStatusEnum;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.interceptor.ServerInterceptorUtil;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class PersistedJpaBundleProvider implements IBundleProvider {

	private static final Logger ourLog = LoggerFactory.getLogger(PersistedJpaBundleProvider.class);

	/*
	 * Autowired fields
	 */
	private final RequestDetails myRequest;
	@Autowired
	protected PlatformTransactionManager myTxManager;
	@PersistenceContext
	private EntityManager myEntityManager;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;
	@Autowired
	private HistoryBuilderFactory myHistoryBuilderFactory;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired
	private ISearchCacheSvc mySearchCacheSvc;
	@Autowired
	private RequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@Autowired
	private DaoConfig myDaoConfig;

	/*
	 * Non autowired fields (will be different for every instance
	 * of this class, since it's a prototype
	 */
	@Autowired
	private MemoryCacheService myMemoryCacheService;
	private Search mySearchEntity;
	private String myUuid;
	private SearchCacheStatusEnum myCacheStatus;
	private RequestPartitionId myRequestPartitionId;

	/**
	 * Constructor
	 */
	public PersistedJpaBundleProvider(RequestDetails theRequest, String theSearchUuid) {
		myRequest = theRequest;
		myUuid = theSearchUuid;
	}

	/**
	 * Constructor
	 */
	public PersistedJpaBundleProvider(RequestDetails theRequest, Search theSearch) {
		myRequest = theRequest;
		mySearchEntity = theSearch;
	}

	/**
	 * When HAPI FHIR server is running "for real", a new
	 * instance of the bundle provider is created to serve
	 * every HTTP request, so it's ok for us to keep
	 * state in here and expect that it will go away. But
	 * in unit tests we keep this object around for longer
	 * sometimes.
	 */
	public void clearCachedDataForUnitTest() {
		mySearchEntity = null;
	}

	/**
	 * Perform a history search
	 */
	private List<IBaseResource> doHistoryInTransaction(Integer theOffset, int theFromIndex, int theToIndex) {

		HistoryBuilder historyBuilder = myHistoryBuilderFactory.newHistoryBuilder(mySearchEntity.getResourceType(), mySearchEntity.getResourceId(), mySearchEntity.getLastUpdatedLow(), mySearchEntity.getLastUpdatedHigh());

		RequestPartitionId partitionId = getRequestPartitionIdForHistory();
		List<ResourceHistoryTable> results = historyBuilder.fetchEntities(partitionId, theOffset, theFromIndex, theToIndex);

		List<IBaseResource> retVal = new ArrayList<>();
		for (ResourceHistoryTable next : results) {
			BaseHasResource resource;
			resource = next;

			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(next.getResourceType());
			retVal.add(dao.toResource(resource, true));
		}


		// Interceptor call: STORAGE_PREACCESS_RESOURCES
		{
			SimplePreResourceAccessDetails accessDetails = new SimplePreResourceAccessDetails(retVal);
			HookParams params = new HookParams()
				.add(IPreResourceAccessDetails.class, accessDetails)
				.add(RequestDetails.class, myRequest)
				.addIfMatchesType(ServletRequestDetails.class, myRequest);
			CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.STORAGE_PREACCESS_RESOURCES, params);

			for (int i = retVal.size() - 1; i >= 0; i--) {
				if (accessDetails.isDontReturnResourceAtIndex(i)) {
					retVal.remove(i);
				}
			}
		}

		// Interceptor broadcast: STORAGE_PRESHOW_RESOURCES
		{
			SimplePreResourceShowDetails showDetails = new SimplePreResourceShowDetails(retVal);
			HookParams params = new HookParams()
				.add(IPreResourceShowDetails.class, showDetails)
				.add(RequestDetails.class, myRequest)
				.addIfMatchesType(ServletRequestDetails.class, myRequest);
			CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.STORAGE_PRESHOW_RESOURCES, params);
			retVal = showDetails.toList();
		}


		return retVal;
	}

	@Nonnull
	private RequestPartitionId getRequestPartitionIdForHistory() {
		if (myRequestPartitionId == null) {
			if (mySearchEntity.getResourceId() != null) {
				// If we have an ID, we've already checked the partition and made sure it's appropriate
				myRequestPartitionId = RequestPartitionId.allPartitions();
			} else {
				myRequestPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequest(myRequest, mySearchEntity.getResourceType(), null);
			}
		}
		return myRequestPartitionId;
	}

	protected List<IBaseResource> doSearchOrEverything(final int theFromIndex, final int theToIndex) {
		if (mySearchEntity.getTotalCount() != null && mySearchEntity.getNumFound() <= 0) {
			// No resources to fetch (e.g. we did a _summary=count search)
			return Collections.emptyList();
		}
		String resourceName = mySearchEntity.getResourceType();
		Class<? extends IBaseResource> resourceType = myContext.getResourceDefinition(resourceName).getImplementingClass();
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceName);

		final ISearchBuilder sb = mySearchBuilderFactory.newSearchBuilder(dao, resourceName, resourceType);

		final List<ResourcePersistentId> pidsSubList = mySearchCoordinatorSvc.getResources(myUuid, theFromIndex, theToIndex, myRequest);

		TransactionTemplate template = new TransactionTemplate(myTxManager);
		template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		return template.execute(theStatus -> toResourceList(sb, pidsSubList));
	}


	/**
	 * Returns false if the entity can't be found
	 */
	public boolean ensureSearchEntityLoaded() {
		if (mySearchEntity == null) {
			Optional<Search> searchOpt = mySearchCacheSvc.fetchByUuid(myUuid);
			if (!searchOpt.isPresent()) {
				return false;
			}

			setSearchEntity(searchOpt.get());

			ourLog.trace("Retrieved search with version {} and total {}", mySearchEntity.getVersion(), mySearchEntity.getTotalCount());

			return true;
		}

		if (mySearchEntity.getSearchType() == SearchTypeEnum.HISTORY) {
			if (mySearchEntity.getTotalCount() == null) {
				calculateHistoryCount();
			}
		}

		return true;
	}

	/**
	 * Note that this method is called outside a DB transaction, and uses a loading cache
	 * (assuming the default {@literal COUNT_CACHED} mode) so this effectively throttles
	 * access to the database by preventing multiple concurrent DB calls for an expensive
	 * count operation.
	 */
	private void calculateHistoryCount() {
		MemoryCacheService.HistoryCountKey key;
		if (mySearchEntity.getResourceId() != null) {
			key = MemoryCacheService.HistoryCountKey.forInstance(mySearchEntity.getResourceId());
		} else if (mySearchEntity.getResourceType() != null) {
			key = MemoryCacheService.HistoryCountKey.forType(mySearchEntity.getResourceType());
		} else {
			key = MemoryCacheService.HistoryCountKey.forSystem();
		}

		Function<MemoryCacheService.HistoryCountKey, Integer> supplier = k -> new TransactionTemplate(myTxManager).execute(t -> {
			HistoryBuilder historyBuilder = myHistoryBuilderFactory.newHistoryBuilder(mySearchEntity.getResourceType(), mySearchEntity.getResourceId(), mySearchEntity.getLastUpdatedLow(), mySearchEntity.getLastUpdatedHigh());
			Long count = historyBuilder.fetchCount(getRequestPartitionIdForHistory());
			return count.intValue();
		});

		boolean haveOffset = mySearchEntity.getLastUpdatedLow() != null || mySearchEntity.getLastUpdatedHigh() != null;

		switch (myDaoConfig.getHistoryCountMode()) {
			case COUNT_ACCURATE: {
				int count = supplier.apply(key);
				mySearchEntity.setTotalCount(count);
				break;
			}
			case CACHED_ONLY_WITHOUT_OFFSET: {
				if (!haveOffset) {
					int count = myMemoryCacheService.get(MemoryCacheService.CacheEnum.HISTORY_COUNT, key, supplier);
					mySearchEntity.setTotalCount(count);
				}
				break;
			}
			case COUNT_DISABLED: {
				break;
			}
		}

	}

	@Override
	public InstantDt getPublished() {
		ensureSearchEntityLoaded();
		return new InstantDt(mySearchEntity.getCreated());
	}

	@Nonnull
	@Override
	public List<IBaseResource> getResources(final int theFromIndex, final int theToIndex) {
		TransactionTemplate template = new TransactionTemplate(myTxManager);

		template.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				boolean entityLoaded = ensureSearchEntityLoaded();
				assert entityLoaded;
			}
		});

		assert mySearchEntity != null;
		assert mySearchEntity.getSearchType() != null;

		switch (mySearchEntity.getSearchType()) {
			case HISTORY:
				return template.execute(theStatus -> doHistoryInTransaction(mySearchEntity.getOffset(), theFromIndex, theToIndex));
			case SEARCH:
			case EVERYTHING:
			default:
				List<IBaseResource> retVal = doSearchOrEverything(theFromIndex, theToIndex);
				/*
				 * If we got fewer resources back than we asked for, it's possible that the search
				 * completed. If that's the case, the cached version of the search entity is probably
				 * no longer valid so let's force a reload if it gets asked for again (most likely
				 * because someone is calling size() on us)
				 */
				if (retVal.size() < theToIndex - theFromIndex) {
					mySearchEntity = null;
				}
				return retVal;
		}
	}

	@Override
	public String getUuid() {
		return myUuid;
	}

	public SearchCacheStatusEnum getCacheStatus() {
		return myCacheStatus;
	}

	void setCacheStatus(SearchCacheStatusEnum theSearchCacheStatusEnum) {
		myCacheStatus = theSearchCacheStatusEnum;
	}

	@Override
	public Integer preferredPageSize() {
		ensureSearchEntityLoaded();
		return mySearchEntity.getPreferredPageSize();
	}

	public void setContext(FhirContext theContext) {
		myContext = theContext;
	}

	public void setEntityManager(EntityManager theEntityManager) {
		myEntityManager = theEntityManager;
	}

	@VisibleForTesting
	public void setSearchCoordinatorSvcForUnitTest(ISearchCoordinatorSvc theSearchCoordinatorSvc) {
		mySearchCoordinatorSvc = theSearchCoordinatorSvc;
	}

	@VisibleForTesting
	public void setTxManagerForUnitTest(PlatformTransactionManager theTxManager) {
		myTxManager = theTxManager;
	}

	// Note: Leave as protected, HSPC depends on this
	@SuppressWarnings("WeakerAccess")
	protected void setSearchEntity(Search theSearchEntity) {
		mySearchEntity = theSearchEntity;
	}

	@Override
	public Integer size() {
		ensureSearchEntityLoaded();
		SearchCoordinatorSvcImpl.verifySearchHasntFailedOrThrowInternalErrorException(mySearchEntity);

		Integer size = mySearchEntity.getTotalCount();
		if (size != null) {
			return Math.max(0, size);
		}

		if (mySearchEntity.getSearchType() == SearchTypeEnum.HISTORY) {
			return null;
		} else {
			return mySearchCoordinatorSvc.getSearchTotal(myUuid).orElse(null);
		}

	}

	protected boolean hasIncludes() {
		ensureSearchEntityLoaded();
		return !mySearchEntity.getIncludes().isEmpty();
	}

	// Note: Leave as protected, HSPC depends on this
	@SuppressWarnings("WeakerAccess")
	protected List<IBaseResource> toResourceList(ISearchBuilder theSearchBuilder, List<ResourcePersistentId> thePids) {

		List<ResourcePersistentId> includedPidList = new ArrayList<>();
		if (mySearchEntity.getSearchType() == SearchTypeEnum.SEARCH) {
			Integer maxIncludes = myDaoConfig.getMaximumIncludesToLoadPerPage();

			// Load _revincludes
			Set<ResourcePersistentId> includedPids = theSearchBuilder.loadIncludes(myContext, myEntityManager, thePids, mySearchEntity.toRevIncludesList(), true, mySearchEntity.getLastUpdated(), myUuid, myRequest, maxIncludes);
			if (maxIncludes != null) {
				maxIncludes -= includedPids.size();
			}
			thePids.addAll(includedPids);
			includedPidList.addAll(includedPids);

			// Load _includes
			Set<ResourcePersistentId> revIncludedPids = theSearchBuilder.loadIncludes(myContext, myEntityManager, thePids, mySearchEntity.toIncludesList(), false, mySearchEntity.getLastUpdated(), myUuid, myRequest, maxIncludes);
			thePids.addAll(revIncludedPids);
			includedPidList.addAll(revIncludedPids);

		}

		// Execute the query and make sure we return distinct results
		List<IBaseResource> resources = new ArrayList<>();
		theSearchBuilder.loadResourcesByPid(thePids, includedPidList, resources, false, myRequest);

		resources = ServerInterceptorUtil.fireStoragePreshowResource(resources, myRequest, myInterceptorBroadcaster);

		return resources;
	}

	public void setInterceptorBroadcaster(IInterceptorBroadcaster theInterceptorBroadcaster) {
		myInterceptorBroadcaster = theInterceptorBroadcaster;
	}

	@VisibleForTesting
	public void setSearchCacheSvcForUnitTest(ISearchCacheSvc theSearchCacheSvc) {
		mySearchCacheSvc = theSearchCacheSvc;
	}

	@VisibleForTesting
	public void setDaoRegistryForUnitTest(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
	}

	@VisibleForTesting
	public void setDaoConfigForUnitTest(DaoConfig theDaoConfig) {
		myDaoConfig = theDaoConfig;
	}

	@VisibleForTesting
	public void setSearchBuilderFactoryForUnitTest(SearchBuilderFactory theSearchBuilderFactory) {
		mySearchBuilderFactory = theSearchBuilderFactory;
	}
}
