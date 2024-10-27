/*-
 * #%L
 * hapi-fhir-storage-mdm
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
package ca.uhn.fhir.mdm.batch2;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.chunk.ChunkRangeJson;
import ca.uhn.fhir.batch2.util.Batch2Utils;
import ca.uhn.fhir.mdm.batch2.clear.MdmClearJobParameters;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class MdmGenerateRangeChunksStep implements IFirstJobStepWorker<MdmClearJobParameters, ChunkRangeJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmGenerateRangeChunksStep.class);

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<MdmClearJobParameters, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<ChunkRangeJson> theDataSink)
			throws JobExecutionFailedException {
		MdmClearJobParameters params = theStepExecutionDetails.getParameters();

		Date start = Batch2Utils.BATCH_START_DATE;
		Date end = new Date();

		for (String nextResourceType : params.getResourceNames()) {
			ourLog.info("Initiating mdm clear of [{}]] Golden Resources from {} to {}", nextResourceType, start, end);
			assert nextResourceType != null;
			ChunkRangeJson nextRange = new ChunkRangeJson(start, end)
					.setResourceType(nextResourceType)
					.setPartitionId(params.getRequestPartitionId());
			theDataSink.accept(nextRange);
		}

		return RunOutcome.SUCCESS;
	}
}
