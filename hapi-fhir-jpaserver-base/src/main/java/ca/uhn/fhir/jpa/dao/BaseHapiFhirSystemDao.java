package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.model.ExpungeOutcome;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public abstract class BaseHapiFhirSystemDao<T extends IBaseBundle, MT> extends BaseHapiFhirDao<IBaseResource> implements IFhirSystemDao<T, MT> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseHapiFhirSystemDao.class);
	public static final Predicate[] EMPTY_PREDICATE_ARRAY = new Predicate[0];
	public ResourceCountCache myResourceCountsCache;
	@Autowired
	private TransactionProcessor myTransactionProcessor;
	@Autowired
	private ApplicationContext myApplicationContext;

	@VisibleForTesting
	public void setTransactionProcessorForUnitTest(TransactionProcessor theTransactionProcessor) {
		myTransactionProcessor = theTransactionProcessor;
	}

	@Override
	@PostConstruct
	public void start() {
		super.start();
		myTransactionProcessor.setDao(this);
	}

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public ExpungeOutcome expunge(ExpungeOptions theExpungeOptions, RequestDetails theRequestDetails) {
		return myExpungeService.expunge(null, null, theExpungeOptions, theRequestDetails);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Map<String, Long> getResourceCounts() {
		Map<String, Long> retVal = new HashMap<>();

		List<Map<?, ?>> counts = myResourceTableDao.getResourceCounts();
		for (Map<?, ?> next : counts) {
			retVal.put(next.get("type").toString(), Long.parseLong(next.get("count").toString()));
		}

		return retVal;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Nullable
	@Override
	public Map<String, Long> getResourceCountsFromCache() {
		if (myResourceCountsCache == null) {
			// Lazy load this to avoid a circular dependency
			myResourceCountsCache = myApplicationContext.getBean("myResourceCountsCache", ResourceCountCache.class);
		}
		return myResourceCountsCache.get();
	}

	@Override
	public IBundleProvider history(Date theSince, Date theUntil, Integer theOffset, RequestDetails theRequestDetails) {
		if (theRequestDetails != null) {
			// Notify interceptors
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails);
			notifyInterceptors(RestOperationTypeEnum.HISTORY_SYSTEM, requestDetails);
		}

		StopWatch w = new StopWatch();
		IBundleProvider retVal = super.history(theRequestDetails, null, null, theSince, theUntil, theOffset);
		ourLog.info("Processed global history in {}ms", w.getMillisAndRestart());
		return retVal;
	}

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public T transaction(RequestDetails theRequestDetails, T theRequest) {
		return myTransactionProcessor.transaction(theRequestDetails, theRequest, false);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public T transactionNested(RequestDetails theRequestDetails, T theRequest) {
		return myTransactionProcessor.transaction(theRequestDetails, theRequest, true);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void preFetchResources(List<ResourcePersistentId> theResolvedIds) {
		List<Long> pids = theResolvedIds
			.stream()
			.map(t -> t.getIdAsLong())
			.collect(Collectors.toList());

		new QueryChunker<Long>().chunk(pids, ids->{

			/*
			 * Pre-fetch the resources we're touching in this transaction in mass - this reduced the
			 * number of database round trips.
			 *
			 * The thresholds below are kind of arbitrary. It's not
			 * actually guaranteed that this pre-fetching will help (e.g. if a Bundle contains
			 * a bundle of NOP conditional creates for example, the pre-fetching is actually loading
			 * more data than would otherwise be loaded).
			 *
			 * However, for realistic average workloads, this should reduce the number of round trips.
			 */
			if (ids.size() >= 2) {
				List<ResourceTable> loadedResourceTableEntries = new ArrayList<>();
				preFetchIndexes(ids, "forcedId", "myForcedId", loadedResourceTableEntries);

				List<Long> entityIds;

				entityIds = loadedResourceTableEntries.stream().filter(t -> t.isParamsStringPopulated()).map(t->t.getId()).collect(Collectors.toList());
				if (entityIds.size() > 0) {
					preFetchIndexes(entityIds, "string", "myParamsString", null);
				}

				entityIds = loadedResourceTableEntries.stream().filter(t -> t.isParamsTokenPopulated()).map(t->t.getId()).collect(Collectors.toList());
				if (entityIds.size() > 0) {
					preFetchIndexes(entityIds, "token", "myParamsToken", null);
				}

				entityIds = loadedResourceTableEntries.stream().filter(t -> t.isParamsDatePopulated()).map(t->t.getId()).collect(Collectors.toList());
				if (entityIds.size() > 0) {
					preFetchIndexes(entityIds, "date", "myParamsDate", null);
				}

				entityIds = loadedResourceTableEntries.stream().filter(t -> t.isParamsQuantityPopulated()).map(t->t.getId()).collect(Collectors.toList());
				if (entityIds.size() > 0) {
					preFetchIndexes(entityIds, "quantity", "myParamsQuantity", null);
				}

				entityIds = loadedResourceTableEntries.stream().filter(t -> t.isHasLinks()).map(t->t.getId()).collect(Collectors.toList());
				if (entityIds.size() > 0) {
					preFetchIndexes(entityIds, "resourceLinks", "myResourceLinks", null);
				}

				entityIds = loadedResourceTableEntries.stream().filter(t -> t.isHasTags()).map(t->t.getId()).collect(Collectors.toList());
				if (entityIds.size() > 0) {
					myResourceTagDao.findByResourceIds(entityIds);
					preFetchIndexes(entityIds, "tags", "myTags", null);
				}

				entityIds = loadedResourceTableEntries.stream().map(t->t.getId()).collect(Collectors.toList());
				if (myDaoConfig.getIndexMissingFields() == DaoConfig.IndexEnabledEnum.ENABLED) {
					preFetchIndexes(entityIds, "searchParamPresence", "mySearchParamPresents", null);
				}

				new QueryChunker<ResourceTable>().chunk(loadedResourceTableEntries, SearchBuilder.getMaximumPageSize() / 2, entries -> {

					Map<Long, ResourceTable> entities = entries
						.stream()
						.collect(Collectors.toMap(t -> t.getId(), t -> t));

					CriteriaBuilder b = myEntityManager.getCriteriaBuilder();
					CriteriaQuery<ResourceHistoryTable> q = b.createQuery(ResourceHistoryTable.class);
					Root<ResourceHistoryTable> from = q.from(ResourceHistoryTable.class);

					from.fetch("myProvenance", JoinType.LEFT);

					List<Predicate> orPredicates = new ArrayList<>();
					for (ResourceTable next : entries) {
						Predicate resId = b.equal(from.get("myResourceId"), next.getId());
						Predicate resVer = b.equal(from.get("myResourceVersion"), next.getVersion());
						orPredicates.add(b.and(resId, resVer));
					}
					q.where(b.or(orPredicates.toArray(EMPTY_PREDICATE_ARRAY)));
					List<ResourceHistoryTable> resultList = myEntityManager.createQuery(q).getResultList();
					for (ResourceHistoryTable next : resultList) {
						ResourceTable nextEntity = entities.get(next.getResourceId());
						if (nextEntity != null) {
							nextEntity.setCurrentVersionEntity(next);
						}
					}

				});




			}


		});
	}

	private void preFetchIndexes(List<Long> theIds, String typeDesc, String fieldName, @Nullable List<ResourceTable> theEntityListToPopulate) {
		new QueryChunker<Long>().chunk(theIds, ids->{
			TypedQuery<ResourceTable> query = myEntityManager.createQuery("FROM ResourceTable r LEFT JOIN FETCH r." + fieldName + " WHERE r.myId IN ( :IDS )", ResourceTable.class);
			query.setParameter("IDS", ids);
			List<ResourceTable> indexFetchOutcome = query.getResultList();
			ourLog.debug("Pre-fetched {} {}} indexes", indexFetchOutcome.size(), typeDesc);
			if (theEntityListToPopulate != null) {
				theEntityListToPopulate.addAll(indexFetchOutcome);
			}
		});
	}


	@Nullable
	@Override
	protected String getResourceName() {
		return null;
	}

}
