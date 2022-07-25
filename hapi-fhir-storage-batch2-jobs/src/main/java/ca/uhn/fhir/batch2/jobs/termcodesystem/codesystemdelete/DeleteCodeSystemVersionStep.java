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

public class DeleteCodeSystemVersionStep implements IJobStepWorker<TermCodeSystemDeleteJobParameters, CodeSystemVersionPIDResult, CodeSystemVersionPIDResult> {

	@Autowired
	private ITermCodeSystemDeleteJobSvc myITermCodeSystemSvc;

	@NotNull
	@Override
	public RunOutcome run(
		@NotNull StepExecutionDetails<TermCodeSystemDeleteJobParameters, CodeSystemVersionPIDResult> theStepExecutionDetails,
		@NotNull IJobDataSink<CodeSystemVersionPIDResult> theDataSink
	) throws JobExecutionFailedException {
		CodeSystemVersionPIDResult versionPidResult = theStepExecutionDetails.getData();

		long versionId = versionPidResult.getCodeSystemVersionPID();

		myITermCodeSystemSvc.deleteCodeSystemVersion(versionId);

		theDataSink.accept(versionPidResult);
		return RunOutcome.SUCCESS;
	}
}
