/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.cr.config;

public class CareGapsProperties {
	private boolean myThreadedCareGapsEnabled = true;
	private String myCareGapsReporter;
	private String myCareGapsCompositionSectionAuthor;

	public boolean getThreadedCareGapsEnabled() {
		return myThreadedCareGapsEnabled;
	}

	public void setThreadedCareGapsEnabled(boolean theThreadedCareGapsEnabled) {
		myThreadedCareGapsEnabled = theThreadedCareGapsEnabled;
	}

	public boolean isThreadedCareGapsEnabled() {
		return myThreadedCareGapsEnabled;
	}

	public String getCareGapsReporter() {
		return myCareGapsReporter;
	}

	public void setCareGapsReporter(String theCareGapsReporter) {
		myCareGapsReporter = theCareGapsReporter;
	}

	public String getCareGapsCompositionSectionAuthor() {
		return myCareGapsCompositionSectionAuthor;
	}

	public void setCareGapsCompositionSectionAuthor(String theCareGapsCompositionSectionAuthor) {
		myCareGapsCompositionSectionAuthor = theCareGapsCompositionSectionAuthor;
	}
}
