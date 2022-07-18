package ca.uhn.fhir.batch2.jobs.export;

/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
		BulkExportJobResults results = new BulkExportJobResults();

		if (myResourceToBinaryIds != null) {
			ourLog.info("Bulk Export Report creation step");

			results.setResourceTypeToBinaryIds(myResourceToBinaryIds);

			myResourceToBinaryIds = null;
		} else {
			String msg = "Export complete, but no data to generate report.";
			ourLog.warn(msg);

			results.setReportMsg(msg);
		}

		// accept saves the report
		theDataSink.accept(results);
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
