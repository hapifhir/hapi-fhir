package ca.uhn.fhir.batch2.jobs.installpackage;

import ca.uhn.fhir.batch2.jobs.installpackage.model.InstallationOutcomeJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageContentsJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ca.uhn.fhir.util.Batch2JobDefinitionConstants.INSTALL_PACKAGE;

@Configuration
public class InstallPackageAppCtx {

	@Bean("installPackageJobDefinition")
	public JobDefinition<PackageInstallationJobParameters> installPackageJobDefinition() {
		return JobDefinition.newBuilder()
				.setJobDefinitionId(INSTALL_PACKAGE)
				.setJobDescription("Install NPM Package")
				.setJobDefinitionVersion(1)
				.setParametersType(PackageInstallationJobParameters.class)
				.addFirstStep("fetch-package", "Fetch the NPM Package", PackageContentsJson.class, fetchPackageStep())
				.addIntermediateStep(
						"process-package",
						"Install the contents of the NPM package",
						InstallationOutcomeJson.class,
						processPackageStep())
				.addFinalReducerStep(
						"finalize",
						"Refresh caches and consolidate output",
						PackageInstallOutcomeJson.class,
						finalizeInstallationStep())
				.build();
	}

	@Bean
	public FetchPackageStep fetchPackageStep() {
		return new FetchPackageStep();
	}

	@Bean
	public ProcessPackageStep processPackageStep() {
		return new ProcessPackageStep();
	}

	@Bean
	public FinalizeInstallationStep finalizeInstallationStep() {
		return new FinalizeInstallationStep();
	}
}
