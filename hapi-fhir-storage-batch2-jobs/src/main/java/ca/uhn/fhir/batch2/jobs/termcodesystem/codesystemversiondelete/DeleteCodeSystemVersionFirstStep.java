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
package ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemversiondelete;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemDeleteJobSvc;
import ca.uhn.fhir.jpa.term.models.CodeSystemVersionPIDResult;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteVersionJobParameters;
import jakarta.annotation.Nonnull;

public class DeleteCodeSystemVersionFirstStep
		implements IFirstJobStepWorker<TermCodeSystemDeleteVersionJobParameters, CodeSystemVersionPIDResult> {

	private final ITermCodeSystemDeleteJobSvc myTermCodeSystemSvc;

	public DeleteCodeSystemVersionFirstStep(ITermCodeSystemDeleteJobSvc theCodeSystemDeleteJobSvc) {
		myTermCodeSystemSvc = theCodeSystemDeleteJobSvc;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<TermCodeSystemDeleteVersionJobParameters, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<CodeSystemVersionPIDResult> theDataSink)
			throws JobExecutionFailedException {
		long versionId = theStepExecutionDetails.getParameters().getCodeSystemVersionPid();

		myTermCodeSystemSvc.deleteCodeSystemConceptsByCodeSystemVersionPid(versionId);

		CodeSystemVersionPIDResult result = new CodeSystemVersionPIDResult();
		result.setCodeSystemVersionPID(versionId);
		theDataSink.accept(result);

		return RunOutcome.SUCCESS;
	}
}
