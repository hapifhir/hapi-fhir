/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.batch2.jobs.parameters.JobParameters;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.util.Logs;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.List;

import static ca.uhn.fhir.batch2.util.Batch2Utils.BATCH_START_DATE;

public class GenerateRangeChunksStep<PT extends JobParameters> implements IFirstJobStepWorker<PT, ChunkRangeJson> {
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

		// there are partitions configured in either of the following lists, which are both optional
		// the following code considers all use-cases
		// the logic can be simplified once PartitionedUrl.myRequestPartitionId is deprecated
		// @see IJobPartitionProvider

		List<RequestPartitionId> partitionIds = params.getRequestPartitionIds();
		List<PartitionedUrl> partitionedUrls = params.getPartitionedUrls();

		if (partitionIds.isEmpty()) {
			if (partitionedUrls.isEmpty()) {
				ChunkRangeJson chunkRangeJson = new ChunkRangeJson(start, end);
				sendChunk(chunkRangeJson, theDataSink);
				return RunOutcome.SUCCESS;
			}
			partitionedUrls.forEach(partitionedUrl -> {
				String url = partitionedUrl.getUrl();
				RequestPartitionId partitionId = partitionedUrl.getRequestPartitionId();
				ChunkRangeJson chunkRangeJson =
						new ChunkRangeJson(start, end).setUrl(url).setPartitionId(partitionId);
				sendChunk(chunkRangeJson, theDataSink);
			});
			return RunOutcome.SUCCESS;
		}

		partitionIds.forEach(partitionId -> {
			if (partitionedUrls.isEmpty()) {
				ChunkRangeJson chunkRangeJson = new ChunkRangeJson(start, end).setPartitionId(partitionId);
				sendChunk(chunkRangeJson, theDataSink);
				return;
			}
			partitionedUrls.forEach(partitionedUrl -> {
				String url = partitionedUrl.getUrl();
				RequestPartitionId urlPartitionId = partitionedUrl.getRequestPartitionId();
				RequestPartitionId narrowPartitionId = determineNarrowPartitionId(partitionId, urlPartitionId);
				ChunkRangeJson chunkRangeJson =
						new ChunkRangeJson(start, end).setUrl(url).setPartitionId(narrowPartitionId);
				sendChunk(chunkRangeJson, theDataSink);
			});
		});

		return RunOutcome.SUCCESS;
	}

	private RequestPartitionId determineNarrowPartitionId(
			@Nonnull RequestPartitionId theRequestPartitionId,
			@Nullable RequestPartitionId theOtherRequestPartitionId) {
		if (theOtherRequestPartitionId == null) {
			return theRequestPartitionId;
		}
		if (theRequestPartitionId.isAllPartitions() && !theOtherRequestPartitionId.isAllPartitions()) {
			return theOtherRequestPartitionId;
		}
		if (theRequestPartitionId.isDefaultPartition()
				&& !theOtherRequestPartitionId.isDefaultPartition()
				&& !theOtherRequestPartitionId.isAllPartitions()) {
			return theOtherRequestPartitionId;
		}
		return theRequestPartitionId;
	}

	private void sendChunk(ChunkRangeJson theData, IJobDataSink<ChunkRangeJson> theDataSink) {
		String url = theData.getUrl();
		ourLog.info(
				"Creating chunks for [{}] from {} to {} for partition {}",
				!StringUtils.isEmpty(url) ? url : "everything",
				theData.getStart(),
				theData.getEnd(),
				theData.getPartitionId());
		theDataSink.accept(theData);
	}
}
