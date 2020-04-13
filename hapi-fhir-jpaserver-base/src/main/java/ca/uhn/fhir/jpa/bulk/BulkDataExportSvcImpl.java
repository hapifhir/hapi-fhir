package ca.uhn.fhir.jpa.bulk;

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
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionFileDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionEntity;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionFileEntity;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.sched.HapiJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.BinaryUtil;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.InstantType;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.UrlUtil.escapeUrlParam;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class BulkDataExportSvcImpl implements IBulkDataExportSvc {

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
	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;
	private TransactionTemplate myTxTemplate;

	private long myFileMaxChars = 500 * FileUtils.ONE_KB;
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
			myTxTemplate.execute(t -> {
				processJob(jobUuid);
				return null;
			});
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

		Optional<BulkExportJobEntity> jobOpt = myBulkExportJobDao.findByJobId(theJobUuid);
		if (!jobOpt.isPresent()) {
			ourLog.info("Job appears to be deleted");
			return;
		}

		StopWatch jobStopwatch = new StopWatch();
		AtomicInteger jobResourceCounter = new AtomicInteger();

		BulkExportJobEntity job = jobOpt.get();
		ourLog.info("Bulk export starting generation for batch export job: {}", job);

		for (BulkExportCollectionEntity nextCollection : job.getCollections()) {

			String nextType = nextCollection.getResourceType();
			IFhirResourceDao dao = myDaoRegistry.getResourceDao(nextType);

			ourLog.info("Bulk export assembling export of type {} for job {}", nextType, theJobUuid);

			Class<? extends IBaseResource> nextTypeClass = myContext.getResourceDefinition(nextType).getImplementingClass();
			ISearchBuilder sb = mySearchBuilderFactory.newSearchBuilder(dao, nextType, nextTypeClass);

			SearchParameterMap map = new SearchParameterMap();
			map.setLoadSynchronous(true);
			if (job.getSince() != null) {
				map.setLastUpdated(new DateRangeParam(job.getSince(), null));
			}

			IResultIterator resultIterator = sb.createQuery(map, new SearchRuntimeDetails(null, theJobUuid), null);
			storeResultsToFiles(nextCollection, sb, resultIterator, jobResourceCounter, jobStopwatch);
		}

		job.setStatus(BulkJobStatusEnum.COMPLETE);
		updateExpiry(job);
		myBulkExportJobDao.save(job);

		ourLog.info("Bulk export completed job in {}: {}", jobStopwatch, job);

	}

	private void storeResultsToFiles(BulkExportCollectionEntity theExportCollection, ISearchBuilder theSearchBuilder, IResultIterator theResultIterator, AtomicInteger theJobResourceCounter, StopWatch theJobStopwatch) {

		try (IResultIterator query = theResultIterator) {
			if (!query.hasNext()) {
				return;
			}

			AtomicInteger fileCounter = new AtomicInteger(0);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(outputStream, Constants.CHARSET_UTF8);
			IParser parser = myContext.newJsonParser().setPrettyPrint(false);

			List<ResourcePersistentId> pidsSpool = new ArrayList<>();
			List<IBaseResource> resourcesSpool = new ArrayList<>();
			while (query.hasNext()) {
				pidsSpool.add(query.next());
				fileCounter.incrementAndGet();
				theJobResourceCounter.incrementAndGet();

				if (pidsSpool.size() >= 10 || !query.hasNext()) {

					theSearchBuilder.loadResourcesByPid(pidsSpool, Collections.emptyList(), resourcesSpool, false, null);

					for (IBaseResource nextFileResource : resourcesSpool) {
						parser.encodeResourceToWriter(nextFileResource, writer);
						writer.append("\n");
					}

					pidsSpool.clear();
					resourcesSpool.clear();

					if (outputStream.size() >= myFileMaxChars || !query.hasNext()) {
						Optional<IIdType> createdId = flushToFiles(theExportCollection, fileCounter, outputStream);
						createdId.ifPresent(theIIdType -> ourLog.info("Created resource {} for bulk export file containing {} resources of type {} - Total {} resources ({}/sec)", theIIdType.toUnqualifiedVersionless().getValue(), fileCounter.get(), theExportCollection.getResourceType(), theJobResourceCounter.get(), theJobStopwatch.formatThroughput(theJobResourceCounter.get(), TimeUnit.SECONDS)));
						fileCounter.set(0);
					}

				}
			}

		} catch (IOException e) {
			throw new InternalErrorException(e);
		}
	}

	private Optional<IIdType> flushToFiles(BulkExportCollectionEntity theCollection, AtomicInteger theCounter, ByteArrayOutputStream theOutputStream) {
		if (theOutputStream.size() > 0) {
			IBaseBinary binary = BinaryUtil.newBinary(myContext);
			binary.setContentType(Constants.CT_FHIR_NDJSON);
			binary.setContent(theOutputStream.toByteArray());

			IIdType createdId = getBinaryDao().create(binary).getResource().getIdElement();

			BulkExportCollectionFileEntity file = new BulkExportCollectionFileEntity();
			theCollection.getFiles().add(file);
			file.setCollection(theCollection);
			file.setResource(createdId.getIdPart());
			myBulkExportCollectionFileDao.saveAndFlush(file);
			theOutputStream.reset();

			return Optional.of(createdId);
		}

		return Optional.empty();
	}

	@SuppressWarnings("unchecked")
	private IFhirResourceDao<IBaseBinary> getBinaryDao() {
		return myDaoRegistry.getResourceDao("Binary");
	}

	@PostConstruct
	public void start() {
		myTxTemplate = new TransactionTemplate(myTxManager);

		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(getClass().getName());
		jobDetail.setJobClass(Job.class);
		mySchedulerService.scheduleClusteredJob(10 * DateUtils.MILLIS_PER_SECOND, jobDetail);
	}

	public static class Job implements HapiJob {
		@Autowired
		private IBulkDataExportSvc myTarget;

		@Override
		public void execute(JobExecutionContext theContext) {
			myTarget.buildExportFiles();
		}
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
			requestBuilder.append("&").append(JpaConstants.PARAM_EXPORT_TYPE).append("=").append(String.join(",", resourceTypes));
		}
		Date since = theSince;
		if (since != null) {
			requestBuilder.append("&").append(JpaConstants.PARAM_EXPORT_SINCE).append("=").append(new InstantType(since).setTimeZoneZulu(true).getValueAsString());
		}
		if (theFilters != null && theFilters.size() > 0) {
			requestBuilder.append("&").append(JpaConstants.PARAM_EXPORT_TYPE_FILTER).append("=").append(String.join(",", theFilters));
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
			resourceTypes = myContext.getResourceNames();
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

		updateExpiry(job);
		myBulkExportJobDao.save(job);

		for (String nextType : resourceTypes) {
			if (!myDaoRegistry.isResourceTypeSupported(nextType)) {
				String msg = myContext.getLocalizer().getMessage(BulkDataExportSvcImpl.class, "unknownResourceType", nextType);
				throw new InvalidRequestException(msg);
			}

			BulkExportCollectionEntity collection = new BulkExportCollectionEntity();
			collection.setJob(job);
			collection.setResourceType(nextType);
			job.getCollections().add(collection);
			myBulkExportCollectionDao.save(collection);
		}

		ourLog.info("Bulk export job submitted: {}", job.toString());

		return toSubmittedJobInfo(job);
	}

	private JobInfo toSubmittedJobInfo(BulkExportJobEntity theJob) {
		return new JobInfo().setJobId(theJob.getJobId());
	}


	private void updateExpiry(BulkExportJobEntity theJob) {
		theJob.setExpiry(DateUtils.addMilliseconds(new Date(), myRetentionPeriod));
	}

	@Transactional
	@Override
	public JobInfo getJobStatusOrThrowResourceNotFound(String theJobId) {
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
}
