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
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.r4.model.ValueSet;

public class ExpandConceptsWorkChunkJson implements IModelJson {
	private final FhirContext myCanonicalFhirContext = FhirContext.forR4Cached();

	@JsonProperty("stagingUrl")
	private String myStagingUrl;

	@JsonProperty("stagingVersion")
	private String myStagingVersion;

	@JsonProperty("include")
	private boolean myInclude;

	@JsonProperty("compose")
	private String myCompose;

	@JsonProperty("startingOrder")
	private int myStartingOrder;

	public int getStartingOrder() {
		return myStartingOrder;
	}

	public void setStartingOrder(int theStartingOrder) {
		myStartingOrder = theStartingOrder;
	}

	public String getStagingUrl() {
		return myStagingUrl;
	}

	public void setStagingUrl(String theStagingUrl) {
		myStagingUrl = theStagingUrl;
	}

	public boolean isInclude() {
		return myInclude;
	}

	public void setInclude(boolean theInclude) {
		myInclude = theInclude;
	}

	public ValueSet.ConceptSetComponent getCompose() {
		ValueSet.ConceptSetComponent retVal = null;
		if (myCompose != null) {
			retVal = new ValueSet.ConceptSetComponent();
			myCanonicalFhirContext.newJsonParser().setPrettyPrint(false).parseInto(myCompose, retVal);
		}
		return retVal;
	}

	public void setCompose(ValueSet.ConceptSetComponent theValueSetCompose) {
		String serialized = null;
		if (theValueSetCompose != null) {
			serialized =
					myCanonicalFhirContext.newJsonParser().setPrettyPrint(false).encodeToString(theValueSetCompose);
		}
		myCompose = serialized;
	}

	public String getStagingVersion() {
		return myStagingVersion;
	}

	public void setStagingVersion(String theStagingVersion) {
		myStagingVersion = theStagingVersion;
	}

	public String getComposeAsJson() {
		return myCompose;
	}
}
