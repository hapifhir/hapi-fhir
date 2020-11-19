package ca.uhn.fhir.mdm.model;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import org.hl7.fhir.r4.model.Person;

public enum CanonicalIdentityAssuranceLevel {
	LEVEL1("level1"),
	LEVEL2("level2"),
	LEVEL3("level3"),
	LEVEL4("level4");

	private String myCanonicalLevel;
	private CanonicalIdentityAssuranceLevel(String theCanonicalLevel) {
		myCanonicalLevel = theCanonicalLevel;
	}

	public Person.IdentityAssuranceLevel toR4() {
		return Person.IdentityAssuranceLevel.fromCode(myCanonicalLevel);
	}

	public org.hl7.fhir.dstu3.model.Person.IdentityAssuranceLevel toDstu3() {
		return org.hl7.fhir.dstu3.model.Person.IdentityAssuranceLevel.fromCode(myCanonicalLevel);
	}

}
