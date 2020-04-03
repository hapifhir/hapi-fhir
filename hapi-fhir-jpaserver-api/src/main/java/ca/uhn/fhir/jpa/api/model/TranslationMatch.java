package ca.uhn.fhir.jpa.api.model;

/*
 * #%L
 * HAPI FHIR JPA API
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

import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.r4.model.UriType;

public class TranslationMatch {
	private Coding myConcept;
	private CodeType myEquivalence;
	private UriType mySource;

	public TranslationMatch() {
		super();
	}

	public Coding getConcept() {
		return myConcept;
	}

	public void setConcept(Coding theConcept) {
		myConcept = theConcept;
	}

	public CodeType getEquivalence() {
		return myEquivalence;
	}

	public void setEquivalence(CodeType theEquivalence) {
		myEquivalence = theEquivalence;
	}

	public UriType getSource() {
		return mySource;
	}

	public void setSource(UriType theSource) {
		mySource = theSource;
	}

	public void toParameterParts(ParametersParameterComponent theParam) {
		if (myEquivalence != null) {
			theParam.addPart().setName("equivalence").setValue(myEquivalence);
		}

		if (myConcept != null) {
			theParam.addPart().setName("concept").setValue(myConcept);
		}

		if (mySource != null) {
			theParam.addPart().setName("source").setValue(mySource);
		}
	}
}
