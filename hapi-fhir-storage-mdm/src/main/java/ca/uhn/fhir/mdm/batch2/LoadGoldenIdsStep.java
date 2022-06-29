package ca.uhn.fhir.mdm.batch2;

/*-
 * #%L
 * hapi-fhir-storage-mdm
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
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunkJson;
import ca.uhn.fhir.batch2.jobs.step.IIdChunkProducer;
import ca.uhn.fhir.batch2.jobs.step.ResourceIdListStep;
import ca.uhn.fhir.jpa.api.svc.IGoldenResourceSearchSvc;
import ca.uhn.fhir.mdm.batch2.clear.MdmClearJobParameters;

import javax.annotation.Nonnull;

public class LoadGoldenIdsStep implements IJobStepWorker<MdmClearJobParameters, MdmChunkRangeJson, ResourceIdListWorkChunkJson> {
	private final ResourceIdListStep<MdmClearJobParameters, MdmChunkRangeJson> myResourceIdListStep;

	public LoadGoldenIdsStep(IGoldenResourceSearchSvc theGoldenResourceSearchSvc) {
		IIdChunkProducer<MdmChunkRangeJson> idChunkProducer = new MdmIdChunkProducer(theGoldenResourceSearchSvc);

		myResourceIdListStep = new ResourceIdListStep<>(idChunkProducer);
	}

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<MdmClearJobParameters, MdmChunkRangeJson> theStepExecutionDetails, @Nonnull IJobDataSink<ResourceIdListWorkChunkJson> theDataSink) throws JobExecutionFailedException {
		return myResourceIdListStep.run(theStepExecutionDetails, theDataSink);
	}
}
