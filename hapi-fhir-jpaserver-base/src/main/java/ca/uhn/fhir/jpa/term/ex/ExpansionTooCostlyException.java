package ca.uhn.fhir.jpa.term.ex;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public class ExpansionTooCostlyException extends InternalErrorException {

	public ExpansionTooCostlyException(String theMessage) {
		super(theMessage);
	}

}
