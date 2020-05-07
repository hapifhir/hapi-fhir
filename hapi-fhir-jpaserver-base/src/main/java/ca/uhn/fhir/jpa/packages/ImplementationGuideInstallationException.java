package ca.uhn.fhir.jpa.packages;

/**
 * Used internally to indicate a failure to install the implementation guide
 */
public class ImplementationGuideInstallationException extends RuntimeException {

	public ImplementationGuideInstallationException(String message) {
		super(message);
	}

	public ImplementationGuideInstallationException(String message, Throwable cause) {
		super(message, cause);
	}
}
