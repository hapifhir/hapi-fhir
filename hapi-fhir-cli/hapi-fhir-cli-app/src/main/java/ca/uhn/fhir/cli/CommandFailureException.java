package ca.uhn.fhir.cli;

public class CommandFailureException extends Error {

	public CommandFailureException(String theMessage) {
		super(theMessage);
	}

	public CommandFailureException(String theString, Exception theCause) {
		super(theString, theCause);
	}

	private static final long serialVersionUID = 1L;

}
