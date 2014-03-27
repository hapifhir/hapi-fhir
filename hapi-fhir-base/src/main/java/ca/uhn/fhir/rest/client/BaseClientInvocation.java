package ca.uhn.fhir.rest.client;

import java.io.IOException;

import org.apache.http.client.methods.HttpRequestBase;

import ca.uhn.fhir.parser.DataFormatException;

public abstract class BaseClientInvocation {

	/**
	 * Create an HTTP request out of this client request
	 * 
	 * @param theUrlBase The FHIR server base url (with a trailing "/")
	 */
	public abstract HttpRequestBase asHttpRequest(String theUrlBase) throws DataFormatException, IOException;
	
}
