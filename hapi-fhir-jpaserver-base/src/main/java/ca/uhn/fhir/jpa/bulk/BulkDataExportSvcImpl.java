package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionEntity;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.jpa.model.sched.FireAtIntervalJob;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.sched.ScheduledJobDefinition;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
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

public class BulkDataExportSvcImpl implements IBulkDataExportSvc {

	private static final long REFRESH_INTERVAL = 10 * DateUtils.MILLIS_PER_SECOND;
	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataExportSvcImpl.class);

	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;
	@Autowired
	private IBulkExportCollectionDao myBulkExportCollectionDao;
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

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	private EntityManager myEntityManager;

	@Transactional(value = Transactional.TxType.NEVER)
	@Override
	public synchronized void runPass() {

		Optional<BulkExportJobEntity> jobToProcessOpt = myTxTemplate.execute(t -> {
			Pageable page = PageRequest.of(0, 1);
			Slice<BulkExportJobEntity> submittedJobs = myBulkExportJobDao.findByStatus(page, BulkExportJobEntity.StatusEnum.SUBMITTED);
			if (submittedJobs.isEmpty()) {
				return Optional.empty();
			}
			return Optional.of(submittedJobs.getContent().get(0));
		});

		if (!jobToProcessOpt.isPresent()) {
			return;
		}

		myTxTemplate.execute(t -> {
			String jobUuid = jobToProcessOpt.get().getJobId();
			ourLog.info("Starting generation for batch export job: {}", jobUuid);

			Optional<BulkExportJobEntity> job = myBulkExportJobDao.findById(jobToProcessOpt.get().getId());
			if (!job.isPresent()) {
				ourLog.info("Job appears to be deleted");
				return;
			}

			for (BulkExportCollectionEntity nextCollection : job.get().getCollections()) {

				String nextType = nextCollection.getResourceType();
				IFhirResourceDao dao = myDaoRegistry.getResourceDao(nextType);

				ISearchBuilder sb = dao.newSearchBuilder();
				SearchParameterMap map = new SearchParameterMap();
				map.setLoadSynchronous(true);
				dao.search(map);
				try (IResultIterator query = sb.createQuery(map, new SearchRuntimeDetails(jobUuid))) {

					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					OutputStreamWriter writer = new OutputStreamWriter(outputStream, Constants.CHARSET_UTF8);
					IParser parser = myContext.newJsonParser().setPrettyPrint(false);

					List<Long> filePids = new ArrayList<>();
					List<IBaseResource> fileResources = new ArrayList<>();
					while (query.hasNext()) {
						filePids.add(query.next());

						if (filePids.size() >= 10) {

							sb.loadResourcesByPid(filePids, fileResources, Collections.emptySet(), false, myEntityManager, myContext, dao);

							for (IBaseResource nextFileResource : fileResources) {
								parser.encodeResourceToWriter(nextFileResource, writer);
								writer.append("\n");
							}

							filePids.clear();
							fileResources.clear();

							if (outputStream.size() >= myFileMaxChars) {

								IBaseBinary binary = BinaryUtil.newBinary(myContext);
								binary.setContentType(Constants.CT_FHIR_NDJSON);
								binary.setContent(outputStream.toByteArray());

								IFhirResourceDao binaryDao = myDaoRegistry.getResourceDao("Binary");
								IIdType createdId = binaryDao.create(binary).getId();


								outputStream.reset();
							}

						}
					}


				} catch (IOException e) {
					throw new InternalErrorException(e);
				}

			}
		});

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
	public NewJobInfo submitJob(Set<String> theResourceTypes, DateTimeType theSince, Set<String> theFilters) {
		BulkExportJobEntity job = new BulkExportJobEntity();
		job.setJobId(UUID.randomUUID().toString());
		job.setStatus(BulkExportJobEntity.StatusEnum.SUBMITTED);
		job.setStatusTime(new Date());
		myBulkExportJobDao.save(job);

		ourLog.info("New bulk data export job submitted with ID: {}", job.getJobId());

		return new NewJobInfo().setJobId(job.getJobId());
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
			myTarget.runPass();
		}
	}
	

}
