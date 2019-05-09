package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionFileDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionEntity;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionFileEntity;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.jpa.model.sched.FireAtIntervalJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.ExpungeOptions;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.BinaryUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.DateTimeType;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BulkDataExportSvcImpl implements IBulkDataExportSvc {

	private static final long REFRESH_INTERVAL = 10 * DateUtils.MILLIS_PER_SECOND;
	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataExportSvcImpl.class);

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

	private long myFileMaxChars = FileUtils.ONE_MB;
	private int myRetentionPeriod = (int) DateUtils.MILLIS_PER_DAY;

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;

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
					submittedJobs.get().setStatus(BulkJobStatusEnum.ERROR);
					myBulkExportJobDao.save(submittedJobs.get());
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

			ourLog.info("Deleting bulk export job: {}", jobToDelete.get().getJobId());

			myTxTemplate.execute(t -> {

				BulkExportJobEntity job = myBulkExportJobDao.getOne(jobToDelete.get().getId());
				for (BulkExportCollectionEntity nextCollection : job.getCollections()) {
					for (BulkExportCollectionFileEntity nextFile : nextCollection.getFiles()) {

						ourLog.info("Purging bulk data file: {}", nextFile.getResourceId());
						getBinaryDao().delete(toId(nextFile.getResourceId()));
						getBinaryDao().forceExpungeInExistingTransaction(toId(nextFile.getResourceId()), new ExpungeOptions().setExpungeDeletedResources(true).setExpungeOldVersions(true));
						myBulkExportCollectionFileDao.delete(nextFile);

					}

					myBulkExportCollectionDao.delete(nextCollection);
				}

				myBulkExportJobDao.delete(job);
				return null;
			});

		}

	}

	private void processJob(String theJobUuid) {
		ourLog.info("Starting generation for batch export jobOpt: {}", theJobUuid);

		Optional<BulkExportJobEntity> jobOpt = myBulkExportJobDao.findByJobId(theJobUuid);
		if (!jobOpt.isPresent()) {
			ourLog.info("Job appears to be deleted");
			return;
		}

		BulkExportJobEntity job = jobOpt.get();
		for (BulkExportCollectionEntity nextCollection : job.getCollections()) {

			String nextType = nextCollection.getResourceType();
			IFhirResourceDao dao = myDaoRegistry.getResourceDao(nextType);

			ISearchBuilder sb = dao.newSearchBuilder();
			Class<? extends IBaseResource> nextTypeClass = myContext.getResourceDefinition(nextType).getImplementingClass();
			sb.setType(nextTypeClass, nextType);

			SearchParameterMap map = new SearchParameterMap();
			map.setLoadSynchronous(true);
			dao.search(map);
			try (IResultIterator query = sb.createQuery(map, new SearchRuntimeDetails(theJobUuid))) {

				AtomicInteger counter = new AtomicInteger(0);
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				OutputStreamWriter writer = new OutputStreamWriter(outputStream, Constants.CHARSET_UTF8);
				IParser parser = myContext.newJsonParser().setPrettyPrint(false);

				List<Long> pidsSpool = new ArrayList<>();
				List<IBaseResource> resourcesSpool = new ArrayList<>();
				while (query.hasNext()) {
					pidsSpool.add(query.next());
					counter.incrementAndGet();

					if (pidsSpool.size() >= 10) {

						sb.loadResourcesByPid(pidsSpool, resourcesSpool, Collections.emptySet(), false, myEntityManager, myContext, dao);

						for (IBaseResource nextFileResource : resourcesSpool) {
							parser.encodeResourceToWriter(nextFileResource, writer);
							writer.append("\n");
						}

						pidsSpool.clear();
						resourcesSpool.clear();

						if (outputStream.size() >= myFileMaxChars) {
							flushToFiles(nextCollection, counter, outputStream);
						}

					}
				}

				flushToFiles(nextCollection, counter, outputStream);

			} catch (IOException e) {
				throw new InternalErrorException(e);
			}


		}

		job.setStatus(BulkJobStatusEnum.COMPLETE);
		updateExpiry(job);
		myBulkExportJobDao.save(job);

	}

	private void flushToFiles(BulkExportCollectionEntity theCollection, AtomicInteger theCounter, ByteArrayOutputStream theOutputStream) {
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

			ourLog.info("Created resource {} for bulk export file containing {} resources of type {}", createdId, theCounter.get(), theCollection.getResourceType());

		}
	}

	@SuppressWarnings("unchecked")
	private IFhirResourceDao<IBaseBinary> getBinaryDao() {
		return myDaoRegistry.getResourceDao("Binary");
	}

	@PostConstruct
	public void start() {
		myTxTemplate = new TransactionTemplate(myTxManager);
	}

	@PostConstruct
	public void registerScheduledJob() {
		ScheduledJobDefinition jobDetail = new ScheduledJobDefinition();
		jobDetail.setId(BulkDataExportSvcImpl.class.getName());
		jobDetail.setJobClass(BulkDataExportSvcImpl.SubmitJob.class);
		mySchedulerService.scheduleFixedDelay(REFRESH_INTERVAL, true, jobDetail);
	}

	@Transactional
	@Override
	public JobInfo submitJob(Set<String> theResourceTypes, DateTimeType theSince, Set<String> theFilters) {
		BulkExportJobEntity job = new BulkExportJobEntity();
		job.setJobId(UUID.randomUUID().toString());
		job.setStatus(BulkJobStatusEnum.SUBMITTED);
		updateExpiry(job);
		myBulkExportJobDao.save(job);

		for (String nextType : theResourceTypes) {
			BulkExportCollectionEntity collection = new BulkExportCollectionEntity();
			collection.setJob(job);
			collection.setResourceType(nextType);
			job.getCollections().add(collection);
			myBulkExportCollectionDao.save(collection);
		}

		ourLog.info("New bulk data export job submitted with ID: {}", job.getJobId());

		return new JobInfo().setJobId(job.getJobId());
	}

	private void updateExpiry(BulkExportJobEntity theJob) {
		theJob.setExpiry(DateUtils.addMilliseconds(new Date(), myRetentionPeriod));
	}

	@Transactional
	@Override
	public JobInfo getJobStatus(String theJobId) {
		BulkExportJobEntity job = myBulkExportJobDao
			.findByJobId(theJobId)
			.orElseThrow(() -> new ResourceNotFoundException(theJobId));

		JobInfo retVal = new JobInfo();
		retVal.setJobId(theJobId);
		retVal.setStatus(job.getStatus());
		retVal.setFiles(new ArrayList<>());

		if (job.getStatus() == BulkJobStatusEnum.COMPLETE) {
			for (BulkExportCollectionEntity nextCollection : job.getCollections()) {
				for (BulkExportCollectionFileEntity nextFile : nextCollection.getFiles()) {
					retVal.getFiles().add(new FileEntry()
						.setResourceType(nextCollection.getResourceType())
						.setResourceId(toQualifiedBinaryId(nextFile.getResourceId())));
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
	@Transactional
	public synchronized void cancelAndPurgeAllJobs() {
		myBulkExportJobDao.deleteAll();
	}

	@DisallowConcurrentExecution
	@PersistJobDataAfterExecution
	public static class SubmitJob extends FireAtIntervalJob {
		@Autowired
		private IBulkDataExportSvc myTarget;

		public SubmitJob() {
			super(REFRESH_INTERVAL);
		}

		@Override
		protected void doExecute(JobExecutionContext theContext) {
			myTarget.buildExportFiles();
		}
	}


}
