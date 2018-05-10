package ca.uhn.fhir.jpa.term;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.UriType;

import java.util.ArrayList;
import java.util.List;

public class TranslationRequest {
	private CodeableConcept myCodeableConcept;
	private Long myResourceId;
	private BooleanType myReverse;
	private UriType mySource;
	private UriType myTarget;
	private UriType myTargetSystem;

	public TranslationRequest() {
		super();

		myCodeableConcept = new CodeableConcept();
	}

	public CodeableConcept getCodeableConcept() {
		return myCodeableConcept;
	}

	public void setCodeableConcept(CodeableConcept theCodeableConcept) {
		myCodeableConcept = theCodeableConcept;
	}

	public boolean hasResourceId() {
		return myResourceId != null;
	}

	public Long getResourceId() {
		return myResourceId;
	}

	public void setResourceId(Long theResourceId) {
		myResourceId = theResourceId;
	}

	public boolean hasReverse() {
		return myReverse != null;
	}

	public BooleanType getReverse() {
		return myReverse;
	}

	public boolean getReverseAsBoolean() {
		if (hasReverse()) {
			return myReverse.booleanValue();
		}

		return false;
	}

	public void setReverse(BooleanType theReverse) {
		myReverse = theReverse;
	}

	public void setReverse(boolean theReverse) {
		myReverse = new BooleanType(theReverse);
	}

	public boolean hasSource() {
		return mySource != null && mySource.hasValue();
	}

	public UriType getSource() {
		return mySource;
	}

	public void setSource(UriType theSource) {
		mySource = theSource;
	}

	public boolean hasTarget() {
		return myTarget != null && myTarget.hasValue();
	}

	public UriType getTarget() {
		return myTarget;
	}

	public void setTarget(UriType theTarget) {
		myTarget = theTarget;
	}

	public boolean hasTargetSystem() {
		return myTargetSystem != null && myTargetSystem.hasValue();
	}

	public UriType getTargetSystem() {
		return myTargetSystem;
	}

	public void setTargetSystem(UriType theTargetSystem) {
		myTargetSystem = theTargetSystem;
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
}
