package ca.uhn.fhir.jpa.search;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.entity.BaseHasResource;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.search.cache.ISearchCacheSvc;
import ca.uhn.fhir.jpa.util.InterceptorUtil;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IPreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceAccessDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceShowDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
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
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PersistedJpaBundleProvider implements IBundleProvider {

	private static final Logger ourLog = LoggerFactory.getLogger(PersistedJpaBundleProvider.class);

	/*
	 * Autowired fields
	 */

	@PersistenceContext
	private EntityManager myEntityManager;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	protected PlatformTransactionManager myTxManager;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired
	private ISearchCacheSvc mySearchCacheSvc;

	/*
	 * Non autowired fields (will be different for every instance
	 * of this class, since it's a prototype
	 */

	private final RequestDetails myRequest;
	private Search mySearchEntity;
	private String myUuid;
	private boolean myCacheHit;

	/**
	 * Constructor
	 */
	public PersistedJpaBundleProvider(RequestDetails theRequest, String theSearchUuid) {
		myRequest = theRequest;
		myUuid = theSearchUuid;
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

	private List<IBaseResource> doHistoryInTransaction(int theFromIndex, int theToIndex) {
		List<ResourceHistoryTable> results;

		CriteriaBuilder cb = myEntityManager.getCriteriaBuilder();
		CriteriaQuery<ResourceHistoryTable> q = cb.createQuery(ResourceHistoryTable.class);
		Root<ResourceHistoryTable> from = q.from(ResourceHistoryTable.class);
		List<Predicate> predicates = new ArrayList<>();

		if (mySearchEntity.getResourceType() == null) {
			// All resource types
		} else if (mySearchEntity.getResourceId() == null) {
			predicates.add(cb.equal(from.get("myResourceType"), mySearchEntity.getResourceType()));
		} else {
			predicates.add(cb.equal(from.get("myResourceId"), mySearchEntity.getResourceId()));
		}

		if (mySearchEntity.getLastUpdatedLow() != null) {
			predicates.add(cb.greaterThanOrEqualTo(from.get("myUpdated").as(Date.class), mySearchEntity.getLastUpdatedLow()));
		}
		if (mySearchEntity.getLastUpdatedHigh() != null) {
			predicates.add(cb.lessThanOrEqualTo(from.get("myUpdated").as(Date.class), mySearchEntity.getLastUpdatedHigh()));
		}

		if (predicates.size() > 0) {
			q.where(predicates.toArray(new Predicate[0]));
		}

		q.orderBy(cb.desc(from.get("myUpdated")));

		TypedQuery<ResourceHistoryTable> query = myEntityManager.createQuery(q);

		if (theToIndex - theFromIndex > 0) {
			query.setFirstResult(theFromIndex);
			query.setMaxResults(theToIndex - theFromIndex);
		}

		results = query.getResultList();

		ArrayList<IBaseResource> retVal = new ArrayList<>();
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
			JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.STORAGE_PREACCESS_RESOURCES, params);

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
			JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, myRequest, Pointcut.STORAGE_PRESHOW_RESOURCES, params);
		}


		return retVal;
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
		return true;
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
				return template.execute(theStatus -> doHistoryInTransaction(theFromIndex, theToIndex));
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

	public boolean isCacheHit() {
		return myCacheHit;
	}

	void setCacheHit() {
		myCacheHit = true;
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

	// Note: Leave as protected, HSPC depends on this
	@SuppressWarnings("WeakerAccess")
	protected List<IBaseResource> toResourceList(ISearchBuilder theSearchBuilder, List<ResourcePersistentId> thePids) {
		Set<ResourcePersistentId> includedPids = new HashSet<>();

		if (mySearchEntity.getSearchType() == SearchTypeEnum.SEARCH) {
			includedPids.addAll(theSearchBuilder.loadIncludes(myContext, myEntityManager, thePids, mySearchEntity.toRevIncludesList(), true, mySearchEntity.getLastUpdated(), myUuid, myRequest));
			includedPids.addAll(theSearchBuilder.loadIncludes(myContext, myEntityManager, thePids, mySearchEntity.toIncludesList(), false, mySearchEntity.getLastUpdated(), myUuid, myRequest));
		}

		List<ResourcePersistentId> includedPidList = new ArrayList<>(includedPids);

		// Execute the query and make sure we return distinct results
		List<IBaseResource> resources = new ArrayList<>();
		theSearchBuilder.loadResourcesByPid(thePids, includedPidList, resources, false, myRequest);

		InterceptorUtil.fireStoragePreshowResource(resources, myRequest, myInterceptorBroadcaster);

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
	public void setSearchBuilderFactoryForUnitTest(SearchBuilderFactory theSearchBuilderFactory) {
		mySearchBuilderFactory = theSearchBuilderFactory;
	}
}
