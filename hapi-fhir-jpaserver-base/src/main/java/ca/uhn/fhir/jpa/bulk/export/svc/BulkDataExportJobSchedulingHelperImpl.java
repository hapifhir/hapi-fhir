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

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionFileDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionEntity;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionFileEntity;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.IHasScheduledJobs;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IIdType;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class BulkDataExportJobSchedulingHelperImpl implements IBulkDataExportJobSchedulingHelper, IHasScheduledJobs {
	private static final Logger ourLog = getLogger(BulkDataExportJobSchedulingHelperImpl.class);

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private IBulkExportCollectionDao myBulkExportCollectionDao;

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

		Optional<BulkExportJobEntity> jobToDelete = myTxTemplate.execute(t -> {
			Pageable page = PageRequest.of(0, 1);
			Slice<BulkExportJobEntity> submittedJobs = myBulkExportJobDao.findNotRunningByExpiry(page, new Date());
			if (submittedJobs.isEmpty()) {
				return Optional.empty();
			}
			return Optional.of(submittedJobs.getContent().get(0));
		});

		if (jobToDelete.isPresent()) {
			ourLog.info("Deleting bulk export job: {}", jobToDelete.get());

			myTxTemplate.execute(t -> {
				BulkExportJobEntity job = myBulkExportJobDao.getOne(jobToDelete.get().getId());
				for (BulkExportCollectionEntity nextCollection : job.getCollections()) {
					for (BulkExportCollectionFileEntity nextFile : nextCollection.getFiles()) {

						ourLog.info("Purging bulk data file: {}", nextFile.getResourceId());
						IIdType id = myBulkExportHelperSvc.toId(nextFile.getResourceId());
						getBinaryDao().delete(id, new SystemRequestDetails());
						getBinaryDao().forceExpungeInExistingTransaction(id, new ExpungeOptions().setExpungeDeletedResources(true).setExpungeOldVersions(true), new SystemRequestDetails());
						myBulkExportCollectionFileDao.deleteByPid(nextFile.getId());

					}

					myBulkExportCollectionDao.deleteByPid(nextCollection.getId());
				}

				ourLog.debug("*** About to delete job with ID {}", job.getId());
				myBulkExportJobDao.deleteByPid(job.getId());
				return null;
			});

			ourLog.info("Finished deleting bulk export job: {}", jobToDelete.get());
		}
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

