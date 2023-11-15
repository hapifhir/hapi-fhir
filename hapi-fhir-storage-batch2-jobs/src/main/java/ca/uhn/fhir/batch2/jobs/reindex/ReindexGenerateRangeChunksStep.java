/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.reindex;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.PartitionedUrlChunkRangeJson;
import ca.uhn.fhir.batch2.jobs.step.GenerateRangeChunksStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class ReindexGenerateRangeChunksStep extends GenerateRangeChunksStep<ReindexJobParameters> {
	private static final Logger ourLog = LoggerFactory.getLogger(ReindexGenerateRangeChunksStep.class);

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<ReindexJobParameters, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<PartitionedUrlChunkRangeJson> theDataSink)
			throws JobExecutionFailedException {

		ReindexJobParameters parameters = theStepExecutionDetails.getParameters();
		ourLog.info(
				"Beginning reindex job - OptimizeStorage[{}] - ReindexSearchParameters[{}]",
				parameters.getOptimizeStorage(),
				parameters.getReindexSearchParameters());

		return super.run(theStepExecutionDetails, theDataSink);
	}
}
