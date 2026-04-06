package ca.uhn.fhir.batch2.jobs.installpackage;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.installpackage.model.InstallationOutcomeJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.util.JsonUtil;
import jakarta.annotation.Nonnull;

/**
 * This is a bit of a deviation from our normal workflow. We need to use a reduction step here in order to return
 * the package outcome as the final result of the job as a whole. However, there will only ever be one input chunk,
 * so we are only actually reducing the list of outcomes within that one chunk.
 */
public class FinalizeInstallationStep
		implements IReductionStepWorker<
				PackageInstallationJobParameters, InstallationOutcomeJson, PackageInstallOutcomeJson> {

	private PackageInstallOutcomeJson myOutcome;

	@Nonnull
	@Override
	public ChunkOutcome consume(
			ChunkExecutionDetails<PackageInstallationJobParameters, InstallationOutcomeJson> theChunkDetails) {
		// Since recursively installing dependencies isn't in scope yet, there's nothing to consolidate
		System.out.println(JsonUtil.serialize(theChunkDetails.getData(), true));

		myOutcome = theChunkDetails.getData().getOutcomes().stream().findFirst().orElse(null);

		return ChunkOutcome.SUCCESS();
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<PackageInstallationJobParameters, InstallationOutcomeJson>
							theStepExecutionDetails,
			@Nonnull IJobDataSink<PackageInstallOutcomeJson> theDataSink)
			throws JobExecutionFailedException {

		System.out.println(JsonUtil.serialize(myOutcome, true));

		theDataSink.accept(myOutcome);

		return RunOutcome.SUCCESS;
	}

	@Override
	public IReductionStepWorker<PackageInstallationJobParameters, InstallationOutcomeJson, PackageInstallOutcomeJson>
			newInstance() {
		return new FinalizeInstallationStep();
	}
}
