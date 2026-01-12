/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
// Created by claude-opus-4-5-20250101
package ca.uhn.fhir.jpa.packages.batch;

import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Job parameters for asynchronous package installation via Batch2.
 * <p>
 * Note: This class does not support inline package contents (byte[]).
 * For async installation, use either packageUrl or name+version.
 * </p>
 *
 * @since 8.2.0
 */
public class PackageInstallJobParameters implements IModelJson {

	@JsonProperty("name")
	private String myName;

	@JsonProperty("version")
	private String myVersion;

	@JsonProperty("packageUrl")
	@Nullable
	private String myPackageUrl;

	@JsonProperty("installMode")
	private PackageInstallationSpec.InstallModeEnum myInstallMode;

	@JsonProperty("installResourceTypes")
	@Nullable
	private List<String> myInstallResourceTypes;

	@JsonProperty("fetchDependencies")
	private boolean myFetchDependencies;

	@JsonProperty("reloadExisting")
	private boolean myReloadExisting = true;

	@JsonProperty("dependencyExcludes")
	@Nullable
	private List<String> myDependencyExcludes;

	@JsonProperty("additionalResourceFolders")
	@Nullable
	private Set<String> myAdditionalResourceFolders;

	public String getName() {
		return myName;
	}

	public PackageInstallJobParameters setName(String theName) {
		myName = theName;
		return this;
	}

	public String getVersion() {
		return myVersion;
	}

	public PackageInstallJobParameters setVersion(String theVersion) {
		myVersion = theVersion;
		return this;
	}

	@Nullable
	public String getPackageUrl() {
		return myPackageUrl;
	}

	public PackageInstallJobParameters setPackageUrl(@Nullable String thePackageUrl) {
		myPackageUrl = thePackageUrl;
		return this;
	}

	public PackageInstallationSpec.InstallModeEnum getInstallMode() {
		return myInstallMode;
	}

	public PackageInstallJobParameters setInstallMode(PackageInstallationSpec.InstallModeEnum theInstallMode) {
		myInstallMode = theInstallMode;
		return this;
	}

	public List<String> getInstallResourceTypes() {
		if (myInstallResourceTypes == null) {
			myInstallResourceTypes = new ArrayList<>();
		}
		return myInstallResourceTypes;
	}

	public PackageInstallJobParameters setInstallResourceTypes(@Nullable List<String> theInstallResourceTypes) {
		myInstallResourceTypes = theInstallResourceTypes;
		return this;
	}

	public boolean isFetchDependencies() {
		return myFetchDependencies;
	}

	public PackageInstallJobParameters setFetchDependencies(boolean theFetchDependencies) {
		myFetchDependencies = theFetchDependencies;
		return this;
	}

	public boolean isReloadExisting() {
		return myReloadExisting;
	}

	public PackageInstallJobParameters setReloadExisting(boolean theReloadExisting) {
		myReloadExisting = theReloadExisting;
		return this;
	}

	public List<String> getDependencyExcludes() {
		if (myDependencyExcludes == null) {
			myDependencyExcludes = new ArrayList<>();
		}
		return myDependencyExcludes;
	}

	public PackageInstallJobParameters setDependencyExcludes(@Nullable List<String> theDependencyExcludes) {
		myDependencyExcludes = theDependencyExcludes;
		return this;
	}

	@Nullable
	public Set<String> getAdditionalResourceFolders() {
		return myAdditionalResourceFolders;
	}

	public PackageInstallJobParameters setAdditionalResourceFolders(
			@Nullable Set<String> theAdditionalResourceFolders) {
		myAdditionalResourceFolders = theAdditionalResourceFolders;
		return this;
	}

	/**
	 * Creates job parameters from a PackageInstallationSpec.
	 * <p>
	 * Note: packageContents (byte[]) is not supported for async mode.
	 * If the spec contains packageContents, an IllegalArgumentException is thrown.
	 * </p>
	 *
	 * @param theSpec the installation specification
	 * @return the job parameters
	 * @throws IllegalArgumentException if packageContents is not null
	 */
	public static PackageInstallJobParameters fromSpec(PackageInstallationSpec theSpec) {
		if (theSpec.getPackageContents() != null) {
			throw new IllegalArgumentException("Async package installation does not support inline package contents. "
					+ "Use packageUrl or name+version instead.");
		}

		PackageInstallJobParameters params = new PackageInstallJobParameters();
		params.setName(theSpec.getName());
		params.setVersion(theSpec.getVersion());
		params.setPackageUrl(theSpec.getPackageUrl());
		params.setInstallMode(theSpec.getInstallMode());
		params.setFetchDependencies(theSpec.isFetchDependencies());
		params.setReloadExisting(theSpec.isReloadExisting());
		params.setAdditionalResourceFolders(theSpec.getAdditionalResourceFolders());

		if (!theSpec.getInstallResourceTypes().isEmpty()) {
			params.setInstallResourceTypes(new ArrayList<>(theSpec.getInstallResourceTypes()));
		}

		if (!theSpec.getDependencyExcludes().isEmpty()) {
			params.setDependencyExcludes(new ArrayList<>(theSpec.getDependencyExcludes()));
		}

		return params;
	}

	/**
	 * Converts these job parameters back to a PackageInstallationSpec.
	 *
	 * @return the installation specification
	 */
	public PackageInstallationSpec toSpec() {
		PackageInstallationSpec spec = new PackageInstallationSpec();
		spec.setName(myName);
		spec.setVersion(myVersion);
		spec.setPackageUrl(myPackageUrl);
		spec.setInstallMode(myInstallMode);
		spec.setFetchDependencies(myFetchDependencies);
		spec.setReloadExisting(myReloadExisting);
		spec.setAdditionalResourceFolders(myAdditionalResourceFolders);

		if (myInstallResourceTypes != null) {
			spec.setInstallResourceTypes(new ArrayList<>(myInstallResourceTypes));
		}

		if (myDependencyExcludes != null) {
			spec.setDependencyExcludes(new ArrayList<>(myDependencyExcludes));
		}

		return spec;
	}
}
