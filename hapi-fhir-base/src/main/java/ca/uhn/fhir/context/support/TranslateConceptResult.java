package ca.uhn.fhir.context.support;

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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TranslateConceptResult {
	private String mySystem;
	private String myCode;
	private String myDisplay;
	private String myEquivalence;
	private String myConceptMapUrl;
	private String myValueSet;
	private String mySystemVersion;

	/**
	 * Constructor
	 */
	public TranslateConceptResult() {
		super();
	}

	public String getSystem() {
		return mySystem;
	}

	public TranslateConceptResult setSystem(String theSystem) {
		mySystem = theSystem;
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("system", mySystem)
			.append("code", myCode)
			.append("display", myDisplay)
			.append("equivalence", myEquivalence)
			.append("conceptMapUrl", myConceptMapUrl)
			.append("valueSet", myValueSet)
			.append("systemVersion", mySystemVersion)
			.toString();
	}

	public String getCode() {
		return myCode;
	}

	public TranslateConceptResult setCode(String theCode) {
		myCode = theCode;
		return this;
	}

	public String getDisplay() {
		return myDisplay;
	}

	public TranslateConceptResult setDisplay(String theDisplay) {
		myDisplay = theDisplay;
		return this;
	}

	public String getEquivalence() {
		return myEquivalence;
	}

	public TranslateConceptResult setEquivalence(String theEquivalence) {
		myEquivalence = theEquivalence;
		return this;
	}

	public String getSystemVersion() {
		return mySystemVersion;
	}

	public void setSystemVersion(String theSystemVersion) {
		mySystemVersion = theSystemVersion;
	}

	public String getValueSet() {
		return myValueSet;
	}

	public TranslateConceptResult setValueSet(String theValueSet) {
		myValueSet = theValueSet;
		return this;
	}

	public String getConceptMapUrl() {
		return myConceptMapUrl;
	}

	public TranslateConceptResult setConceptMapUrl(String theConceptMapUrl) {
		myConceptMapUrl = theConceptMapUrl;
		return this;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) {
			return true;
		}

		if (theO == null || getClass() != theO.getClass()) {
			return false;
		}

		TranslateConceptResult that = (TranslateConceptResult) theO;

		EqualsBuilder b = new EqualsBuilder();
		b.append(mySystem, that.mySystem);
		b.append(myCode, that.myCode);
		b.append(myDisplay, that.myDisplay);
		b.append(myEquivalence, that.myEquivalence);
		b.append(myConceptMapUrl, that.myConceptMapUrl);
		b.append(myValueSet, that.myValueSet);
		b.append(mySystemVersion, that.mySystemVersion);
		return b.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder(17, 37);
		b.append(mySystem);
		b.append(myCode);
		b.append(myDisplay);
		b.append(myEquivalence);
		b.append(myConceptMapUrl);
		b.append(myValueSet);
		b.append(mySystemVersion);
		return b.toHashCode();
	}
}
