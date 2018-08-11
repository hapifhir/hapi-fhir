package ca.uhn.fhir.jpa.search;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import ca.uhn.fhir.jpa.dao.IDao;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

public class PersistedJpaBundleProvider implements IBundleProvider {

	private FhirContext myContext;
	private IDao myDao;
	private EntityManager myEntityManager;
	private PlatformTransactionManager myPlatformTransactionManager;
	private ISearchCoordinatorSvc mySearchCoordinatorSvc;
	private ISearchDao mySearchDao;
	private Search mySearchEntity;
	private String myUuid;
	private boolean myCacheHit;

	public PersistedJpaBundleProvider(String theSearchUuid, IDao theDao) {
		myUuid = theSearchUuid;
		myDao = theDao;
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
			q.where(predicates.toArray(new Predicate[predicates.size()]));
		}

		q.orderBy(cb.desc(from.get("myUpdated")));

		TypedQuery<ResourceHistoryTable> query = myEntityManager.createQuery(q);

		if (theToIndex - theFromIndex > 0) {
			query.setFirstResult(theFromIndex);
			query.setMaxResults(theToIndex - theFromIndex);
		}

		results = query.getResultList();

		ArrayList<IBaseResource> retVal = new ArrayList<IBaseResource>();
		for (ResourceHistoryTable next : results) {
			BaseHasResource resource;
			resource = next;

			retVal.add(myDao.toResource(resource, true));
		}

		return retVal;
	}

	protected List<IBaseResource> doSearchOrEverything(final int theFromIndex, final int theToIndex) {
		final ISearchBuilder sb = myDao.newSearchBuilder();

		String resourceName = mySearchEntity.getResourceType();
		Class<? extends IBaseResource> resourceType = myContext.getResourceDefinition(resourceName).getImplementingClass();
		sb.setType(resourceType, resourceName);

		final List<Long> pidsSubList = mySearchCoordinatorSvc.getResources(myUuid, theFromIndex, theToIndex);

		TransactionTemplate template = new TransactionTemplate(myPlatformTransactionManager);
		template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		return template.execute(theStatus -> toResourceList(sb, pidsSubList));
	}

	private void ensureDependenciesInjected() {
		if (myPlatformTransactionManager == null) {
			myDao.injectDependenciesIntoBundleProvider(this);
		}
	}

	/**
	 * Returns false if the entity can't be found
	 */
	public boolean ensureSearchEntityLoaded() {
		if (mySearchEntity == null) {
			ensureDependenciesInjected();

			TransactionTemplate template = new TransactionTemplate(myPlatformTransactionManager);
			template.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRED);
			return template.execute(new TransactionCallback<Boolean>() {
				@Override
				public Boolean doInTransaction(TransactionStatus theStatus) {
					try {
						setSearchEntity(mySearchDao.findByUuid(myUuid));

						if (mySearchEntity == null) {
							return false;
						}

						// Load the includes now so that they are available outside of this transaction
						mySearchEntity.getIncludes().size();

						return true;
					} catch (NoResultException e) {
						return false;
					}
				}
			});
		}
		return true;
	}

	@Override
	public InstantDt getPublished() {
		ensureSearchEntityLoaded();
		return new InstantDt(mySearchEntity.getCreated());
	}

	@Override
	public List<IBaseResource> getResources(final int theFromIndex, final int theToIndex) {
		ensureDependenciesInjected();

		TransactionTemplate template = new TransactionTemplate(myPlatformTransactionManager);

		template.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				ensureSearchEntityLoaded();
			}
		});

		switch (mySearchEntity.getSearchType()) {
			case HISTORY:
				return template.execute(new TransactionCallback<List<IBaseResource>>() {
					@Override
					public List<IBaseResource> doInTransaction(TransactionStatus theStatus) {
						return doHistoryInTransaction(theFromIndex, theToIndex);
					}
				});
			case SEARCH:
			case EVERYTHING:
			default:
				return doSearchOrEverything(theFromIndex, theToIndex);
		}
	}

	@Override
	public String getUuid() {
		return myUuid;
	}

	public boolean isCacheHit() {
		return myCacheHit;
	}

	void setCacheHit(boolean theCacheHit) {
		myCacheHit = theCacheHit;
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

	public void setPlatformTransactionManager(PlatformTransactionManager thePlatformTransactionManager) {
		myPlatformTransactionManager = thePlatformTransactionManager;
	}

	public void setSearchCoordinatorSvc(ISearchCoordinatorSvc theSearchCoordinatorSvc) {
		mySearchCoordinatorSvc = theSearchCoordinatorSvc;
	}

	public void setSearchDao(ISearchDao theSearchDao) {
		mySearchDao = theSearchDao;
	}

	void setSearchEntity(Search theSearchEntity) {
		mySearchEntity = theSearchEntity;
	}

	@Override
	public Integer size() {
		ensureSearchEntityLoaded();
		SearchCoordinatorSvcImpl.verifySearchHasntFailedOrThrowInternalErrorException(mySearchEntity);

		Integer size = mySearchEntity.getTotalCount();
		if (size == null) {
			return null;
		}
		return Math.max(0, size);
	}

	List<IBaseResource> toResourceList(ISearchBuilder sb, List<Long> pidsSubList) {
		Set<Long> includedPids = new HashSet<>();
		if (mySearchEntity.getSearchType() == SearchTypeEnum.SEARCH) {
			includedPids.addAll(sb.loadIncludes(myDao, myContext, myEntityManager, pidsSubList, mySearchEntity.toRevIncludesList(), true, mySearchEntity.getLastUpdated()));
			includedPids.addAll(sb.loadIncludes(myDao, myContext, myEntityManager, pidsSubList, mySearchEntity.toIncludesList(), false, mySearchEntity.getLastUpdated()));
		}

		// Execute the query and make sure we return distinct results
		List<IBaseResource> resources = new ArrayList<>();
		sb.loadResourcesByPid(pidsSubList, resources, includedPids, false, myEntityManager, myContext, myDao);

		return resources;
	}

}
