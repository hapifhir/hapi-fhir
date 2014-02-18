package ca.uhn.fhir.parser;

public class DataFormatException extends Exception {

	private static final long serialVersionUID = 1L;

	public DataFormatException() {
		super();
	}

	public DataFormatException(String theMessage) {
		super(theMessage);
	}

	public DataFormatException(String theMessage, Throwable theCause) {
		super(theMessage, theCause);
	}

	public DataFormatException(Throwable theCause) {
		super(theCause);
	}

}
