package ca.uhn.fhir.batch2.jobs.mdm;

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

import ca.uhn.fhir.batch2.api.*;
import ca.uhn.fhir.batch2.jobs.chunk.ResourceIdListWorkChunk;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexChunkRange;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexIdChunkProducer;
import ca.uhn.fhir.batch2.jobs.step.IIdChunkProducer;
import ca.uhn.fhir.batch2.jobs.step.ResourceIdListStep;
import ca.uhn.fhir.jpa.api.svc.IGoldenResourceSearchSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

public class LoadGoldenIdsStep implements IJobStepWorker<MdmJobParameters, MdmChunkRange, ResourceIdListWorkChunk> {
	@Autowired
	private IGoldenResourceSearchSvc myGoldenResourceSearchSvc;

	private final ResourceIdListStep<MdmJobParameters, MdmChunkRange> myResourceIdListStep;

	public LoadGoldenIdsStep() {
		IIdChunkProducer<MdmChunkRange> idChunkProducer = new MdmIdChunkProducer(myGoldenResourceSearchSvc);

		myResourceIdListStep = new ResourceIdListStep<>(idChunkProducer);
	}

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<MdmJobParameters, MdmChunkRange> theStepExecutionDetails, @Nonnull IJobDataSink<ResourceIdListWorkChunk> theDataSink) throws JobExecutionFailedException {
		return myResourceIdListStep.run(theStepExecutionDetails, theDataSink);
	}
}
