package ca.uhn.fhir.jpa.term;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class VersionIndependentConcept implements Comparable<VersionIndependentConcept> {

	private final String mySystem;
	private final String myCode;
	private int myHashCode;

	/**
	 * Constructor
	 */
	public VersionIndependentConcept(String theSystem, String theCode) {
		mySystem = theSystem;
		myCode = theCode;
		myHashCode = new HashCodeBuilder(17, 37)
			.append(mySystem)
			.append(myCode)
			.toHashCode();
	}

	public String getSystem() {
		return mySystem;
	}


	public String getCode() {
		return myCode;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}

		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}

		VersionIndependentConcept that = (VersionIndependentConcept) theO;

		return new EqualsBuilder()
			.append(mySystem, that.mySystem)
			.append(myCode, that.myCode)
			.isEquals();
	}

	@Override
	public int hashCode() {
		return myHashCode;
	}

	@Override
	public int compareTo(VersionIndependentConcept theOther) {
		CompareToBuilder b = new CompareToBuilder();
		b.append(mySystem, theOther.getSystem());
		b.append(myCode, theOther.getCode());
		return b.toComparison();
	}

	@Override
	public String toString() {
		return "[" + mySystem + "|" + myCode + "]";
	}
}
