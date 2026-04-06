package ca.uhn.fhir.batch2.jobs.installpackage;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.installpackage.model.InstallationOutcomeJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageContentsJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.packages.IPackageInstallerSvc;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistryController;
import ca.uhn.fhir.util.JsonUtil;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class ProcessPackageStep
		implements IJobStepWorker<PackageInstallationJobParameters, PackageContentsJson, InstallationOutcomeJson> {

	@Autowired
	IPackageInstallerSvc myPackageInstallerSvc;

	@Autowired
	ISearchParamRegistryController mySearchParamRegistryController;

	@Autowired
	IValidationSupport myValidationSupport;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<PackageInstallationJobParameters, PackageContentsJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<InstallationOutcomeJson> theDataSink)
			throws JobExecutionFailedException {
		try {
			byte[] encodedContents = theStepExecutionDetails.getData().getContents();
			byte[] decodedContents = Base64.getDecoder().decode(encodedContents);
			NpmPackage npmPackage = NpmPackage.fromPackage(new ByteArrayInputStream(decodedContents));
			PackageInstallOutcomeJson packageOutcome = new PackageInstallOutcomeJson();

			PackageInstallationSpec installationSpec =
					theStepExecutionDetails.getParameters().getInstallationSpec();

			myPackageInstallerSvc.installPackage(npmPackage, installationSpec, packageOutcome);

			if (installationSpec.getInstallMode() == PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY) {
				packageOutcome
						.getMessage()
						.add(
								"Resources have been successfully installed. This is INSTALL only, so there will be no NPM packages persisted.");
			}

			System.out.println(JsonUtil.serialize(packageOutcome, true));

			mySearchParamRegistryController.refreshCacheIfNecessary();

			myValidationSupport.invalidateCaches();

			InstallationOutcomeJson outcome = new InstallationOutcomeJson();
			outcome.getOutcomes().add(packageOutcome);

			System.out.println(JsonUtil.serialize(outcome, true));

			theDataSink.accept(outcome);
		} catch (IOException e) {
			// error handling is in the scope of a later ticket
			throw new RuntimeException(e);
		}
		return RunOutcome.SUCCESS;
	}
}
