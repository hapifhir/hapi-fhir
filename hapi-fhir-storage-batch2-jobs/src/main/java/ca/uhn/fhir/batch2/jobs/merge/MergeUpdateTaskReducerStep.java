package ca.uhn.fhir.batch2.jobs.merge;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferencePatchOutcomeJson;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferenceResultsJson;
import ca.uhn.fhir.batch2.jobs.replacereferences.ReplaceReferenceUpdateTaskReducerStep;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import jakarta.annotation.Nonnull;

public class MergeUpdateTaskReducerStep extends ReplaceReferenceUpdateTaskReducerStep<MergeJobParameters> {
	public MergeUpdateTaskReducerStep(DaoRegistry theDaoRegistry) {
		super(theDaoRegistry);
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<MergeJobParameters, ReplaceReferencePatchOutcomeJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<ReplaceReferenceResultsJson> theDataSink)
			throws JobExecutionFailedException {
		// FIXME ED add in extra merge steps here e.g. updating source and target resources
		return super.run(theStepExecutionDetails, theDataSink);
	}
}
