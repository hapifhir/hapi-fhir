package ca.uhn.fhir.rest.server;

import javax.servlet.http.HttpServletRequest;

/**
 * Provides the server base for a given incoming request. This can be used to account for
 * multi-homed servers or other unusual network configurations.
 */
public interface IServerAddressStrategy {

	/**
	 * Determine the server base for a given request
	 */
	public String determineServerBase(HttpServletRequest theRequest);
	
}
