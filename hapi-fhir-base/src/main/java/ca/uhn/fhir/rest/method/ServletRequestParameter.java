package ca.uhn.fhir.rest.method;

import java.util.List;
import java.util.Map;

import ca.uhn.fhir.rest.param.IParameter;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

class ServletRequestParameter implements IParameter {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServletRequestParameter.class);

	@Override
	public void translateClientArgumentIntoQueryArgument(Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments) throws InternalErrorException {
		/*
		 * Does nothing, since we just ignore HttpServletRequest arguments
		 */
		ourLog.trace("Ignoring HttpServletRequest argument: {}", theSourceClientArgument);
	}

	@Override
	public Object translateQueryParametersIntoServerArgument(Request theRequest, Object theRequestContents) throws InternalErrorException, InvalidRequestException {
		return theRequest.getServletRequest();
	}

}
