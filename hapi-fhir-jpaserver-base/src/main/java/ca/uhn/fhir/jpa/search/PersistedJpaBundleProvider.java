package ca.uhn.fhir.jpa.search;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IDao;
import ca.uhn.fhir.jpa.dao.SearchBuilder;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.entity.BaseHasResource;
import ca.uhn.fhir.jpa.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.entity.SearchResult;
import ca.uhn.fhir.jpa.entity.SearchTypeEnum;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.server.IBundleProvider;

public final class PersistedJpaBundleProvider implements IBundleProvider {

	private FhirContext myContext;
	private IDao myDao;
	private EntityManager myEntityManager;
	private PlatformTransactionManager myPlatformTransactionManager;
	private IResourceHistoryTableDao myResourceHistoryTableDao;
	private ISearchDao mySearchDao;
	private Search mySearchEntity;
	private ISearchResultDao mySearchResultDao;
	private String myUuid;

	public PersistedJpaBundleProvider(String theSearchUuid, IDao theDao) {
		myUuid = theSearchUuid;
		myDao = theDao;
	}

	@Autowired
	private IResourceTableDao myResourceTableDao;
	
	protected List<IBaseResource> doHistoryInTransaction(int theFromIndex, int theToIndex) {
		
		Date cutoff = mySearchEntity.getLastUpdatedHigh();
		
		Pageable pageable = toPage(theFromIndex, theToIndex);
		
		List<ResourceHistoryTable> results;
		
		if (cutoff != null) {
			if (mySearchEntity.getResourceType() == null) {
				results = myResourceHistoryTableDao.findForAllResourceTypes(cutoff, pageable);
			} else if (mySearchEntity.getResourceId() == null) {
				results = myResourceHistoryTableDao.findForResourceType(mySearchEntity.getResourceType(), cutoff, pageable);
			} else {
				results = myResourceHistoryTableDao.findForResourceInstance(mySearchEntity.getResourceId(), cutoff, pageable);
			}
		} else {
			if (mySearchEntity.getResourceType() == null) {
				results = myResourceHistoryTableDao.findForAllResourceTypes(pageable);
			} else if (mySearchEntity.getResourceId() == null) {
				results = myResourceHistoryTableDao.findForResourceType(mySearchEntity.getResourceType(), pageable);
			} else {
				results = myResourceHistoryTableDao.findForResourceInstance(mySearchEntity.getResourceId(), pageable);
			}
		}
		
		ArrayList<IBaseResource> retVal = new ArrayList<IBaseResource>();
		for (ResourceHistoryTable next : results) {
			BaseHasResource resource;
			resource = next;
			
			retVal.add(myDao.toResource(resource, true));
		}
		
		return retVal;
	}

	protected List<IBaseResource> doSearchOrEverythingInTransaction(final int theFromIndex, final int theToIndex) {

		Pageable page = toPage(theFromIndex, theToIndex);
		if (page == null) {
			return Collections.emptyList();
		}
		
		Page<SearchResult> search = mySearchResultDao.findWithSearchUuid(mySearchEntity, page);

		List<Long> pidsSubList = new ArrayList<Long>();
		for (SearchResult next : search) {
			pidsSubList.add(next.getResourcePid());
		}

		// Load includes
		pidsSubList = new ArrayList<Long>(pidsSubList);

		Set<Long> revIncludedPids = new HashSet<Long>();
		if (mySearchEntity.getSearchType() == SearchTypeEnum.SEARCH) {
			revIncludedPids.addAll(SearchBuilder.loadReverseIncludes(myContext, myEntityManager, pidsSubList, mySearchEntity.toRevIncludesList(), true, mySearchEntity.getLastUpdated()));
		}
		revIncludedPids.addAll(SearchBuilder.loadReverseIncludes(myContext, myEntityManager, pidsSubList, mySearchEntity.toIncludesList(), false, mySearchEntity.getLastUpdated()));

		// Execute the query and make sure we return distinct results
		List<IBaseResource> resources = new ArrayList<IBaseResource>();
		SearchBuilder.loadResourcesByPid(pidsSubList, resources, revIncludedPids, false, myEntityManager, myContext, myDao);

		return resources;
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
						mySearchEntity = mySearchDao.findByUuid(myUuid);
						
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

	private void ensureDependenciesInjected() {
		if (myPlatformTransactionManager == null) {
		myDao.injectDependenciesIntoBundleProvider(this);
		}
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

		return template.execute(new TransactionCallback<List<IBaseResource>>() {
			@Override
			public List<IBaseResource> doInTransaction(TransactionStatus theStatus) {
				ensureSearchEntityLoaded();

				switch (mySearchEntity.getSearchType()) {
				case HISTORY:
					return doHistoryInTransaction(theFromIndex, theToIndex);
				case SEARCH:
				case EVERYTHING:
				default:
					return doSearchOrEverythingInTransaction(theFromIndex, theToIndex);
				}
			}


		});
	}

	public String getSearchUuid() {
		return myUuid;
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

	public void setResourceHistoryTableDao(IResourceHistoryTableDao theResourceHistoryTableDao) {
		myResourceHistoryTableDao = theResourceHistoryTableDao;
	}

	public void setSearchDao(ISearchDao theSearchDao) {
		mySearchDao = theSearchDao;
	}

	public void setSearchResultDao(ISearchResultDao theSearchResultDao) {
		mySearchResultDao = theSearchResultDao;
	}

	@Override
	public int size() {
		ensureSearchEntityLoaded();
		return mySearchEntity.getTotalCount();
	}

	private Pageable toPage(int theFromIndex, int theToIndex) {
		int pageSize = theToIndex - theFromIndex;
		if (pageSize < 1) {
			return null;
		}

		int pageIndex = theFromIndex / pageSize;

		Pageable page = new PageRequest(pageIndex, pageSize);
		return page;
	}
}