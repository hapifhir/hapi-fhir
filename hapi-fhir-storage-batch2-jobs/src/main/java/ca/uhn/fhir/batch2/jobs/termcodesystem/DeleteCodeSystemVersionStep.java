package ca.uhn.fhir.batch2.jobs.termcodesystem;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemSvc;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteJobParameters;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemVersionPidResult;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

public class DeleteCodeSystemVersionStep implements IJobStepWorker<TermCodeSystemDeleteJobParameters, TermCodeSystemVersionPidResult, TermCodeSystemVersionPidResult> {

	@Autowired
	private ITermCodeSystemSvc myITermCodeSystemSvc;

	@NotNull
	@Override
	public RunOutcome run(
		@NotNull StepExecutionDetails<TermCodeSystemDeleteJobParameters, TermCodeSystemVersionPidResult> theStepExecutionDetails,
		@NotNull IJobDataSink<TermCodeSystemVersionPidResult> theDataSink
	) throws JobExecutionFailedException {
		TermCodeSystemVersionPidResult versionPidResult = theStepExecutionDetails.getData();

		myITermCodeSystemSvc.deleteCodeSystemConceptsByVersion(versionPidResult.getTermVersionPID());

		theDataSink.accept(versionPidResult);
		return RunOutcome.SUCCESS;
	}
}
