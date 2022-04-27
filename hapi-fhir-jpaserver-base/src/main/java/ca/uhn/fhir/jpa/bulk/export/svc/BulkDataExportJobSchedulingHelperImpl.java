package ca.uhn.fhir.jpa.bulk.export.svc;

/*-
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionFileDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionEntity;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionFileEntity;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IIdType;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class BulkDataExportJobSchedulingHelperImpl implements IBulkDataExportJobSchedulingHelper {
	private static final Logger ourLog = getLogger(BulkDataExportJobSchedulingHelperImpl.class);

	private static final Long READ_CHUNK_SIZE = 10L;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private IBatchJobSubmitter myJobSubmitter;

	@Autowired
	private IBulkExportCollectionDao myBulkExportCollectionDao;

	@Autowired
	private IBulkExportCollectionFileDao myBulkExportCollectionFileDao;

	@Autowired
	private PlatformTransactionManager myTxManager;
	private TransactionTemplate myTxTemplate;

	@Autowired
	private ISchedulerService mySchedulerService;

	@Autowired
	@Qualifier(BatchConstants.BULK_EXPORT_JOB_NAME)
	private org.springframework.batch.core.Job myBulkExportJob;

	@Autowired
	@Qualifier(BatchConstants.GROUP_BULK_EXPORT_JOB_NAME)
	private org.springframework.batch.core.Job myGroupBulkExportJob;

	@Autowired
	@Qualifier(BatchConstants.PATIENT_BULK_EXPORT_JOB_NAME)
	private org.springframework.batch.core.Job myPatientBulkExportJob;

	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;

	@Autowired
	private DaoConfig myDaoConfig;

	@Autowired
	private FhirContext myContext;

	@PostConstruct
	public void start() {
		myTxTemplate = new TransactionTemplate(myTxManager);

		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(Job.class.getName());
		jobDetail.setJobClass(Job.class);
		mySchedulerService.scheduleClusteredJob(10 * DateUtils.MILLIS_PER_SECOND, jobDetail);

		jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(PurgeExpiredFilesJob.class.getName());
		jobDetail.setJobClass(PurgeExpiredFilesJob.class);
		mySchedulerService.scheduleClusteredJob(DateUtils.MILLIS_PER_HOUR, jobDetail);
	}

	/**
	 * This method is called by the scheduler to run a pass of the
	 * generator
	 */
	@Transactional(value = Transactional.TxType.NEVER)
	@Override
	public synchronized void startSubmittedJobs() {
		if (!myDaoConfig.isEnableTaskBulkExportJobExecution()) {
			return;
		}

		Optional<BulkExportJobEntity> jobToProcessOpt = myTxTemplate.execute(t -> {
			Pageable page = PageRequest.of(0, 1);
			Slice<BulkExportJobEntity> submittedJobs = myBulkExportJobDao.findByStatus(page, BulkExportJobStatusEnum.SUBMITTED);
			if (submittedJobs.isEmpty()) {
				return Optional.empty();
			}
			return Optional.of(submittedJobs.getContent().get(0));
		});

		if (!jobToProcessOpt.isPresent()) {
			return;
		}

		BulkExportJobEntity bulkExportJobEntity = jobToProcessOpt.get();

		String jobUuid = bulkExportJobEntity.getJobId();
		try {
			processJob(bulkExportJobEntity);
		} catch (Exception e) {
			ourLog.error("Failure while preparing bulk export extract", e);
			myTxTemplate.execute(t -> {
				Optional<BulkExportJobEntity> submittedJobs = myBulkExportJobDao.findByJobId(jobUuid);
				if (submittedJobs.isPresent()) {
					BulkExportJobEntity jobEntity = submittedJobs.get();
					jobEntity.setStatus(BulkExportJobStatusEnum.ERROR);
					jobEntity.setStatusMessage(e.getMessage());
					myBulkExportJobDao.save(jobEntity);
				}
				return null;
			});
		}

	}





	@Override
	@Transactional(Transactional.TxType.NEVER)
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
	@Transactional(value = Transactional.TxType.NEVER)
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
						getBinaryDao().delete(toId(nextFile.getResourceId()), new SystemRequestDetails());
						getBinaryDao().forceExpungeInExistingTransaction(toId(nextFile.getResourceId()), new ExpungeOptions().setExpungeDeletedResources(true).setExpungeOldVersions(true), new SystemRequestDetails());
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

	private IIdType toId(String theResourceId) {
		IIdType retVal = myContext.getVersion().newIdType();
		retVal.setValue(theResourceId);
		return retVal;
	}

	private void processJob(BulkExportJobEntity theBulkExportJobEntity) {
		String theJobUuid = theBulkExportJobEntity.getJobId();
		JobParametersBuilder parameters = new JobParametersBuilder()
			.addString(BatchConstants.JOB_UUID_PARAMETER, theJobUuid)
			.addLong(BatchConstants.READ_CHUNK_PARAMETER, READ_CHUNK_SIZE);

		ourLog.info("Submitting bulk export job {} to job scheduler", theJobUuid);

		try {
			if (isGroupBulkJob(theBulkExportJobEntity)) {
				enhanceBulkParametersWithGroupParameters(theBulkExportJobEntity, parameters);
				myJobSubmitter.runJob(myGroupBulkExportJob, parameters.toJobParameters());
			} else if (isPatientBulkJob(theBulkExportJobEntity)) {
				myJobSubmitter.runJob(myPatientBulkExportJob, parameters.toJobParameters());
			} else {
				myJobSubmitter.runJob(myBulkExportJob, parameters.toJobParameters());
			}
		} catch (JobParametersInvalidException theE) {
			ourLog.error("Unable to start job with UUID: {}, the parameters are invalid. {}", theJobUuid, theE.getMessage());
		}
	}

	private String getQueryParameterIfPresent(String theRequestString, String theParameter) {
		Map<String, String[]> stringMap = UrlUtil.parseQueryString(theRequestString);
		if (stringMap != null) {
			String[] strings = stringMap.get(theParameter);
			if (strings != null) {
				return String.join(",", strings);
			}
		}
		return null;

	}

	private boolean isPatientBulkJob(BulkExportJobEntity theBulkExportJobEntity) {
		return theBulkExportJobEntity.getRequest().startsWith("/Patient/");
	}

	private boolean isGroupBulkJob(BulkExportJobEntity theBulkExportJobEntity) {
		return theBulkExportJobEntity.getRequest().startsWith("/Group/");
	}

	private void enhanceBulkParametersWithGroupParameters(BulkExportJobEntity theBulkExportJobEntity, JobParametersBuilder theParameters) {
		String theGroupId = getQueryParameterIfPresent(theBulkExportJobEntity.getRequest(), JpaConstants.PARAM_EXPORT_GROUP_ID);
		String expandMdm = getQueryParameterIfPresent(theBulkExportJobEntity.getRequest(), JpaConstants.PARAM_EXPORT_MDM);
		theParameters.addString(BatchConstants.GROUP_ID_PARAMETER, theGroupId);
		theParameters.addString(BatchConstants.EXPAND_MDM_PARAMETER, expandMdm);
	}

	public static class Job implements HapiJob {
		@Autowired
		private IBulkDataExportJobSchedulingHelper myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.startSubmittedJobs();
		}
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

