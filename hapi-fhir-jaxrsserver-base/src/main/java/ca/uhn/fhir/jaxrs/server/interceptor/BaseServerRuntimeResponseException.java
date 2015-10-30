package ca.uhn.fhir.jaxrs.server.interceptor;

import javax.ejb.ApplicationException;

import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

@ApplicationException(rollback=false)
public class BaseServerRuntimeResponseException extends BaseServerResponseException {

	private static final long serialVersionUID = 1L;

	public BaseServerRuntimeResponseException(BaseServerResponseException base) {
		super(base.getStatusCode(), base.getMessage(), base.getCause(), base.getOperationOutcome());
	}

}
