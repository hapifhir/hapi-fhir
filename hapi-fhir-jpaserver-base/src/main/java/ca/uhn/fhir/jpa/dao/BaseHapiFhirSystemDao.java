/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.model.ExpungeOutcome;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.BaseHasResource;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProviderFactory;
import ca.uhn.fhir.jpa.search.SearchConstants;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BaseHapiFhirSystemDao<T extends IBaseBundle, MT> extends BaseStorageDao
		implements IFhirSystemDao<T, MT> {
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
		RequestPartitionId requestPartitionId =
				myRequestPartitionHelperService.determineReadPartitionForRequestForHistory(
						theRequestDetails, null, null);
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

	/**
	 * Prefetch entities into the Hibernate session.
	 *
	 * When processing several resources (e.g. transaction bundle, $reindex chunk, etc.)
	 * it would be slow to fetch each piece of a resource (e.g. all token index rows)
	 * one resource at a time.
	 * Instead, we fetch all the linked resources for the entire batch and populate the Hibernate Session.
	 *
	 * @param theResolvedIds the pids
	 * @param thePreFetchIndexes Should resource indexes be loaded
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public <P extends IResourcePersistentId> void preFetchResources(
			List<P> theResolvedIds, boolean thePreFetchIndexes) {
		HapiTransactionService.requireTransaction();
		List<Long> pids = theResolvedIds.stream().map(t -> ((JpaPid) t).getId()).collect(Collectors.toList());

		new QueryChunker<Long>().chunk(pids, idChunk -> {

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
			if (idChunk.size() >= 2) {
				List<ResourceTable> entityChunk = prefetchResourceTableHistoryAndProvenance(idChunk);

				if (thePreFetchIndexes) {

					prefetchByField("string", "myParamsString", ResourceTable::isParamsStringPopulated, entityChunk);
					prefetchByField("token", "myParamsToken", ResourceTable::isParamsTokenPopulated, entityChunk);
					prefetchByField("date", "myParamsDate", ResourceTable::isParamsDatePopulated, entityChunk);
					prefetchByField(
							"quantity", "myParamsQuantity", ResourceTable::isParamsQuantityPopulated, entityChunk);
					prefetchByField("resourceLinks", "myResourceLinks", ResourceTable::isHasLinks, entityChunk);

					prefetchByJoinClause(
							"tags",
							// fetch the TagResources and the actual TagDefinitions
							"LEFT JOIN FETCH r.myTags t LEFT JOIN FETCH t.myTag",
							BaseHasResource::isHasTags,
							entityChunk);

					prefetchByField(
							"comboStringUnique",
							"myParamsComboStringUnique",
							ResourceTable::isParamsComboStringUniquePresent,
							entityChunk);
					prefetchByField(
							"comboTokenNonUnique",
							"myParamsComboTokensNonUnique",
							ResourceTable::isParamsComboTokensNonUniquePresent,
							entityChunk);

					if (myStorageSettings.getIndexMissingFields() == JpaStorageSettings.IndexEnabledEnum.ENABLED) {
						prefetchByField("searchParamPresence", "mySearchParamPresents", r -> true, entityChunk);
					}
				}
			}
		});
	}

	@Nonnull
	private List<ResourceTable> prefetchResourceTableHistoryAndProvenance(List<Long> idChunk) {
		assert idChunk.size() < SearchConstants.MAX_PAGE_SIZE : "assume pre-chunked";

		Query query = myEntityManager.createQuery("select r, h "
				+ " FROM ResourceTable r "
				+ " LEFT JOIN fetch ResourceHistoryTable h "
				+ "      on r.myVersion = h.myResourceVersion and r.id = h.myResourceId "
				+ " left join fetch h.myProvenance "
				+ " WHERE r.myId IN ( :IDS ) ");
		query.setParameter("IDS", idChunk);

		@SuppressWarnings("unchecked")
		Stream<Object[]> queryResultStream = query.getResultStream();
		return queryResultStream
				.map(nextPair -> {
					// Store the matching ResourceHistoryTable in the transient slot on ResourceTable
					ResourceTable result = (ResourceTable) nextPair[0];
					ResourceHistoryTable currentVersion = (ResourceHistoryTable) nextPair[1];
					result.setCurrentVersionEntity(currentVersion);
					return result;
				})
				.collect(Collectors.toList());
	}

	/**
	 * Prefetch a join field for the active subset of some ResourceTable entities.
	 * Convenience wrapper around prefetchByJoinClause() for simple fields.
	 *
	 * @param theDescription             for logging
	 * @param theJpaFieldName            the join field from ResourceTable
	 * @param theEntityPredicate         select which ResourceTable entities need this join
	 * @param theEntities                the ResourceTable entities to consider
	 */
	private void prefetchByField(
			String theDescription,
			String theJpaFieldName,
			Predicate<ResourceTable> theEntityPredicate,
			List<ResourceTable> theEntities) {

		String joinClause = "LEFT JOIN FETCH r." + theJpaFieldName;

		prefetchByJoinClause(theDescription, joinClause, theEntityPredicate, theEntities);
	}

	/**
	 * Prefetch a join field for the active subset of some ResourceTable entities.
	 *
	 * @param theDescription             for logging
	 * @param theJoinClause              the JPA join expression to add to `ResourceTable r`
	 * @param theEntityPredicate         selects which entities need this prefetch
	 * @param theEntities                the ResourceTable entities to consider
	 */
	private void prefetchByJoinClause(
			String theDescription,
			String theJoinClause,
			Predicate<ResourceTable> theEntityPredicate,
			List<ResourceTable> theEntities) {

		// Which entities need this prefetch?
		List<Long> idSubset = theEntities.stream()
				.filter(theEntityPredicate)
				.map(ResourceTable::getId)
				.collect(Collectors.toList());

		if (idSubset.isEmpty()) {
			// nothing to do
			return;
		}

		String jqlQuery = "FROM ResourceTable r " + theJoinClause + " WHERE r.myId IN ( :IDS )";

		TypedQuery<ResourceTable> query = myEntityManager.createQuery(jqlQuery, ResourceTable.class);
		query.setParameter("IDS", idSubset);
		List<ResourceTable> indexFetchOutcome = query.getResultList();

		ourLog.debug("Pre-fetched {} {} indexes", indexFetchOutcome.size(), theDescription);
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
