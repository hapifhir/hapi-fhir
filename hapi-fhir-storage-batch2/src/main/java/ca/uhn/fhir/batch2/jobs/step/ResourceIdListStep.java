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
import ca.uhn.fhir.jpa.api.pid.IResourcePidList;
import ca.uhn.fhir.util.Logs;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

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
		int pageSize = DEFAULT_PAGE_SIZE;
		if (batchSize != null) {
			pageSize = batchSize.intValue();
		}

		ourLog.info("Beginning scan for reindex IDs in range {} to {}", start, end);

		RequestPartitionId requestPartitionId =
				theStepExecutionDetails.getParameters().getRequestPartitionId();
		int totalIdsFound = 0;
		int chunkCount = 0;

		int maxBatchId = MAX_BATCH_OF_IDS;
		if (batchSize != null) {
			// we won't go over MAX_BATCH_OF_IDS
			maxBatchId = Math.min(batchSize.intValue(), maxBatchId);
		}

		final IResourcePidList nextChunk = myIdChunkProducer.fetchResourceIdsPage(
				start, end, pageSize, requestPartitionId, theStepExecutionDetails.getData());

		if (nextChunk.isEmpty()) {
			ourLog.info("No data returned");
		} else {
			ourLog.debug("Found {} IDs from {} to {}", nextChunk.size(), start, nextChunk.getLastDate());

			final Set<TypedPidJson> idBuffer = nextChunk.getTypedResourcePids().stream()
					.map(TypedPidJson::new)
					.collect(Collectors.toCollection(LinkedHashSet::new));

			final UnmodifiableIterator<List<TypedPidJson>> partition =
					Iterators.partition(idBuffer.iterator(), maxBatchId);

			while (partition.hasNext()) {
				final List<TypedPidJson> submissionIds = partition.next();

				totalIdsFound += submissionIds.size();
				chunkCount++;
				submitWorkChunk(submissionIds, nextChunk.getRequestPartitionId(), theDataSink);
			}

			ourLog.info("Submitted {} chunks with {} resource IDs", chunkCount, totalIdsFound);
		}
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
