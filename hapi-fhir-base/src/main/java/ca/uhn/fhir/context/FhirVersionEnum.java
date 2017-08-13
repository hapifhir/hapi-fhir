package ca.uhn.fhir.context;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
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

	DSTU1("ca.uhn.fhir.model.dstu.FhirDstu1", null, false, new Version("0.0.82")),

	DSTU2("ca.uhn.fhir.model.dstu2.FhirDstu2", null, false, new Version("1.0.2")),

	DSTU2_HL7ORG("org.hl7.fhir.instance.FhirDstu2Hl7Org", DSTU2, true, new Version("1.0.2")),

	DSTU2_1("org.hl7.fhir.dstu2016may.hapi.ctx.FhirDstu2_1", null, true, new Version("1.4.0")),

	DSTU3("org.hl7.fhir.dstu3.hapi.ctx.FhirDstu3", null, true, new Dstu3Version());

	private final FhirVersionEnum myEquivalent;
	private final boolean myIsRi;
	private volatile Boolean myPresentOnClasspath;
	private final String myVersionClass;
	private volatile IFhirVersion myVersionImplementation;
	private String myFhirVersionString;

	FhirVersionEnum(String theVersionClass, FhirVersionEnum theEquivalent, boolean theIsRi, IVersionProvider theVersionExtractor) {
		myVersionClass = theVersionClass;
		myEquivalent = theEquivalent;
		myFhirVersionString = theVersionExtractor.provideVersion();
		myIsRi = theIsRi;
	}

	public String getFhirVersionString() {
		return myFhirVersionString;
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
		return !isEquivalentTo(theVersion) && ordinal() > theVersion.ordinal();
	}

	public boolean isOlderThan(FhirVersionEnum theVersion) {
		return !isEquivalentTo(theVersion) && ordinal() < theVersion.ordinal();
	}

	/**
	 * Returns true if the given version is present on the classpath
	 */
	public boolean isPresentOnClasspath() {
		Boolean retVal = myPresentOnClasspath;
		if (retVal == null) {
			try {
				Class.forName(myVersionClass);
				retVal = true;
			} catch (Exception e) {
				retVal = false;
			}
			myPresentOnClasspath = retVal;
		}
		return retVal;
	}

	/**
	 * Is this version using the HL7.org RI structures?
	 */
	public boolean isRi() {
		return myIsRi;
	}

	private static class Version implements IVersionProvider {

		public Version(String theVersion) {
			super();
			myVersion = theVersion;
		}

		private String myVersion;

		@Override
		public String provideVersion() {
			return myVersion;
		}

	}

	private interface IVersionProvider {
		String provideVersion();
	}

	private static class Dstu3Version implements IVersionProvider {

		public Dstu3Version() {
			try {
				Class<?> c = Class.forName("org.hl7.fhir.dstu3.model.Constants");
				myVersion = (String) c.getDeclaredField("VERSION").get(null);
			} catch (Exception e) {
				myVersion = "UNKNOWN";
			}
		}

		private String myVersion;

		@Override
		public String provideVersion() {
			return myVersion;
		}

	}

}
