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

import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent;
import org.hl7.fhir.r4.model.StringType;

import java.util.ArrayList;
import java.util.List;

public class TranslationResult {
	private List<TranslationMatch> myMatches;
	private StringType myMessage;
	private BooleanType myResult;

	public TranslationResult() {
		super();

		myMatches = new ArrayList<>();
	}

	public List<TranslationMatch> getMatches() {
		return myMatches;
	}

	public void setMatches(List<TranslationMatch> theMatches) {
		myMatches = theMatches;
	}

	public boolean addMatch(TranslationMatch theMatch) {
		return myMatches.add(theMatch);
	}

	public StringType getMessage() {
		return myMessage;
	}

	public void setMessage(StringType theMessage) {
		myMessage = theMessage;
	}

	public BooleanType getResult() {
		return myResult;
	}

	public void setResult(BooleanType theMatched) {
		myResult = theMatched;
	}

	public Parameters toParameters() {
		Parameters retVal = new Parameters();

		if (myResult != null) {
			retVal.addParameter().setName("result").setValue(myResult);
		}

		if (myMessage != null) {
			retVal.addParameter().setName("message").setValue(myMessage);
		}

		for (TranslationMatch translationMatch : myMatches) {
			ParametersParameterComponent matchParam = retVal.addParameter().setName("match");
			translationMatch.toParameterParts(matchParam);
		}

		return retVal;
	}
}
