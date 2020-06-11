package ca.uhn.fhir.jpa.search.lastn;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.dao.data.IObservationCodeIndexJobDao;
import ca.uhn.fhir.jpa.dao.data.IResourceHistoryTableDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.entity.ObservationCodeIndexJobEntity;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.search.util.impl.Executors;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.InstantType;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ObservationCodeIndexingSvcImpl {

	private static final Date BEGINNING_OF_TIME = new Date(0);
	private static final Logger ourLog = LoggerFactory.getLogger(ObservationCodeIndexingSvcImpl.class);
	private static final int PASS_SIZE = 25000;
	private final ReentrantLock myIndexingLock = new ReentrantLock();
	@Autowired
	private IObservationCodeIndexJobDao myIndexJobDao;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private PlatformTransactionManager myTxManager;
	private TransactionTemplate myTxTemplate;
	private ThreadFactory myIndexingThreadFactory = new BasicThreadFactory.Builder().namingPattern("ResourceIndex-%d").build();
	private ThreadPoolExecutor myTaskExecutor;
	@Autowired
	private IResourceTableDao myResourceTableDao;
	@Autowired
	private IResourceHistoryTableDao myResourceHistoryTableDao;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private IForcedIdDao myForcedIdDao;
	@Autowired
	private FhirContext myContext;
	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private ISchedulerService mySchedulerService;

	@PostConstruct
	public void start() {
		myTxTemplate = new TransactionTemplate(myTxManager);
		initExecutor();
		scheduleJob();
	}

	public void initExecutor() {
		// Create the threadpool executor used for index jobs
		int indexThreadCount = myDaoConfig.getReindexThreadCount();
		RejectedExecutionHandler rejectHandler = new Executors.BlockPolicy();
		myTaskExecutor = new ThreadPoolExecutor(0, indexThreadCount,
			0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<>(100),
			myIndexingThreadFactory,
			rejectHandler
		);
	}

	public void scheduleJob() {
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(getClass().getName());
		jobDetail.setJobClass(Job.class);
		mySchedulerService.scheduleClusteredJob(10 * DateUtils.MILLIS_PER_SECOND, jobDetail);
	}

	public static class Job implements HapiJob {
		@Autowired
		private ObservationCodeIndexingSvcImpl myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.runIndexingPass();
		}
	}

	public Integer runIndexingPass() {
		if (myDaoConfig.isSchedulingDisabled()) {
			return null;
		}
		if (myIndexingLock.tryLock()) {
			try {
				return doIndexingPassInsideLock();
			} finally {
				myIndexingLock.unlock();
			}
		}
		return null;
	}

	private int doIndexingPassInsideLock() {
		return runIndexJobs();
	}

	public int forceIndexingPass() {
		myIndexingLock.lock();
		try {
			return doIndexingPassInsideLock();
		} finally {
			myIndexingLock.unlock();
		}
	}

	public void cancelAndPurgeAllJobs() {
		ourLog.info("Cancelling and purging all resource indexing jobs");
		myIndexingLock.lock();
		try {

		myTaskExecutor.shutdown();
		initExecutor();

		} finally {
			myIndexingLock.unlock();
		}
	}

	private int runIndexJobs() {
		Collection<ObservationCodeIndexJobEntity> jobs = getResourceIndexJobEntities();

		if (jobs.size() > 0) {
			ourLog.info("Running {} index jobs: {}", jobs.size(), jobs);
		} else {
			ourLog.debug("Running {} index jobs: {}", jobs.size(), jobs);
			return 0;
		}

		int count = 0;
		for (ObservationCodeIndexJobEntity next : jobs) {

			if (next.getThresholdLow() != null && next.getThresholdLow().getTime() >= next.getThresholdHigh().getTime()) {
				markJobAsDeleted(next);
				continue;
			}

			count += runIndexJob(next);
		}
		return count;
	}

	private Collection<ObservationCodeIndexJobEntity> getResourceIndexJobEntities() {
		Collection<ObservationCodeIndexJobEntity> jobs = myTxTemplate.execute(t -> myIndexJobDao.findAll(PageRequest.of(0, 10), false));
		assert jobs != null;
		return jobs;
	}

	private void markJobAsDeleted(ObservationCodeIndexJobEntity theJob) {
		ourLog.info("Marking indexing job ID[{}] as deleted", theJob.getId());
		myTxTemplate.execute(t -> {
			myIndexJobDao.markAsDeletedById(theJob.getId());
			return null;
		});
	}

	private int runIndexJob(ObservationCodeIndexJobEntity theJob) {
		if (theJob.getSuspendedUntil() != null) {
			if (theJob.getSuspendedUntil().getTime() > System.currentTimeMillis()) {
				return 0;
			}
		}

		ourLog.info("Performing index pass for JOB[{}]", theJob.getId());
		StopWatch sw = new StopWatch();
		AtomicInteger counter = new AtomicInteger();

		// Calculate range
		Date low = theJob.getThresholdLow() != null ? theJob.getThresholdLow() : BEGINNING_OF_TIME;
		Date high = theJob.getThresholdHigh();

		// Query for resources within threshold
		StopWatch pageSw = new StopWatch();
		Slice<Long> range = myTxTemplate.execute(t -> {
			PageRequest page = PageRequest.of(0, PASS_SIZE);
			return myResourceTableDao.findIdsOfResourcesWithinUpdatedRangeOrderedFromOldest(page, "Observation", low, high);
		});
		Validate.notNull(range);
		int count = range.getNumberOfElements();
		ourLog.info("Loaded {} resources for indexing in {}", count, pageSw.toString());

		// If we didn't find any results at all, mark as deleted
		if (count == 0) {
			markJobAsDeleted(theJob);
			return 0;
		}

		// Submit each resource requiring indexing
		List<Future<Date>> futures = range
			.stream()
			.map(t -> myTaskExecutor.submit(new ObservationCodeIndexingTask(t, counter)))
			.collect(Collectors.toList());

		Date latestDate = null;
		for (Future<Date> next : futures) {
			Date nextDate;
			try {
				nextDate = next.get();
			} catch (Exception e) {
				ourLog.error("Failure indexing", e);
				Date suspendedUntil = DateUtils.addMinutes(new Date(), 1);
				myTxTemplate.execute(t -> {
					myIndexJobDao.setSuspendedUntil(suspendedUntil);
					return null;
				});
				return counter.get();
			}

			if (nextDate != null) {
				if (latestDate == null || latestDate.getTime() < nextDate.getTime()) {
					latestDate = new Date(nextDate.getTime());
				}
			}
		}

		Validate.notNull(latestDate);
		Date newLow;
		if (latestDate.getTime() == low.getTime()) {
			if (count == PASS_SIZE) {
				// Just in case we end up in some sort of infinite loop. This shouldn't happen, and couldn't really
				// happen unless there were 10000 resources with the exact same update time down to the
				// millisecond.
				ourLog.error("Final pass time for index JOB[{}] has same ending low value: {}", theJob.getId(), latestDate);
			}

			newLow = new Date(latestDate.getTime() + 1);
		} else {
			newLow = latestDate;
		}

		myTxTemplate.execute(t -> {
			myIndexJobDao.setThresholdLow(theJob.getId(), newLow);
			Integer existingCount = myIndexJobDao.getIndexCount(theJob.getId()).orElse(0);
			int newCount = existingCount + counter.get();
			myIndexJobDao.setIndexCount(theJob.getId(), newCount);
			return null;
		});

		ourLog.info("Completed pass of index JOB[{}] - Indexed {} resources in {} ({} / sec) - Have indexed until: {}", theJob.getId(), count, sw.toString(), sw.formatThroughput(count, TimeUnit.SECONDS), new InstantType(newLow));
		return counter.get();
	}

	private void expungeJobsMarkedAsDeleted() {
		myTxTemplate.execute(t -> {
			Collection<ObservationCodeIndexJobEntity> toDelete = myIndexJobDao.findAll(PageRequest.of(0, 10), true);
			toDelete.forEach(job -> {
				ourLog.info("Purging deleted job[{}]", job.getId());
				myIndexJobDao.deleteById(job.getId());
			});
			return null;
		});
	}

	private void markResourceAsIndexingFailed(final long theId) {
		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		txTemplate.execute((TransactionCallback<Void>) theStatus -> {
			ourLog.info("Marking resource with PID {} as indexing_failed", new Object[]{theId});

			myResourceTableDao.updateIndexStatus(theId, BaseHapiFhirDao.INDEX_STATUS_INDEXING_FAILED);

			Query q = myEntityManager.createQuery("DELETE FROM ResourceTag t WHERE t.myResourceId = :id");
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
		});
	}

	private class ObservationCodeIndexingTask implements Callable<Date> {
		private final Long myNextId;
		private final AtomicInteger myCounter;
		private Date myUpdated;

		ObservationCodeIndexingTask(Long theNextId, AtomicInteger theCounter) {
			myNextId = theNextId;
			myCounter = theCounter;
		}


		@SuppressWarnings("unchecked")
		private <T extends IBaseResource> void doindex(ResourceTable theResourceTable, T theResource) {
			RuntimeResourceDefinition resourceDefinition = myContext.getResourceDefinition(theResource.getClass());
			Class<T> resourceClass = (Class<T>) resourceDefinition.getImplementingClass();
			final IFhirResourceDao<T> dao = myDaoRegistry.getResourceDao(resourceClass);
			dao.reindex(theResource, theResourceTable);

			myCounter.incrementAndGet();
		}

		@Override
		public Date call() {
			Throwable indexFailure = null;
			try {
				/*
				 TODO: Add a call to the Elasticsearch service to update the document with ID = myNextId?
				 Elasticsearch service would need to:
				 	1. Determine which codings the the current observation has.
				 	2. Determine if there is a CodeableConcept for the codings.
				 	3. Update the CodeableConcept if needed.
				 	4. If multiple CodeableConcepts are found to be linked:
				 		4.1. Consolidate the CodeableConcepts under a new CodeableConcept.
				 	 	4.2. Lookup all observations referencing the old CodeableConcept IDs.
				 	 	4.3. Update each observation document with the new CodeableConcept ID.
				 */

/*				indexFailure = myTxTemplate.execute(t -> {
					ResourceTable resourceTable = myResourceTableDao.findById(myNextId).orElseThrow(IllegalStateException::new);
					myUpdated = resourceTable.getUpdatedDate();

					try {

						IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceTable.getResourceType());
						long expectedVersion = resourceTable.getVersion();
						IBaseResource resource = dao.read(resourceTable.getIdDt().toVersionless(), null, true);
						if (resource == null) {
							throw new InternalErrorException("Could not find resource version " + resourceTable.getIdDt().toUnqualified().getValue() + " in database");
						}

						Long actualVersion = resource.getIdElement().getVersionIdPartAsLong();
						if (actualVersion < expectedVersion) {
							ourLog.warn("Resource {} version {} does not exist, renumbering version {}", resource.getIdElement().toUnqualifiedVersionless().getValue(), resource.getIdElement().getVersionIdPart(), expectedVersion);
							myResourceHistoryTableDao.updateVersion(resourceTable.getId(), actualVersion, expectedVersion);
						}

						doindex(resourceTable, resource);
						return null;

					} catch (Exception e) {
						ourLog.error("Failed to index resource {}: {}", resourceTable.getIdDt(), e.toString(), e);
						t.setRollbackOnly();
						return e;
					}
				});
*/
			} catch (ResourceVersionConflictException e) {
				/*
				 * We index in multiple threads, so it's technically possible that two threads try
				 * to index resources that cause a constraint error now (i.e. because a unique index has been
				 * added that didn't previously exist). In this case, one of the threads would succeed and
				 * not get this error, so we'll let the other one fail and try
				 * again later.
				 */
				ourLog.info("Failed to index because of a version conflict. Leaving in unindexed state: {}", e.getMessage());
				indexFailure = null;
			}

			if (indexFailure != null) {
				ourLog.info("Setting resource PID[{}] status to ERRORED", myNextId);
				markResourceAsIndexingFailed(myNextId);
			}

			return myUpdated;
		}
	}
}
