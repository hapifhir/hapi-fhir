package ca.uhn.fhir.jpa.bulk.export.svc;

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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.bulk.export.job.BulkExportJobConfig;
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
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.InstantType;
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

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions.ExportStyle.GROUP;
import static ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions.ExportStyle.PATIENT;
import static ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions.ExportStyle.SYSTEM;
import static ca.uhn.fhir.util.UrlUtil.escapeUrlParam;
import static ca.uhn.fhir.util.UrlUtil.escapeUrlParams;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class BulkDataExportSvcImpl implements IBulkDataExportSvc {

	private static final Long READ_CHUNK_SIZE = 10L;
	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataExportSvcImpl.class);
	private final int myReuseBulkExportForMillis = (int) (60 * DateUtils.MILLIS_PER_MINUTE);

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
	@Qualifier(BatchConstants.BULK_EXPORT_JOB_NAME)
	private org.springframework.batch.core.Job myBulkExportJob;

	@Autowired
	@Qualifier(BatchConstants.GROUP_BULK_EXPORT_JOB_NAME)
	private org.springframework.batch.core.Job myGroupBulkExportJob;

	@Autowired
	@Qualifier(BatchConstants.PATIENT_BULK_EXPORT_JOB_NAME)
	private org.springframework.batch.core.Job myPatientBulkExportJob;

	private Set<String> myCompartmentResources;

	private final int myRetentionPeriod = (int) (2 * DateUtils.MILLIS_PER_HOUR);

	/**
	 * This method is called by the scheduler to run a pass of the
	 * generator
	 */
	@Transactional(value = Transactional.TxType.NEVER)
	@Override
	public synchronized void buildExportFiles() {
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

	@Autowired
	private DaoConfig myDaoConfig;

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
						getBinaryDao().delete(toId(nextFile.getResourceId()), new SystemRequestDetails());
						getBinaryDao().forceExpungeInExistingTransaction(toId(nextFile.getResourceId()), new ExpungeOptions().setExpungeDeletedResources(true).setExpungeOldVersions(true), new SystemRequestDetails());
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

	private void processJob(BulkExportJobEntity theBulkExportJobEntity) {
		String theJobUuid = theBulkExportJobEntity.getJobId();
		JobParametersBuilder parameters = new JobParametersBuilder()
			.addString(BatchConstants.JOB_UUID_PARAMETER, theJobUuid)
			.addLong(BulkExportJobConfig.READ_CHUNK_PARAMETER, READ_CHUNK_SIZE);

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

	private boolean isPatientBulkJob(BulkExportJobEntity theBulkExportJobEntity) {
		return theBulkExportJobEntity.getRequest().startsWith("/Patient/");
	}

	private boolean isGroupBulkJob(BulkExportJobEntity theBulkExportJobEntity) {
		return theBulkExportJobEntity.getRequest().startsWith("/Group/");
	}

	private void enhanceBulkParametersWithGroupParameters(BulkExportJobEntity theBulkExportJobEntity, JobParametersBuilder theParameters) {
		String theGroupId = getQueryParameterIfPresent(theBulkExportJobEntity.getRequest(), JpaConstants.PARAM_EXPORT_GROUP_ID);
		String expandMdm  = getQueryParameterIfPresent(theBulkExportJobEntity.getRequest(), JpaConstants.PARAM_EXPORT_MDM);
		theParameters.addString(BulkExportJobConfig.GROUP_ID_PARAMETER, theGroupId);
		theParameters.addString(BulkExportJobConfig.EXPAND_MDM_PARAMETER, expandMdm);
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
	@Deprecated
	public JobInfo submitJob(BulkDataExportOptions theBulkDataExportOptions) {
		return submitJob(theBulkDataExportOptions, true, null);
	}

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Transactional
	@Override
	public JobInfo submitJob(BulkDataExportOptions theBulkDataExportOptions, Boolean useCache, RequestDetails theRequestDetails) {
		String outputFormat = Constants.CT_FHIR_NDJSON;
		if (isNotBlank(theBulkDataExportOptions.getOutputFormat())) {
			outputFormat = theBulkDataExportOptions.getOutputFormat();
		}
		if (!Constants.CTS_NDJSON.contains(outputFormat)) {
			throw new InvalidRequestException("Invalid output format: " + theBulkDataExportOptions.getOutputFormat());
		}

		// Interceptor call: STORAGE_INITIATE_BULK_EXPORT
		HookParams params = new HookParams()
			.add(BulkDataExportOptions.class, theBulkDataExportOptions)
			.add(RequestDetails.class, theRequestDetails)
			.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
		CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequestDetails, Pointcut.STORAGE_INITIATE_BULK_EXPORT, params);

		// TODO GGG KS can we encode BulkDataExportOptions as a JSON string as opposed to this request string.  Feels like it would be a more extensible encoding...
		//Probably yes, but this will all need to be rebuilt when we remove this bridge entity
		StringBuilder requestBuilder = new StringBuilder();
		requestBuilder.append("/");

		//Prefix the export url with Group/[id]/ or /Patient/ depending on what type of request it is.
		if (theBulkDataExportOptions.getExportStyle().equals(GROUP)) {
			requestBuilder.append(theBulkDataExportOptions.getGroupId().toVersionless()).append("/");
		} else if (theBulkDataExportOptions.getExportStyle().equals(PATIENT)) {
			requestBuilder.append("Patient/");
		}

		requestBuilder.append(JpaConstants.OPERATION_EXPORT);
		requestBuilder.append("?").append(JpaConstants.PARAM_EXPORT_OUTPUT_FORMAT).append("=").append(escapeUrlParam(outputFormat));
		Set<String> resourceTypes = theBulkDataExportOptions.getResourceTypes();
		if (resourceTypes != null) {
			requestBuilder.append("&").append(JpaConstants.PARAM_EXPORT_TYPE).append("=").append(String.join(",", escapeUrlParams(resourceTypes)));
		}
		Date since = theBulkDataExportOptions.getSince();
		if (since != null) {
			requestBuilder.append("&").append(JpaConstants.PARAM_EXPORT_SINCE).append("=").append(new InstantType(since).setTimeZoneZulu(true).getValueAsString());
		}
		if (theBulkDataExportOptions.getFilters() != null && theBulkDataExportOptions.getFilters().size() > 0) {
			theBulkDataExportOptions.getFilters().stream()
				.forEach(filter -> requestBuilder.append("&").append(JpaConstants.PARAM_EXPORT_TYPE_FILTER).append("=").append(escapeUrlParam(filter)));
		}

		if (theBulkDataExportOptions.getExportStyle().equals(GROUP)) {
			requestBuilder.append("&").append(JpaConstants.PARAM_EXPORT_GROUP_ID).append("=").append(theBulkDataExportOptions.getGroupId().getValue());
			requestBuilder.append("&").append(JpaConstants.PARAM_EXPORT_MDM).append("=").append(theBulkDataExportOptions.isExpandMdm());
		}

		String request = requestBuilder.toString();

		//If we are using the cache, then attempt to retrieve a matching job based on the Request String, otherwise just make a new one.
		if (useCache) {
			Date cutoff = DateUtils.addMilliseconds(new Date(), -myReuseBulkExportForMillis);
			Pageable page = PageRequest.of(0, 10);
			Slice<BulkExportJobEntity> existing = myBulkExportJobDao.findExistingJob(page, request, cutoff, BulkExportJobStatusEnum.ERROR);
			if (!existing.isEmpty()) {
				return toSubmittedJobInfo(existing.iterator().next());
			}
		}

		if (resourceTypes != null && resourceTypes.contains("Binary")) {
			String msg = myContext.getLocalizer().getMessage(BulkDataExportSvcImpl.class, "onlyBinarySelected");
			throw new InvalidRequestException(msg);
		}

		if (resourceTypes == null || resourceTypes.isEmpty()) {
			// This is probably not a useful default, but having the default be "download the whole
			// server" seems like a risky default too. We'll deal with that by having the default involve
			// only returning a small time span
			resourceTypes = getAllowedResourceTypesForBulkExportStyle(theBulkDataExportOptions.getExportStyle());
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
		job.setStatus(BulkExportJobStatusEnum.SUBMITTED);
		job.setSince(since);
		job.setCreated(new Date());
		job.setRequest(request);

		// Validate types
		validateTypes(resourceTypes);
		validateTypeFilters(theBulkDataExportOptions.getFilters(), resourceTypes);

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
			for (String next : theTheFilters) {
				if (!next.contains("?")) {
					throw new InvalidRequestException("Invalid " + JpaConstants.PARAM_EXPORT_TYPE_FILTER + " value \"" + next + "\". Must be in the form [ResourceType]?[params]");
				}
				String resourceType = next.substring(0, next.indexOf("?"));
				if (!theResourceTypes.contains(resourceType)) {
					throw new InvalidRequestException("Invalid " + JpaConstants.PARAM_EXPORT_TYPE_FILTER + " value \"" + next + "\". Resource type does not appear in " + JpaConstants.PARAM_EXPORT_TYPE+ " list");
				}
			}
		}
	}

	private JobInfo toSubmittedJobInfo(BulkExportJobEntity theJob) {
		return new JobInfo()
			.setJobId(theJob.getJobId())
			.setStatus(theJob.getStatus());
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

		if (job.getStatus() == BulkExportJobStatusEnum.COMPLETE) {
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

	@Override
	public Set<String> getPatientCompartmentResources() {
		if (myCompartmentResources == null) {
			myCompartmentResources = myContext.getResourceTypes().stream()
				.filter(this::resourceTypeIsInPatientCompartment)
				.collect(Collectors.toSet());
		}
		return myCompartmentResources;
	}

	/**
	 * Return true if any search parameter in the resource can point at a patient, false otherwise
	 */
	private boolean resourceTypeIsInPatientCompartment(String theResourceType) {
		RuntimeResourceDefinition runtimeResourceDefinition = myContext.getResourceDefinition(theResourceType);
		List<RuntimeSearchParam> searchParams = runtimeResourceDefinition.getSearchParamsForCompartmentName("Patient");
		return searchParams != null && searchParams.size() >= 1;
	}

	public Set<String> getAllowedResourceTypesForBulkExportStyle(BulkDataExportOptions.ExportStyle theExportStyle) {
		if (theExportStyle.equals(SYSTEM)) {
			return myContext.getResourceTypes();
		} else if (theExportStyle.equals(GROUP) || theExportStyle.equals(PATIENT)) {
			return getPatientCompartmentResources();
		} else {
			throw new IllegalArgumentException(String.format("HAPI FHIR does not recognize a Bulk Export request of type: %s", theExportStyle));
		}
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
