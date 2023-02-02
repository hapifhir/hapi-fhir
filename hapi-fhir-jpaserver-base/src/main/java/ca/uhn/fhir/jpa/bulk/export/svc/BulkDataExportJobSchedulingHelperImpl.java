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
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.IHasScheduledJobs;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import ca.uhn.fhir.util.JsonUtil;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IIdType;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class BulkDataExportJobSchedulingHelperImpl implements IBulkDataExportJobSchedulingHelper, IHasScheduledJobs {
	private static final Logger ourLog = getLogger(BulkDataExportJobSchedulingHelperImpl.class);
	private static final String BINARY = "Binary";

	// TODO: clean up all old dependencies
	// TODO: reference config properties

	@Autowired
	private DaoRegistry myDaoRegistry;


	@Autowired
	private PlatformTransactionManager myTxManager;
	private TransactionTemplate myTxTemplate;

	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private BulkExportHelperService myBulkExportHelperSvc;

	@Autowired
	private IJobPersistence myJpaJobPersistence;

	@PostConstruct
	public void start() {
		myTxTemplate = new TransactionTemplate(myTxManager);
	}

	// TODO:  unit test for jpa object fetch new
	// TODO:  bulk
	@Override
	public void scheduleJobs(ISchedulerService theSchedulerService) {
		// job to cleanup unneeded BulkExportJobEntities that are persisted, but unwanted
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(PurgeExpiredFilesJob.class.getName());
		jobDetail.setJobClass(PurgeExpiredFilesJob.class);

		// TODO:  restore this to what it was on master after testing is complete
		final long bulkExportScheduledCleanupIntervalInMillis = 15 * DateUtils.MILLIS_PER_MINUTE;
		theSchedulerService.scheduleClusteredJob(bulkExportScheduledCleanupIntervalInMillis, jobDetail);
	}

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public synchronized void cancelAndPurgeAllJobs() {
		// TODO:  decide whether or not to get rid of this altogether
		// TODO:  figure out how to implement this
		// TODO:  consider the ramifications for the Mongo implementation
//		myTxTemplate.execute(t -> {
//			ourLog.info("Deleting all files");
//			myBulkExportCollectionFileDao.deleteAllFiles();
//			ourLog.info("Deleting all collections");
//			myBulkExportCollectionDao.deleteAllFiles();
//			ourLog.info("Deleting all jobs");
//			myBulkExportJobDao.deleteAllFiles();
//
//			return null;
//		});
	}

	/**
	 * This method is called by the scheduler to run a pass of the
	 * generator
	 */
	@Transactional(propagation = Propagation.NEVER)
	@Override
	// TODO: unit test mostly by calling this directly
	public void purgeExpiredFiles() {
		if (!myDaoConfig.isEnableTaskBulkExportJobExecution()) {
			return;
		}

		final List<JobInstance> jobInstancesToDelete = myTxTemplate.execute(t -> {
			final List<JobInstance> jobInstances = myJpaJobPersistence.fetchInstances(Batch2JobDefinitionConstants.BULK_EXPORT, StatusEnum.getEndedStatuses(), computeCutoffFromConfig(), PageRequest.of(0, 100));
			jobInstances.forEach(jobInstance -> ourLog.info("4088: Found jobInstance with ID: {} and endTime: {}", jobInstance.getInstanceId(), jobInstance.getEndTime()));
			return jobInstances;
		});

		if (jobInstancesToDelete == null || jobInstancesToDelete.isEmpty()) {
			// TODO:  debug?
			ourLog.info("4088: No batch 2 bulk export jobs found!  Nothing to do!");
		}

		for (JobInstance jobInstance : jobInstancesToDelete) {
			ourLog.info("Deleting batch 2 bulk export job: {}", jobInstance);

			myTxTemplate.execute(t -> {
				// TODO:  test this and make sure it works
				final Optional<JobInstance> optJobInstanceForInstanceId = myJpaJobPersistence.fetchInstance(jobInstance.getInstanceId());
				ourLog.info("4088: optJobInstanceForInstanceId: {}", optJobInstanceForInstanceId);

				if (optJobInstanceForInstanceId.isPresent()) {
					final JobInstance jobInstanceForInstanceId = optJobInstanceForInstanceId.get();
					// TODO: check for FAILED status and skip
					if (StatusEnum.FAILED == jobInstanceForInstanceId.getStatus()) {
						ourLog.info("4088: skipping because the status is FAILED for ID: {}" + jobInstanceForInstanceId.getInstanceId());
						return null;
					}

					final String jobInstanceReportString = jobInstanceForInstanceId.getReport();

					if (jobInstanceReportString == null) {
						// TODO: better logging
						ourLog.info("4088: WTF? null bulk export report");
						return null;
					}

					// TODO:  need to for from a report String to a BulkExportJobResults
					// TODO: parsing Exception?
					final BulkExportJobResults bulkExportJobResults = JsonUtil.deserialize(jobInstanceReportString, BulkExportJobResults.class);
					// TODO: check for Exception and and skip binary logic here
					// TODO: unit test by delibertately add a badly-formatted job

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

					final String batch2BulkExportJobInstanceId = jobInstanceForInstanceId.getInstanceId();
					ourLog.debug("*** About to delete batch 2 bulk export job with ID {}", batch2BulkExportJobInstanceId);
					ourLog.info("4088: Deleting batch2 job with ID: {}", batch2BulkExportJobInstanceId);

					myJpaJobPersistence.deleteInstanceAndChunks(batch2BulkExportJobInstanceId);
				} else {
					ourLog.error("4088: WTF?  can't find job instance?!?!?!?");
				}

				return null;
			});

			ourLog.info("Finished deleting bulk export job");
		}
	}

	@Nonnull
	private Date computeCutoffFromConfig() {
		final int bulkExportFileRetentionPeriodHours = myDaoConfig.getBulkExportFileRetentionPeriodHours();
		ourLog.info("4088: bulkExportFileRetentionPeriodHours: {}", bulkExportFileRetentionPeriodHours);

		final LocalDateTime cutoffLocalDateTime = LocalDateTime.now()
			// TODO: reverse this change when functional testing is done
			.minusHours(bulkExportFileRetentionPeriodHours);
//			.minusMinutes(bulkExportFileRetentionPeriodHours);

		final long bulkExportScheduledCleanupIntervalInMillis = bulkExportFileRetentionPeriodHours * DateUtils.MILLIS_PER_MINUTE;
		ourLog.info("4088: bulkExportScheduledCleanupIntervalInMillis: {}", bulkExportScheduledCleanupIntervalInMillis);
		return Date.from(cutoffLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
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
	void setJpaJobPersistence(IJobPersistence theJpaJobPersistence) {
		myJpaJobPersistence = theJpaJobPersistence;
	}

	@VisibleForTesting
	void setBulkExportHelperSvc(BulkExportHelperService theBulkExportHelperSvc) {
		myBulkExportHelperSvc = theBulkExportHelperSvc;
	}

	@VisibleForTesting
	void setDaoRegistry(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
	}

	@SuppressWarnings("unchecked")
	private IFhirResourceDao<IBaseBinary> getBinaryDao() {
		return myDaoRegistry.getResourceDao(BINARY);
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

