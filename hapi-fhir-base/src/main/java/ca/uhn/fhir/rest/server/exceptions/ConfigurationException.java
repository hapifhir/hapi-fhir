package ca.uhn.fhir.rest.server.exceptions;

import javax.servlet.ServletException;

public class ConfigurationException extends ServletException {

	public ConfigurationException(String theString) {
		super(theString);
	}

	private static final long serialVersionUID = 1L;

}
