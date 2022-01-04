package ca.uhn.fhir.jpa.packages;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import ca.uhn.fhir.model.api.annotation.ExampleSupplier;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Schema(
	name = "PackageInstallationSpec",
	description =
		"Defines a set of instructions for package installation"
)
@JsonPropertyOrder({
	"name", "version", "packageUrl", "installMode", "installResourceTypes", "validationMode"
})
@ExampleSupplier({PackageInstallationSpec.ExampleSupplier.class, PackageInstallationSpec.ExampleSupplier2.class})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class PackageInstallationSpec {

	@Schema(description = "The direct package URL")
	@JsonProperty("packageUrl")
	private String myPackageUrl;
	@Schema(description = "The NPM package Name")
	@JsonProperty("name")
	private String myName;
	@Schema(description = "The direct package version")
	@JsonProperty("version")
	private String myVersion;
	@Schema(description = "Should resources from this package be extracted from the package and installed into the repository individually")
	@JsonProperty("installMode")
	private InstallModeEnum myInstallMode;
	@Schema(description = "If resources are being installed individually, this is list provides the resource types to install. By default, all conformance resources will be installed.")
	@JsonProperty("installResourceTypes")
	private List<String> myInstallResourceTypes;
	@Schema(description = "Should dependencies be automatically resolved, fetched and installed with the same settings")
	@JsonProperty("fetchDependencies")
	private boolean myFetchDependencies;
	@Schema(description = "Any values provided here will be interpreted as a regex. Dependencies with an ID matching any regex will be skipped.")
	private List<String> myDependencyExcludes;
	@JsonIgnore
	private byte[] myPackageContents;

	public List<String> getDependencyExcludes() {
		if (myDependencyExcludes == null) {
			myDependencyExcludes = new ArrayList<>();
		}
		return myDependencyExcludes;
	}

	public void setDependencyExcludes(List<String> theDependencyExcludes) {
		myDependencyExcludes = theDependencyExcludes;
	}

	public boolean isFetchDependencies() {
		return myFetchDependencies;
	}

	public PackageInstallationSpec setFetchDependencies(boolean theFetchDependencies) {
		myFetchDependencies = theFetchDependencies;
		return this;
	}

	public String getPackageUrl() {
		return myPackageUrl;
	}

	public PackageInstallationSpec setPackageUrl(String thePackageUrl) {
		myPackageUrl = thePackageUrl;
		return this;
	}

	public InstallModeEnum getInstallMode() {
		return myInstallMode;
	}

	public PackageInstallationSpec setInstallMode(InstallModeEnum theInstallMode) {
		myInstallMode = theInstallMode;
		return this;
	}

	public List<String> getInstallResourceTypes() {
		if (myInstallResourceTypes == null) {
			myInstallResourceTypes = new ArrayList<>();
		}
		return myInstallResourceTypes;
	}

	public void setInstallResourceTypes(List<String> theInstallResourceTypes) {
		myInstallResourceTypes = theInstallResourceTypes;
	}

	public String getName() {
		return myName;
	}

	public PackageInstallationSpec setName(String theName) {
		myName = theName;
		return this;
	}

	public String getVersion() {
		return myVersion;
	}

	public PackageInstallationSpec setVersion(String theVersion) {
		myVersion = theVersion;
		return this;
	}

	public byte[] getPackageContents() {
		return myPackageContents;
	}

	public PackageInstallationSpec setPackageContents(byte[] thePackageContents) {
		myPackageContents = thePackageContents;
		return this;
	}

	public PackageInstallationSpec addDependencyExclude(String theExclude) {
		getDependencyExcludes().add(theExclude);
		return this;
	}

	public PackageInstallationSpec addInstallResourceTypes(String... theResourceTypes) {
		for (String next : theResourceTypes) {
			getInstallResourceTypes().add(next);
		}
		return this;
	}

	public enum InstallModeEnum {
		STORE_ONLY,
		STORE_AND_INSTALL
	}

	public enum ValidationModeEnum {
		NOT_AVAILABLE,
		AVAILABLE
	}

	public static class ExampleSupplier implements Supplier<PackageInstallationSpec> {

		@Override
		public PackageInstallationSpec get() {
			return new PackageInstallationSpec()
				.setName("hl7.fhir.us.core")
				.setVersion("3.1.0")
				.setInstallMode(InstallModeEnum.STORE_ONLY)
				.setFetchDependencies(true);
		}
	}

	public static class ExampleSupplier2 implements Supplier<PackageInstallationSpec> {

		@Override
		public PackageInstallationSpec get() {
			return new PackageInstallationSpec()
				.setName("com.example.my-resources")
				.setVersion("1.0")
				.setPackageUrl("classpath:/my-resources.tgz")
				.setInstallMode(InstallModeEnum.STORE_AND_INSTALL)
				.addInstallResourceTypes("Organization", "Medication", "PlanDefinition", "SearchParameter");
		}
	}

}
