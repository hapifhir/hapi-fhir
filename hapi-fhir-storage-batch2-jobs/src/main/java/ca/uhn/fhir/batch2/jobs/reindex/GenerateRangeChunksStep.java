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

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.jpa.api.svc.IResourceReindexSvc;
import org.hl7.fhir.r4.model.InstantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.Date;

public class GenerateRangeChunksStep implements IFirstJobStepWorker<ReindexJobParameters, ReindexChunkRange> {
	private static final Logger ourLog = LoggerFactory.getLogger(GenerateRangeChunksStep.class);

	@Autowired
	private IResourceReindexSvc myResourceReindexSvc;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<ReindexJobParameters, VoidModel> theStepExecutionDetails, @Nonnull IJobDataSink<ReindexChunkRange> theDataSink) throws JobExecutionFailedException {
		ReindexJobParameters params = theStepExecutionDetails.getParameters();

		Date start = new InstantType("2000-01-01T00:00:00Z").getValue();
		Date end = new Date();

		if (params.getUrl().isEmpty()) {
			ourLog.info("Initiating reindex of All Resources from {} to {}", start, end);
			ReindexChunkRange nextRange = new ReindexChunkRange();
			nextRange.setStart(start);
			nextRange.setEnd(end);
			theDataSink.accept(nextRange);
		} else {
			for (String nextUrl : params.getUrl()) {
				ourLog.info("Initiating reindex of [{}]] from {} to {}", nextUrl, start, end);
				ReindexChunkRange nextRange = new ReindexChunkRange();
				nextRange.setUrl(nextUrl);
				nextRange.setStart(start);
				nextRange.setEnd(end);
				theDataSink.accept(nextRange);
			}
		}

		return RunOutcome.SUCCESS;
	}

}
