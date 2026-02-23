/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.rules.json;

import ca.uhn.fhir.mdm.rules.matcher.models.MatchTypeEnum;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MdmMatcherJson implements IModelJson {
	@JsonProperty(value = "algorithm", required = true)
	String myAlgorithm;

	@JsonProperty(value = "identifierSystem", required = false)
	String myIdentifierSystem;

	/**
	 * For String value types, should the values be normalized (case, accents) before they are compared
	 */
	@JsonProperty(value = "exact")
	boolean myExact;

	public String getAlgorithm() {
		return myAlgorithm;
	}

	public MdmMatcherJson setAlgorithm(String theAlgorithm) {
		myAlgorithm = theAlgorithm;
		return this;
	}

	/**
	 * Convenience overload for backward compatibility with code that passes a {@link MatchTypeEnum}.
	 */
	public MdmMatcherJson setAlgorithm(MatchTypeEnum theAlgorithm) {
		myAlgorithm = theAlgorithm.name();
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
}
