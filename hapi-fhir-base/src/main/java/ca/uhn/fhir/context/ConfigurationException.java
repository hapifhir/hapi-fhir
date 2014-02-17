package ca.uhn.fhir.context;

/**
 * Non-checked exception indicating that HAPI was unable to initialize due to 
 * a detected configuration problem
 */
public class ConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConfigurationException() {
		super();
	}

	public ConfigurationException(String theMessage, Throwable theCause) {
		super(theMessage, theCause);
	}

	public ConfigurationException(String theMessage) {
		super(theMessage);
	}

	public ConfigurationException(Throwable theCause) {
		super(theCause);
	}

}
