package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptDao;
import ca.uhn.fhir.jpa.entity.ForcedId;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.util.*;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;
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
	@Autowired
	@Qualifier("myResourceCountsCache")
	public ResourceCountCache myResourceCountsCache;


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
		return txTemplate.execute(new TransactionCallback<Integer>() {
			@SuppressWarnings("unchecked")
			@Override
			public Integer doInTransaction(@Nonnull TransactionStatus theStatus) {

				int maxResult = 500;
				if (theCount != null) {
					maxResult = Math.min(theCount, 2000);
				}
				maxResult = Math.max(maxResult, 10);

				TypedQuery<Long> q = myEntityManager.createQuery("SELECT t.myId FROM ResourceTable t WHERE t.myIndexStatus IS NULL", Long.class);

				ourLog.debug("Beginning indexing query with maximum {}", maxResult);
				q.setMaxResults(maxResult);
				Collection<Long> resources = q.getResultList();

				int count = 0;
				long start = System.currentTimeMillis();

				for (Long nextId : resources) {
					ResourceTable resourceTable = myResourceTableDao.findOne(nextId);

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

						@SuppressWarnings("rawtypes") final IFhirResourceDao dao = getDao(resource.getClass());

						dao.reindex(resource, resourceTable);
					} catch (Exception e) {
						ourLog.error("Failed to index resource {}: {}", new Object[]{resourceTable.getIdDt(), e.toString(), e});
						throw new ReindexFailureException(resourceTable.getId());
					}
					count++;

					if (count >= maxResult) {
						break;
					}
				}

				long delay = System.currentTimeMillis() - start;
				long avg;
				if (count > 0) {
					avg = (delay / count);
					ourLog.info("Indexed {} resources in {}ms - Avg {}ms / resource", new Object[]{count, delay, avg});
				} else {
					ourLog.debug("Indexed 0 resources in {}ms", delay);
				}

				return count;
			}
		});
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ExpungeOutcome expunge(ExpungeOptions theExpungeOptions) {
		return doExpunge(null, null, null, theExpungeOptions);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	@Override
	public Map<String, Long> getResourceCounts() {
		Map<String, Long> retVal = new HashMap<>();

		List<Map<?,?>> counts = myResourceTableDao.getResourceCounts();
		for (Map<?, ?> next : counts) {
			retVal.put(next.get("type").toString(), Long.parseLong(next.get("count").toString()));
		}

		return retVal;
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
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
				ourLog.info("Marking resource with PID {} as indexing_failed", new Object[]{theId});
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
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Integer performReindexingPass(final Integer theCount) {
		if (!myReindexLock.tryLock()) {
			return null;
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

}
