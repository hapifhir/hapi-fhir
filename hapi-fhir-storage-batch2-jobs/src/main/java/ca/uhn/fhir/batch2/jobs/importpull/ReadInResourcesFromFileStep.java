package ca.uhn.fhir.batch2.jobs.importpull;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.importpull.models.Batch2BulkImportPullJobParameters;
import ca.uhn.fhir.batch2.importpull.models.BulkImportFilePartitionResult;
import ca.uhn.fhir.batch2.importpull.models.BulkImportRecord;
import ca.uhn.fhir.batch2.importpull.svc.IBulkImportPullSvc;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.bulk.imprt.model.BulkImportJobFileJson;
import ca.uhn.fhir.jpa.bulk.imprt.model.JobFileRowProcessingModeEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.util.IoUtil;
import com.google.common.io.LineReader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.StringReader;

public class ReadInResourcesFromFileStep implements IJobStepWorker<Batch2BulkImportPullJobParameters, BulkImportFilePartitionResult, BulkImportRecord> {

	private static final Logger ourLog = LoggerFactory.getLogger(ReadInResourcesFromFileStep.class);

	@Autowired
	private IBulkImportPullSvc myBulkImportPullSvc;

	// because we are using an unstable google api
	@SuppressWarnings("UnstableApiUsage")
	@NotNull
	@Override
	public RunOutcome run(
		@NotNull StepExecutionDetails<Batch2BulkImportPullJobParameters, BulkImportFilePartitionResult> theStepExecutionDetails,
		@NotNull IJobDataSink<BulkImportRecord> theDataSink
	) throws JobExecutionFailedException {
		String jobId = theStepExecutionDetails.getParameters().getJobId();
		int fileIndex = theStepExecutionDetails.getData().getFileIndex();
		JobFileRowProcessingModeEnum mode = theStepExecutionDetails.getData().getProcessingMode();

		BulkImportJobFileJson file = myBulkImportPullSvc.fetchFileByIdAndIndex(jobId, fileIndex);
		String tenantName = file.getTenantName();
		String contents = file.getContents();

		StringReader reader = new StringReader(contents);

		// data explodes into even more chunks
		LineReader lineReader = new LineReader(reader);
		try {
			int lineIndex = 0;
			String nextLine;
			do {
				nextLine = lineReader.readLine();

				if (nextLine != null) {
					BulkImportRecord record = new BulkImportRecord();
					record.setResourceString(nextLine);
					record.setLineIndex(lineIndex);
					record.setTenantName(tenantName);
					record.setProcessingMode(mode);
					record.setFileIndex(fileIndex);

					theDataSink.accept(record);
				}
				lineIndex++;
			} while (nextLine != null);
		} catch (IOException ex) {
			ourLog.error("Failed to read file : " + ex.getMessage());

			throw new JobExecutionFailedException(Msg.code(2107)
				+ " : Could not read file"
			);
		} finally {
			IoUtil.closeQuietly(reader);
		}

		return RunOutcome.SUCCESS;
	}
}
