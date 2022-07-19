package ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemversiondelete;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.ILastJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemSvc;
import ca.uhn.fhir.jpa.term.models.CodeSystemVersionPIDResult;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteVersionJobParameters;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

public class DeleteCodeSystemVersionFinalStep implements ILastJobStepWorker<TermCodeSystemDeleteVersionJobParameters, CodeSystemVersionPIDResult> {

	@Autowired
	private ITermCodeSystemSvc myTermCodeSystemSvc;

	@NotNull
	@Override
	public RunOutcome run(
		@NotNull StepExecutionDetails<TermCodeSystemDeleteVersionJobParameters, CodeSystemVersionPIDResult> theStepExecutionDetails,
		@NotNull IJobDataSink<VoidModel> theDataSink
	) throws JobExecutionFailedException {
		long versionPid = theStepExecutionDetails.getParameters().getCodeSystemVersionPid();

		myTermCodeSystemSvc.deleteCodeSystemVersion(versionPid);

		return RunOutcome.SUCCESS;
	}
}
