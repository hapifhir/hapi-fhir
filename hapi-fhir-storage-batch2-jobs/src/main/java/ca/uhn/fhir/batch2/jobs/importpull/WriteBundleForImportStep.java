package ca.uhn.fhir.batch2.jobs.importpull;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.ILastJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.importpull.models.Batch2BulkImportPullJobParameters;
import ca.uhn.fhir.batch2.importpull.models.BulkImportRecord;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.bulk.imprt.model.JobFileRowProcessingModeEnum;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class WriteBundleForImportStep implements ILastJobStepWorker<Batch2BulkImportPullJobParameters, BulkImportRecord> {

	private static final Logger ourLog = LoggerFactory.getLogger(WriteBundleForImportStep.class);

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@SuppressWarnings({"SwitchStatementWithTooFewBranches", "rawtypes", "unchecked"})
	@NotNull
	@Override
	public RunOutcome run(
		@NotNull StepExecutionDetails<Batch2BulkImportPullJobParameters, BulkImportRecord> theStepExecutionDetails,
		@NotNull IJobDataSink<VoidModel> theDataSink
	) throws JobExecutionFailedException {

		BulkImportRecord record = theStepExecutionDetails.getData();

		JobFileRowProcessingModeEnum mode = record.getProcessingMode();
		int fileIndex = record.getFileIndex();
		String content = record.getResourceString();
		String tenantName = record.getTenantName();
		int lineIndex = record.getLineIndex();
		String jobId = theStepExecutionDetails.getParameters().getJobId();

		ourLog.info(
			"Beginning bulk import write row {} for Job[{}] FileIndex[{}]",
			lineIndex,
			jobId,
			fileIndex
		);

		IParser parser = myFhirContext.newJsonParser();

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.setTenantId(tenantName);

		IBaseResource bundle = parser.parseResource(content);

		// Yeah this is a lame switch - We'll add more later I swear
		switch (mode) {
			default:
			case FHIR_TRANSACTION:
				IFhirSystemDao systemDao = myDaoRegistry.getSystemDao();
				systemDao.transaction(requestDetails, bundle);
				break;
		}

		ourLog.info(
			"Completed bulk import write for row {} Job[{}] FileIndex[{}]",
			lineIndex,
			jobId,
			fileIndex
		);
		return RunOutcome.SUCCESS;
	}
}
