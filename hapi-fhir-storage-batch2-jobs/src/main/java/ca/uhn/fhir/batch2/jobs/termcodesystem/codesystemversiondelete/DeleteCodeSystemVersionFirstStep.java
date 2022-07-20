package ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemversiondelete;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemSvc;
import ca.uhn.fhir.jpa.term.models.CodeSystemVersionPIDResult;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteVersionJobParameters;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

public class DeleteCodeSystemVersionFirstStep implements IFirstJobStepWorker<TermCodeSystemDeleteVersionJobParameters, CodeSystemVersionPIDResult> {

	@Autowired
	private ITermCodeSystemSvc myTermCodeSystemSvc;

	@NotNull
	@Override
	public RunOutcome run(
		@NotNull StepExecutionDetails<TermCodeSystemDeleteVersionJobParameters, VoidModel> theStepExecutionDetails,
		@NotNull IJobDataSink<CodeSystemVersionPIDResult> theDataSink
	) throws JobExecutionFailedException {
		long versionId = theStepExecutionDetails.getParameters().getCodeSystemVersionPid();

		myTermCodeSystemSvc.deleteCodeSystemConceptsByCodeSystemVersionPid(versionId);

		CodeSystemVersionPIDResult result = new CodeSystemVersionPIDResult();
		result.setCodeSystemVersionPID(versionId);
		theDataSink.accept(result);

		return RunOutcome.SUCCESS;
	}
}
