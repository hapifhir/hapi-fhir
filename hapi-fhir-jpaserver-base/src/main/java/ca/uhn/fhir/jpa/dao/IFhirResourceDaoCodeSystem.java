package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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

public interface IFhirResourceDaoCodeSystem<T extends IBaseResource, CD, CC> extends IFhirResourceDao<T> {

	List<IIdType> findCodeSystemIdsContainingSystemAndCode(String theCode, String theSystem);

	LookupCodeResult lookupCode(IPrimitiveType<String> theCode, IPrimitiveType<String> theSystem, CD theCoding, RequestDetails theRequestDetails);

	class LookupCodeResult {

		private String myCodeDisplay;
		private boolean myCodeIsAbstract;
		private String myCodeSystemDisplayName;
		private String myCodeSystemVersion;
		private boolean myFound;
		private String mySearchedForCode;
		private String mySearchedForSystem;
		/**
		 * Constructor
		 */
		public LookupCodeResult() {
			super();
		}

		public String getCodeDisplay() {
			return myCodeDisplay;
		}

		public void setCodeDisplay(String theCodeDisplay) {
			myCodeDisplay = theCodeDisplay;
		}

		public String getCodeSystemDisplayName() {
			return myCodeSystemDisplayName;
		}

		public void setCodeSystemDisplayName(String theCodeSystemDisplayName) {
			myCodeSystemDisplayName = theCodeSystemDisplayName;
		}

		public String getCodeSystemVersion() {
			return myCodeSystemVersion;
		}

		public void setCodeSystemVersion(String theCodeSystemVersion) {
			myCodeSystemVersion = theCodeSystemVersion;
		}

		public String getSearchedForCode() {
			return mySearchedForCode;
		}

		public void setSearchedForCode(String theSearchedForCode) {
			mySearchedForCode = theSearchedForCode;
		}

		public String getSearchedForSystem() {
			return mySearchedForSystem;
		}

		public void setSearchedForSystem(String theSearchedForSystem) {
			mySearchedForSystem = theSearchedForSystem;
		}

		public boolean isCodeIsAbstract() {
			return myCodeIsAbstract;
		}

		public void setCodeIsAbstract(boolean theCodeIsAbstract) {
			myCodeIsAbstract = theCodeIsAbstract;
		}

		public boolean isFound() {
			return myFound;
		}

		public void setFound(boolean theFound) {
			myFound = theFound;
		}

		public void throwNotFoundIfAppropriate() {
			if (isFound() == false) {
				throw new ResourceNotFoundException("Unable to find code[" + getSearchedForCode() + "] in system[" + getSearchedForSystem() + "]");
			}
		}

		public Parameters toParameters() {
			Parameters retVal = new Parameters();

			retVal.addParameter().setName("name").setValue(new StringType(getCodeSystemDisplayName()));
			if (isNotBlank(getCodeSystemVersion())) {
				retVal.addParameter().setName("version").setValue(new StringType(getCodeSystemVersion()));
			}
			retVal.addParameter().setName("display").setValue(new StringType(getCodeDisplay()));
			retVal.addParameter().setName("abstract").setValue(new BooleanType(isCodeIsAbstract()));

			return retVal;
		}
	}

}
