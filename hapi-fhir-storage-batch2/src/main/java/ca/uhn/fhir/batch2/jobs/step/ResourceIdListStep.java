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

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.chunk.TypedPidJson;
import ca.uhn.fhir.batch2.jobs.parameters.JobParameters;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.pid.IResourcePidStream;
import ca.uhn.fhir.util.Logs;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static ca.uhn.fhir.util.StreamUtil.partition;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class ResourceIdListStep<PT extends JobParameters>
		implements IJobStepWorker<PT, ChunkRangeJson, ResourceIdListWorkChunkJson> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	protected static final int MAX_BATCH_OF_IDS = 500;

	private final IIdChunkProducer<ChunkRangeJson> myIdChunkProducer;

	public ResourceIdListStep(IIdChunkProducer<ChunkRangeJson> theIdChunkProducer) {
		myIdChunkProducer = theIdChunkProducer;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<PT, ChunkRangeJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<ResourceIdListWorkChunkJson> theDataSink)
			throws JobExecutionFailedException {
		ChunkRangeJson data = theStepExecutionDetails.getData();

		Date start = data.getStart();
		Date end = data.getEnd();
		Integer batchSize = theStepExecutionDetails.getParameters().getBatchSize();

		ourLog.info("Beginning to submit chunks in range {} to {}", start, end);

		int chunkSize = Math.min(defaultIfNull(batchSize, MAX_BATCH_OF_IDS), MAX_BATCH_OF_IDS);
		final IResourcePidStream searchResult =
				myIdChunkProducer.fetchResourceIdStream(theStepExecutionDetails.getData());

		searchResult.visitStreamNoResult(typedResourcePidStream -> {
			AtomicInteger totalIdsFound = new AtomicInteger();
			AtomicInteger chunkCount = new AtomicInteger();

			Stream<TypedPidJson> jsonStream = typedResourcePidStream.map(TypedPidJson::new);

			// chunk by size maxBatchId and submit the batches
			partition(jsonStream, chunkSize).forEach(idBatch -> {
				totalIdsFound.addAndGet(idBatch.size());
				chunkCount.getAndIncrement();
				submitWorkChunk(idBatch, searchResult.getRequestPartitionId(), theDataSink);
			});
			ourLog.info("Submitted {} chunks with {} resource IDs", chunkCount, totalIdsFound);
		});

		return RunOutcome.SUCCESS;
	}

	private void submitWorkChunk(
			Collection<TypedPidJson> theTypedPids,
			RequestPartitionId theRequestPartitionId,
			IJobDataSink<ResourceIdListWorkChunkJson> theDataSink) {
		if (theTypedPids.isEmpty()) {
			return;
		}
		ourLog.info("Submitting work chunk in partition {} with {} IDs", theRequestPartitionId, theTypedPids.size());
		ResourceIdListWorkChunkJson data = new ResourceIdListWorkChunkJson(theTypedPids, theRequestPartitionId);
		ourLog.debug("IDs are: {}", data);
		theDataSink.accept(data);
	}
}
