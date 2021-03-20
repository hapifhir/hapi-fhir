package ca.uhn.fhir.jpa.api.model;

/*
 * #%L
 * HAPI FHIR JPA API
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.support.IValidationSupport;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.r4.model.UriType;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TranslationMatch {
	private String mySystem;
	private String myCode;
	private String myDisplay;
	private Enumerations.ConceptMapEquivalence myEquivalence;
	private String myConceptMapUrl;
	private String myValueSet;
	private String mySystemVersion;

	/**
	 * Constructor
	 */
	public TranslationMatch() {
		super();
	}

	public String getSystem() {
		return mySystem;
	}

	public void setSystem(String theSystem) {
		mySystem = theSystem;
	}

	public String getCode() {
		return myCode;
	}

	public void setCode(String theCode) {
		myCode = theCode;
	}

	public String getDisplay() {
		return myDisplay;
	}

	public void setDisplay(String theDisplay) {
		myDisplay = theDisplay;
	}

	public Enumerations.ConceptMapEquivalence getEquivalence() {
		return myEquivalence;
	}

	public void setEquivalence(Enumerations.ConceptMapEquivalence theEquivalence) {
		myEquivalence = theEquivalence;
	}

	public void toParameterParts(ParametersParameterComponent theParam) {
		if (myEquivalence != null) {
			theParam.addPart().setName("equivalence").setValue(new CodeType(myEquivalence.toCode()));
		}

		if (isNotBlank(getSystem()) || isNotBlank(getCode()) || isNotBlank(getDisplay())) {
			theParam.addPart().setName("concept").setValue(new Coding(getSystem(), getCode(), getDisplay()));
		}

		if (isNotBlank(getConceptMapUrl())) {
			theParam.addPart().setName("source").setValue(new UriType(getConceptMapUrl()));
		}
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

	public void setValueSet(String theValueSet) {
		myValueSet = theValueSet;
	}

	public String getConceptMapUrl() {
		return myConceptMapUrl;
	}

	public void setConceptMapUrl(String theConceptMapUrl) {
		myConceptMapUrl = theConceptMapUrl;
	}

	public IValidationSupport.TranslateCodeResult toTranslateCodeResult() {
		return new IValidationSupport.TranslateCodeResult()
			.setCodeSystemUrl(mySystem)
			.setCode(myCode)
			.setDisplay(myDisplay);
	}
}
