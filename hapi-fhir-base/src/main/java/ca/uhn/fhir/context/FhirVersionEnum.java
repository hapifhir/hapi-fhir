package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public enum FhirVersionEnum {

	DSTU1("ca.uhn.fhir.model.dstu.FhirDstu1");
	
	private final String myVersionClass;
	private volatile Boolean myPresentOnClasspath;
	private volatile IFhirVersion myVersionImplementation;

	FhirVersionEnum(String theVersionClass) {
		myVersionClass = theVersionClass;
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
