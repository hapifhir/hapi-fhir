package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportBinaryFileId;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class BulkExportCreateReportStep implements IReductionStepWorker<BulkExportJobParameters, BulkExportBinaryFileId, BulkExportJobResults> {
	private static final Logger ourLog = getLogger(BulkExportCreateReportStep.class);

	@Autowired
	private IBulkExportProcessor myBulkIdProcessor;

	public BulkExportCreateReportStep() {
		ourLog.info("\n\nBULKEXPORTCREATERPORT STEP CONSTRUTOR\n\n");
	}

	private List<String> myIdList;

	@NotNull
	@Override
	public RunOutcome run(@NotNull StepExecutionDetails<BulkExportJobParameters, BulkExportBinaryFileId> theStepExecutionDetails,
								 @NotNull IJobDataSink<BulkExportJobResults> theDataSink) throws JobExecutionFailedException {
		if (myIdList != null) {
			ourLog.info("Bulk Export Report creation step");

			BulkExportJobResults results = new BulkExportJobResults();
			results.setResourceType(theStepExecutionDetails.getData().getResourceType());
			results.setBinaryIds(myIdList);

			// accept saves the report
			theDataSink.accept(results);

			myIdList = null;
		} else {
			ourLog.warn("Export complete, but no data to generate report.");
		}
		return RunOutcome.SUCCESS;
	}

	@NotNull
	@Override
	public ChunkOutcome consume(ChunkExecutionDetails<BulkExportJobParameters,
		BulkExportBinaryFileId> theChunkDetails) {
		if (myIdList == null) {
			myIdList = new ArrayList<>();
		}

		BulkExportBinaryFileId fileId = theChunkDetails.getData();
		myIdList.add(fileId.getBinaryId());

		return ChunkOutcome.SUCCESS();
	}
}
