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
import ca.uhn.fhir.batch2.jobs.parameters.JobParameters;
import ca.uhn.fhir.jpa.api.svc.IBatch2DaoSvc;
import jakarta.annotation.Nonnull;

public class LoadIdsStep<PT extends JobParameters>
		implements IJobStepWorker<PT, ChunkRangeJson, ResourceIdListWorkChunkJson> {
	private final ResourceIdListStep<PT> myResourceIdListStep;

	public LoadIdsStep(IBatch2DaoSvc theBatch2DaoSvc) {
		IIdChunkProducer<ChunkRangeJson> idChunkProducer = new ChunkProducer(theBatch2DaoSvc);
		myResourceIdListStep = new ResourceIdListStep<>(idChunkProducer);
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<PT, ChunkRangeJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<ResourceIdListWorkChunkJson> theDataSink)
			throws JobExecutionFailedException {
		return myResourceIdListStep.run(theStepExecutionDetails, theDataSink);
	}
}
