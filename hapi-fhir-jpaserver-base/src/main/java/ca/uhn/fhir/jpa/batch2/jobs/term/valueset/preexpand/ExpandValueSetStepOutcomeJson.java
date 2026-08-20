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
package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.r4.model.ValueSet;

public class ExpandValueSetStepOutcomeJson implements IModelJson {
	private final FhirContext myCanonicalFhirContext = FhirContext.forR4Cached();

	@JsonProperty("sourceCompose")
	private String mySourceCompose;

	@JsonProperty("recordsAddedCounter")
	private TerminologyFileSetJson.RecordsAddedCounter myRecordsAddedCounter;

	@JsonProperty("startingOrder")
	private int myStartingOrder;

	@JsonProperty("stagingVersion")
	private String myStagingVersion;

	@JsonProperty("failureMessage")
	private String myFailureMessage;

	public void setRecordsAddedCounter(TerminologyFileSetJson.RecordsAddedCounter theRecordsAddedCounter) {
		myRecordsAddedCounter = theRecordsAddedCounter;
	}

	public TerminologyFileSetJson.RecordsAddedCounter getRecordsAddedCounter() {
		return myRecordsAddedCounter;
	}

	public void setSourceCompose(ValueSet.ValueSetComposeComponent theSourceCompose) {
		String compose = null;
		if (theSourceCompose != null) {
			compose =
					myCanonicalFhirContext.newJsonParser().setPrettyPrint(false).encodeToString(theSourceCompose);
		}
		mySourceCompose = compose;
	}

	public ValueSet.ValueSetComposeComponent getSourceCompose() {
		ValueSet.ValueSetComposeComponent retVal = null;
		if (mySourceCompose != null) {
			retVal = new ValueSet.ValueSetComposeComponent();
			myCanonicalFhirContext.newJsonParser().parseInto(mySourceCompose, retVal);
		}
		return retVal;
	}

	public void setStartingOrder(int theStartingOrder) {
		myStartingOrder = theStartingOrder;
	}

	public int getStartingOrder() {
		return myStartingOrder;
	}

	public void setStagingVersion(String theStagingVersion) {
		myStagingVersion = theStagingVersion;
	}

	public String getStagingVersion() {
		return myStagingVersion;
	}

	public void setFailureMessage(String theFailureMessage) {
		myFailureMessage = theFailureMessage;
	}

	public String getFailureMessage() {
		return myFailureMessage;
	}
}
