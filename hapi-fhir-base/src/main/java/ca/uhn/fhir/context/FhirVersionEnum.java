package ca.uhn.fhir.context;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

	DSTU2("ca.uhn.fhir.model.dstu2.FhirDstu2", null, false, new Version("1.0.2")),

	DSTU2_HL7ORG("org.hl7.fhir.dstu2.hapi.ctx.FhirDstu2Hl7Org", DSTU2, true, new Version("1.0.2")),

	DSTU2_1("org.hl7.fhir.dstu2016may.hapi.ctx.FhirDstu2_1", null, true, new Version("1.4.0")),

	DSTU3("org.hl7.fhir.dstu3.hapi.ctx.FhirDstu3", null, true, new Dstu3Version()),

	R4("org.hl7.fhir.r4.hapi.ctx.FhirR4", null, true, new R4Version()),

	R5("org.hl7.fhir.r5.hapi.ctx.FhirR5", null, true, new R5Version());

	private final FhirVersionEnum myEquivalent;
	private final boolean myIsRi;
	private final String myVersionClass;
	private volatile Boolean myPresentOnClasspath;
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

	public boolean isEqualOrNewerThan(FhirVersionEnum theVersion) {
		return ordinal() >= theVersion.ordinal();
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

	public FhirContext newContext() {
		switch (this) {
			case DSTU2:
				return FhirContext.forDstu2();
			case DSTU2_HL7ORG:
				return FhirContext.forDstu2Hl7Org();
			case DSTU2_1:
				return FhirContext.forDstu2_1();
			case DSTU3:
				return FhirContext.forDstu3();
			case R4:
				return FhirContext.forR4();
			case R5:
				return FhirContext.forR5();
		}
		throw new IllegalStateException("Unknown version: " + this); // should not happen
	}

	/**
	 * Returns the {@link FhirVersionEnum} which corresponds to a specific version of
	 * FHIR. Partial version strings (e.g. "3.0") are acceptable.
	 *
	 * @return Returns null if no version exists matching the given string
	 */
	public static FhirVersionEnum forVersionString(String theVersionString) {
		for (FhirVersionEnum next : values()) {
			if (next.getFhirVersionString().startsWith(theVersionString)) {
				return next;
			}
		}
		return null;
	}

	private interface IVersionProvider {
		String provideVersion();
	}

	private static class Version implements IVersionProvider {

		private String myVersion;

		public Version(String theVersion) {
			super();
			myVersion = theVersion;
		}

		@Override
		public String provideVersion() {
			return myVersion;
		}

	}

	/**
	 * This class attempts to read the FHIR version from the actual model
	 * classes in order to supply an accurate version string even over time
	 */
	private static class Dstu3Version implements IVersionProvider {

		private String myVersion;

		Dstu3Version() {
			try {
				Class<?> c = Class.forName("org.hl7.fhir.dstu3.model.Constants");
				myVersion = (String) c.getDeclaredField("VERSION").get(null);
			} catch (Exception e) {
				myVersion = "3.0.1";
			}
		}

		@Override
		public String provideVersion() {
			return myVersion;
		}

	}

	private static class R4Version implements IVersionProvider {

		private String myVersion;

		R4Version() {
			try {
				Class<?> c = Class.forName("org.hl7.fhir.r4.model.Constants");
				myVersion = (String) c.getDeclaredField("VERSION").get(null);
			} catch (Exception e) {
				myVersion = "4.0.0";
			}
		}

		@Override
		public String provideVersion() {
			return myVersion;
		}

	}

	private static class R5Version implements IVersionProvider {

		private String myVersion;

		R5Version() {
			try {
				Class<?> c = Class.forName("org.hl7.fhir.r5.model.Constants");
				myVersion = (String) c.getDeclaredField("VERSION").get(null);
			} catch (Exception e) {
				myVersion = "5.0.0";
			}
		}

		@Override
		public String provideVersion() {
			return myVersion;
		}

	}

}
