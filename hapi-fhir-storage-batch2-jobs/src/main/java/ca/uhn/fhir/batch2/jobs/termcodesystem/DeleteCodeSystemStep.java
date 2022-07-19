package ca.uhn.fhir.batch2.jobs.termcodesystem;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.ILastJobStepWorker;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemSvc;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteJobParameters;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemVersionPidResult;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

public class DeleteCodeSystemStep implements IReductionStepWorker<TermCodeSystemDeleteJobParameters, TermCodeSystemVersionPidResult, VoidModel> {

	@Autowired
	private ITermCodeSystemSvc myITermCodeSystemSvc;

	@NotNull
	@Override
	public RunOutcome run(
		@NotNull StepExecutionDetails<TermCodeSystemDeleteJobParameters, TermCodeSystemVersionPidResult> theStepExecutionDetails,
		@NotNull IJobDataSink<VoidModel> theDataSink
	) throws JobExecutionFailedException {
		// final step
		long codeId = theStepExecutionDetails.getParameters().getTermPid();
		myITermCodeSystemSvc.deleteCodeSystem(codeId);

		theDataSink.accept(new VoidModel()); // nothing required here

		return RunOutcome.SUCCESS;
	}

	@NotNull
	@Override
	public ChunkOutcome consume(ChunkExecutionDetails<TermCodeSystemDeleteJobParameters, TermCodeSystemVersionPidResult> theChunkDetails) {
		/*
		 * A single code system can have multiple versions.
		 * We don't want to call delete on all these systems
		 * (because if 2 threads do so at the same time, we get exceptions)
		 * so we'll use the reducer step to ensure we only call delete
		 * a single time.
		 */
		return ChunkOutcome.SUCCESS();
	}
}
