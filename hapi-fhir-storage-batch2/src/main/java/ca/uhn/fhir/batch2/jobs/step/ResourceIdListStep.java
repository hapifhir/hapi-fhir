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
import ca.uhn.fhir.jpa.api.pid.TypedResourcePid;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.util.Logs;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ResourceIdListStep<PT extends PartitionedJobParameters, IT extends ChunkRangeJson> implements IJobStepWorker<PT, IT, ResourceIdListWorkChunkJson> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	public static final int DEFAULT_PAGE_SIZE = 20000;

	private static final int MAX_BATCH_OF_IDS = 500;

	private final IIdChunkProducer<IT> myIdChunkProducer;

	public ResourceIdListStep(IIdChunkProducer<IT> theIdChunkProducer) {
		myIdChunkProducer = theIdChunkProducer;
	}

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<PT, IT> theStepExecutionDetails, @Nonnull IJobDataSink<ResourceIdListWorkChunkJson> theDataSink) throws JobExecutionFailedException {
		IT data = theStepExecutionDetails.getData();

		Date start = data.getStart();
		Date end = data.getEnd();
		Integer pageSize = theStepExecutionDetails.getParameters().getBatchSize();
		if (pageSize == null) {
			pageSize = DEFAULT_PAGE_SIZE;
		}

		ourLog.info("Beginning scan for reindex IDs in range {} to {}", start, end);

		Date nextStart = start;
		RequestPartitionId requestPartitionId = theStepExecutionDetails.getParameters().getRequestPartitionId();
		Set<TypedPidJson> idBuffer = new LinkedHashSet<>();
		long previousLastTime = 0L;
		int totalIdsFound = 0;
		int chunkCount = 0;
		while (true) {
			IResourcePidList nextChunk = myIdChunkProducer.fetchResourceIdsPage(nextStart, end, pageSize, requestPartitionId, theStepExecutionDetails.getData());

			if (nextChunk.isEmpty()) {
				ourLog.info("No data returned");
				break;
			}

			ourLog.info("Found {} IDs from {} to {}", nextChunk.size(), nextStart, nextChunk.getLastDate());
			if (nextChunk.size() < 10 && HapiSystemProperties.isTestModeEnabled()) {
				// TODO: I've added this in order to troubleshoot MultitenantBatchOperationR4Test
				// which is failing intermittently. If that stops, makes sense to remove this
				ourLog.info(" * PIDS: {}", nextChunk);
			}

			for (TypedResourcePid typedResourcePid : nextChunk.getTypedResourcePids()) {
				TypedPidJson nextId = new TypedPidJson(typedResourcePid);
				idBuffer.add(nextId);
			}

			// If we get the same last time twice in a row, we've clearly reached the end
			if (nextChunk.getLastDate().getTime() == previousLastTime) {
				ourLog.info("Matching final timestamp of {}, loading is completed", new Date(previousLastTime));
				break;
			}

			previousLastTime = nextChunk.getLastDate().getTime();
			nextStart = nextChunk.getLastDate();

			while (idBuffer.size() > MAX_BATCH_OF_IDS) {
				List<TypedPidJson> submissionIds = new ArrayList<>();
				for (Iterator<TypedPidJson> iter = idBuffer.iterator(); iter.hasNext(); ) {
					submissionIds.add(iter.next());
					iter.remove();
					if (submissionIds.size() == MAX_BATCH_OF_IDS) {
						break;
					}
				}

				totalIdsFound += submissionIds.size();
				chunkCount++;
				submitWorkChunk(submissionIds, nextChunk.getRequestPartitionId(), theDataSink);
			}
		}

		totalIdsFound += idBuffer.size();
		chunkCount++;
		submitWorkChunk(idBuffer, requestPartitionId, theDataSink);

		ourLog.info("Submitted {} chunks with {} resource IDs", chunkCount, totalIdsFound);
		return RunOutcome.SUCCESS;
	}

	private void submitWorkChunk(Collection<TypedPidJson> theTypedPids, RequestPartitionId theRequestPartitionId, IJobDataSink<ResourceIdListWorkChunkJson> theDataSink) {
		if (theTypedPids.isEmpty()) {
			return;
		}
		ourLog.info("Submitting work chunk with {} IDs", theTypedPids.size());
		ResourceIdListWorkChunkJson data = new ResourceIdListWorkChunkJson(theTypedPids, theRequestPartitionId);
		ourLog.debug("IDs are: {}", data);
		theDataSink.accept(data);
	}
}
