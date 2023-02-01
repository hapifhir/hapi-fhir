package ca.uhn.fhir.jpa.bulk.export.svc;

/*-
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

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.FetchJobInstancesRequest;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.models.JobInstanceFetchRequest;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionFileDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.IHasScheduledJobs;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.JsonUtil;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IIdType;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class BulkDataExportJobSchedulingHelperImpl implements IBulkDataExportJobSchedulingHelper, IHasScheduledJobs {
	private static final Logger ourLog = getLogger(BulkDataExportJobSchedulingHelperImpl.class);

	@Autowired
	private DaoRegistry myDaoRegistry;

	// TODO:  inject batch2 equivalent
	@Autowired
	private IBulkExportCollectionDao myBulkExportCollectionDao;

	// TODO:  inject batch2 equivalent
	@Autowired
	private IBulkExportCollectionFileDao myBulkExportCollectionFileDao;

	@Autowired
	private PlatformTransactionManager myTxManager;
	private TransactionTemplate myTxTemplate;

	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;

	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private BulkExportHelperService myBulkExportHelperSvc;

	// TODO:  figure out how to make use of this
	@Autowired
	private IBatch2JobInstanceRepository myBatch2JobInstanceRepository;

	@Autowired
	private IJobPersistence myJpaJobPersistence;

	@PostConstruct
	public void start() {
		myTxTemplate = new TransactionTemplate(myTxManager);
	}

	@Override
	public void scheduleJobs(ISchedulerService theSchedulerService) {
		// job to cleanup unneeded BulkExportJobEntities that are persisted, but unwanted
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(PurgeExpiredFilesJob.class.getName());
		jobDetail.setJobClass(PurgeExpiredFilesJob.class);
		// TODO: figure out how to inject the following property:  module.persistence.config.bulk_export.file_retention_hours
//		theSchedulerService.scheduleClusteredJob(DateUtils.MILLIS_PER_HOUR, jobDetail);
		// TODO: reverse this change when functional testing is done
		final int numMinutes = 10;
		theSchedulerService.scheduleClusteredJob(numMinutes*DateUtils.MILLIS_PER_MINUTE, jobDetail);
	}

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public synchronized void cancelAndPurgeAllJobs() {
		myTxTemplate.execute(t -> {
			ourLog.info("Deleting all files");
			myBulkExportCollectionFileDao.deleteAllFiles();
			ourLog.info("Deleting all collections");
			myBulkExportCollectionDao.deleteAllFiles();
			ourLog.info("Deleting all jobs");
			myBulkExportJobDao.deleteAllFiles();
			return null;
		});
	}

	/**
	 * This method is called by the scheduler to run a pass of the
	 * generator
	 */
	@Transactional(propagation = Propagation.NEVER)
	@Override
	public void purgeExpiredFiles() {
		if (!myDaoConfig.isEnableTaskBulkExportJobExecution()) {
			return;
		}

		final Optional<JobInstance> optJobInstanceToDelete = Optional.ofNullable(myTxTemplate.execute(t -> {
			final FetchJobInstancesRequest fetchRequest = new FetchJobInstancesRequest("BULK_EXPORT", "", StatusEnum.COMPLETED, StatusEnum.FAILED);
			// TODO:  wrap in a transaction
			final List<JobInstance> submittedJobInstances = myJpaJobPersistence.fetchInstances(fetchRequest, 0, 1);

			return submittedJobInstances.isEmpty() ? null : submittedJobInstances.get(0);
		}));

		if (optJobInstanceToDelete.isPresent()) {
			ourLog.info("Deleting batch 2 bulk export job: {}", optJobInstanceToDelete.get());

			myTxTemplate.execute(t -> {
				// TODO:  test this and make sure it works
				final Optional<JobInstance> optJobInstanceForInstanceId = myJpaJobPersistence.fetchInstance(optJobInstanceToDelete.get().getInstanceId());

				ourLog.info("4088: optJobInstanceForInstanceId: {}", optJobInstanceForInstanceId);

				if (optJobInstanceForInstanceId.isPresent()) {
					final JobInstance jobInstanceForInstanceId = optJobInstanceForInstanceId.get();
					final String JobInstanceReportString = jobInstanceForInstanceId.getReport();
					if (JobInstanceReportString != null) {
						// TODO:  need to for from a report String to a BulkExportJobResults
						// TODO: parsing Exception?
						final BulkExportJobResults bulkExportJobResults = JsonUtil.deserialize(JobInstanceReportString, BulkExportJobResults.class);

						ourLog.info("4088: bulkExportJobResults: {}", bulkExportJobResults);
						ourLog.info("4088: bulkExportJobResults.getResourceTypeToBinaryIds(): {}", bulkExportJobResults.getResourceTypeToBinaryIds());
						ourLog.info("4088: bulkExportJobResults.getReportMsg(): {}", bulkExportJobResults.getReportMsg());
						ourLog.info("4088: bulkExportJobResults.getOriginalRequestUrl(): {}", bulkExportJobResults.getOriginalRequestUrl());

						final Map<String, List<String>> resourceTypeToBinaryIds = bulkExportJobResults.getResourceTypeToBinaryIds();
						for (String resourceType : resourceTypeToBinaryIds.keySet()) {
							final List<String> binaryIds = resourceTypeToBinaryIds.get(resourceType);
							for (String binaryId : binaryIds) {
								ourLog.info("Purging batch 2 bulk export binary: {}", binaryId);
								IIdType id = myBulkExportHelperSvc.toId(binaryId);
								ourLog.info("4088: about to delete Binary with ID: {}", id);
								getBinaryDao().delete(id, new SystemRequestDetails());
							}
						}
					} else {
						ourLog.info("4088: WTF? null bulk export report");
					}

					final String batch2BulkExportJobInstanceId = jobInstanceForInstanceId.getInstanceId();
					ourLog.debug("*** About to delete batch 2 bulk export job with ID {}", batch2BulkExportJobInstanceId);
					ourLog.info("4088: Deleting batch2 job with ID: {}", batch2BulkExportJobInstanceId);
					// TODO:  wrap in a transaction
					// TODO: do this LAST
					myJpaJobPersistence.deleteInstanceAndChunks(batch2BulkExportJobInstanceId);

				} else {
					ourLog.error("4088: WTF?  can't find job instance?!?!?!?");
				}

				return null;

//				BulkExportJobEntity job = myBulkExportJobDao.getOne(optJobInstanceToDelete.get().getId());
//				for (BulkExportCollectionEntity nextCollection : job.getCollections()) {
//					for (BulkExportCollectionFileEntity nextFile : nextCollection.getFiles()) {

//						ourLog.info("Purging bulk data file: {}", nextFile.getResourceId());
//						IIdType id = myBulkExportHelperSvc.toId(nextFile.getResourceId());
//						getBinaryDao().delete(id, new SystemRequestDetails());
//						getBinaryDao().forceExpungeInExistingTransaction(id, new ExpungeOptions().setExpungeDeletedResources(true).setExpungeOldVersions(true), new SystemRequestDetails());
//						myBulkExportCollectionFileDao.deleteByPid(nextFile.getId());

//					}

//					myBulkExportCollectionDao.deleteByPid(nextCollection.getId());
//				}

//				ourLog.debug("*** About to delete job with ID {}", job.getId());
//				myBulkExportJobDao.deleteByPid(job.getId());
//				return null;
			});

			ourLog.info("Finished deleting bulk export job: {}", optJobInstanceToDelete.get());
		} else {
			ourLog.info("No batch 2 bulk export jobs found!  Nothing to do!");
		}
	}


	/*
	 * Low-level requirements:
	 *
	 * 1.  Find all batch2 jobs by page, passing in the correct HQL for Batch2JobInstanceEntity or Batch2JobInfo
	 * 2.  If the batch 2 entity is present, do the equivalent of Slice.submittedJobs.getContent().get(0); and populate a Optional<?????>
	 * 3.  If the Optional<?????> is present, call the same doa to get the batch 2 object by ID
	 * 4.
	 */

	private void testCode() {
		// TODO: how do I find batch2 jobs?
		final JobInstanceFetchRequest request = new JobInstanceFetchRequest();
		request.setPageStart(0);
		request.setBatchSize(1);
//		final Page<JobInstance> jobInstances = myJpaJobPersistence.fetchJobInstances(request);
//		final JobInstance jobInstance = jobInstances.getContent().get(0);

		// TODO: what statuses should I use besides COMPLETED?  I need the equivalent of NOT 'BUILDING'
		// TODO:  job defintiion?  Is there an enum or constant for this?  StatusEnum.getEndedStatuses()?
		// TODO:  parameters?
		// TODO:  this returns nothing because it expects JSON
		final FetchJobInstancesRequest fetchRequest = new FetchJobInstancesRequest("BULK_EXPORT", "", StatusEnum.COMPLETED, StatusEnum.FAILED);
		// TODO:  wrap in a transaction
		final List<JobInstance> jobInstances = myJpaJobPersistence.fetchInstances(fetchRequest, 0, 1);

		jobInstances.forEach(theJobInstance -> ourLog.info("4088: batch 2 job instance: {}", theJobInstance));
		// TODO:  how am I supposed to get the binary or resources from a JobInstance?  instanceof and cast?



		// TODO: delete instance only no chunks?
		final JobInstance jobInstance = jobInstances.get(0);

		final String report = jobInstance.getReport();

		ourLog.info("4088: batch 2 job instance report: {}", report);

		if (report != null) {
			// TODO:  need to for from a report String to a BulkExportJobResults
			final BulkExportJobResults bulkExportJobResults = JsonUtil.deserialize(report, BulkExportJobResults.class);

			ourLog.info("4088: bulkExportJobResults: {}", bulkExportJobResults);
			ourLog.info("4088: bulkExportJobResults.getResourceTypeToBinaryIds(): {}", bulkExportJobResults.getResourceTypeToBinaryIds());
			ourLog.info("4088: bulkExportJobResults.getReportMsg(): {}", bulkExportJobResults.getReportMsg());
			ourLog.info("4088: bulkExportJobResults.getOriginalRequestUrl(): {}", bulkExportJobResults.getOriginalRequestUrl());

			final Map<String, List<String>> resourceTypeToBinaryIds = bulkExportJobResults.getResourceTypeToBinaryIds();
			for (String resourceType : resourceTypeToBinaryIds.keySet()) {
				final List<String> binaryIds = resourceTypeToBinaryIds.get(resourceType);
				for (String binaryId : binaryIds) {
					IIdType id = myBulkExportHelperSvc.toId(binaryId);
					ourLog.info("4088: about to delete Binary with ID: {}", id);
					getBinaryDao().delete(id, new SystemRequestDetails());
				}
			}
		} else {
			ourLog.info("4088: null bulk export report");
		}

		ourLog.info("4088: Deleting batch2 job with ID: {}", jobInstance.getInstanceId());
		// TODO:  wrap in a transaction
		// TODO: do this LAST
		myJpaJobPersistence.deleteInstanceAndChunks(jobInstance.getInstanceId());
	}

	@VisibleForTesting
	void setDaoConfig(DaoConfig theDaoConfig) {
		myDaoConfig = theDaoConfig;
	}

	@VisibleForTesting
	void setTxTemplate(TransactionTemplate theTxTemplate) {
		myTxTemplate = theTxTemplate;
	}

	@VisibleForTesting
	void setBulkExportJobDao(IBulkExportJobDao theBulkExportJobDao) {
		myBulkExportJobDao = theBulkExportJobDao;
	}

	@SuppressWarnings("unchecked")
	private IFhirResourceDao<IBaseBinary> getBinaryDao() {
		return myDaoRegistry.getResourceDao("Binary");
	}

	public static class PurgeExpiredFilesJob implements HapiJob {
		@Autowired
		private IBulkDataExportJobSchedulingHelper myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.purgeExpiredFiles();
		}
	}
}

