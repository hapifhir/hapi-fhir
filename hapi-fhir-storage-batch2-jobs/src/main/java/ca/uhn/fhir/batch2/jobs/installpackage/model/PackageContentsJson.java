/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
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
package ca.uhn.fhir.batch2.jobs.installpackage.model;

import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PackageContentsJson implements IModelJson {

	@JsonProperty("contents")
	private byte[] myContents;

	@JsonProperty("report")
	private PackageInstallOutcomeJson myReport;

	public byte[] getContents() {
		return myContents;
	}

	public void setContents(byte[] theContents) {
		myContents = theContents;
	}

	public PackageInstallOutcomeJson getReport() {
		return myReport;
	}

	public void setReport(PackageInstallOutcomeJson theReport) {
		myReport = theReport;
	}
}
