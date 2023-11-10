/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedJobParameters;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.pid.IResourcePidStream;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.util.StreamUtil;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class ResourceIdListStep<PT extends PartitionedJobParameters, IT extends ChunkRangeJson>
		implements IJobStepWorker<PT, IT, ResourceIdListWorkChunkJson> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	public static final int DEFAULT_PAGE_SIZE = 20000;

	protected static final int MAX_BATCH_OF_IDS = 500;

	private final IIdChunkProducer<IT> myIdChunkProducer;

	public ResourceIdListStep(IIdChunkProducer<IT> theIdChunkProducer) {
		myIdChunkProducer = theIdChunkProducer;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<PT, IT> theStepExecutionDetails,
			@Nonnull IJobDataSink<ResourceIdListWorkChunkJson> theDataSink)
			throws JobExecutionFailedException {
		IT data = theStepExecutionDetails.getData();

		Date start = data.getStart();
		Date end = data.getEnd();
		Integer batchSize = theStepExecutionDetails.getParameters().getBatchSize();

		ourLog.info("Beginning scan for reindex IDs in range {} to {}", start, end);

		RequestPartitionId requestPartitionId =
				theStepExecutionDetails.getParameters().getRequestPartitionId();

		int chunkSize = Math.min(defaultIfNull(batchSize, MAX_BATCH_OF_IDS), MAX_BATCH_OF_IDS);

		// wipmb force uniqueness upstream
		final IResourcePidStream nextChunk = myIdChunkProducer.fetchResourceIdStream(
				start, end, requestPartitionId, theStepExecutionDetails.getData());

		nextChunk.visitStream(typedResourcePidStream -> {
			Stream<TypedPidJson> jsonStream = typedResourcePidStream.map(TypedPidJson::new);

			// chunk by size maxBatchId
			Stream<List<TypedPidJson>> chunkStream = StreamUtil.partition(jsonStream, chunkSize);

			AtomicInteger totalIdsFound = new AtomicInteger();
			AtomicInteger chunkCount = new AtomicInteger();
			chunkStream.forEach(idBatch -> {
				totalIdsFound.addAndGet(idBatch.size());
				chunkCount.getAndIncrement();
				submitWorkChunk(idBatch, nextChunk.getRequestPartitionId(), theDataSink);
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
		ourLog.info("Submitting work chunk with {} IDs", theTypedPids.size());
		ResourceIdListWorkChunkJson data = new ResourceIdListWorkChunkJson(theTypedPids, theRequestPartitionId);
		ourLog.debug("IDs are: {}", data);
		theDataSink.accept(data);
	}
}
