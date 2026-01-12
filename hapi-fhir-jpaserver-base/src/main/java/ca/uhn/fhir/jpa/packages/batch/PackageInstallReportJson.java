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
 * Final report output for package installation jobs.
 * This is stored in the job instance report field and returned when polling for job status.
 *
 * @since 8.2.0
 */
public class PackageInstallReportJson implements IModelJson {

	@JsonProperty("reportMessage")
	private String myReportMessage;

	@JsonProperty("messages")
	private List<String> myMessages;

	@JsonProperty("resourcesInstalled")
	private Map<String, Integer> myResourcesInstalled;

	@JsonProperty("packageName")
	private String myPackageName;

	@JsonProperty("packageVersion")
	private String myPackageVersion;

	@JsonProperty("totalResourcesInstalled")
	private int myTotalResourcesInstalled;

	public String getReportMessage() {
		return myReportMessage;
	}

	public PackageInstallReportJson setReportMessage(String theReportMessage) {
		myReportMessage = theReportMessage;
		return this;
	}

	public List<String> getMessages() {
		if (myMessages == null) {
			myMessages = new ArrayList<>();
		}
		return myMessages;
	}

	public PackageInstallReportJson setMessages(List<String> theMessages) {
		myMessages = theMessages;
		return this;
	}

	public Map<String, Integer> getResourcesInstalled() {
		if (myResourcesInstalled == null) {
			myResourcesInstalled = new HashMap<>();
		}
		return myResourcesInstalled;
	}

	public PackageInstallReportJson setResourcesInstalled(Map<String, Integer> theResourcesInstalled) {
		myResourcesInstalled = theResourcesInstalled;
		return this;
	}

	public String getPackageName() {
		return myPackageName;
	}

	public PackageInstallReportJson setPackageName(String thePackageName) {
		myPackageName = thePackageName;
		return this;
	}

	public String getPackageVersion() {
		return myPackageVersion;
	}

	public PackageInstallReportJson setPackageVersion(String thePackageVersion) {
		myPackageVersion = thePackageVersion;
		return this;
	}

	public int getTotalResourcesInstalled() {
		return myTotalResourcesInstalled;
	}

	public PackageInstallReportJson setTotalResourcesInstalled(int theTotalResourcesInstalled) {
		myTotalResourcesInstalled = theTotalResourcesInstalled;
		return this;
	}

	/**
	 * Creates a report from a step outcome.
	 *
	 * @param theStepOutcome the step outcome
	 * @return the report
	 */
	public static PackageInstallReportJson fromStepOutcome(PackageInstallStepOutcomeJson theStepOutcome) {
		PackageInstallReportJson report = new PackageInstallReportJson();
		report.setPackageName(theStepOutcome.getPackageName());
		report.setPackageVersion(theStepOutcome.getPackageVersion());
		report.setMessages(new ArrayList<>(theStepOutcome.getMessages()));
		report.setResourcesInstalled(new HashMap<>(theStepOutcome.getResourcesInstalled()));
		report.setTotalResourcesInstalled(theStepOutcome.getResourcesInstalledCount());

		// Generate human-readable report message
		StringBuilder sb = new StringBuilder();
		sb.append("Package installation completed for ");
		sb.append(theStepOutcome.getPackageName());
		if (theStepOutcome.getPackageVersion() != null) {
			sb.append("#").append(theStepOutcome.getPackageVersion());
		}
		sb.append(". ");
		sb.append("Total resources installed: ").append(theStepOutcome.getResourcesInstalledCount());
		sb.append(".");

		Map<String, Integer> installed = theStepOutcome.getResourcesInstalled();
		if (!installed.isEmpty()) {
			sb.append(" Breakdown: ");
			boolean first = true;
			for (Map.Entry<String, Integer> entry : installed.entrySet()) {
				if (!first) {
					sb.append(", ");
				}
				sb.append(entry.getKey()).append("=").append(entry.getValue());
				first = false;
			}
			sb.append(".");
		}

		report.setReportMessage(sb.toString());
		return report;
	}

	/**
	 * Converts this report to a PackageInstallOutcomeJson for API response.
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
