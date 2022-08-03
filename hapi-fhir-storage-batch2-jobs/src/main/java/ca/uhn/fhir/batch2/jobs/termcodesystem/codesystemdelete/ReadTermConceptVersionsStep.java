package ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemdelete;

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
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemDeleteJobSvc;
import ca.uhn.fhir.jpa.term.models.CodeSystemVersionPIDResult;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteJobParameters;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class ReadTermConceptVersionsStep implements IFirstJobStepWorker<TermCodeSystemDeleteJobParameters, CodeSystemVersionPIDResult> {

	private final ITermCodeSystemDeleteJobSvc myITermCodeSystemSvc;

	public ReadTermConceptVersionsStep(ITermCodeSystemDeleteJobSvc theCodeSystemDeleteJobSvc) {
		myITermCodeSystemSvc = theCodeSystemDeleteJobSvc;
	}

	@NotNull
	@Override
	public RunOutcome run(
		@NotNull StepExecutionDetails<TermCodeSystemDeleteJobParameters, VoidModel> theStepExecutionDetails,
		@NotNull IJobDataSink<CodeSystemVersionPIDResult> theDataSink
	) throws JobExecutionFailedException {
		TermCodeSystemDeleteJobParameters parameters = theStepExecutionDetails.getParameters();

		long pid = parameters.getTermPid();

		Iterator<Long> versionPids = myITermCodeSystemSvc.getAllCodeSystemVersionForCodeSystemPid(pid);
		while (versionPids.hasNext()) {
			long next = versionPids.next().longValue();
			CodeSystemVersionPIDResult versionPidResult = new CodeSystemVersionPIDResult();
			versionPidResult.setCodeSystemVersionPID(next);
			theDataSink.accept(versionPidResult);
		}

		return RunOutcome.SUCCESS;
	}
}
