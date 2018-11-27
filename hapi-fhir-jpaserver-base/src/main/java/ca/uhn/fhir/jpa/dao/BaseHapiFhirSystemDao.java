package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.entity.ForcedId;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.util.ExpungeOptions;
import ca.uhn.fhir.jpa.util.ExpungeOutcome;
import ca.uhn.fhir.jpa.util.ReindexFailureException;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.hibernate.search.util.impl.Executors;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Query;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

import static org.apache.commons.lang3.StringUtils.isBlank;

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
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public abstract class BaseHapiFhirSystemDao<T, MT> extends BaseHapiFhirDao<IBaseResource> implements IFhirSystemDao<T, MT> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseHapiFhirSystemDao.class);
	@Autowired
	@Qualifier("myResourceCountsCache")
	public ResourceCountCache myResourceCountsCache;
	@Autowired
	private IForcedIdDao myForcedIdDao;
	private ReentrantLock myReindexLock = new ReentrantLock(false);
	@Autowired
	private ITermConceptDao myTermConceptDao;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private PlatformTransactionManager myTxManager;
	@Autowired
	private IResourceTableDao myResourceTableDao;
	private ThreadFactory myReindexingThreadFactory = new BasicThreadFactory.Builder().namingPattern("ResourceReindex-%d").build();

	private int doPerformReindexingPass(final Integer theCount) {
		/*
		 * If any search parameters have been recently added or changed,
		 * this makes sure that the cache has been reloaded to reflect
		 * them.
		 */
		mySearchParamRegistry.refreshCacheIfNecessary();

		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRED);
		return doPerformReindexingPassForResources(theCount, txTemplate);
	}

	@SuppressWarnings("ConstantConditions")
	private int doPerformReindexingPassForResources(final Integer theCount, TransactionTemplate txTemplate) {

		// Determine the IDs needing reindexing
		List<Long> idsToReindex = txTemplate.execute(theStatus -> {
			int maxResult = 500;
			if (theCount != null) {
				maxResult = Math.min(theCount, 2000);
			}
			maxResult = Math.max(maxResult, 10);

			ourLog.debug("Beginning indexing query with maximum {}", maxResult);
			return myResourceTableDao
				.findIdsOfResourcesRequiringReindexing(new PageRequest(0, maxResult))
				.getContent();
		});

		// If no IDs need reindexing, we're good here
		if (idsToReindex.isEmpty()) {
			return 0;
		}

		// Reindex
		StopWatch sw = new StopWatch();

		// Execute each reindex in a task within a threadpool
		int threadCount = getConfig().getReindexThreadCount();
		RejectedExecutionHandler rejectHandler = new Executors.BlockPolicy();
		ThreadPoolExecutor executor = new ThreadPoolExecutor(threadCount, threadCount,
			0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<>(),
			myReindexingThreadFactory,
			rejectHandler
			);
		List<Future<?>> futures = new ArrayList<>();
		for (Long nextId : idsToReindex) {
			futures.add(executor.submit(new ResourceReindexingTask(nextId)));
		}
		for (Future<?> next : futures) {
			try {
				next.get();
			} catch (Exception e) {
				throw new InternalErrorException("Failed to reindex: ", e);
			}
		}
		executor.shutdown();

		ourLog.info("Reindexed {} resources in {} threads - {}ms/resource", idsToReindex.size(), threadCount, sw.getMillisPerOperation(idsToReindex.size()));
		return idsToReindex.size();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ExpungeOutcome expunge(ExpungeOptions theExpungeOptions) {
		return doExpunge(null, null, null, theExpungeOptions);
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
		return myResourceCountsCache.get();
	}

	@Override
	public IBundleProvider history(Date theSince, Date theUntil, RequestDetails theRequestDetails) {
		if (theRequestDetails != null) {
			// Notify interceptors
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails);
			notifyInterceptors(RestOperationTypeEnum.HISTORY_SYSTEM, requestDetails);
		}

		StopWatch w = new StopWatch();
		IBundleProvider retVal = super.history(null, null, theSince, theUntil);
		ourLog.info("Processed global history in {}ms", w.getMillisAndRestart());
		return retVal;
	}

	@Transactional()
	@Override
	public int markAllResourcesForReindexing() {

		ourLog.info("Marking all resources as needing reindexing");
		int retVal = myEntityManager.createQuery("UPDATE " + ResourceTable.class.getSimpleName() + " t SET t.myIndexStatus = null").executeUpdate();

		ourLog.info("Marking all concepts as needing reindexing");
		retVal += myTermConceptDao.markAllForReindexing();

		ourLog.info("Done marking reindexing");
		return retVal;
	}

	private void markResourceAsIndexingFailed(final long theId) {
		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
		txTemplate.execute(new TransactionCallback<Void>() {
			@Override
			public Void doInTransaction(@Nonnull TransactionStatus theStatus) {
				ourLog.info("Marking resource with PID {} as indexing_failed", new Object[] {theId});
				Query q = myEntityManager.createQuery("UPDATE ResourceTable t SET t.myIndexStatus = :status WHERE t.myId = :id");
				q.setParameter("status", INDEX_STATUS_INDEXING_FAILED);
				q.setParameter("id", theId);
				q.executeUpdate();

				q = myEntityManager.createQuery("DELETE FROM ResourceTag t WHERE t.myResourceId = :id");
				q.setParameter("id", theId);
				q.executeUpdate();

				q = myEntityManager.createQuery("DELETE FROM ResourceIndexedSearchParamCoords t WHERE t.myResourcePid = :id");
				q.setParameter("id", theId);
				q.executeUpdate();

				q = myEntityManager.createQuery("DELETE FROM ResourceIndexedSearchParamDate t WHERE t.myResourcePid = :id");
				q.setParameter("id", theId);
				q.executeUpdate();

				q = myEntityManager.createQuery("DELETE FROM ResourceIndexedSearchParamNumber t WHERE t.myResourcePid = :id");
				q.setParameter("id", theId);
				q.executeUpdate();

				q = myEntityManager.createQuery("DELETE FROM ResourceIndexedSearchParamQuantity t WHERE t.myResourcePid = :id");
				q.setParameter("id", theId);
				q.executeUpdate();

				q = myEntityManager.createQuery("DELETE FROM ResourceIndexedSearchParamString t WHERE t.myResourcePid = :id");
				q.setParameter("id", theId);
				q.executeUpdate();

				q = myEntityManager.createQuery("DELETE FROM ResourceIndexedSearchParamToken t WHERE t.myResourcePid = :id");
				q.setParameter("id", theId);
				q.executeUpdate();

				q = myEntityManager.createQuery("DELETE FROM ResourceIndexedSearchParamUri t WHERE t.myResourcePid = :id");
				q.setParameter("id", theId);
				q.executeUpdate();

				q = myEntityManager.createQuery("DELETE FROM ResourceLink t WHERE t.mySourceResourcePid = :id");
				q.setParameter("id", theId);
				q.executeUpdate();

				q = myEntityManager.createQuery("DELETE FROM ResourceLink t WHERE t.myTargetResourcePid = :id");
				q.setParameter("id", theId);
				q.executeUpdate();

				return null;
			}
		});
	}

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public Integer performReindexingPass(final Integer theCount) {
		if (getConfig().isStatusBasedReindexingDisabled()) {
			return -1;
		}
		if (!myReindexLock.tryLock()) {
			return -1;
		}
		try {
			return doPerformReindexingPass(theCount);
		} catch (ReindexFailureException e) {
			ourLog.warn("Reindexing failed for resource {}", e.getResourceId());
			markResourceAsIndexingFailed(e.getResourceId());
			return -1;
		} finally {
			myReindexLock.unlock();
		}
	}

	private class ResourceReindexingTask implements Runnable {
		private final Long myNextId;

		public ResourceReindexingTask(Long theNextId) {
			myNextId = theNextId;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
			txTemplate.afterPropertiesSet();

			Throwable reindexFailure;
			try {
				reindexFailure = txTemplate.execute(new TransactionCallback<Throwable>() {
					@Override
					public Throwable doInTransaction(TransactionStatus theStatus) {
						ResourceTable resourceTable = myResourceTableDao.findById(myNextId).orElseThrow(IllegalStateException::new);

						try {
							/*
							 * This part is because from HAPI 1.5 - 1.6 we changed the format of forced ID to be "type/id" instead of just "id"
							 */
							ForcedId forcedId = resourceTable.getForcedId();
							if (forcedId != null) {
								if (isBlank(forcedId.getResourceType())) {
									ourLog.info("Updating resource {} forcedId type to {}", forcedId.getForcedId(), resourceTable.getResourceType());
									forcedId.setResourceType(resourceTable.getResourceType());
									myForcedIdDao.save(forcedId);
								}
							}

							final IBaseResource resource = toResource(resourceTable, false);

							Class<? extends IBaseResource> resourceClass = getContext().getResourceDefinition(resourceTable.getResourceType()).getImplementingClass();
							@SuppressWarnings("rawtypes") final IFhirResourceDao dao = getDaoOrThrowException(resourceClass);
							dao.reindex(resource, resourceTable);
							return null;

						} catch (Exception e) {
							ourLog.error("Failed to index resource {}: {}", resourceTable.getIdDt(), e.toString(), e);
							theStatus.setRollbackOnly();
							return e;
						}
					}
				});
			} catch (ResourceVersionConflictException e) {
				/*
				 * We reindex in multiple threads, so it's technically possible that two threads try
				 * to index resources that cause a constraint error now (i.e. because a unique index has been
				 * added that didn't previously exist). In this case, one of the threads would succeed and
				 * not get this error, so we'll let the other one fail and try
				 * again later.
				 */
				ourLog.info("Failed to reindex {} because of a version conflict. Leaving in unindexed state: {}", e.getMessage());
				reindexFailure = null;
			}

			if (reindexFailure != null) {
				txTemplate.execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
						ourLog.info("Setting resource PID[{}] status to ERRORED", myNextId);
						myResourceTableDao.updateStatusToErrored(myNextId);
					}
				});
			}
		}
	}
}
