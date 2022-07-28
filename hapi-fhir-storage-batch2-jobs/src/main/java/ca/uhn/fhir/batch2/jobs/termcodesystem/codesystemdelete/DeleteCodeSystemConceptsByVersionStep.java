package ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemdelete;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemDeleteJobSvc;
import ca.uhn.fhir.jpa.term.models.CodeSystemVersionPIDResult;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteJobParameters;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

public class DeleteCodeSystemConceptsByVersionStep implements IJobStepWorker<TermCodeSystemDeleteJobParameters, CodeSystemVersionPIDResult, CodeSystemVersionPIDResult> {

	private final ITermCodeSystemDeleteJobSvc myITermCodeSystemSvc;

	public DeleteCodeSystemConceptsByVersionStep (ITermCodeSystemDeleteJobSvc theCodeSystemDeleteJobSvc) {
		myITermCodeSystemSvc = theCodeSystemDeleteJobSvc;
	}

	@NotNull
	@Override
	public RunOutcome run(
		@NotNull StepExecutionDetails<TermCodeSystemDeleteJobParameters, CodeSystemVersionPIDResult> theStepExecutionDetails,
		@NotNull IJobDataSink<CodeSystemVersionPIDResult> theDataSink
	) throws JobExecutionFailedException {
		CodeSystemVersionPIDResult versionPidResult = theStepExecutionDetails.getData();

		myITermCodeSystemSvc.deleteCodeSystemConceptsByCodeSystemVersionPid(versionPidResult.getCodeSystemVersionPID());

		theDataSink.accept(versionPidResult);

		return RunOutcome.SUCCESS;
	}
}
