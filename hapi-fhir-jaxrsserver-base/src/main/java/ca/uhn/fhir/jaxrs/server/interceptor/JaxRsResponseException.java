package ca.uhn.fhir.jaxrs.server.interceptor;

import javax.ejb.ApplicationException;

import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

/**
 * A JEE wrapper exception that will not force a rollback.
 * 
 * @author Peter Van Houte | peter.vanhoute@agfa.com | Agfa Healthcare
 */
@ApplicationException(rollback=false)
public class JaxRsResponseException extends BaseServerResponseException {

	private static final long serialVersionUID = 1L;

	/**
	 * Utility constructor
	 * 
	 * @param base the base exception
	 */
	public JaxRsResponseException(BaseServerResponseException base) {
		super(base.getStatusCode(), base.getMessage(), base.getCause(), base.getOperationOutcome());
	}

}
