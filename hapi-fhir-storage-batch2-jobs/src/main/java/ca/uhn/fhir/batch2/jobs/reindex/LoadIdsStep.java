package ca.uhn.fhir.batch2.jobs.reindex;

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

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IResourceReindexSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LoadIdsStep implements IJobStepWorker<ReindexJobParameters, ReindexChunkRange, ReindexChunkIds> {
	private static final Logger ourLog = LoggerFactory.getLogger(LoadIdsStep.class);

	@Autowired
	private IResourceReindexSvc myResourceReindexSvc;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<ReindexJobParameters, ReindexChunkRange> theStepExecutionDetails, @Nonnull IJobDataSink<ReindexChunkIds> theDataSink) throws JobExecutionFailedException {

		ReindexChunkRange data = theStepExecutionDetails.getData();

		Date start = data.getStart();
		Date end = data.getEnd();

		ourLog.info("Beginning scan for reindex IDs in range {} to {}", start, end);

		Date nextStart = start;
		RequestPartitionId requestPartitionId = theStepExecutionDetails.getParameters().getRequestPartitionId();
		Set<ReindexChunkIds.Id> idBuffer = new LinkedHashSet<>();
		long previousLastTime = 0L;
		int totalIdsFound = 0;
		int chunkCount = 0;
		while (true) {
			String url = theStepExecutionDetails.getData().getUrl();

			ourLog.info("Fetching resource ID chunk for URL {} - Range {} - {}", url, nextStart, end);
			IResourceReindexSvc.IdChunk nextChunk = myResourceReindexSvc.fetchResourceIdsPage(nextStart, end, requestPartitionId, url);

			if (nextChunk.getIds().isEmpty()) {
				ourLog.info("No data returned");
				break;
			}

			ourLog.info("Found {} IDs from {} to {}", nextChunk.getIds().size(), nextStart, nextChunk.getLastDate());

			for (int i = 0; i < nextChunk.getIds().size(); i++) {
				ReindexChunkIds.Id nextId = new ReindexChunkIds.Id();
				nextId.setResourceType(nextChunk.getResourceTypes().get(i));
				nextId.setId(nextChunk.getIds().get(i).getId().toString());
				idBuffer.add(nextId);
			}

			// If we get the same last time twice in a row, we've clearly reached the end
			if (nextChunk.getLastDate().getTime() == previousLastTime) {
				ourLog.info("Matching final timestamp of {}, loading is completed", new Date(previousLastTime));
				break;
			}

			previousLastTime = nextChunk.getLastDate().getTime();
			nextStart = nextChunk.getLastDate();

			while (idBuffer.size() >= 1000) {

				List<ReindexChunkIds.Id> submissionIds = new ArrayList<>();
				for (Iterator<ReindexChunkIds.Id> iter = idBuffer.iterator(); iter.hasNext(); ) {
					submissionIds.add(iter.next());
					iter.remove();
					if (submissionIds.size() >= 1000) {
						break;
					}
				}

				totalIdsFound += submissionIds.size();
				chunkCount++;
				submitWorkChunk(submissionIds, theDataSink);
			}
		}

		totalIdsFound += idBuffer.size();
		chunkCount++;
		submitWorkChunk(idBuffer, theDataSink);

		ourLog.info("Submitted {} chunks with {} resource IDs", chunkCount, totalIdsFound);
		return RunOutcome.SUCCESS;
	}

	private void submitWorkChunk(Collection<ReindexChunkIds.Id> theIdBuffer, IJobDataSink<ReindexChunkIds> theDataSink) {
		if (theIdBuffer.isEmpty()) {
			return;
		}
		ourLog.info("Submitting work chunk with {} IDs", theIdBuffer.size());

		ReindexChunkIds data = new ReindexChunkIds();
		data.getIds().addAll(theIdBuffer);
		theDataSink.accept(data);
	}

}
