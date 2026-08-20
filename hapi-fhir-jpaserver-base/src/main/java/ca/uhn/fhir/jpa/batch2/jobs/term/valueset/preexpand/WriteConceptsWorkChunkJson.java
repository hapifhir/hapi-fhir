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

public class WriteConceptsWorkChunkJson implements IModelJson {
	private final FhirContext myCanonicalFhirContext = FhirContext.forR4Cached();

	@JsonProperty("startingOrder")
	private int myStartingOrder;

	@JsonProperty("startingOrderOffset")
	private int myStartingOrderOffset;

	@JsonProperty("valueSet")
	private String myValueSet;

	@JsonProperty("stagingVersion")
	private String myStagingVersion;

	public int getStartingOrderOffset() {
		return myStartingOrderOffset;
	}

	public void setStartingOrderOffset(int theStartingOrderOffset) {
		myStartingOrderOffset = theStartingOrderOffset;
	}

	public ValueSet getValueSet() {
		ValueSet retVal = null;
		if (myValueSet != null) {
			retVal = new ValueSet();
			myCanonicalFhirContext.newJsonParser().setPrettyPrint(false).parseInto(myValueSet, retVal);
		}
		return retVal;
	}

	public void setValueSet(ValueSet theValueSet) {
		String serialized = null;
		if (theValueSet != null) {
			serialized =
					myCanonicalFhirContext.newJsonParser().setPrettyPrint(false).encodeToString(theValueSet);
		}
		myValueSet = serialized;
	}

	public int getStartingOrder() {
		return myStartingOrder;
	}

	public void setStartingOrder(int theStartingOrder) {
		myStartingOrder = theStartingOrder;
	}

	public void setStagingVersion(String theStagingVersion) {
		myStagingVersion = theStagingVersion;
	}

	public String getStagingVersion() {
		return myStagingVersion;
	}
}
