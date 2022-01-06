package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class FhirVersionIndependentConcept implements Comparable<FhirVersionIndependentConcept> {

	private final String mySystem;
	private final String mySystemVersion;
	private final String myCode;
	private final String myDisplay;
	private int myHashCode;

	/**
	 * Constructor
	 */
	public FhirVersionIndependentConcept(String theSystem, String theCode) {
		this(theSystem, theCode, null);
	}

	public FhirVersionIndependentConcept(String theSystem, String theCode, String theDisplay) {
		this(theSystem, theCode, theDisplay, null);
	}

	public FhirVersionIndependentConcept(String theSystem, String theCode, String theDisplay, String theSystemVersion) {
		mySystem = theSystem;
		mySystemVersion = theSystemVersion;
		myCode = theCode;
		myDisplay = theDisplay;
		myHashCode = new HashCodeBuilder(17, 37)
			.append(mySystem)
			.append(myCode)
			.toHashCode();
	}

	public String getDisplay() {
		return myDisplay;
	}

	public String getSystem() {
		return mySystem;
	}

	public String getSystemVersion() {
		return mySystemVersion;
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

		FhirVersionIndependentConcept that = (FhirVersionIndependentConcept) theO;

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
	public int compareTo(FhirVersionIndependentConcept theOther) {
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
