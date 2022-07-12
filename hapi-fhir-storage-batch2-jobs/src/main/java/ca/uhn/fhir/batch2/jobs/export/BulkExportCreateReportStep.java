package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportBinaryFileId;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.jpa.api.model.BulkExportJobResults;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class BulkExportCreateReportStep implements IReductionStepWorker<BulkExportJobParameters, BulkExportBinaryFileId, BulkExportJobResults> {
	private static final Logger ourLog = getLogger(BulkExportCreateReportStep.class);

	private Map<String, List<String>> myResourceToBinaryIds;

	@NotNull
	@Override
	public RunOutcome run(@NotNull StepExecutionDetails<BulkExportJobParameters, BulkExportBinaryFileId> theStepExecutionDetails,
								 @NotNull IJobDataSink<BulkExportJobResults> theDataSink) throws JobExecutionFailedException {
		if (myResourceToBinaryIds != null) {
			ourLog.info("Bulk Export Report creation step");

			BulkExportJobResults results = new BulkExportJobResults();
			results.setResourceTypeToBinaryIds(myResourceToBinaryIds);

			// accept saves the report
			theDataSink.accept(results);

			myResourceToBinaryIds = null;
		} else {
			ourLog.warn("Export complete, but no data to generate report.");
		}
		return RunOutcome.SUCCESS;
	}

	@NotNull
	@Override
	public ChunkOutcome consume(ChunkExecutionDetails<BulkExportJobParameters,
		BulkExportBinaryFileId> theChunkDetails) {
		BulkExportBinaryFileId fileId = theChunkDetails.getData();
		if (myResourceToBinaryIds == null) {
			myResourceToBinaryIds = new HashMap<>();
		}

		myResourceToBinaryIds.putIfAbsent(fileId.getResourceType(), new ArrayList<>());

		myResourceToBinaryIds.get(fileId.getResourceType()).add(fileId.getBinaryId());

		return ChunkOutcome.SUCCESS();
	}
}
