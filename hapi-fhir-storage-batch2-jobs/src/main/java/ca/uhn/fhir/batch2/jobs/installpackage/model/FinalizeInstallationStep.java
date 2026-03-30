package ca.uhn.fhir.batch2.jobs.installpackage.model;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistryController;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;

public class FinalizeInstallationStep
		implements IJobStepWorker<
				PackageInstallationJobParameters, InstallationOutcomeJson, PackageInstallOutcomeJson> {

	@Autowired
	ISearchParamRegistryController mySearchParamRegistryController;

	@Autowired
	IValidationSupport myValidationSupport;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<PackageInstallationJobParameters, InstallationOutcomeJson>
							theStepExecutionDetails,
			@Nonnull IJobDataSink<PackageInstallOutcomeJson> theDataSink)
			throws JobExecutionFailedException {

		mySearchParamRegistryController.refreshCacheIfNecessary();

		myValidationSupport.invalidateCaches();

		// Since recursively installing dependencies isn't in scope yet, there's nothing to consolidate
		PackageInstallOutcomeJson outcome = theStepExecutionDetails.getData().getOutcomes().stream()
				.findFirst()
				.orElse(null);
		theDataSink.accept(outcome);

		return RunOutcome.SUCCESS;
	}
}
