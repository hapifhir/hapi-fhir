package ca.uhn.fhir.context;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public enum FhirVersionEnum {

	/*
	 * ***********************
	 * Don't auto-sort this type!!!
	 * 
	 * Or more accurately, entries should be sorted from OLDEST FHIR release 
	 * to NEWEST FHIR release instead of alphabetically
	 * ***********************
	 */
	
	DSTU1("ca.uhn.fhir.model.dstu.FhirDstu1", null), 
	
	DSTU2("ca.uhn.fhir.model.dstu2.FhirDstu2", null),
	
	DEV("ca.uhn.fhir.model.dev.FhirDev", null), 
	
	DSTU2_HL7ORG("org.hl7.fhir.instance.FhirDstu2Hl7Org", DSTU2);


	private final String myVersionClass;
	private final FhirVersionEnum myEquivalent;
	private volatile Boolean myPresentOnClasspath;
	private volatile IFhirVersion myVersionImplementation;

	FhirVersionEnum(String theVersionClass, FhirVersionEnum theEquivalent) {
		myVersionClass = theVersionClass;
		myEquivalent = theEquivalent;
	}
	
	public boolean isEquivalentTo(FhirVersionEnum theVersion) {
		if (this.equals(theVersion)) {
			return true;
		}
		if (myEquivalent != null) {
			return myEquivalent.equals(theVersion);
		}
		return false;
	}
	
	public boolean isNewerThan(FhirVersionEnum theVersion) {
		return ordinal() > theVersion.ordinal();
	}
	
	/**
	 * Returns true if the given version is present on the classpath
	 */
	public boolean isPresentOnClasspath() {
		Boolean retVal = myPresentOnClasspath;
		if (retVal==null) {
			try {
				Class.forName(myVersionClass);
				retVal= true;
			} catch (Exception e) {
				retVal= false;
			}
			myPresentOnClasspath = retVal;
		}
		return retVal;
	}

	public IFhirVersion getVersionImplementation() {
		if (!isPresentOnClasspath()) {
			throw new IllegalStateException("Version " + name() + " is not present on classpath");
		}
		if (myVersionImplementation == null) {
			try {
				myVersionImplementation = (IFhirVersion) Class.forName(myVersionClass).newInstance();
			} catch (Exception e) {
				throw new InternalErrorException("Failed to instantiate FHIR version " + name(), e);
			}
		}
		return myVersionImplementation;
	}
	
}
