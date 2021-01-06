package ca.uhn.fhir.jpa.bulk.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.bulk.api.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.bulk.model.BulkJobStatusEnum;
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
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.InstantType;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
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
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.UrlUtil.escapeUrlParam;
import static ca.uhn.fhir.util.UrlUtil.escapeUrlParams;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class BulkDataExportSvcImpl implements IBulkDataExportSvc {

	private static final Long READ_CHUNK_SIZE = 10L;
	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataExportSvcImpl.class);
	private int myReuseBulkExportForMillis = (int) (60 * DateUtils.MILLIS_PER_MINUTE);

	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;
	@Autowired
	private IBulkExportCollectionDao myBulkExportCollectionDao;
	@Autowired
	private IBulkExportCollectionFileDao myBulkExportCollectionFileDao;
	@Autowired
	private ISchedulerService mySchedulerService;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private PlatformTransactionManager myTxManager;
	private TransactionTemplate myTxTemplate;

	@Autowired
	private IBatchJobSubmitter myJobSubmitter;

	@Autowired
	@Qualifier("bulkExportJob")
	private org.springframework.batch.core.Job myBulkExportJob;

	private int myRetentionPeriod = (int) (2 * DateUtils.MILLIS_PER_HOUR);

	/**
	 * This method is called by the scheduler to run a pass of the
	 * generator
	 */
	@Transactional(value = Transactional.TxType.NEVER)
	@Override
	public synchronized void buildExportFiles() {

		Optional<BulkExportJobEntity> jobToProcessOpt = myTxTemplate.execute(t -> {
			Pageable page = PageRequest.of(0, 1);
			Slice<BulkExportJobEntity> submittedJobs = myBulkExportJobDao.findByStatus(page, BulkJobStatusEnum.SUBMITTED);
			if (submittedJobs.isEmpty()) {
				return Optional.empty();
			}
			return Optional.of(submittedJobs.getContent().get(0));
		});

		if (!jobToProcessOpt.isPresent()) {
			return;
		}

		String jobUuid = jobToProcessOpt.get().getJobId();

		try {
			processJob(jobUuid);
		} catch (Exception e) {
			ourLog.error("Failure while preparing bulk export extract", e);
			myTxTemplate.execute(t -> {
				Optional<BulkExportJobEntity> submittedJobs = myBulkExportJobDao.findByJobId(jobUuid);
				if (submittedJobs.isPresent()) {
					BulkExportJobEntity jobEntity = submittedJobs.get();
					jobEntity.setStatus(BulkJobStatusEnum.ERROR);
					jobEntity.setStatusMessage(e.getMessage());
					myBulkExportJobDao.save(jobEntity);
				}
				return null;
			});
		}

	}


	/**
	 * This method is called by the scheduler to run a pass of the
	 * generator
	 */
	@Transactional(value = Transactional.TxType.NEVER)
	@Override
	public void purgeExpiredFiles() {
		Optional<BulkExportJobEntity> jobToDelete = myTxTemplate.execute(t -> {
			Pageable page = PageRequest.of(0, 1);
			Slice<BulkExportJobEntity> submittedJobs = myBulkExportJobDao.findByExpiry(page, new Date());
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
						getBinaryDao().delete(toId(nextFile.getResourceId()));
						getBinaryDao().forceExpungeInExistingTransaction(toId(nextFile.getResourceId()), new ExpungeOptions().setExpungeDeletedResources(true).setExpungeOldVersions(true), null);
						myBulkExportCollectionFileDao.deleteByPid(nextFile.getId());

					}

					myBulkExportCollectionDao.deleteByPid(nextCollection.getId());
				}

				ourLog.info("*** ABOUT TO DELETE");
				myBulkExportJobDao.deleteByPid(job.getId());
				return null;
			});

			ourLog.info("Finished deleting bulk export job: {}", jobToDelete.get());

		}

	}

	private void processJob(String theJobUuid) {
		JobParameters parameters = new JobParametersBuilder()
			.addString("jobUUID", theJobUuid)
			.addLong("readChunkSize", READ_CHUNK_SIZE)
			.toJobParameters();

		ourLog.info("Submitting bulk export job {} to job scheduler", theJobUuid);

		try {
			myJobSubmitter.runJob(myBulkExportJob, parameters);
		} catch (JobParametersInvalidException theE) {
			ourLog.error("Unable to start job with UUID: {}, the parameters are invalid. {}", theJobUuid, theE.getMessage());
		}
	}


	@SuppressWarnings("unchecked")
	private IFhirResourceDao<IBaseBinary> getBinaryDao() {
		return myDaoRegistry.getResourceDao("Binary");
	}

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

	@Transactional
	@Override
	public JobInfo submitJob(String theOutputFormat, Set<String> theResourceTypes, Date theSince, Set<String> theFilters) {
		String outputFormat = Constants.CT_FHIR_NDJSON;
		if (isNotBlank(theOutputFormat)) {
			outputFormat = theOutputFormat;
		}
		if (!Constants.CTS_NDJSON.contains(outputFormat)) {
			throw new InvalidRequestException("Invalid output format: " + theOutputFormat);
		}

		StringBuilder requestBuilder = new StringBuilder();
		requestBuilder.append("/").append(JpaConstants.OPERATION_EXPORT);
		requestBuilder.append("?").append(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT).append("=").append(escapeUrlParam(outputFormat));
		Set<String> resourceTypes = theResourceTypes;
		if (resourceTypes != null) {
			requestBuilder.append("&").append(JpaConstants.PARAM_EXPORT_TYPE).append("=").append(String.join(",", escapeUrlParams(resourceTypes)));
		}
		Date since = theSince;
		if (since != null) {
			requestBuilder.append("&").append(JpaConstants.PARAM_EXPORT_SINCE).append("=").append(new InstantType(since).setTimeZoneZulu(true).getValueAsString());
		}
		if (theFilters != null && theFilters.size() > 0) {
			requestBuilder.append("&").append(JpaConstants.PARAM_EXPORT_TYPE_FILTER).append("=").append(String.join(",", escapeUrlParams(theFilters)));
		}
		String request = requestBuilder.toString();

		Date cutoff = DateUtils.addMilliseconds(new Date(), -myReuseBulkExportForMillis);
		Pageable page = PageRequest.of(0, 10);
		Slice<BulkExportJobEntity> existing = myBulkExportJobDao.findExistingJob(page, request, cutoff, BulkJobStatusEnum.ERROR);
		if (!existing.isEmpty()) {
			return toSubmittedJobInfo(existing.iterator().next());
		}

		if (resourceTypes != null && resourceTypes.contains("Binary")) {
			String msg = myContext.getLocalizer().getMessage(BulkDataExportSvcImpl.class, "onlyBinarySelected");
			throw new InvalidRequestException(msg);
		}

		if (resourceTypes == null || resourceTypes.isEmpty()) {
			// This is probably not a useful default, but having the default be "download the whole
			// server" seems like a risky default too. We'll deal with that by having the default involve
			// only returning a small time span
			resourceTypes = myContext.getResourceTypes();
			if (since == null) {
				since = DateUtils.addDays(new Date(), -1);
			}
		}

		resourceTypes =
			resourceTypes
				.stream()
				.filter(t -> !"Binary".equals(t))
				.collect(Collectors.toSet());

		BulkExportJobEntity job = new BulkExportJobEntity();
		job.setJobId(UUID.randomUUID().toString());
		job.setStatus(BulkJobStatusEnum.SUBMITTED);
		job.setSince(since);
		job.setCreated(new Date());
		job.setRequest(request);

		// Validate types
		validateTypes(resourceTypes);
		validateTypeFilters(theFilters, resourceTypes);

		updateExpiry(job);
		myBulkExportJobDao.save(job);

		for (String nextType : resourceTypes) {

			BulkExportCollectionEntity collection = new BulkExportCollectionEntity();
			collection.setJob(job);
			collection.setResourceType(nextType);
			job.getCollections().add(collection);
			myBulkExportCollectionDao.save(collection);
		}

		ourLog.info("Bulk export job submitted: {}", job.toString());

		return toSubmittedJobInfo(job);
	}

	public void validateTypes(Set<String> theResourceTypes) {
		for (String nextType : theResourceTypes) {
			if (!myDaoRegistry.isResourceTypeSupported(nextType)) {
				String msg = myContext.getLocalizer().getMessage(BulkDataExportSvcImpl.class, "unknownResourceType", nextType);
				throw new InvalidRequestException(msg);
			}
		}
	}

	public void validateTypeFilters(Set<String> theTheFilters, Set<String> theResourceTypes) {
		if (theTheFilters != null) {
			Set<String> types = new HashSet<>();
			for (String next : theTheFilters) {
				if (!next.contains("?")) {
					throw new InvalidRequestException("Invalid " + JpaConstants.PARAM_EXPORT_TYPE_FILTER + " value \"" + next + "\". Must be in the form [ResourceType]?[params]");
				}
				String resourceType = next.substring(0, next.indexOf("?"));
				if (!theResourceTypes.contains(resourceType)) {
					throw new InvalidRequestException("Invalid " + JpaConstants.PARAM_EXPORT_TYPE_FILTER + " value \"" + next + "\". Resource type does not appear in " + JpaConstants.PARAM_EXPORT_TYPE + " list");
				}
				if (!types.add(resourceType)) {
					throw new InvalidRequestException("Invalid " + JpaConstants.PARAM_EXPORT_TYPE_FILTER + " value \"" + next + "\". Multiple filters found for type " + resourceType);
				}
			}
		}
	}

	private JobInfo toSubmittedJobInfo(BulkExportJobEntity theJob) {
		return new JobInfo().setJobId(theJob.getJobId());
	}

	private void updateExpiry(BulkExportJobEntity theJob) {
		theJob.setExpiry(DateUtils.addMilliseconds(new Date(), myRetentionPeriod));
	}

	@Transactional
	@Override
	public JobInfo getJobInfoOrThrowResourceNotFound(String theJobId) {
		BulkExportJobEntity job = myBulkExportJobDao
			.findByJobId(theJobId)
			.orElseThrow(() -> new ResourceNotFoundException(theJobId));

		JobInfo retVal = new JobInfo();
		retVal.setJobId(theJobId);
		retVal.setStatus(job.getStatus());
		retVal.setStatus(job.getStatus());
		retVal.setStatusTime(job.getStatusTime());
		retVal.setStatusMessage(job.getStatusMessage());
		retVal.setRequest(job.getRequest());

		if (job.getStatus() == BulkJobStatusEnum.COMPLETE) {
			for (BulkExportCollectionEntity nextCollection : job.getCollections()) {
				for (BulkExportCollectionFileEntity nextFile : nextCollection.getFiles()) {
					retVal.addFile()
						.setResourceType(nextCollection.getResourceType())
						.setResourceId(toQualifiedBinaryId(nextFile.getResourceId()));
				}
			}
		}

		return retVal;
	}

	private IIdType toId(String theResourceId) {
		IIdType retVal = myContext.getVersion().newIdType();
		retVal.setValue(theResourceId);
		return retVal;
	}

	private IIdType toQualifiedBinaryId(String theIdPart) {
		IIdType retVal = myContext.getVersion().newIdType();
		retVal.setParts(null, "Binary", theIdPart, null);

		return retVal;
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

	public static class Job implements HapiJob {
		@Autowired
		private IBulkDataExportSvc myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.buildExportFiles();
		}
	}

	public static class PurgeExpiredFilesJob implements HapiJob {
		@Autowired
		private IBulkDataExportSvc myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.purgeExpiredFiles();
		}
	}

}
