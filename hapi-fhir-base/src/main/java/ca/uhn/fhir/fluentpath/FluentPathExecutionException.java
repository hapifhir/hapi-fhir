package ca.uhn.fhir.fluentpath;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

/**
 * This exception is thrown if a FluentPath expression can not be executed successfully
 * for any reason
 */
public class FluentPathExecutionException extends InternalErrorException {

	private static final long serialVersionUID = 1L;

	public FluentPathExecutionException(Throwable theCause) {
		super(theCause);
	}

	public FluentPathExecutionException(String theMessage) {
		super(theMessage);
	}

}
