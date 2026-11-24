/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * First step of the staging version cleanup job: emits one work chunk for each staging version the job
 * was started to drop, so that each one is dropped, and retried, on its own.
 */
// Created by Claude Fable 5.2
public class CleanupStep1ListStagingVersions
		implements IJobStepWorker<PreExpandValueSetCleanupParameters, VoidModel, DropStagingVersionWorkChunkJson> {

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<PreExpandValueSetCleanupParameters, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<DropStagingVersionWorkChunkJson> theDataSink)
			throws JobExecutionFailedException {
		List<String> stagingVersionIds = theStepExecutionDetails.getParameters().getStagingVersionIds();
		for (String stagingVersionId : stagingVersionIds) {
			DropStagingVersionWorkChunkJson workChunk = new DropStagingVersionWorkChunkJson();
			workChunk.setStagingVersionId(stagingVersionId);
			theDataSink.accept(workChunk);
		}
		return new RunOutcome(stagingVersionIds.size());
	}
}
