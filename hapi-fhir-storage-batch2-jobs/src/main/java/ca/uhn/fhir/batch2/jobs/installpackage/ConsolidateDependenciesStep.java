package ca.uhn.fhir.batch2.jobs.installpackage;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RetryChunkLaterException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageContentsJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageWithDependenciesJson;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import jakarta.annotation.Nonnull;

import java.util.List;

public class ConsolidateDependenciesStep
		implements IJobStepWorker<PackageInstallationJobParameters, PackageWithDependenciesJson, PackageContentsJson> {

	private IJobCoordinator myJobCoordinator;

	public ConsolidateDependenciesStep(IJobCoordinator theJobCoordinator) {
		this.myJobCoordinator = theJobCoordinator;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<PackageInstallationJobParameters, PackageWithDependenciesJson>
							theStepExecutionDetails,
			@Nonnull IJobDataSink<PackageContentsJson> theDataSink)
			throws JobExecutionFailedException {

		PackageWithDependenciesJson packageWithDependenciesJson = theStepExecutionDetails.getData();

		List<JobInstance> jobInstances = packageWithDependenciesJson.getDependencyJobIds().stream()
				.map(myJobCoordinator::getInstance)
				.toList();

		// If any of the child jobs are still in flight, try again later
		if (jobInstances.stream().map(JobInstance::getStatus).anyMatch(s -> !s.isEnded())) {
			throw new RetryChunkLaterException(Msg.code(2907));
		}

		// pass the bytes along unchanged
		PackageContentsJson packageContentsJson = new PackageContentsJson();
		packageContentsJson.setContents(packageWithDependenciesJson.getContents());

		// merge all the reports together
		PackageInstallOutcomeJson inboundReport = packageWithDependenciesJson.getReport();
		PackageInstallOutcomeJson outboundReport = new PackageInstallOutcomeJson();

		outboundReport.getMessage().addAll(inboundReport.getMessage());

		packageContentsJson.setReport(outboundReport);

		theDataSink.accept(packageContentsJson);

		return RunOutcome.SUCCESS;
	}
}
