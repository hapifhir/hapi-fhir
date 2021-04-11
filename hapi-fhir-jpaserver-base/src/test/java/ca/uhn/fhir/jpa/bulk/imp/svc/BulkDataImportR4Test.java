package ca.uhn.fhir.jpa.bulk.imp.svc;

import ca.uhn.fhir.jpa.batch.BatchJobsConfig;
import ca.uhn.fhir.jpa.bulk.BaseBatchJobR4Test;
import ca.uhn.fhir.jpa.bulk.imp.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imp.model.BulkImportJobFileJson;
import ca.uhn.fhir.jpa.bulk.imp.model.BulkImportJobJson;
import ca.uhn.fhir.jpa.bulk.imp.model.JobFileRowProcessingModeEnum;
import ca.uhn.fhir.jpa.dao.data.IBulkImportJobDao;
import ca.uhn.fhir.jpa.dao.data.IBulkImportJobFileDao;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.explore.JobExplorer;
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
	@Autowired
	private JobExplorer myJobExplorer;

	@Test
	public void testFlow_TransactionRows() {
		int transactionsPerFile = 2;
		int fileCount = 2;

		List<BulkImportJobFileJson> files = new ArrayList<>();
		for (int fileIndex = 0; fileIndex < fileCount; fileIndex++) {
			StringBuilder fileContents = new StringBuilder();

			for (int transactionIdx = 0; transactionIdx < transactionsPerFile; transactionIdx++) {
				BundleBuilder bundleBuilder = new BundleBuilder(myFhirCtx);
				IBaseResource patient = buildPatient(withId("PT-" + fileIndex + "-" + transactionIdx), withFamily("FAM " + fileIndex + " " + transactionIdx));
				bundleBuilder.addTransactionUpdateEntry(patient);
				fileContents.append(myFhirCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(bundleBuilder.getBundle()));
				fileContents.append("\n");
			}

			BulkImportJobFileJson nextFile = new BulkImportJobFileJson();
			nextFile.setContents(fileContents.toString());
			files.add(nextFile);
		}

		BulkImportJobJson job = new BulkImportJobJson();
		job.setProcessingMode(JobFileRowProcessingModeEnum.FHIR_TRANSACTION);
		String jobId = mySvc.createNewJob(job, files);
		mySvc.markJobAsReadyForActivation(jobId);

		boolean activateJobOutcome = mySvc.activateNextReadyJob();
		assertTrue(activateJobOutcome);

		awaitAllBulkJobCompletions();

		// Should not fail
		Patient pt = myPatientDao.read(new IdType("Patient/PT-3-3"));
		assertEquals("PT-3-3", pt.getNameFirstRep().getFamily());
	}

	protected void awaitAllBulkJobCompletions() {
		awaitAllBulkJobCompletions(BatchJobsConfig.BULK_IMPORT_JOB_NAME);
	}

}
