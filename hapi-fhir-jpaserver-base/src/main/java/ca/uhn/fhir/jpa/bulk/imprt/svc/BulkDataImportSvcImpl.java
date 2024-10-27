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
package ca.uhn.fhir.jpa.bulk.imprt.svc;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.importpull.models.Batch2BulkImportPullJobParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imprt.model.ActivateJobResult;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobFileJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobStatusEnum;
import ca.uhn.fhir.jpa.dao.data.IBulkImportJobDao;
import ca.uhn.fhir.jpa.dao.data.IBulkImportJobFileDao;
import ca.uhn.fhir.jpa.entity.BulkImportJobEntity;
import ca.uhn.fhir.jpa.entity.BulkImportJobFileEntity;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.IHasScheduledJobs;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.util.ValidateUtil;
import com.apicatalog.jsonld.StringUtils;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Semaphore;

import static ca.uhn.fhir.batch2.jobs.importpull.BulkImportPullConfig.BULK_IMPORT_JOB_NAME;

public class BulkDataImportSvcImpl implements IBulkDataImportSvc, IHasScheduledJobs {
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
	private IJobCoordinator myJobCoordinator;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@PostConstruct
	public void start() {
		myTxTemplate = new TransactionTemplate(myTxManager);
	}

	@Override
	public void scheduleJobs(ISchedulerService theSchedulerService) {
		// This job should be local so that each node in the cluster can pick up jobs
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(ActivationJob.class.getName());
		jobDetail.setJobClass(ActivationJob.class);
		theSchedulerService.scheduleLocalJob(10 * DateUtils.MILLIS_PER_SECOND, jobDetail);
	}

	@Override
	@Transactional
	public String createNewJob(
			BulkImportJobJson theJobDescription, @Nonnull List<BulkImportJobFileJson> theInitialFiles) {
		ValidateUtil.isNotNullOrThrowUnprocessableEntity(theJobDescription, "Job must not be null");
		ValidateUtil.isNotNullOrThrowUnprocessableEntity(
				theJobDescription.getProcessingMode(), "Job File Processing mode must not be null");
		ValidateUtil.isTrueOrThrowInvalidRequest(
				theJobDescription.getBatchSize() > 0, "Job File Batch Size must be > 0");

		String biJobId = UUID.randomUUID().toString();

		ourLog.info(
				"Creating new Bulk Import job with {} files, assigning bijob ID: {}", theInitialFiles.size(), biJobId);

		BulkImportJobEntity job = new BulkImportJobEntity();
		job.setJobId(biJobId);
		job.setFileCount(theInitialFiles.size());
		job.setStatus(BulkImportJobStatusEnum.STAGING);
		job.setJobDescription(theJobDescription.getJobDescription());
		job.setBatchSize(theJobDescription.getBatchSize());
		job.setRowProcessingMode(theJobDescription.getProcessingMode());
		job = myJobDao.save(job);

		int nextSequence = 0;
		addFilesToJob(theInitialFiles, job, nextSequence);

		return biJobId;
	}

	@Override
	@Transactional
	public void addFilesToJob(String theBiJobId, List<BulkImportJobFileJson> theFiles) {
		ourLog.info("Adding {} files to bulk import job with bijob id {}", theFiles.size(), theBiJobId);

		BulkImportJobEntity job = findJobByBiJobId(theBiJobId);

		ValidateUtil.isTrueOrThrowInvalidRequest(
				job.getStatus() == BulkImportJobStatusEnum.STAGING,
				"bijob id %s has status %s and can not be added to",
				theBiJobId,
				job.getStatus());

		addFilesToJob(theFiles, job, job.getFileCount());

		job.setFileCount(job.getFileCount() + theFiles.size());
		myJobDao.save(job);
	}

	private BulkImportJobEntity findJobByBiJobId(String theBiJobId) {
		BulkImportJobEntity job = myJobDao.findByJobId(theBiJobId)
				.orElseThrow(() -> new InvalidRequestException("Unknown bijob id: " + theBiJobId));
		return job;
	}

	@Override
	@Transactional
	public void markJobAsReadyForActivation(String theBiJobId) {
		ourLog.info("Activating bulk import bijob {}", theBiJobId);

		BulkImportJobEntity job = findJobByBiJobId(theBiJobId);
		ValidateUtil.isTrueOrThrowInvalidRequest(
				job.getStatus() == BulkImportJobStatusEnum.STAGING,
				"Bulk import bijob %s can not be activated in status: %s",
				theBiJobId,
				job.getStatus());

		job.setStatus(BulkImportJobStatusEnum.READY);
		myJobDao.save(job);
	}

	/**
	 * To be called by the job scheduler
	 */
	@Transactional(propagation = Propagation.NEVER)
	@Override
	public ActivateJobResult activateNextReadyJob() {
		if (!myStorageSettings.isEnableTaskBulkImportJobExecution()) {
			Logs.getBatchTroubleshootingLog()
					.trace("Bulk import job execution is not enabled on this server. No action taken.");
			return new ActivateJobResult(false, null);
		}

		if (!myRunningJobSemaphore.tryAcquire()) {
			Logs.getBatchTroubleshootingLog().trace("Already have a running batch job, not going to check for more");
			return new ActivateJobResult(false, null);
		}

		try {
			ActivateJobResult retval = doActivateNextReadyJob();
			if (!StringUtils.isBlank(retval.jobId)) {
				ourLog.info("Batch job submitted with batch job id {}", retval.jobId);
			}
			return retval;
		} finally {
			myRunningJobSemaphore.release();
		}
	}

	private ActivateJobResult doActivateNextReadyJob() {
		Optional<BulkImportJobEntity> jobToProcessOpt = Objects.requireNonNull(myTxTemplate.execute(t -> {
			Pageable page = PageRequest.of(0, 1);
			Slice<BulkImportJobEntity> submittedJobs = myJobDao.findByStatus(page, BulkImportJobStatusEnum.READY);
			if (submittedJobs.isEmpty()) {
				return Optional.empty();
			}
			return Optional.of(submittedJobs.getContent().get(0));
		}));

		if (!jobToProcessOpt.isPresent()) {
			return new ActivateJobResult(false, null);
		}

		BulkImportJobEntity bulkImportJobEntity = jobToProcessOpt.get();

		String jobUuid = bulkImportJobEntity.getJobId();
		String biJobId = null;
		try {
			biJobId = processJob(bulkImportJobEntity);
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
				return new ActivateJobResult(false, null);
			});
		}

		return new ActivateJobResult(true, biJobId);
	}

	@Override
	@Transactional
	public void setJobToStatus(String theBiJobId, BulkImportJobStatusEnum theStatus) {
		setJobToStatus(theBiJobId, theStatus, null);
	}

	@Override
	public void setJobToStatus(String theBiJobId, BulkImportJobStatusEnum theStatus, String theStatusMessage) {
		BulkImportJobEntity job = findJobByBiJobId(theBiJobId);
		job.setStatus(theStatus);
		job.setStatusMessage(theStatusMessage);
		myJobDao.save(job);
	}

	@Override
	@Transactional
	public BulkImportJobJson fetchJob(String theBiJobId) {
		BulkImportJobEntity job = findJobByBiJobId(theBiJobId);
		return job.toJson();
	}

	@Override
	public JobInfo getJobStatus(String theBiJobId) {
		BulkImportJobEntity theJob = findJobByBiJobId(theBiJobId);
		return new JobInfo()
				.setStatus(theJob.getStatus())
				.setStatusMessage(theJob.getStatusMessage())
				.setStatusTime(theJob.getStatusTime());
	}

	@Transactional
	@Override
	public BulkImportJobFileJson fetchFile(String theBiJobId, int theFileIndex) {
		BulkImportJobEntity job = findJobByBiJobId(theBiJobId);

		return myJobFileDao
				.findForJob(job, theFileIndex)
				.map(t -> t.toJson())
				.orElseThrow(() ->
						new IllegalArgumentException("Invalid index " + theFileIndex + " for bijob " + theBiJobId));
	}

	@Transactional
	@Override
	public String getFileDescription(String theBiJobId, int theFileIndex) {
		BulkImportJobEntity job = findJobByBiJobId(theBiJobId);

		return myJobFileDao.findFileDescriptionForJob(job, theFileIndex).orElse("");
	}

	@Override
	@Transactional
	public void deleteJobFiles(String theBiJobId) {
		BulkImportJobEntity job = findJobByBiJobId(theBiJobId);
		List<Long> files = myJobFileDao.findAllIdsForJob(theBiJobId);
		for (Long next : files) {
			myJobFileDao.deleteById(next);
		}
		myJobDao.delete(job);
	}

	private String processJob(BulkImportJobEntity theBulkExportJobEntity) {
		String biJobId = theBulkExportJobEntity.getJobId();
		int batchSize = theBulkExportJobEntity.getBatchSize();

		Batch2BulkImportPullJobParameters jobParameters = new Batch2BulkImportPullJobParameters();
		jobParameters.setJobId(biJobId);
		jobParameters.setBatchSize(batchSize);

		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(BULK_IMPORT_JOB_NAME);
		request.setParameters(jobParameters);

		ourLog.info("Submitting bulk import with bijob id {} to job scheduler", biJobId);

		return myJobCoordinator.startInstance(request).getInstanceId();
	}

	private void addFilesToJob(
			@Nonnull List<BulkImportJobFileJson> theInitialFiles, BulkImportJobEntity job, int nextSequence) {
		for (BulkImportJobFileJson nextFile : theInitialFiles) {
			ValidateUtil.isNotBlankOrThrowUnprocessableEntity(
					nextFile.getContents(), "Job File Contents mode must not be null");

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
