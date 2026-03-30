package ca.uhn.fhir.batch2.jobs.installpackage;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.installpackage.model.InstallationOutcomeJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageContentsJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.jpa.packages.IPackageInstallerSvc;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ProcessPackageStep
		implements IJobStepWorker<PackageInstallationJobParameters, PackageContentsJson, InstallationOutcomeJson> {

	@Autowired
	IPackageInstallerSvc myPackageInstallerSvc;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<PackageInstallationJobParameters, PackageContentsJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<InstallationOutcomeJson> theDataSink)
			throws JobExecutionFailedException {
		try {
			NpmPackage npmPackage = NpmPackage.fromPackage(
					new ByteArrayInputStream(theStepExecutionDetails.getData().getContents()));
			PackageInstallOutcomeJson packageOutcome = new PackageInstallOutcomeJson();
			myPackageInstallerSvc.installPackage(
					npmPackage, theStepExecutionDetails.getParameters().getInstallationSpec(), packageOutcome);

			InstallationOutcomeJson outcome = new InstallationOutcomeJson();
			outcome.getOutcomes().add(packageOutcome);

			theDataSink.accept(outcome);
		} catch (IOException e) {
			// error handling is in the scop of a later ticket
			throw new RuntimeException(e);
		}
		return RunOutcome.SUCCESS;
	}
}
