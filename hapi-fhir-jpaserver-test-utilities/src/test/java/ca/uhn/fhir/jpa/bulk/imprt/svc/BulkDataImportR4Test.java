package ca.uhn.fhir.jpa.bulk.imprt.svc;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobFileJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.JobFileRowProcessingModeEnum;
import ca.uhn.fhir.jpa.dao.data.IBulkImportJobDao;
import ca.uhn.fhir.jpa.dao.data.IBulkImportJobFileDao;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.BulkImportJobEntity;
import ca.uhn.fhir.jpa.entity.BulkImportJobFileEntity;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.test.utilities.BatchJobHelper;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.BULK_IMPORT_JOB_NAME;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BulkDataImportR4Test extends BaseJpaR4Test implements ITestDataBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataImportR4Test.class);
	@Autowired
	private IBulkDataImportSvc mySvc;
	@Autowired
	private IBulkImportJobDao myBulkImportJobDao;
	@Autowired
	private IBulkImportJobFileDao myBulkImportJobFileDao;
	@Autowired
	private JobExplorer myJobExplorer;
	@Autowired
	private JobRegistry myJobRegistry;
	@Autowired
	private BatchJobHelper myBatchJobHelper;

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof IAnonymousInterceptor);
		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof MyFailAfterThreeCreatesInterceptor);
	}

	@Order(-1)
	@Test
	public void testFlow_ErrorDuringWrite() {
		myInterceptorRegistry.registerInterceptor(new MyFailAfterThreeCreatesInterceptor());

		int transactionsPerFile = 5;
		int fileCount = 5;
		List<BulkImportJobFileJson> files = createInputFiles(transactionsPerFile, fileCount);

		BulkImportJobJson job = new BulkImportJobJson();
		job.setProcessingMode(JobFileRowProcessingModeEnum.FHIR_TRANSACTION);
		job.setJobDescription("This is the job description");
		job.setBatchSize(3);
		String jobId = mySvc.createNewJob(job, files);
		mySvc.markJobAsReadyForActivation(jobId);

		boolean activateJobOutcome = mySvc.activateNextReadyJob();
		assertTrue(activateJobOutcome);

		String[] jobNames = new String[]{BULK_IMPORT_JOB_NAME};
		assert jobNames.length > 0;

		await().until(() -> runInTransaction(() -> {
			JobInstance jobInstance = myJobExplorer.getLastJobInstance(BULK_IMPORT_JOB_NAME);
			JobExecution jobExecution = myJobExplorer.getLastJobExecution(jobInstance);
			ourLog.info("Exit status: {}", jobExecution.getExitStatus());
			return jobExecution.getExitStatus().getExitCode().equals(ExitStatus.FAILED.getExitCode());
		}));

		JobInstance jobInstance = myJobExplorer.getLastJobInstance(BULK_IMPORT_JOB_NAME);
		JobExecution jobExecution = myJobExplorer.getLastJobExecution(jobInstance);
		List<StepExecution> failedExecutions = jobExecution
			.getStepExecutions()
			.stream()
			.filter(t -> t.getExitStatus().getExitCode().equals("FAILED"))
			.collect(Collectors.toList());

		assertEquals(2, failedExecutions.size());
		assertThat(failedExecutions.get(1).getStepName(), containsString(":File With Description"));

	}

	@Order(0)
	@Test
	public void testFlow_TransactionRows() {
		int transactionsPerFile = 10;
		int fileCount = 10;
		List<BulkImportJobFileJson> files = createInputFiles(transactionsPerFile, fileCount);

		BulkImportJobJson job = new BulkImportJobJson();
		job.setProcessingMode(JobFileRowProcessingModeEnum.FHIR_TRANSACTION);
		job.setJobDescription("testFlow_TransactionRows");
		job.setBatchSize(3);
		String jobId = mySvc.createNewJob(job, files);
		mySvc.markJobAsReadyForActivation(jobId);

		boolean activateJobOutcome = mySvc.activateNextReadyJob();
		assertTrue(activateJobOutcome);

		List<JobExecution> executions = awaitAllBulkImportJobCompletion();
		assertEquals("testFlow_TransactionRows", executions.get(0).getJobParameters().getString(BatchConstants.JOB_DESCRIPTION));

		runInTransaction(() -> {
			List<BulkImportJobEntity> jobs = myBulkImportJobDao.findAll();
			assertEquals(0, jobs.size());

			List<BulkImportJobFileEntity> jobFiles = myBulkImportJobFileDao.findAll();
			assertEquals(0, jobFiles.size());

		});

		IBundleProvider searchResults = myPatientDao.search(SearchParameterMap.newSynchronous());
		assertEquals(transactionsPerFile * fileCount, searchResults.sizeOrThrowNpe());
	}

	@Order(1)
	@Test
	public void testFlow_WithTenantNamesInInput() {
		int transactionsPerFile = 5;
		int fileCount = 10;
		List<BulkImportJobFileJson> files = createInputFiles(transactionsPerFile, fileCount);
		for (int i = 0; i < fileCount; i++) {
			files.get(i).setTenantName("TENANT" + i);
		}

		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED, interceptor);

		BulkImportJobJson job = new BulkImportJobJson();
		job.setProcessingMode(JobFileRowProcessingModeEnum.FHIR_TRANSACTION);
		job.setBatchSize(5);
		String jobId = mySvc.createNewJob(job, files);
		mySvc.markJobAsReadyForActivation(jobId);

		boolean activateJobOutcome = mySvc.activateNextReadyJob();
		assertTrue(activateJobOutcome);

		awaitAllBulkImportJobCompletion();

		ArgumentCaptor<HookParams> paramsCaptor = ArgumentCaptor.forClass(HookParams.class);
		verify(interceptor, times(50)).invoke(any(), paramsCaptor.capture());
		List<String> tenantNames = paramsCaptor
			.getAllValues()
			.stream()
			.map(t -> t.get(RequestDetails.class).getTenantId())
			.distinct()
			.sorted()
			.collect(Collectors.toList());
		assertThat(tenantNames, containsInAnyOrder(
			"TENANT0", "TENANT1", "TENANT2", "TENANT3", "TENANT4", "TENANT5", "TENANT6", "TENANT7", "TENANT8", "TENANT9"
		));
	}

	@Nonnull
	private List<BulkImportJobFileJson> createInputFiles(int transactionsPerFile, int fileCount) {
		List<BulkImportJobFileJson> files = new ArrayList<>();
		int counter = 0;
		for (int fileIndex = 0; fileIndex < fileCount; fileIndex++) {
			StringBuilder fileContents = new StringBuilder();

			for (int transactionIdx = 0; transactionIdx < transactionsPerFile; transactionIdx++) {
				BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
				IBaseResource patient = buildPatient(withFamily("FAM " + fileIndex + " " + transactionIdx), withIdentifier(null, "patient" + counter++));
				bundleBuilder.addTransactionCreateEntry(patient);
				fileContents.append(myFhirContext.newJsonParser().setPrettyPrint(false).encodeResourceToString(bundleBuilder.getBundle()));
				fileContents.append("\n");
			}

			BulkImportJobFileJson nextFile = new BulkImportJobFileJson();
			nextFile.setContents(fileContents.toString());
			nextFile.setDescription("File With Description " + fileIndex);
			files.add(nextFile);
		}
		return files;
	}

	@Order(3)
	@Test
	public void testJobsAreRegisteredWithJobRegistry() throws NoSuchJobException {
		Job job = myJobRegistry.getJob(BULK_IMPORT_JOB_NAME);
		assertEquals(true, job.isRestartable());
	}

	protected List<JobExecution> awaitAllBulkImportJobCompletion() {
		return myBatchJobHelper.awaitAllBulkJobCompletions(BatchConstants.BULK_IMPORT_JOB_NAME);
	}

	@Interceptor
	public class MyFailAfterThreeCreatesInterceptor {

		public static final String ERROR_MESSAGE = "This is an error message";

		@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
		public void create(IBaseResource thePatient) {
			Patient patient = (Patient) thePatient;
			if (patient.getIdentifierFirstRep().getValue().equals("patient10")) {
				throw new InternalErrorException(ERROR_MESSAGE);
			}
		}
	}
}
