package ca.uhn.fhir.jpa.bulk.imprt.svc;

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

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imprt.job.BulkImportJobConfig;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobFileJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobStatusEnum;
import ca.uhn.fhir.jpa.dao.data.IBulkImportJobDao;
import ca.uhn.fhir.jpa.dao.data.IBulkImportJobFileDao;
import ca.uhn.fhir.jpa.entity.BulkImportJobEntity;
import ca.uhn.fhir.jpa.entity.BulkImportJobFileEntity;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ValidateUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Semaphore;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class BulkDataImportSvcImpl implements IBulkDataImportSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataImportSvcImpl.class);
	private final Semaphore myRunningJobSemaphore = new Semaphore(1);
	@Autowired
	private IBulkImportJobDao myJobDao;
	@Autowired
	private IBulkImportJobFileDao myJobFileDao;
	@Autowired
	private PlatformTransactionManager myTxManager;
	private TransactionTemplate myTxTemplate;
	@Autowired
	private ISchedulerService mySchedulerService;
	@Autowired
	private IBatchJobSubmitter myJobSubmitter;
	@Autowired
	@Qualifier(BatchConstants.BULK_IMPORT_JOB_NAME)
	private org.springframework.batch.core.Job myBulkImportJob;
	@Autowired
	private DaoConfig myDaoConfig;

	@PostConstruct
	public void start() {
		myTxTemplate = new TransactionTemplate(myTxManager);

		// This job should be local so that each node in the cluster can pick up jobs
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(ActivationJob.class.getName());
		jobDetail.setJobClass(ActivationJob.class);
		mySchedulerService.scheduleLocalJob(10 * DateUtils.MILLIS_PER_SECOND, jobDetail);
	}

	@Override
	@Transactional
	public String createNewJob(BulkImportJobJson theJobDescription, @Nonnull List<BulkImportJobFileJson> theInitialFiles) {
		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theJobDescription, "Job must not be null");
		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theJobDescription.getProcessingMode(), "Job File Processing mode must not be null");
		ValidateUtil.isTrueOrThrowInvalidRequest(theJobDescription.getBatchSize() > 0, "Job File Batch Size must be > 0");

		String jobId = UUID.randomUUID().toString();

		ourLog.info("Creating new Bulk Import job with {} files, assigning job ID: {}", theInitialFiles.size(), jobId);

		BulkImportJobEntity job = new BulkImportJobEntity();
		job.setJobId(jobId);
		job.setFileCount(theInitialFiles.size());
		job.setStatus(BulkImportJobStatusEnum.STAGING);
		job.setJobDescription(theJobDescription.getJobDescription());
		job.setBatchSize(theJobDescription.getBatchSize());
		job.setRowProcessingMode(theJobDescription.getProcessingMode());
		job = myJobDao.save(job);

		int nextSequence = 0;
		addFilesToJob(theInitialFiles, job, nextSequence);

		return jobId;
	}

	@Override
	@Transactional
	public void addFilesToJob(String theJobId, List<BulkImportJobFileJson> theFiles) {
		ourLog.info("Adding {} files to bulk import job: {}", theFiles.size(), theJobId);

		BulkImportJobEntity job = findJobByJobId(theJobId);

		ValidateUtil.isTrueOrThrowInvalidRequest(job.getStatus() == BulkImportJobStatusEnum.STAGING, "Job %s has status %s and can not be added to", theJobId, job.getStatus());

		addFilesToJob(theFiles, job, job.getFileCount());

		job.setFileCount(job.getFileCount() + theFiles.size());
		myJobDao.save(job);
	}

	private BulkImportJobEntity findJobByJobId(String theJobId) {
		BulkImportJobEntity job = myJobDao
			.findByJobId(theJobId)
			.orElseThrow(() -> new InvalidRequestException("Unknown job ID: " + theJobId));
		return job;
	}

	@Override
	@Transactional
	public void markJobAsReadyForActivation(String theJobId) {
		ourLog.info("Activating bulk import job {}", theJobId);

		BulkImportJobEntity job = findJobByJobId(theJobId);
		ValidateUtil.isTrueOrThrowInvalidRequest(job.getStatus() == BulkImportJobStatusEnum.STAGING, "Bulk import job %s can not be activated in status: %s", theJobId, job.getStatus());

		job.setStatus(BulkImportJobStatusEnum.READY);
		myJobDao.save(job);
	}

	/**
	 * To be called by the job scheduler
	 */
	@Transactional(value = Transactional.TxType.NEVER)
	@Override
	public boolean activateNextReadyJob() {
		if (!myDaoConfig.isEnableTaskBulkImportJobExecution()) {
			Logs.getBatchTroubleshootingLog().trace("Bulk import job execution is not enabled on this server. No action taken.");
			return false;
		}

		if (!myRunningJobSemaphore.tryAcquire()) {
			Logs.getBatchTroubleshootingLog().trace("Already have a running batch job, not going to check for more");
			return false;
		}

		try {
			return doActivateNextReadyJob();
		} finally {
			myRunningJobSemaphore.release();
		}
	}

	private boolean doActivateNextReadyJob() {
		Optional<BulkImportJobEntity> jobToProcessOpt = Objects.requireNonNull(myTxTemplate.execute(t -> {
			Pageable page = PageRequest.of(0, 1);
			Slice<BulkImportJobEntity> submittedJobs = myJobDao.findByStatus(page, BulkImportJobStatusEnum.READY);
			if (submittedJobs.isEmpty()) {
				return Optional.empty();
			}
			return Optional.of(submittedJobs.getContent().get(0));
		}));

		if (!jobToProcessOpt.isPresent()) {
			return false;
		}

		BulkImportJobEntity bulkImportJobEntity = jobToProcessOpt.get();

		String jobUuid = bulkImportJobEntity.getJobId();
		try {
			processJob(bulkImportJobEntity);
		} catch (Exception e) {
			ourLog.error("Failure while preparing bulk export extract", e);
			myTxTemplate.execute(t -> {
				Optional<BulkImportJobEntity> submittedJobs = myJobDao.findByJobId(jobUuid);
				if (submittedJobs.isPresent()) {
					BulkImportJobEntity jobEntity = submittedJobs.get();
					jobEntity.setStatus(BulkImportJobStatusEnum.ERROR);
					jobEntity.setStatusMessage(e.getMessage());
					myJobDao.save(jobEntity);
				}
				return false;
			});
		}

		return true;
	}

	@Override
	@Transactional
	public void setJobToStatus(String theJobId, BulkImportJobStatusEnum theStatus) {
		setJobToStatus(theJobId, theStatus, null);
	}

	@Override
	public void setJobToStatus(String theJobId, BulkImportJobStatusEnum theStatus, String theStatusMessage) {
		BulkImportJobEntity job = findJobByJobId(theJobId);
		job.setStatus(theStatus);
		job.setStatusMessage(theStatusMessage);
		myJobDao.save(job);
	}

	@Override
	@Transactional
	public BulkImportJobJson fetchJob(String theJobId) {
		BulkImportJobEntity job = findJobByJobId(theJobId);
		return job.toJson();
	}

        @Override
        public JobInfo getJobStatus(String theJobId) {
                BulkImportJobEntity theJob = findJobByJobId(theJobId);
                return new JobInfo()
                        .setStatus(theJob.getStatus())
                        .setStatusMessage(theJob.getStatusMessage())
                        .setStatusTime(theJob.getStatusTime());
        }

	@Transactional
	@Override
	public BulkImportJobFileJson fetchFile(String theJobId, int theFileIndex) {
		BulkImportJobEntity job = findJobByJobId(theJobId);

		return myJobFileDao
			.findForJob(job, theFileIndex)
			.map(t -> t.toJson())
			.orElseThrow(() -> new IllegalArgumentException("Invalid index " + theFileIndex + " for job " + theJobId));
	}

	@Transactional
	@Override
	public String getFileDescription(String theJobId, int theFileIndex) {
		BulkImportJobEntity job = findJobByJobId(theJobId);

		return myJobFileDao.findFileDescriptionForJob(job, theFileIndex).orElse("");
	}

	@Override
	@Transactional
	public void deleteJobFiles(String theJobId) {
		BulkImportJobEntity job = findJobByJobId(theJobId);
		List<Long> files = myJobFileDao.findAllIdsForJob(theJobId);
		for (Long next : files) {
			myJobFileDao.deleteById(next);
		}
		myJobDao.delete(job);
	}

	private void processJob(BulkImportJobEntity theBulkExportJobEntity) throws JobParametersInvalidException {
		String jobId = theBulkExportJobEntity.getJobId();
		int batchSize = theBulkExportJobEntity.getBatchSize();
		ValidateUtil.isTrueOrThrowInvalidRequest(batchSize > 0, "Batch size must be positive");

		JobParametersBuilder parameters = new JobParametersBuilder()
			.addString(BatchConstants.JOB_UUID_PARAMETER, jobId)
			.addLong(BulkImportJobConfig.JOB_PARAM_COMMIT_INTERVAL, (long) batchSize);

		if (isNotBlank(theBulkExportJobEntity.getJobDescription())) {
			parameters.addString(BatchConstants.JOB_DESCRIPTION, theBulkExportJobEntity.getJobDescription());
		}

		ourLog.info("Submitting bulk import job {} to job scheduler", jobId);

		myJobSubmitter.runJob(myBulkImportJob, parameters.toJobParameters());
	}

	private void addFilesToJob(@Nonnull List<BulkImportJobFileJson> theInitialFiles, BulkImportJobEntity job, int nextSequence) {
		for (BulkImportJobFileJson nextFile : theInitialFiles) {
			ValidateUtil.isNotBlankOrThrowUnprocessableEntity(nextFile.getContents(), "Job File Contents mode must not be null");

			BulkImportJobFileEntity jobFile = new BulkImportJobFileEntity();
			jobFile.setJob(job);
			jobFile.setContents(nextFile.getContents());
			jobFile.setTenantName(nextFile.getTenantName());
			jobFile.setFileDescription(nextFile.getDescription());
			jobFile.setFileSequence(nextSequence++);
			myJobFileDao.save(jobFile);
		}
	}


	public static class ActivationJob implements HapiJob {
		@Autowired
		private IBulkDataImportSvc myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.activateNextReadyJob();
		}
	}
}
