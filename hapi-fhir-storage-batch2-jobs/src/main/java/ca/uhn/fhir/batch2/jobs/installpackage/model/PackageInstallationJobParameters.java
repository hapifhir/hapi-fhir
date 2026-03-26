package ca.uhn.fhir.batch2.jobs.installpackage.model;

import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import ca.uhn.fhir.model.api.BaseBatchJobParameters;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PackageInstallationJobParameters extends BaseBatchJobParameters {

	@JsonProperty("spec")
	private PackageInstallationSpec myInstallationSpec;

	public PackageInstallationSpec getInstallationSpec() {
		return myInstallationSpec;
	}
	public void setInstallationSpec(PackageInstallationSpec theInstallationSpec) {
		myInstallationSpec = theInstallationSpec;
	}
}
