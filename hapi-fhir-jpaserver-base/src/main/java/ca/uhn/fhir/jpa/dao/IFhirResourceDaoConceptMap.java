package ca.uhn.fhir.jpa.dao;

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

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Parameters.ParametersParameterComponent;

import java.util.ArrayList;
import java.util.List;

public interface IFhirResourceDaoConceptMap<T extends IBaseResource> extends IFhirResourceDao<T> {
	TranslationResult translate(IPrimitiveType<String> theSourceCodeSystem, IPrimitiveType<String> theTargetCodeSystem, IPrimitiveType<String> theSourceCode, RequestDetails theRequestDetails);

	class TranslationResult {
		private BooleanType myResult;
		private StringType myMessage;
		private List<TranslationMatch> myMatches;

		public TranslationResult() {
			super();

			myMatches = new ArrayList<>();
		}

		public BooleanType getResult() {
			return myResult;
		}

		public void setResult(BooleanType theMatched) {
			myResult = theMatched;
		}

		public StringType getMessage() {
			return myMessage;
		}

		public void setMessage(StringType theMessage) {
			myMessage = theMessage;
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

		public Parameters toParameters() {
			Parameters retVal = new Parameters();

			retVal.addParameter().setName("result").setValue(myResult);

			retVal.addParameter().setName("message").setValue(myMessage);

			for (TranslationMatch translationMatch : myMatches) {
				ParametersParameterComponent matchParam = retVal.addParameter().setName("match");
				matchParam.addPart().setName("equivalence").setValue(translationMatch.getEquivalence());
				matchParam.addPart().setName("concept").setValue(translationMatch.getConcept());
				matchParam.addPart().setName("source").setValue(translationMatch.getSource());
			}

			return retVal;
		}
	}

	class TranslationMatch {
		private CodeType equivalence;
		private Coding concept;
		private UriType source;

		public TranslationMatch() {
			super();
		}

		public CodeType getEquivalence() {
			return equivalence;
		}

		public void setEquivalence(CodeType theEquivalence) {
			this.equivalence = theEquivalence;
		}

		public Coding getConcept() {
			return concept;
		}

		public void setConcept(Coding theConcept) {
			this.concept = theConcept;
		}

		public UriType getSource() {
			return source;
		}

		public void setSource(UriType theSource) {
			this.source = theSource;
		}
	}
}
