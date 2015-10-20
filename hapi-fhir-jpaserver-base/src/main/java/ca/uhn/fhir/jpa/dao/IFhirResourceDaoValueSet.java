package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;

public interface IFhirResourceDaoValueSet<T extends IBaseResource> extends IFhirResourceDao<T> {

	ValueSet expand(IIdType theId, String theFilter);

	ValueSet expand(ValueSet theSource, String theFilter);

	ValueSet expandByIdentifier(String theUri, String theFilter);

	LookupCodeResult lookupCode(CodeDt theCode, UriDt theSystem, CodingDt theCoding);

	ValidateCodeResult validateCode(UriDt theValueSetIdentifier, IIdType theId, CodeDt theCode, UriDt theSystem, StringDt theDisplay, CodingDt theCoding, CodeableConceptDt theCodeableConcept);

	public class LookupCodeResult {
		private String myCodeDisplay;
		private boolean myCodeIsAbstract;
		private String myCodeSystemDisplayName;
		private String myCodeSystemVersion;
		private boolean myFound;
		private String mySearchedForCode;
		private String mySearchedForSystem;

		public String getCodeDisplay() {
			return myCodeDisplay;
		}

		public String getCodeSystemDisplayName() {
			return myCodeSystemDisplayName;
		}

		public String getCodeSystemVersion() {
			return myCodeSystemVersion;
		}

		public String getSearchedForCode() {
			return mySearchedForCode;
		}

		public String getSearchedForSystem() {
			return mySearchedForSystem;
		}

		public boolean isCodeIsAbstract() {
			return myCodeIsAbstract;
		}

		public boolean isFound() {
			return myFound;
		}

		public void setCodeDisplay(String theCodeDisplay) {
			myCodeDisplay = theCodeDisplay;
		}

		public void setCodeIsAbstract(boolean theCodeIsAbstract) {
			myCodeIsAbstract = theCodeIsAbstract;
		}

		public void setCodeSystemDisplayName(String theCodeSystemDisplayName) {
			myCodeSystemDisplayName = theCodeSystemDisplayName;
		}

		public void setCodeSystemVersion(String theCodeSystemVersion) {
			myCodeSystemVersion = theCodeSystemVersion;
		}

		public void setFound(boolean theFound) {
			myFound = theFound;
		}

		public void setSearchedForCode(String theSearchedForCode) {
			mySearchedForCode = theSearchedForCode;
		}

		public void setSearchedForSystem(String theSearchedForSystem) {
			mySearchedForSystem = theSearchedForSystem;
		}
	}

	public class ValidateCodeResult {
		private String myDisplay;
		private String myMessage;
		private boolean myResult;

		public ValidateCodeResult(boolean theResult, String theMessage, String theDisplay) {
			super();
			myResult = theResult;
			myMessage = theMessage;
			myDisplay = theDisplay;
		}

		public String getDisplay() {
			return myDisplay;
		}

		public String getMessage() {
			return myMessage;
		}

		public boolean isResult() {
			return myResult;
		}
	}

}
