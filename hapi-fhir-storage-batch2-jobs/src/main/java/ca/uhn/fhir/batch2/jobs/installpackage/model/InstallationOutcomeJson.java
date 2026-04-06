package ca.uhn.fhir.batch2.jobs.installpackage.model;

import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class InstallationOutcomeJson implements IModelJson {
	@JsonProperty("outcomes")
	List<PackageInstallOutcomeJson> myOutcomes = new ArrayList<>();

	public List<PackageInstallOutcomeJson> getOutcomes() {
		return myOutcomes;
	}

	public void setOutcomes(List<PackageInstallOutcomeJson> theOutcomes) {
		myOutcomes = theOutcomes;
	}
}
