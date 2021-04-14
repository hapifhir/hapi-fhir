package ca.uhn.fhir.jpa.bulk.imprt.svc;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.batch.BatchJobsConfig;
import ca.uhn.fhir.jpa.bulk.BaseBatchJobR4Test;
import ca.uhn.fhir.jpa.bulk.export.job.BulkExportJobConfig;
import ca.uhn.fhir.jpa.bulk.imprt.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobFileJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobStatusEnum;
import ca.uhn.fhir.jpa.bulk.imprt.model.JobFileRowProcessingModeEnum;
import ca.uhn.fhir.jpa.dao.data.IBulkImportJobDao;
import ca.uhn.fhir.jpa.dao.data.IBulkImportJobFileDao;
import ca.uhn.fhir.jpa.entity.BulkImportJobEntity;
import ca.uhn.fhir.jpa.entity.BulkImportJobFileEntity;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BulkDataImportR4Test extends BaseBatchJobR4Test implements ITestDataBuilder {

	@Autowired
	private IBulkDataImportSvc mySvc;
	@Autowired
	private IBulkImportJobDao myBulkImportJobDao;
	@Autowired
	private IBulkImportJobFileDao myBulkImportJobFileDao;

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof IAnonymousInterceptor);
	}

	@Test
	public void testFlow_TransactionRows() {
		int transactionsPerFile = 10;
		int fileCount = 10;
		List<BulkImportJobFileJson> files = createInputFiles(transactionsPerFile, fileCount);

		BulkImportJobJson job = new BulkImportJobJson();
		job.setProcessingMode(JobFileRowProcessingModeEnum.FHIR_TRANSACTION);
		job.setJobDescription("This is the description");
		job.setBatchSize(3);
		String jobId = mySvc.createNewJob(job, files);
		mySvc.markJobAsReadyForActivation(jobId);

		boolean activateJobOutcome = mySvc.activateNextReadyJob();
		assertTrue(activateJobOutcome);

		List<JobExecution> executions = awaitAllBulkJobCompletions();
		assertEquals(1, executions.size());
		assertEquals("", executions.get(0).getJobParameters().getString(BulkExportJobConfig.JOB_DESCRIPTION));

		runInTransaction(() -> {
			List<BulkImportJobEntity> jobs = myBulkImportJobDao.findAll();
			assertEquals(1, jobs.size());
			assertEquals(BulkImportJobStatusEnum.COMPLETE, jobs.get(0).getStatus(), jobs.get(0).getStatusMessage());

			List<BulkImportJobFileEntity> jobFiles = myBulkImportJobFileDao.findAll();
			assertEquals(0, jobFiles.size());

		});

		IBundleProvider searchResults = myPatientDao.search(SearchParameterMap.newSynchronous());
		assertEquals(transactionsPerFile * fileCount, searchResults.sizeOrThrowNpe());

	}

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

		awaitAllBulkJobCompletions();

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
		for (int fileIndex = 0; fileIndex < fileCount; fileIndex++) {
			StringBuilder fileContents = new StringBuilder();

			for (int transactionIdx = 0; transactionIdx < transactionsPerFile; transactionIdx++) {
				BundleBuilder bundleBuilder = new BundleBuilder(myFhirCtx);
				IBaseResource patient = buildPatient(withFamily("FAM " + fileIndex + " " + transactionIdx));
				bundleBuilder.addTransactionCreateEntry(patient);
				fileContents.append(myFhirCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(bundleBuilder.getBundle()));
				fileContents.append("\n");
			}

			BulkImportJobFileJson nextFile = new BulkImportJobFileJson();
			nextFile.setContents(fileContents.toString());
			files.add(nextFile);
		}
		return files;
	}

	protected List<JobExecution> awaitAllBulkJobCompletions() {
		return awaitAllBulkJobCompletions(BatchJobsConfig.BULK_IMPORT_JOB_NAME);
	}

}
