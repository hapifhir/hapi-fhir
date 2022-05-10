package ca.uhn.fhir.jpa.api.model;

/*-
 * #%L
 * HAPI FHIR Storage api
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

import ca.uhn.fhir.context.support.IValidationSupport;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TranslationRequest {
	private CodeableConcept myCodeableConcept;
	private Long myResourceId;
	private BooleanType myReverse;
	private UriType myUrl;
	private StringType myConceptMapVersion;
	private UriType mySource;
	private UriType myTarget;
	private UriType myTargetSystem;

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

	public Long getResourceId() {
		return myResourceId;
	}

	public void setResourceId(Long theResourceId) {
		myResourceId = theResourceId;
	}

	public BooleanType getReverse() {
		return myReverse;
	}

	public void setReverse(BooleanType theReverse) {
		myReverse = theReverse;
	}

	public void setReverse(boolean theReverse) {
		myReverse = new BooleanType(theReverse);
	}

	public boolean getReverseAsBoolean() {
		if (hasReverse()) {
			return myReverse.booleanValue();
		}

		return false;
	}

	public UriType getUrl() {
		return myUrl;
	}

	public TranslationRequest setUrl(UriType theUrl) {
		myUrl = theUrl;
		return this;
	}

	public StringType getConceptMapVersion() {
		return myConceptMapVersion;
	}

	public TranslationRequest setConceptMapVersion(StringType theConceptMapVersion) {
		myConceptMapVersion = theConceptMapVersion;
		return this;
	}

	public UriType getSource() {
		return mySource;
	}

	public TranslationRequest setSource(UriType theSource) {
		mySource = theSource;
		return this;
	}

	public UriType getTarget() {
		return myTarget;
	}

	public TranslationRequest setTarget(UriType theTarget) {
		myTarget = theTarget;
		return this;
	}

	public UriType getTargetSystem() {
		return myTargetSystem;
	}

	public TranslationRequest setTargetSystem(UriType theTargetSystem) {
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
		return myUrl != null && myUrl.hasValue();
	}

	public boolean hasConceptMapVersion() {
		return myConceptMapVersion != null && myConceptMapVersion.hasValue();
	}

	public boolean hasSource() {
		return mySource != null && mySource.hasValue();
	}

	public boolean hasTarget() {
		return myTarget != null && myTarget.hasValue();
	}

	public boolean hasTargetSystem() {
		return myTargetSystem != null && myTargetSystem.hasValue();
	}

	public IValidationSupport.TranslateCodeRequest asTranslateCodeRequest() {
		return new IValidationSupport.TranslateCodeRequest(
			Collections.unmodifiableList(this.getCodeableConcept().getCoding()),
			this.getTargetSystem() != null ? this.getTargetSystem().asStringValue() : null,
			this.getUrl() != null ? this.getUrl().asStringValue() : null,
			this.getConceptMapVersion() != null ? this.getConceptMapVersion().asStringValue() : null,
			this.getSource() != null ? this.getSource().asStringValue() : null,
			this.getTarget() != null ? this.getTarget().asStringValue() : null,
			this.getResourceId(),
			this.getReverseAsBoolean()
		);
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
		translationRequest.setConceptMapVersion(new StringType(theRequest.getConceptMapVersion()));
		translationRequest.setUrl(new UriType(theRequest.getConceptMapUrl()));
		translationRequest.setSource(new UriType(theRequest.getSourceValueSetUrl()));
		translationRequest.setTarget(new UriType(theRequest.getTargetValueSetUrl()));
		translationRequest.setTargetSystem(new UriType(theRequest.getTargetSystemUrl()));
		translationRequest.setResourceId(theRequest.getResourcePid());
		translationRequest.setReverse(theRequest.isReverse());
		return translationRequest;

	}
}
