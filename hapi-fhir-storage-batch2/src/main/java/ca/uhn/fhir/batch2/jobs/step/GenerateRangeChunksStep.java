package ca.uhn.fhir.batch2.jobs.step;

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

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.PartitionedUrlChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrlListJobParameters;
import ca.uhn.fhir.util.Logs;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.Date;

import static ca.uhn.fhir.batch2.util.Batch2Constants.BATCH_START_DATE;

public class GenerateRangeChunksStep<PT extends PartitionedUrlListJobParameters> implements IFirstJobStepWorker<PT, PartitionedUrlChunkRangeJson> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<PT, VoidModel> theStepExecutionDetails, @Nonnull IJobDataSink<PartitionedUrlChunkRangeJson> theDataSink) throws JobExecutionFailedException {
		PT params = theStepExecutionDetails.getParameters();

		Date start = BATCH_START_DATE;
		Date end = new Date();

		if (params.getPartitionedUrls().isEmpty()) {
			ourLog.info("Searching for All Resources from {} to {}", start, end);
			PartitionedUrlChunkRangeJson nextRange = new PartitionedUrlChunkRangeJson();
			nextRange.setStart(start);
			nextRange.setEnd(end);
			theDataSink.accept(nextRange);
		} else {
			for (PartitionedUrl nextPartitionedUrl : params.getPartitionedUrls()) {
				ourLog.info("Searching for [{}]] from {} to {}", nextPartitionedUrl, start, end);
				PartitionedUrlChunkRangeJson nextRange = new PartitionedUrlChunkRangeJson();
				nextRange.setPartitionedUrl(nextPartitionedUrl);
				nextRange.setStart(start);
				nextRange.setEnd(end);
				theDataSink.accept(nextRange);
			}
		}

		return RunOutcome.SUCCESS;
	}

}
