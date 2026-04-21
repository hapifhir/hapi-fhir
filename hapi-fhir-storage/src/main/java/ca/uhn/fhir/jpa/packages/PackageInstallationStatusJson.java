/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PackageInstallationStatusJson implements IModelJson {

	private final DateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@JsonProperty("jobId")
	private String myJobId;

	@JsonProperty("status")
	private String myStatus;

	@JsonProperty("progress")
	private double myProgress;

	@JsonProperty("currentStep")
	private String myCurrentStep;

	@JsonProperty("startTime")
	private String myStartTime;

	@JsonProperty("outcome")
	private String myOutcome;

	public String getJobId() {
		return myJobId;
	}

	public void setJobId(String theJobId) {
		myJobId = theJobId;
	}

	public String getStatus() {
		return myStatus;
	}

	public void setStatus(String theStatus) {
		myStatus = theStatus;
	}

	public double getProgress() {
		return myProgress;
	}

	public void setProgress(double theProgress) {
		myProgress = theProgress;
	}

	public String getCurrentStep() {
		return myCurrentStep;
	}

	public void setCurrentStep(String theCurrentStep) {
		myCurrentStep = theCurrentStep;
	}

	public String getStartTime() {
		return myStartTime;
	}

	public void setStartTime(String theStartTime) {
		myStartTime = theStartTime;
	}

	public void setStartTime(Date theStartTime) {
		if (theStartTime != null) {
			myStartTime = myDateFormat.format(theStartTime);
		} else {
			myStartTime = null;
		}
	}

	public String getOutcome() {
		return myOutcome;
	}

	public void setOutcome(String theOutcome) {
		myOutcome = theOutcome;
	}
}
