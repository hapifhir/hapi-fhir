/*-
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
package ca.uhn.fhir.jpa.bulk.export.svc;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
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
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Binary;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class BulkDataExportJobSchedulingHelperImpl implements IBulkDataExportJobSchedulingHelper, IHasScheduledJobs {
	private static final Logger ourLog = getLogger(BulkDataExportJobSchedulingHelperImpl.class);

	private final DaoRegistry myDaoRegistry;

	private final PlatformTransactionManager myTxManager;
	private final JpaStorageSettings myDaoConfig;
	private final BulkExportHelperService myBulkExportHelperSvc;
	private final IJobPersistence myJpaJobPersistence;
	private TransactionTemplate myTxTemplate;

	public BulkDataExportJobSchedulingHelperImpl(
			DaoRegistry theDaoRegistry,
			PlatformTransactionManager theTxManager,
			JpaStorageSettings theDaoConfig,
			BulkExportHelperService theBulkExportHelperSvc,
			IJobPersistence theJpaJobPersistence,
			TransactionTemplate theTxTemplate) {
		myDaoRegistry = theDaoRegistry;
		myTxManager = theTxManager;
		myDaoConfig = theDaoConfig;
		myBulkExportHelperSvc = theBulkExportHelperSvc;
		myJpaJobPersistence = theJpaJobPersistence;
		myTxTemplate = theTxTemplate;
	}

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
		theSchedulerService.scheduleClusteredJob(DateUtils.MILLIS_PER_HOUR, jobDetail);
	}

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public synchronized void cancelAndPurgeAllJobs() {
		// This is called by unit test code that also calls ExpungeEverythingService,
		// which explicitly deletes both Batch2WorkChunkEntity and Batch2JobInstanceEntity, as well as ResourceTable, in
		// which Binary's are stored
		// Long story short, this method no longer needs to do anything
	}

	/**
	 * This method is called by the scheduler to run a pass of the
	 * generator
	 */
	@Transactional(propagation = Propagation.NEVER)
	@Override
	public void purgeExpiredFiles() {
		if (!myDaoConfig.isEnableTaskBulkExportJobExecution()) {
			ourLog.debug("bulk export disabled:  doing nothing");
			return;
		}

		final List<JobInstance> jobInstancesToDelete = myTxTemplate.execute(t -> myJpaJobPersistence.fetchInstances(
				Batch2JobDefinitionConstants.BULK_EXPORT,
				StatusEnum.getEndedStatuses(),
				computeCutoffFromConfig(),
				PageRequest.of(0, 50)));

		if (jobInstancesToDelete == null || jobInstancesToDelete.isEmpty()) {
			ourLog.debug("No batch 2 bulk export jobs found!  Nothing to do!");
			ourLog.info("Finished bulk export job deletion with nothing to do");
			return;
		}

		for (JobInstance jobInstance : jobInstancesToDelete) {
			ourLog.info("Deleting batch 2 bulk export job: {}", jobInstance);

			myTxTemplate.execute(t -> {
				final Optional<JobInstance> optJobInstanceForInstanceId =
						myJpaJobPersistence.fetchInstance(jobInstance.getInstanceId());

				if (optJobInstanceForInstanceId.isEmpty()) {
					ourLog.error(
							"Can't find job instance for ID: {} despite having retrieved it in the first step",
							jobInstance.getInstanceId());
					return null;
				}

				final JobInstance jobInstanceForInstanceId = optJobInstanceForInstanceId.get();
				ourLog.info("Deleting bulk export job: {}", jobInstanceForInstanceId);

				// We need to keep these for investigation but we also need a process to manually delete these jobs once
				// we're done investigating
				if (StatusEnum.FAILED == jobInstanceForInstanceId.getStatus()) {
					ourLog.info("skipping because the status is FAILED for ID: {}"
							+ jobInstanceForInstanceId.getInstanceId());
					return null;
				}

				purgeBinariesIfNeeded(jobInstanceForInstanceId, jobInstanceForInstanceId.getReport());

				final String batch2BulkExportJobInstanceId = jobInstanceForInstanceId.getInstanceId();
				ourLog.debug("*** About to delete batch 2 bulk export job with ID {}", batch2BulkExportJobInstanceId);

				myJpaJobPersistence.deleteInstanceAndChunks(batch2BulkExportJobInstanceId);

				ourLog.info("Finished deleting bulk export job: {}", jobInstance.getInstanceId());

				return null;
			});

			ourLog.info("Finished deleting bulk export jobs");
		}
	}

	private void purgeBinariesIfNeeded(JobInstance theJobInstanceForInstanceId, String theJobInstanceReportString) {
		final Optional<BulkExportJobResults> optBulkExportJobResults =
				getBulkExportJobResults(theJobInstanceReportString);

		if (optBulkExportJobResults.isPresent()) {
			final BulkExportJobResults bulkExportJobResults = optBulkExportJobResults.get();
			ourLog.debug(
					"job: {} resource type to binary ID: {}",
					theJobInstanceForInstanceId.getInstanceId(),
					bulkExportJobResults.getResourceTypeToBinaryIds());

			final Map<String, List<String>> resourceTypeToBinaryIds = bulkExportJobResults.getResourceTypeToBinaryIds();
			for (String resourceType : resourceTypeToBinaryIds.keySet()) {
				final List<String> binaryIds = resourceTypeToBinaryIds.get(resourceType);
				for (String binaryId : binaryIds) {
					ourLog.info("Purging batch 2 bulk export binary: {}", binaryId);
					IIdType id = myBulkExportHelperSvc.toId(binaryId);
					getBinaryDao().delete(id, new SystemRequestDetails());
				}
			}
		} // else we can't know what the binary IDs are, so delete this job and move on
	}

	@SuppressWarnings("unchecked")
	private IFhirResourceDao<IBaseBinary> getBinaryDao() {
		return myDaoRegistry.getResourceDao(Binary.class.getSimpleName());
	}

	@Nonnull
	private Optional<BulkExportJobResults> getBulkExportJobResults(String theJobInstanceReportString) {
		if (StringUtils.isBlank(theJobInstanceReportString)) {
			ourLog.error(String.format(
					"Cannot parse job report string because it's null or blank: %s", theJobInstanceReportString));
			return Optional.empty();
		}

		try {
			return Optional.of(JsonUtil.deserialize(theJobInstanceReportString, BulkExportJobResults.class));
		} catch (Exception theException) {
			ourLog.error(String.format("Cannot parse job report string: %s", theJobInstanceReportString), theException);
			return Optional.empty();
		}
	}

	@Nonnull
	private Date computeCutoffFromConfig() {
		final int bulkExportFileRetentionPeriodHours = myDaoConfig.getBulkExportFileRetentionPeriodHours();

		final LocalDateTime cutoffLocalDateTime = LocalDateTime.now().minusHours(bulkExportFileRetentionPeriodHours);

		return Date.from(cutoffLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
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
