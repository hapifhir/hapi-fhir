package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.support.IContextValidationSupport;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
		private List<IContextValidationSupport.BaseConceptProperty> myProperties;

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

		public void setProperties(List<IContextValidationSupport.BaseConceptProperty> theProperties) {
			myProperties = theProperties;
		}

		public void throwNotFoundIfAppropriate() {
			if (isFound() == false) {
				throw new ResourceNotFoundException("Unable to find code[" + getSearchedForCode() + "] in system[" + getSearchedForSystem() + "]");
			}
		}

		public Parameters toParameters(List<? extends IPrimitiveType<String>> theProperties) {
			Parameters retVal = new Parameters();

			retVal.addParameter().setName("name").setValue(new StringType(getCodeSystemDisplayName()));
			if (isNotBlank(getCodeSystemVersion())) {
				retVal.addParameter().setName("version").setValue(new StringType(getCodeSystemVersion()));
			}
			retVal.addParameter().setName("display").setValue(new StringType(getCodeDisplay()));
			retVal.addParameter().setName("abstract").setValue(new BooleanType(isCodeIsAbstract()));

			if (myProperties != null) {

				Set<String> properties = Collections.emptySet();
				if (theProperties != null) {
					properties = theProperties
						.stream()
						.map(IPrimitiveType::getValueAsString)
						.collect(Collectors.toSet());
				}

				for (IContextValidationSupport.BaseConceptProperty next : myProperties) {

					if (!properties.isEmpty()) {
						if (!properties.contains(next.getPropertyName())) {
							continue;
						}
					}

					Parameters.ParametersParameterComponent property = retVal.addParameter().setName("property");
					property
						.addPart()
						.setName("code")
						.setValue(new CodeType(next.getPropertyName()));

					if (next instanceof IContextValidationSupport.StringConceptProperty) {
						IContextValidationSupport.StringConceptProperty prop = (IContextValidationSupport.StringConceptProperty) next;
						property
							.addPart()
							.setName("value")
							.setValue(new StringType(prop.getValue()));
					} else if (next instanceof IContextValidationSupport.CodingConceptProperty) {
						IContextValidationSupport.CodingConceptProperty prop = (IContextValidationSupport.CodingConceptProperty) next;
						property
							.addPart()
							.setName("value")
							.setValue(new Coding()
								.setSystem(prop.getCodeSystem())
								.setCode(prop.getCode())
								.setDisplay(prop.getDisplay()));
					} else {
						throw new IllegalStateException("Don't know how to handle " + next.getClass());
					}
				}
			}

			return retVal;
		}
	}

}
