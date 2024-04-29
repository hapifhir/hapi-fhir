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

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemDeleteJobSvc;
import ca.uhn.fhir.jpa.term.models.CodeSystemVersionPIDResult;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteJobParameters;
import jakarta.annotation.Nonnull;

public class DeleteCodeSystemConceptsByVersionStep
		implements IJobStepWorker<
				TermCodeSystemDeleteJobParameters, CodeSystemVersionPIDResult, CodeSystemVersionPIDResult> {

	private final ITermCodeSystemDeleteJobSvc myITermCodeSystemSvc;

	public DeleteCodeSystemConceptsByVersionStep(ITermCodeSystemDeleteJobSvc theCodeSystemDeleteJobSvc) {
		myITermCodeSystemSvc = theCodeSystemDeleteJobSvc;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<TermCodeSystemDeleteJobParameters, CodeSystemVersionPIDResult>
							theStepExecutionDetails,
			@Nonnull IJobDataSink<CodeSystemVersionPIDResult> theDataSink)
			throws JobExecutionFailedException {
		CodeSystemVersionPIDResult versionPidResult = theStepExecutionDetails.getData();

		myITermCodeSystemSvc.deleteCodeSystemConceptsByCodeSystemVersionPid(versionPidResult.getCodeSystemVersionPID());

		theDataSink.accept(versionPidResult);

		return RunOutcome.SUCCESS;
	}
}
