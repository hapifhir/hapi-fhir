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

import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Step output for package installation that wraps {@link PackageInstallOutcomeJson}
 * with the {@link IModelJson} interface for Batch2 compatibility.
 *
 * @since 8.2.0
 */
public class PackageInstallStepOutcomeJson implements IModelJson {

	@JsonProperty("messages")
	private List<String> myMessages;

	@JsonProperty("resourcesInstalled")
	private Map<String, Integer> myResourcesInstalled;

	@JsonProperty("packageName")
	private String myPackageName;

	@JsonProperty("packageVersion")
	private String myPackageVersion;

	/**
	 * Default constructor for JSON deserialization.
	 */
	public PackageInstallStepOutcomeJson() {
		super();
	}

	/**
	 * Constructor that copies data from a PackageInstallOutcomeJson.
	 *
	 * @param theOutcome the outcome to copy from
	 */
	public PackageInstallStepOutcomeJson(PackageInstallOutcomeJson theOutcome) {
		this();
		if (theOutcome != null) {
			myMessages = new ArrayList<>(theOutcome.getMessage());
			myResourcesInstalled = new HashMap<>(theOutcome.getResourcesInstalled());
		}
	}

	public List<String> getMessages() {
		if (myMessages == null) {
			myMessages = new ArrayList<>();
		}
		return myMessages;
	}

	public PackageInstallStepOutcomeJson setMessages(List<String> theMessages) {
		myMessages = theMessages;
		return this;
	}

	public Map<String, Integer> getResourcesInstalled() {
		if (myResourcesInstalled == null) {
			myResourcesInstalled = new HashMap<>();
		}
		return myResourcesInstalled;
	}

	public PackageInstallStepOutcomeJson setResourcesInstalled(Map<String, Integer> theResourcesInstalled) {
		myResourcesInstalled = theResourcesInstalled;
		return this;
	}

	public String getPackageName() {
		return myPackageName;
	}

	public PackageInstallStepOutcomeJson setPackageName(String thePackageName) {
		myPackageName = thePackageName;
		return this;
	}

	public String getPackageVersion() {
		return myPackageVersion;
	}

	public PackageInstallStepOutcomeJson setPackageVersion(String thePackageVersion) {
		myPackageVersion = thePackageVersion;
		return this;
	}

	/**
	 * Returns the total number of resources installed across all resource types.
	 *
	 * @return the total count
	 */
	public int getResourcesInstalledCount() {
		return getResourcesInstalled().values().stream()
				.mapToInt(Integer::intValue)
				.sum();
	}

	/**
	 * Converts this step outcome back to a PackageInstallOutcomeJson.
	 *
	 * @return the outcome JSON
	 */
	public PackageInstallOutcomeJson toOutcomeJson() {
		PackageInstallOutcomeJson outcome = new PackageInstallOutcomeJson();
		if (myMessages != null) {
			outcome.getMessage().addAll(myMessages);
		}
		if (myResourcesInstalled != null) {
			outcome.getResourcesInstalled().putAll(myResourcesInstalled);
		}
		return outcome;
	}
}
