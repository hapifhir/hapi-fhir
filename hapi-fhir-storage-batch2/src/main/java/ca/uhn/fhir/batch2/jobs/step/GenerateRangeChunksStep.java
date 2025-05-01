/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.step;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrlJobParameters;
import ca.uhn.fhir.util.Logs;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.List;

import static ca.uhn.fhir.batch2.util.Batch2Utils.BATCH_START_DATE;

public class GenerateRangeChunksStep<PT extends PartitionedUrlJobParameters>
		implements IFirstJobStepWorker<PT, ChunkRangeJson> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<PT, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<ChunkRangeJson> theDataSink)
			throws JobExecutionFailedException {
		PT params = theStepExecutionDetails.getParameters();

		Date start = BATCH_START_DATE;
		Date end = new Date();

		List<PartitionedUrl> partitionedUrls = params.getPartitionedUrls();

		if (!partitionedUrls.isEmpty()) {
			partitionedUrls.forEach(partitionedUrl -> {
				ChunkRangeJson chunkRangeJson = new ChunkRangeJson(start, end)
						.setUrl(partitionedUrl.getUrl())
						.setPartitionId(partitionedUrl.getRequestPartitionId());
				sendChunk(chunkRangeJson, theDataSink);
			});
			return RunOutcome.SUCCESS;
		}

		ChunkRangeJson chunkRangeJson = new ChunkRangeJson(start, end);
		sendChunk(chunkRangeJson, theDataSink);
		return RunOutcome.SUCCESS;
	}

	private void sendChunk(ChunkRangeJson theData, IJobDataSink<ChunkRangeJson> theDataSink) {
		String url = theData.getUrl();
		ourLog.trace(
				"Creating chunks for [{}] from {} to {} for partition {}",
				!StringUtils.isEmpty(url) ? url : "everything",
				theData.getStart(),
				theData.getEnd(),
				theData.getPartitionId());
		theDataSink.accept(theData);
	}
}
