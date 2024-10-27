/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.api.model;

import ca.uhn.fhir.context.support.IValidationSupport;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class TranslationRequest {
	private CodeableConcept myCodeableConcept;
	private IIdType myResourceId;
	private Boolean myReverse;
	private String myUrl;
	private String myConceptMapVersion;
	private String mySource;
	private String myTarget;
	private String myTargetSystem;

	public TranslationRequest() {
		super();

		myCodeableConcept = new CodeableConcept();
	}

	/**
	 * This is just a convenience method that creates a codeableconcept if one
	 * doesn't already exist, and adds a coding to it
	 */
	public TranslationRequest addCode(String theSystem, String theCode) {
		Validate.notBlank(theSystem, "theSystem must not be null");
		Validate.notBlank(theCode, "theCode must not be null");
		if (getCodeableConcept() == null) {
			setCodeableConcept(new CodeableConcept());
		}
		getCodeableConcept().addCoding(new Coding().setSystem(theSystem).setCode(theCode));
		return this;
	}

	public CodeableConcept getCodeableConcept() {
		return myCodeableConcept;
	}

	public TranslationRequest setCodeableConcept(CodeableConcept theCodeableConcept) {
		myCodeableConcept = theCodeableConcept;
		return this;
	}

	public IIdType getResourceId() {
		return myResourceId;
	}

	public void setResourceId(IIdType theResourceId) {
		myResourceId = theResourceId;
	}

	public Boolean getReverse() {
		return myReverse;
	}

	public void setReverse(Boolean theReverse) {
		myReverse = theReverse;
	}

	public boolean getReverseAsBoolean() {
		if (hasReverse()) {
			return myReverse;
		}

		return false;
	}

	public String getUrl() {
		return myUrl;
	}

	public TranslationRequest setUrl(String theUrl) {
		myUrl = theUrl;
		return this;
	}

	public String getConceptMapVersion() {
		return myConceptMapVersion;
	}

	public TranslationRequest setConceptMapVersion(String theConceptMapVersion) {
		myConceptMapVersion = theConceptMapVersion;
		return this;
	}

	public String getSource() {
		return mySource;
	}

	public TranslationRequest setSource(String theSource) {
		mySource = theSource;
		return this;
	}

	public String getTarget() {
		return myTarget;
	}

	public TranslationRequest setTarget(String theTarget) {
		myTarget = theTarget;
		return this;
	}

	public String getTargetSystem() {
		return myTargetSystem;
	}

	public TranslationRequest setTargetSystem(String theTargetSystem) {
		myTargetSystem = theTargetSystem;
		return this;
	}

	public List<TranslationQuery> getTranslationQueries() {
		List<TranslationQuery> retVal = new ArrayList<>();

		TranslationQuery translationQuery;
		for (Coding coding : getCodeableConcept().getCoding()) {
			translationQuery = new TranslationQuery();

			translationQuery.setCoding(coding);

			if (this.hasResourceId()) {
				translationQuery.setResourceId(this.getResourceId());
			}

			if (this.hasUrl()) {
				translationQuery.setUrl(this.getUrl());
			}

			if (this.hasConceptMapVersion()) {
				translationQuery.setConceptMapVersion(this.getConceptMapVersion());
			}

			if (this.hasSource()) {
				translationQuery.setSource(this.getSource());
			}

			if (this.hasTarget()) {
				translationQuery.setTarget(this.getTarget());
			}

			if (this.hasTargetSystem()) {
				translationQuery.setTargetSystem(this.getTargetSystem());
			}

			retVal.add(translationQuery);
		}

		return retVal;
	}

	public boolean hasResourceId() {
		return myResourceId != null;
	}

	public boolean hasReverse() {
		return myReverse != null;
	}

	public boolean hasUrl() {
		return isNotBlank(myUrl);
	}

	public boolean hasConceptMapVersion() {
		return isNotBlank(myConceptMapVersion);
	}

	public boolean hasSource() {
		return isNotBlank(mySource);
	}

	public boolean hasTarget() {
		return isNotBlank(myTarget);
	}

	public boolean hasTargetSystem() {
		return isNotBlank(myTargetSystem);
	}

	public IValidationSupport.TranslateCodeRequest asTranslateCodeRequest() {
		return new IValidationSupport.TranslateCodeRequest(
				Collections.unmodifiableList(this.getCodeableConcept().getCoding()),
				this.getTargetSystem(),
				this.getUrl(),
				this.getConceptMapVersion(),
				this.getSource(),
				this.getTarget(),
				this.getResourceId(),
				this.getReverseAsBoolean());
	}

	public static TranslationRequest fromTranslateCodeRequest(IValidationSupport.TranslateCodeRequest theRequest) {
		CodeableConcept sourceCodeableConcept = new CodeableConcept();
		for (IBaseCoding aCoding : theRequest.getCodings()) {
			sourceCodeableConcept
					.addCoding()
					.setSystem(aCoding.getSystem())
					.setCode(aCoding.getCode())
					.setVersion(((Coding) aCoding).getVersion());
		}

		TranslationRequest translationRequest = new TranslationRequest();
		translationRequest.setCodeableConcept(sourceCodeableConcept);
		translationRequest.setConceptMapVersion(theRequest.getConceptMapVersion());
		translationRequest.setUrl(theRequest.getConceptMapUrl());
		translationRequest.setSource(theRequest.getSourceValueSetUrl());
		translationRequest.setTarget(theRequest.getTargetValueSetUrl());
		translationRequest.setTargetSystem(theRequest.getTargetSystemUrl());
		translationRequest.setResourceId(theRequest.getResourceId());
		translationRequest.setReverse(theRequest.isReverse());
		return translationRequest;
	}
}
