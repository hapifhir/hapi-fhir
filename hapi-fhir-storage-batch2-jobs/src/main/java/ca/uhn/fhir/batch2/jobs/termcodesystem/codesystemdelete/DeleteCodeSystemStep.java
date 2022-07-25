package ca.uhn.fhir.batch2.jobs.termcodesystem.codesystemdelete;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemDeleteJobSvc;
import ca.uhn.fhir.jpa.term.models.CodeSystemVersionPIDResult;
import ca.uhn.fhir.jpa.term.models.TermCodeSystemDeleteJobParameters;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

public class DeleteCodeSystemStep implements IReductionStepWorker<TermCodeSystemDeleteJobParameters, CodeSystemVersionPIDResult, VoidModel> {

	@Autowired
	private ITermCodeSystemDeleteJobSvc myITermCodeSystemSvc;

	@NotNull
	@Override
	public RunOutcome run(
		@NotNull StepExecutionDetails<TermCodeSystemDeleteJobParameters, CodeSystemVersionPIDResult> theStepExecutionDetails,
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
	public ChunkOutcome consume(ChunkExecutionDetails<TermCodeSystemDeleteJobParameters, CodeSystemVersionPIDResult> theChunkDetails) {
		/*
		 * A single code system can have multiple versions.
		 * We don't want to call delete on all these systems
		 * (because if 2 threads do so at the same time, we get exceptions)
		 * so we'll use the reducer step to ensure we only call delete
		 * a single time.
		 *
		 * Thus, we don't need to "consume" anything
		 */
		return ChunkOutcome.SUCCESS();
	}
}
