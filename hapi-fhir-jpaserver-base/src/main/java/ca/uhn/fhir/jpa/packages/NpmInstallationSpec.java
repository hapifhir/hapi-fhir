package ca.uhn.fhir.jpa.packages;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel(
	value = "NpmInstallationSpec",
	description =
		"Defines a "
)
@JsonPropertyOrder({
	"packageId", "packageVersion", "packageUrl", "installMode", "installResourceTypes", "validationMode"
})
public class NpmInstallationSpec {

	@ApiModelProperty("The direct package URL")
	@JsonProperty("packageUrl")
	private String myPackageUrl;

	@ApiModelProperty("The NPM package ID")
	@JsonProperty("packageId")
	private String myPackageId;
	@ApiModelProperty("The direct package version")
	@JsonProperty("packageVersion")
	private String myPackageVersion;
	@ApiModelProperty("Should resources from this package be extracted from the package and installed into the repository individually")
	@JsonProperty("installMode")
	private InstallModeEnum myInstallMode;
	@ApiModelProperty("If resources are being installed individually, this is list provides the resource types to install. By default, all conformance resources will be installed.")
	@JsonProperty("installResourceTypes")
	private List<String> myInstallResourceTypes;
	@ApiModelProperty("Should contents be made available to the FHIR validation infrastructure")
	@JsonProperty("validationMode")
	private ValidationModeEnum myValidationMode;

	public String getPackageUrl() {
		return myPackageUrl;
	}

	public NpmInstallationSpec setPackageUrl(String thePackageUrl) {
		myPackageUrl = thePackageUrl;
		return this;
	}

	public InstallModeEnum getInstallMode() {
		return myInstallMode;
	}

	public NpmInstallationSpec setInstallMode(InstallModeEnum theInstallMode) {
		myInstallMode = theInstallMode;
		return this;
	}

	public List<String> getInstallResourceTypes() {
		if (myInstallResourceTypes == null) {
			myInstallResourceTypes = new ArrayList<>();
		}
		return myInstallResourceTypes;
	}

	public ValidationModeEnum getValidationMode() {
		return myValidationMode;
	}

	public NpmInstallationSpec setValidationMode(ValidationModeEnum theValidationMode) {
		myValidationMode = theValidationMode;
		return this;
	}

	public String getPackageId() {
		return myPackageId;
	}

	public NpmInstallationSpec setPackageId(String thePackageId) {
		myPackageId = thePackageId;
		return this;
	}

	public String getPackageVersion() {
		return myPackageVersion;
	}

	public NpmInstallationSpec setPackageVersion(String thePackageVersion) {
		myPackageVersion = thePackageVersion;
		return this;
	}


	public enum InstallModeEnum {
		CACHE_ONLY,
		CACHE_AND_INSTALL
	}

	public enum ValidationModeEnum {
		NOT_AVAILABLE,
		AVAILABLE
	}

}
