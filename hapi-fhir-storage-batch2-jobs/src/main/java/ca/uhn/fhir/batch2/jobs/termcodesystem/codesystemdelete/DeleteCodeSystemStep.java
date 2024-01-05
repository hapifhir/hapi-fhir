/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemdelete;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemDeleteJobSvc;
import ca.uhn.fhir.jpa.term.models.CodeSystemVersionPIDResult;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteJobParameters;
import jakarta.annotation.Nonnull;

public class DeleteCodeSystemStep
		implements IReductionStepWorker<TermCodeSystemDeleteJobParameters, CodeSystemVersionPIDResult, VoidModel> {

	private final ITermCodeSystemDeleteJobSvc myITermCodeSystemSvc;

	public DeleteCodeSystemStep(ITermCodeSystemDeleteJobSvc theCodeSystemDeleteJobSvc) {
		myITermCodeSystemSvc = theCodeSystemDeleteJobSvc;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<TermCodeSystemDeleteJobParameters, CodeSystemVersionPIDResult>
							theStepExecutionDetails,
			@Nonnull IJobDataSink<VoidModel> theDataSink)
			throws JobExecutionFailedException {
		// final step
		long codeId = theStepExecutionDetails.getParameters().getTermPid();
		myITermCodeSystemSvc.deleteCodeSystem(codeId);

		theDataSink.accept(new VoidModel()); // nothing required here

		return RunOutcome.SUCCESS;
	}

	@Nonnull
	@Override
	public ChunkOutcome consume(
			ChunkExecutionDetails<TermCodeSystemDeleteJobParameters, CodeSystemVersionPIDResult> theChunkDetails) {
		/*
		 * A single code system can have multiple versions.
		 * We don't want to call delete on all these systems
		 * (because if 2 threads do so at the same time, we get exceptions)
		 * so we'll use the reducer step to ensure we only call delete
		 * a single time.
		 *
		 * Thus, we don't need to "consume" anything
		 */
		return ChunkOutcome.SUCCESS();
	}
}
