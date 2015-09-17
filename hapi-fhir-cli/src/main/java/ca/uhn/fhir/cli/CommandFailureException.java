package ca.uhn.fhir.cli;

public class CommandFailureException extends Error {

	public CommandFailureException(String theMessage) {
		super(theMessage);
	}

	private static final long serialVersionUID = 1L;

}
