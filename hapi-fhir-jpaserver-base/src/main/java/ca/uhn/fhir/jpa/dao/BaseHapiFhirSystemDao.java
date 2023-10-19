/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.model.ExpungeOutcome;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTagDao;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.BaseHasResource;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProviderFactory;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public abstract class BaseHapiFhirSystemDao<T extends IBaseBundle, MT> extends BaseStorageDao
		implements IFhirSystemDao<T, MT> {

	public static final Predicate[] EMPTY_PREDICATE_ARRAY = new Predicate[0];
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseHapiFhirSystemDao.class);
	public ResourceCountCache myResourceCountsCache;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;

	@Autowired
	private TransactionProcessor myTransactionProcessor;

	@Autowired
	private ApplicationContext myApplicationContext;

	@Autowired
	private ExpungeService myExpungeService;

	@Autowired
	private IResourceTableDao myResourceTableDao;

	@Autowired
	private PersistedJpaBundleProviderFactory myPersistedJpaBundleProviderFactory;

	@Autowired
	private IResourceTagDao myResourceTagDao;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperService;

	@Autowired
	private IHapiTransactionService myTransactionService;

	@VisibleForTesting
	public void setTransactionProcessorForUnitTest(TransactionProcessor theTransactionProcessor) {
		myTransactionProcessor = theTransactionProcessor;
	}

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public ExpungeOutcome expunge(ExpungeOptions theExpungeOptions, RequestDetails theRequestDetails) {
		validateExpungeEnabled(theExpungeOptions);
		return myExpungeService.expunge(null, null, theExpungeOptions, theRequestDetails);
	}

	private void validateExpungeEnabled(ExpungeOptions theExpungeOptions) {
		if (!getStorageSettings().isExpungeEnabled()) {
			throw new MethodNotAllowedException(Msg.code(2080) + "$expunge is not enabled on this server");
		}

		if (theExpungeOptions.isExpungeEverything() && !getStorageSettings().isAllowMultipleDelete()) {
			throw new MethodNotAllowedException(Msg.code(2081) + "Multiple delete is not enabled on this server");
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Map<String, Long> getResourceCounts() {
		Map<String, Long> retVal = new HashMap<>();

		List<Map<?, ?>> counts = myResourceTableDao.getResourceCounts();
		for (Map<?, ?> next : counts) {
			retVal.put(
					next.get("type").toString(),
					Long.parseLong(next.get("count").toString()));
		}

		return retVal;
	}

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
		StopWatch w = new StopWatch();
		ReadPartitionIdRequestDetails details = ReadPartitionIdRequestDetails.forHistory(null, null);
		RequestPartitionId requestPartitionId =
				myRequestPartitionHelperService.determineReadPartitionForRequest(theRequestDetails, details);
		IBundleProvider retVal = myTransactionService
				.withRequest(theRequestDetails)
				.withRequestPartitionId(requestPartitionId)
				.execute(() -> myPersistedJpaBundleProviderFactory.history(
						theRequestDetails, null, null, theSince, theUntil, theOffset, requestPartitionId));
		ourLog.info("Processed global history in {}ms", w.getMillisAndRestart());
		return retVal;
	}

	@Override
	public T transaction(RequestDetails theRequestDetails, T theRequest) {
		HapiTransactionService.noTransactionAllowed();
		return myTransactionProcessor.transaction(theRequestDetails, theRequest, false);
	}

	@Override
	public T transactionNested(RequestDetails theRequestDetails, T theRequest) {
		HapiTransactionService.requireTransaction();
		return myTransactionProcessor.transaction(theRequestDetails, theRequest, true);
	}

	@Override
	public <P extends IResourcePersistentId> void preFetchResources(
			List<P> theResolvedIds, boolean thePreFetchIndexes) {
		HapiTransactionService.requireTransaction();
		List<Long> pids = theResolvedIds.stream().map(t -> ((JpaPid) t).getId()).collect(Collectors.toList());

		new QueryChunker<Long>().chunk(pids, ids -> {

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

				if (thePreFetchIndexes) {
					entityIds = loadedResourceTableEntries.stream()
							.filter(ResourceTable::isParamsStringPopulated)
							.map(ResourceTable::getId)
							.collect(Collectors.toList());
					if (entityIds.size() > 0) {
						preFetchIndexes(entityIds, "string", "myParamsString", null);
					}

					entityIds = loadedResourceTableEntries.stream()
							.filter(ResourceTable::isParamsTokenPopulated)
							.map(ResourceTable::getId)
							.collect(Collectors.toList());
					if (entityIds.size() > 0) {
						preFetchIndexes(entityIds, "token", "myParamsToken", null);
					}

					entityIds = loadedResourceTableEntries.stream()
							.filter(ResourceTable::isParamsDatePopulated)
							.map(ResourceTable::getId)
							.collect(Collectors.toList());
					if (entityIds.size() > 0) {
						preFetchIndexes(entityIds, "date", "myParamsDate", null);
					}

					entityIds = loadedResourceTableEntries.stream()
							.filter(ResourceTable::isParamsQuantityPopulated)
							.map(ResourceTable::getId)
							.collect(Collectors.toList());
					if (entityIds.size() > 0) {
						preFetchIndexes(entityIds, "quantity", "myParamsQuantity", null);
					}

					entityIds = loadedResourceTableEntries.stream()
							.filter(ResourceTable::isHasLinks)
							.map(ResourceTable::getId)
							.collect(Collectors.toList());
					if (entityIds.size() > 0) {
						preFetchIndexes(entityIds, "resourceLinks", "myResourceLinks", null);
					}

					entityIds = loadedResourceTableEntries.stream()
							.filter(BaseHasResource::isHasTags)
							.map(ResourceTable::getId)
							.collect(Collectors.toList());
					if (entityIds.size() > 0) {
						myResourceTagDao.findByResourceIds(entityIds);
						preFetchIndexes(entityIds, "tags", "myTags", null);
					}

					entityIds = loadedResourceTableEntries.stream()
							.map(ResourceTable::getId)
							.collect(Collectors.toList());
					if (myStorageSettings.getIndexMissingFields() == JpaStorageSettings.IndexEnabledEnum.ENABLED) {
						preFetchIndexes(entityIds, "searchParamPresence", "mySearchParamPresents", null);
					}
				}

				new QueryChunker<ResourceTable>()
						.chunk(loadedResourceTableEntries, SearchBuilder.getMaximumPageSize() / 2, entries -> {
							Map<Long, ResourceTable> entities =
									entries.stream().collect(Collectors.toMap(ResourceTable::getId, t -> t));

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
							List<ResourceHistoryTable> resultList =
									myEntityManager.createQuery(q).getResultList();
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

	private void preFetchIndexes(
			List<Long> theIds,
			String typeDesc,
			String fieldName,
			@Nullable List<ResourceTable> theEntityListToPopulate) {
		new QueryChunker<Long>().chunk(theIds, ids -> {
			TypedQuery<ResourceTable> query = myEntityManager.createQuery(
					"FROM ResourceTable r LEFT JOIN FETCH r." + fieldName + " WHERE r.myId IN ( :IDS )",
					ResourceTable.class);
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

	@Override
	protected IInterceptorBroadcaster getInterceptorBroadcaster() {
		return myInterceptorBroadcaster;
	}

	@Override
	protected JpaStorageSettings getStorageSettings() {
		return myStorageSettings;
	}

	@Override
	public FhirContext getContext() {
		return myFhirContext;
	}

	@VisibleForTesting
	public void setStorageSettingsForUnitTest(JpaStorageSettings theStorageSettings) {
		myStorageSettings = theStorageSettings;
	}
}
