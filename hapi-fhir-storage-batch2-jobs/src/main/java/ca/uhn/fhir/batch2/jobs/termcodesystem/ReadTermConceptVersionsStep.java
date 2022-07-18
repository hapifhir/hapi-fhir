package ca.uhn.fhir.batch2.jobs.termcodesystem;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemSvc;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteJobParameters;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemVersionPidResult;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

public class ReadTermConceptVersionsStep implements IFirstJobStepWorker<TermCodeSystemDeleteJobParameters, TermCodeSystemVersionPidResult> {

	@Autowired
	private ITermCodeSystemSvc myITermCodeSystemSvc;

	@NotNull
	@Override
	public RunOutcome run(
		@NotNull StepExecutionDetails<TermCodeSystemDeleteJobParameters, VoidModel> theStepExecutionDetails,
		@NotNull IJobDataSink<TermCodeSystemVersionPidResult> theDataSink
	) throws JobExecutionFailedException {
		TermCodeSystemDeleteJobParameters parameters = theStepExecutionDetails.getParameters();

		long pid = parameters.getTermPid();

		Iterator<Long> versionPids = myITermCodeSystemSvc.getAllCodeSystemVersionForCodeSystemPid(pid);
		while (versionPids.hasNext()) {
			long next = versionPids.next().longValue();
			TermCodeSystemVersionPidResult versionPidResult = new TermCodeSystemVersionPidResult();
			versionPidResult.setTermVersionPID(next);
			theDataSink.accept(versionPidResult);
		}

		return RunOutcome.SUCCESS;
	}
}
