package ca.uhn.fhir.rest.param;

import java.util.List;
import java.util.Map;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.method.Request;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public interface IParameter {

	void translateClientArgumentIntoQueryArgument(Object theSourceClientArgument, Map<String, List<String>> theTargetQueryArguments) throws InternalErrorException;

	/**
	 * This <b>server method</b> method takes the data received by the server in an incoming request, and translates that data into a single argument for a server method invocation. Note that all
	 * received data is passed to this method, but the expectation is that not necessarily that all data is used by every parameter.
	 * 
	 * @param theRequest
	 *            The incoming request object
	 * @param theRequestContents
	 *            The parsed contents of the incoming request. E.g. if the request was an HTTP POST with a resource in the body, this argument would contain the parsed {@link IResource} instance.
	 * @return Returns the argument object as it will be passed to the {@link IResourceProvider} method.
	 */
	Object translateQueryParametersIntoServerArgument(Request theRequest, Object theRequestContents) throws InternalErrorException, InvalidRequestException;

}
