package ca.uhn.fhir.batch2.jobs.installpackage;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageContentsJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.jpa.packages.loader.IPackageLoader;
import ca.uhn.fhir.jpa.packages.loader.NpmPackageData;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Base64;

public class FetchPackageStep implements IFirstJobStepWorker<PackageInstallationJobParameters, PackageContentsJson> {

	@Autowired
	private IPackageLoader myPackageLoader;

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<PackageInstallationJobParameters, VoidModel> theStepExecutionDetails, @Nonnull IJobDataSink<PackageContentsJson> theDataSink) throws JobExecutionFailedException {

		try {
			NpmPackageData npmPackage = myPackageLoader.fetchPackageFromPackageSpec(theStepExecutionDetails.getParameters().getInstallationSpec());
			PackageContentsJson contents = new PackageContentsJson();
			contents.setContents(Base64.getEncoder().encode(npmPackage.getBytes()));
			theDataSink.accept(contents);
		} catch (IOException e) {
			// We're only concerned with the happy path for now - error handling is a separate MR
			throw new RuntimeException(e);
		}

		return RunOutcome.SUCCESS;
	}
}
