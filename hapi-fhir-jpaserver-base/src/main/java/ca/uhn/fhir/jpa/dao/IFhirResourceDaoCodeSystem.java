package ca.uhn.fhir.jpa.dao;

import java.util.List;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.rest.method.RequestDetails;

public interface IFhirResourceDaoCodeSystem<T extends IBaseResource, CD, CC> extends IFhirResourceDao<T> {

	List<IIdType> findCodeSystemIdsContainingSystemAndCode(String theCode, String theSystem);

	LookupCodeResult lookupCode(IPrimitiveType<String> theCode, IPrimitiveType<String> theSystem, CD theCoding, RequestDetails theRequestDetails);

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

}
