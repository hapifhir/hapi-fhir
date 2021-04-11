package ca.uhn.fhir.jpa.bulk.imp.svc;

import ca.uhn.fhir.jpa.batch.BatchJobsConfig;
import ca.uhn.fhir.jpa.bulk.BaseBatchJobR4Test;
import ca.uhn.fhir.jpa.bulk.imp.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imp.model.BulkImportJobFileJson;
import ca.uhn.fhir.jpa.bulk.imp.model.BulkImportJobJson;
import ca.uhn.fhir.jpa.bulk.imp.model.BulkImportJobStatusEnum;
import ca.uhn.fhir.jpa.bulk.imp.model.JobFileRowProcessingModeEnum;
import ca.uhn.fhir.jpa.dao.data.IBulkImportJobDao;
import ca.uhn.fhir.jpa.dao.data.IBulkImportJobFileDao;
import ca.uhn.fhir.jpa.entity.BulkImportJobEntity;
import ca.uhn.fhir.jpa.entity.BulkImportJobFileEntity;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BulkDataImportR4Test extends BaseBatchJobR4Test implements ITestDataBuilder {

	@Autowired
	private IBulkDataImportSvc mySvc;
	@Autowired
	private IBulkImportJobDao myBulkImportJobDao;
	@Autowired
	private IBulkImportJobFileDao myBulkImportJobFileDao;

	@Test
	public void testFlow_TransactionRows() {
		int transactionsPerFile = 10;
		int fileCount = 10;

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

		BulkImportJobJson job = new BulkImportJobJson();
		job.setProcessingMode(JobFileRowProcessingModeEnum.FHIR_TRANSACTION);
		job.setBatchSize(3);
		String jobId = mySvc.createNewJob(job, files);
		mySvc.markJobAsReadyForActivation(jobId);

		boolean activateJobOutcome = mySvc.activateNextReadyJob();
		assertTrue(activateJobOutcome);

		awaitAllBulkJobCompletions();

		IBundleProvider searchResults = myPatientDao.search(SearchParameterMap.newSynchronous());
		assertEquals(transactionsPerFile * fileCount, searchResults.sizeOrThrowNpe());

		runInTransaction(() -> {
			List<BulkImportJobEntity> jobs = myBulkImportJobDao.findAll();
			assertEquals(1, jobs.size());
			assertEquals(BulkImportJobStatusEnum.COMPLETE, jobs.get(0).getStatus());

			List<BulkImportJobFileEntity> jobFiles = myBulkImportJobFileDao.findAll();
			assertEquals(0, jobFiles.size());

		});
	}

	protected void awaitAllBulkJobCompletions() {
		awaitAllBulkJobCompletions(BatchJobsConfig.BULK_IMPORT_JOB_NAME);
	}

}
