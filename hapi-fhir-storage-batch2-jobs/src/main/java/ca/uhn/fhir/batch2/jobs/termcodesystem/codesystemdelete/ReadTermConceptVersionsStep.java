package ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemdelete;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemSvc;
import ca.uhn.fhir.jpa.term.models.CodeSystemVersionPIDResult;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteJobParameters;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

public class ReadTermConceptVersionsStep implements IFirstJobStepWorker<TermCodeSystemDeleteJobParameters, CodeSystemVersionPIDResult> {

	@Autowired
	private ITermCodeSystemSvc myITermCodeSystemSvc;

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
