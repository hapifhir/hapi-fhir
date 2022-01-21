package ca.uhn.fhir.mdm.rules.json;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.rules.matcher.MdmMatcherEnum;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.instance.model.api.IBase;

public class MdmMatcherJson implements IModelJson {
	@JsonProperty(value = "algorithm", required = true)
	MdmMatcherEnum myAlgorithm;

	@JsonProperty(value = "identifierSystem", required = false)
	String myIdentifierSystem;

	/**
	 * For String value types, should the values be normalized (case, accents) before they are compared
	 */
	@JsonProperty(value = "exact")
	boolean myExact;

	public MdmMatcherEnum getAlgorithm() {
		return myAlgorithm;
	}

	public MdmMatcherJson setAlgorithm(MdmMatcherEnum theAlgorithm) {
		myAlgorithm = theAlgorithm;
		return this;
	}

	public String getIdentifierSystem() {
		return myIdentifierSystem;
	}

	public MdmMatcherJson setIdentifierSystem(String theIdentifierSystem) {
		myIdentifierSystem = theIdentifierSystem;
		return this;
	}

	public boolean getExact() {
		return myExact;
	}

	public MdmMatcherJson setExact(boolean theExact) {
		myExact = theExact;
		return this;
	}

	public boolean isMatchingEmptyFields() {
		return myAlgorithm.isMatchingEmptyFields();
	}

	public boolean match(FhirContext theFhirContext, IBase theLeftValue, IBase theRightValue) {
		return myAlgorithm.match(theFhirContext, theLeftValue, theRightValue, myExact, myIdentifierSystem);
	}
}
